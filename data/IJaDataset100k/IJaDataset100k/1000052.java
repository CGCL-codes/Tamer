package de.cachebox_test.Components;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ListenToPhoneState extends PhoneStateListener {

    private int recentState = -1;

    public void onCallStateChanged(int state, String incomingNumber) {
        Lg.info("ListenToPhoneState.onCallStateChanged(state=" + state + ", incomingNumber=" + incomingNumber + ") " + "... state changed=" + stateName(state));
        if (this.recentState == -1 && state == TelephonyManager.CALL_STATE_IDLE) {
        }
    }

    private String stateName(final int state) {
        switch(state) {
            case TelephonyManager.CALL_STATE_IDLE:
                return "Idle";
            case TelephonyManager.CALL_STATE_OFFHOOK:
                return "Off hook";
            case TelephonyManager.CALL_STATE_RINGING:
                return "Ringing";
        }
        return Integer.toString(state);
    }
}
