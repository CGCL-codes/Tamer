package com.srplab.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Message;
import com.srplab.www.starcore.*;

public class Test_server_binActivity extends Activity {

    StarCoreFactory starcore;

    StarSrvGroupClass SrvGroup;

    Timer timer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        starcore = StarCoreFactory.GetFactory();
        StarServiceClass Service = starcore._InitSimple("test", "123", 3008, 0);
        SrvGroup = (StarSrvGroupClass) Service._Get("_ServiceGroup");
        Service._CreateSysRootItem("TestItem", "", null, null);
        StarServiceItemClass SrvItem = Service._GetSysRootItem("TestItem");
        StarObjectClass a = Service._NewGlobal(SrvItem, "TestObject");
        a = a._Assign(new StarObjectClass() {

            public StarBinBufClass GetFile(StarObjectClass self, String FileName) {
                System.out.println("remotecall" + self + FileName);
                StarBinBufClass buf = SrvGroup._NewBinBuf();
                if (buf._LoadFromFile(FileName, false) == true) {
                    return buf;
                } else {
                    return null;
                }
            }
        });
        System.out.println("Server Start ok....");
        timer = new Timer();
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                while (starcore._SRPDispatch(false) == true) ;
            }
        };
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Message mesasge = new Message();
                mesasge.what = 0;
                handler.sendMessage(mesasge);
            }
        }, 0, 10);
    }
}
