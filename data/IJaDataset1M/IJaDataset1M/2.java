package com.cnc.mediaconnect.search.asyn;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.os.Handler;
import com.cnc.mediaconnect.common.Common;
import com.cnc.mediaconnect.common.XMLfunctions;
import com.cnc.mediaconnect.data.Phone;
import com.cnc.mediaconnect1.RecordScreen;

public class LoadPhoneAsyn extends AsyncTask<String, String, String> {

    private RecordScreen recordScreen;

    private Handler handler = new Handler();

    List<Phone> lPhone = new ArrayList<Phone>();

    private int type;

    private String id;

    public LoadPhoneAsyn(RecordScreen recordScreen, int type, String id) {
        this.recordScreen = recordScreen;
        this.id = id;
        this.type = type;
    }

    protected String doInBackground(String... params) {
        if (type == Common._CONTACT) {
            lPhone = XMLfunctions.searchAllPhone(id);
            handler.post(new Runnable() {

                public void run() {
                    try {
                        recordScreen.addPhone(lPhone);
                    } catch (Exception e) {
                    }
                }
            });
        }
        return null;
    }
}
