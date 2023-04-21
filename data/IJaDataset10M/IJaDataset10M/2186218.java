package my.down.stream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.util.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class downStream extends Activity {

    ImageView iv;

    Bitmap bmp;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iv = (ImageView) findViewById(R.id.lpimg);
        final Handler messageHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                iv.setImageBitmap(bmp);
            }
        };
        class DownStreamer implements Runnable {

            ServerSocket serverSocket;

            Socket client;

            InputStream inputStream;

            int p = 8234;

            final String TAG = "downStream [DownStream]";

            boolean exception = false;

            public DownStreamer(int port) {
                p = port;
            }

            @Override
            public void run() {
                try {
                    Log.i(TAG, "Creating down stream server...");
                    serverSocket = new ServerSocket(p);
                    Log.i(TAG, "Down stream server created.");
                } catch (Exception ex) {
                    Log.e(TAG, "Exception in creating down stream server...\n" + ex.toString());
                    exception = true;
                }
                try {
                    Log.i(TAG, "Waiting for connection...");
                    client = serverSocket.accept();
                    Log.i(TAG, "Connected.\nObtaining input stream...");
                    inputStream = client.getInputStream();
                    Log.i(TAG, "Done.");
                } catch (Exception ex) {
                    Log.e(TAG, "Exception in creating connection...\n" + ex.toString());
                    exception = true;
                }
                byte[] imgData = new byte[40054];
                while (true && !exception) {
                    int i = 0;
                    int d = -1;
                    try {
                        while ((d = inputStream.read(imgData, 0, 40054)) >= 0) {
                            {
                                bmp = BitmapFactory.decodeByteArray(imgData, 0, 40054);
                                {
                                    messageHandler.sendEmptyMessage(0);
                                    Log.i(TAG, "Done.");
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "Exception in reading...\n" + ex.toString());
                        exception = true;
                    }
                }
                try {
                    serverSocket.close();
                    client.close();
                    inputStream.close();
                } catch (Exception ex) {
                }
            }
        }
        DownStreamer ds = new DownStreamer(8234);
        new Thread(ds).start();
    }
}
