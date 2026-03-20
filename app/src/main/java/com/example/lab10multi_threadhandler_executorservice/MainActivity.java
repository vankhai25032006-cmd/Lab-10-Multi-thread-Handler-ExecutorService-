package com.example.lab10multi_threadhandler_executorservice;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // 1. Khai báo các thành phần giao diện
    private TextView tvCounter;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Giữ nguyên phần xử lý WindowInsets của hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Ánh xạ View (Phải trùng ID với file activity_main.xml)
        tvCounter = findViewById(R.id.tvCounter);
        btnStart = findViewById(R.id.btnStart);

        // 3. Thiết lập sự kiện khi nhấn nút
        btnStart.setOnClickListener(v -> {
            thucHienDemSo();
        });
    }

    private void thucHienDemSo() {
        // Vô hiệu hóa nút để tránh bấm nhiều lần tạo nhiều luồng trùng nhau
        btnStart.setEnabled(false);
        tvCounter.setText("0");

        // 4. Tạo Background Thread để xử lý tác vụ nặng (đếm số + sleep)
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    final int count = i;

                    try {
                        // Nghỉ 1 giây - Tác vụ này nếu chạy ở UI Thread sẽ gây đơ máy
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 5. Cập nhật giao diện từ luồng phụ phải dùng runOnUiThread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCounter.setText(String.valueOf(count));

                            // Kiểm tra nếu đếm xong
                            if (count == 10) {
                                Toast.makeText(MainActivity.this, "Đã đếm xong!", Toast.LENGTH_SHORT).show();
                                btnStart.setEnabled(true); // Bật lại nút
                            }
                        }
                    });
                }
            }
        });

        // Bắt đầu chạy luồng phụ
        backgroundThread.start();
    }
}