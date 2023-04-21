package com.angis.fx.activity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.angis.fx.data.ChangsuoInformation;
import com.angis.fx.util.UploadUtil;

public class CsCoordUploadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        ChangsuoInformation lInfo = (ChangsuoInformation) intent.getExtras().get("csinfo");
        try {
            UploadUtil.uploadCsCoord(lInfo);
            Toast.makeText(this, "坐标更新成功！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "坐标更新失败，请稍后再试！", Toast.LENGTH_LONG).show();
        } finally {
            this.stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
