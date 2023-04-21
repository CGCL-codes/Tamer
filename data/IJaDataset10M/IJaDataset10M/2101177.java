package com.yuan.sms;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.yuan.sms.R;

public class ViewMessage extends Activity {

    private ListView listView;

    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.view);
        File file = new File("sdcard/Message.txt");
        if (file.exists()) {
            readMessage();
        } else {
            Toast.makeText(ViewMessage.this, "û�б��ݵĶ��ţ�", Toast.LENGTH_LONG).show();
            finish();
        }
        listView = (ListView) findViewById(R.id.listview);
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.listview_backup, new String[] { "content" }, new int[] { R.id.view_message });
        listView.setAdapter(adapter);
    }

    private void readMessage() {
        File f = null;
        StringBuilder text = new StringBuilder();
        f = new File("/sdcard/Message.txt");
        if (f != null) {
            InputStream in = null;
            BufferedReader br = null;
            String tmp;
            try {
                in = new BufferedInputStream(new FileInputStream(f));
                br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            try {
                while ((tmp = br.readLine()) != null) {
                    text.append(tmp);
                    text.append('\n');
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("content", text);
                list.add(map);
                br.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
