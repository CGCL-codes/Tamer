package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.provider.Settings.Secure;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyProperties;

public class CdmaSystemSelectListPreference extends ListPreference {

    private static final String LOG_TAG = "CdmaRoamingListPreference";

    private static final boolean DBG = true;

    private Phone mPhone;

    private MyHandler mHandler = new MyHandler();

    ;

    public CdmaSystemSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPhone = PhoneFactory.getDefaultPhone();
        mHandler = new MyHandler();
        mPhone.queryCdmaRoamingPreference(mHandler.obtainMessage(MyHandler.MESSAGE_GET_ROAMING_PREFERENCE));
    }

    public CdmaSystemSelectListPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void showDialog(Bundle state) {
        if (Boolean.parseBoolean(SystemProperties.get(TelephonyProperties.PROPERTY_INECM_MODE))) {
        } else {
            super.showDialog(state);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult && (getValue() != null)) {
            int buttonCdmaRoamingMode = Integer.valueOf(getValue()).intValue();
            int settingsCdmaRoamingMode = Secure.getInt(mPhone.getContext().getContentResolver(), Secure.CDMA_ROAMING_MODE, Phone.CDMA_RM_HOME);
            if (buttonCdmaRoamingMode != settingsCdmaRoamingMode) {
                int statusCdmaRoamingMode;
                switch(buttonCdmaRoamingMode) {
                    case Phone.CDMA_RM_ANY:
                        statusCdmaRoamingMode = Phone.CDMA_RM_ANY;
                        break;
                    case Phone.CDMA_RM_HOME:
                    default:
                        statusCdmaRoamingMode = Phone.CDMA_RM_HOME;
                }
                Secure.putInt(mPhone.getContext().getContentResolver(), Secure.CDMA_ROAMING_MODE, buttonCdmaRoamingMode);
                mPhone.setCdmaRoamingPreference(statusCdmaRoamingMode, mHandler.obtainMessage(MyHandler.MESSAGE_SET_ROAMING_PREFERENCE));
            }
        } else {
            Log.d(LOG_TAG, String.format("onDialogClosed: positiveResult=%b value=%s -- do nothing", positiveResult, getValue()));
        }
    }

    private class MyHandler extends Handler {

        private static final int MESSAGE_GET_ROAMING_PREFERENCE = 0;

        private static final int MESSAGE_SET_ROAMING_PREFERENCE = 1;

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MESSAGE_GET_ROAMING_PREFERENCE:
                    handleQueryCdmaRoamingPreference(msg);
                    break;
                case MESSAGE_SET_ROAMING_PREFERENCE:
                    handleSetCdmaRoamingPreference(msg);
                    break;
            }
        }

        private void handleQueryCdmaRoamingPreference(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar.exception == null) {
                int statusCdmaRoamingMode = ((int[]) ar.result)[0];
                int settingsRoamingMode = Secure.getInt(mPhone.getContext().getContentResolver(), Secure.CDMA_ROAMING_MODE, Phone.CDMA_RM_HOME);
                if (statusCdmaRoamingMode == Phone.CDMA_RM_HOME || statusCdmaRoamingMode == Phone.CDMA_RM_ANY) {
                    if (statusCdmaRoamingMode != settingsRoamingMode) {
                        settingsRoamingMode = statusCdmaRoamingMode;
                        Secure.putInt(mPhone.getContext().getContentResolver(), Secure.CDMA_ROAMING_MODE, settingsRoamingMode);
                    }
                    setValue(Integer.toString(statusCdmaRoamingMode));
                } else {
                    if (DBG) Log.i(LOG_TAG, "reset cdma roaming mode to default");
                    resetCdmaRoamingModeToDefault();
                }
            }
        }

        private void handleSetCdmaRoamingPreference(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;
            if ((ar.exception == null) && (getValue() != null)) {
                int cdmaRoamingMode = Integer.valueOf(getValue()).intValue();
                Secure.putInt(mPhone.getContext().getContentResolver(), Secure.CDMA_ROAMING_MODE, cdmaRoamingMode);
            } else {
                mPhone.queryCdmaRoamingPreference(obtainMessage(MESSAGE_GET_ROAMING_PREFERENCE));
            }
        }

        private void resetCdmaRoamingModeToDefault() {
            setValue(Integer.toString(Phone.CDMA_RM_HOME));
            Secure.putInt(mPhone.getContext().getContentResolver(), Secure.CDMA_ROAMING_MODE, Phone.CDMA_RM_HOME);
            mPhone.setCdmaRoamingPreference(Phone.CDMA_RM_HOME, obtainMessage(MyHandler.MESSAGE_SET_ROAMING_PREFERENCE));
        }
    }
}
