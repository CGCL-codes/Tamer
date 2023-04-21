package org.nymph.error;

import org.nymph.R;
import org.nymph.Test_UI;
import android.app.ApplicationContext;
import android.app.NotificationManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.AlertDialog;
import android.content.Resources;
import android.util.Log;

/**
 * 
 *
 * @author Sergey Krutsenko
 *
 * @version 1.0
 *
 */
public class ErrorLogHandler implements IErrorLogHandler {

    final String MSG_TAG = "ErrorLogHandler";

    final String LOG_FILE = "nymph_log.txt";

    private ApplicationContext mContext;

    private NotificationManager mNM;

    private Resources mResources;

    private Intent mInvokeIntent;

    private final int ERR_MSG_ERR = 0;

    private final int ERR_MSG_WRN = 1;

    private final int ERR_MSG_DBG = 2;

    private final int ERR_ID = 200;

    private final int ERR_ID_ERR = 201;

    private final int ERR_ID_ERR_TEXT = 202;

    private boolean mSilent = false;

    private boolean mDebug = false;

    /**
	 * Creates a new <code>ErrorLogHandler</code> instance.	 * 
	 * @param context The ApplicationContext with information about an application environment
	 */
    public ErrorLogHandler(ApplicationContext context) {
        super();
        mContext = context;
        mNM = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mResources = mContext.getResources();
        mInvokeIntent = new Intent(mContext, Test_UI.class);
    }

    /**
	 * Clears Error Handler log file
	 */
    public void clearLog() {
        mContext.deleteFile(LOG_FILE);
    }

    /**
	 * Logs debug message.	 * 
	 * @param tag The String defines source class of error message
	 * @param msg The String provides error message itself
	 */
    public void setDebugMessage(String tag, String msg) {
        if (mDebug) logMessage(tag, msg, ERR_MSG_DBG);
    }

    /**
	 * Logs error message.	 * 
	 * @param tag The String defines source class of error message
	 * @param msg The String provides error message itself
	 */
    public void setErrorMessage(String tag, String msg) {
        if (mDebug) logMessage(tag, msg, ERR_MSG_ERR);
        showMessage(mResources.getString(R.string.err_error_title), msg);
    }

    /**
	 * Turns on/off debug mode for log handling.	 * 
	 * @param flag The boolean flag to set mode
	 */
    public void setDebug(boolean flag) {
        mDebug = flag;
    }

    /**
	 * Turns on/off silent mode for error handling.	 * 
	 * @param flag The boolean flag to set mode
	 */
    public void setSilent(boolean flag) {
        mSilent = flag;
    }

    /**
	 * Logs warning message.	  
	 * @param tag The String defines source class of error message
	 * @param msg The String provides error message itself
	 */
    public void setWarningMessage(String tag, String msg) {
        if (mDebug) logMessage(tag, msg, ERR_MSG_WRN);
        showMessage(mResources.getString(R.string.err_warning_title), msg);
    }

    /**
	 * Notifies host about error with persistent notification in status bar.	 * 
	 * @param tag The String defines source class of message
	 * @param src The String defines readable source of message
	 * @param msg The String provides message
	 * @param invoke The Intent to be launched if the user selects 
	 * error icon notification (if null will be used internal one)
	 */
    public void notifyError(String tag, String src, String msg, Intent invoke) {
        if (mDebug) logMessage(tag, msg, ERR_MSG_ERR);
        if (invoke == null) invoke = mInvokeIntent;
        Notification notification = new Notification(R.drawable.error, src, invoke, null, null);
        mNM.notify(ERR_ID_ERR, notification);
        if (!mSilent) {
            notifyMessageToHost(src, msg);
        }
    }

    /**
	 * Notifies host about warning with warning persistent icon in status bar.	 * 
	 * @param tag The String defines source class of message
	 * @param src The String defines readable source of message
	 * @param msg The String provides message
	 * @param invoke The Intent to be launched if the user selects 
	 * warning icon notification (if null will be used internal one) 
	 */
    public void notifyWarning(String tag, String src, String msg, Intent invoke) {
        if (mDebug) logMessage(tag, msg, ERR_MSG_WRN);
        if (invoke == null) invoke = mInvokeIntent;
        Notification notification = new Notification(R.drawable.warning, src, invoke, null, null);
        mNM.notify(ERR_ID_ERR, notification);
        if (!mSilent) {
            notifyMessageToHost(src, msg);
        }
    }

    /**
	 * Cancels notification and clears notification icon in status bar.	 */
    public void cancelNotification() {
        mNM.cancel(ERR_ID_ERR);
    }

    /**
	 * Provides access to log file.
	 * @return The FileInputStream to read data from log file (can be null)
	 */
    public FileInputStream getLog() {
        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(LOG_FILE);
        } catch (IOException e) {
            Log.e(MSG_TAG, e.getMessage());
        }
        return fis;
    }

    /**
	 * Writes message to log file.	 
	 * @param tag The String defines source class of message
	 * @param msg The String provides message
	 * @param level The int defines type of message
	 */
    private synchronized void logMessage(String tag, String msg, int level) {
        switch(level) {
            case ERR_MSG_ERR:
                Log.e(tag, msg);
                break;
            case ERR_MSG_WRN:
                Log.w(tag, msg);
                break;
            case ERR_MSG_DBG:
                Log.d(tag, msg);
                break;
        }
        FileOutputStream fos = null;
        try {
            try {
                fos = mContext.openFileOutput(LOG_FILE, Context.MODE_WORLD_READABLE);
                StringBuilder builder = new StringBuilder();
                builder.append(tag);
                builder.append("\t");
                builder.append(msg);
                fos.write(builder.toString().getBytes());
                fos.flush();
            } finally {
                if (fos != null) fos.close();
            }
        } catch (IOException e) {
            Log.e(MSG_TAG, e.getMessage());
        }
    }

    /**
     * Tells the user about something.     
     * @param src The String defines source of error message
     * @param msg The String message to send
     */
    private void notifyMessageToHost(String src, String msg) {
        StringBuilder builder = new StringBuilder(src);
        builder.append(" ");
        builder.append(msg);
        mNM.notifyWithText(ERR_ID_ERR_TEXT, builder.toString(), NotificationManager.LENGTH_SHORT, null);
    }

    /**
	 * Shows message. <strong>Do not use it to show dialog from separate thread!</strong>
	 * @param title The String defines title in show dialog
	 * @param msg The String defines message to display
	 */
    private void showMessage(String title, String msg) {
        AlertDialog.show(mContext, title.subSequence(0, title.length()), msg.subSequence(0, msg.length()), mResources.getText(R.string.btn_ok), false);
    }
}
