package org.com.cnc.common.android.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class CommonAsynTaskNone extends AsyncTask<String, String, String> {

    private ProgressDialog dialog = null;

    protected Handler handler = new Handler();

    private boolean isRun = false;

    private boolean isClose = false;

    private boolean isPause = false;

    private boolean isFirst = false;

    private Context context;

    public CommonAsynTaskNone(Context context) {
        this.context = context;
    }

    public void showDialog(final boolean isShow, final String title, final String msg) {
        Runnable runnable = new Runnable() {

            public void run() {
                if (isShow) {
                    dialog = ProgressDialog.show(getContext(), title, msg);
                } else if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        handler.post(runnable);
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    protected String doInBackground(String... arg0) {
        return null;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public Context getContext() {
        return context;
    }
}
