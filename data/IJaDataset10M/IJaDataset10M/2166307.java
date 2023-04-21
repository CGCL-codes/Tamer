package com.bus3.test.request;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.DecimalFormat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import robot.arm.provider.asyc.AsycTask;
import android.widget.TextView;
import com.bus3.R;
import com.bus3.test.MoreSecondActivity;

public class MoreSecondRequest extends AsycTask<MoreSecondActivity> {

    private static final int STEP = 1000;

    private static DecimalFormat df = new DecimalFormat("00.00%");

    public static HttpGet get;

    public MoreSecondRequest(MoreSecondActivity activity) {
        super(activity);
    }

    @Override
    public void doCall() {
        final TextView view = (TextView) act.findViewById(R.id.text);
        System.out.println("开始下载");
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            HttpClient client = new DefaultHttpClient();
            get = new HttpGet("http://blogimg.chinaunix.net/blog/upfile2/090428165422.rar");
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            final long contentLength = entity.getContentLength();
            File target = new File("/sdcard/1.rar");
            if (!target.exists()) target.createNewFile();
            outputStream = new BufferedOutputStream(new FileOutputStream(target));
            for (int bt = inputStream.read(), length = 0; bt != -1; bt = inputStream.read()) {
                outputStream.write(bt);
                final int len = ++length;
                if (len % STEP == 0 || len == contentLength) {
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            view.setText(len + "/" + contentLength);
                            view.append("\n");
                            view.append(df.format((double) len / contentLength));
                        }
                    });
                }
            }
        } catch (Exception e) {
            if (e instanceof SocketException) {
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        view.append("\n");
                        view.append("\n手动中断下载");
                    }
                });
                throw new RuntimeException("手动停止");
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                inputStream = null;
                outputStream = null;
            }
        }
    }

    @Override
    public void doResult() {
    }
}
