package com.yingyonghui.market.model;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;
import com.yingyonghui.market.FileManager;
import dalvik.annotation.Signature;

public class DialogExPreference extends DialogPreference {

    private Context context;

    public DialogExPreference(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
    }

    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
        onClick(paramDialogInterface, paramInt);
        if (paramInt == -1) ;
        try {
            ClearTask localClearTask = new ClearTask(null);
            String[] arrayOfString = new String[1];
            arrayOfString[0] = "";
            localClearTask.execute(arrayOfString);
            return;
        } catch (Throwable localThrowable) {
            while (true) localThrowable.printStackTrace();
        }
    }

    @Signature({ "Landroid/os/AsyncTask", "<", "Ljava/lang/String;", "Ljava/lang/Integer;", "Ljava/lang/Boolean;", ">;" })
    class ClearTask extends AsyncTask {

        private ClearTask() {
        }

        protected Boolean doInBackground(String[] paramArrayOfString) {
            FileManager.deleteAllFiles(DialogExPreference.this.context);
            return Boolean.valueOf(1);
        }

        protected void onPostExecute(Boolean paramBoolean) {
            if (paramBoolean.booleanValue()) Toast.makeText(DialogExPreference.this.context, 2131296476, 0).show();
        }

        protected void onProgressUpdate(Integer[] paramArrayOfInteger) {
        }
    }
}
