package org.com.vnp.caothubongbong.asyn;

import org.com.cnc.common.adnroid16.Common;
import org.com.cnc.common.adnroid16.activity.CommonActivity;
import org.com.cnc.common.adnroid16.asyn.CommonAsyncTask;
import org.com.vnp.caothubongbong.CaothubongbongActivity;

public class AsynTime extends CommonAsyncTask {

    public AsynTime(CommonActivity activity) {
        super(activity);
        setClose(false);
        setPause(true);
    }

    protected String doInBackground(String... arg0) {
        while (!isClose()) {
            while (isPause()) {
                if (isClose()) {
                    return "";
                }
            }
            Common.sleep(1000);
            ((CaothubongbongActivity) getActivity()).updateTime();
            if (((CaothubongbongActivity) getActivity()).isClose()) {
                return "close";
            }
        }
        return super.doInBackground(arg0);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if ("close".equals(result)) {
            ((CaothubongbongActivity) getActivity()).updateScore();
        }
    }
}
