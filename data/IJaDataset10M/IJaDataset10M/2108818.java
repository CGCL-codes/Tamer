package com.angis.fx.activity.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.angis.fx.activity.Enforcement;

public class CheckInfoService extends Service {

    private int mTotalCount;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "场所查询已运行!", Toast.LENGTH_LONG).show();
        try {
            handleSearchRequest(intent.getStringExtra("csid"));
        } catch (Exception e) {
            Toast.makeText(this, "场所查询报错!", Toast.LENGTH_LONG).show();
        } finally {
            this.stopSelf();
        }
    }

    private void handleSearchRequest(String pCsId) {
        HttpClient lClient = new DefaultHttpClient();
        StringBuilder lBuilder = new StringBuilder();
        HttpGet lGet = new HttpGet();
        HttpResponse lResponse;
        BufferedReader reader;
        try {
            if (mTotalCount == 0) {
                lGet.setURI(new URI(lBuilder.append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_WHCS.asmx/GetJCInfoByWHCSID").append("?").append("whcsID=").append(pCsId).toString()));
                lResponse = lClient.execute(lGet);
                lBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(lResponse.getEntity().getContent()));
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    lBuilder.append(s);
                }
            }
            Intent intent = new Intent("checkinfo");
            intent.putExtra("results", lBuilder.toString());
            this.sendBroadcast(intent);
        } catch (Exception e) {
            Toast.makeText(this, "获取信息出错!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
