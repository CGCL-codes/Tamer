package com.android.phone;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;

/**
 * Helper class to keep track of enabledness, visibility, and "on/off"
 * or "checked" state of the various controls available in the in-call
 * UI, based on the current telephony state.
 *
 * This class is independent of the exact UI controls used on any given
 * device.  (Some devices use onscreen touchable buttons, for example, and
 * other devices use menu items.)  To avoid cluttering up the InCallMenu
 * and InCallTouchUi code with logic about which functions are available
 * right now, we instead have that logic here, and provide simple boolean
 * flags to indicate the state and/or enabledness of all possible in-call
 * user operations.
 *
 * (In other words, this is the "model" that corresponds to the "view"
 * implemented by InCallMenu and InCallTouchUi.)
 */
public class InCallControlState {

    private static final String LOG_TAG = "InCallControlState";

    private static final boolean DBG = (PhoneApp.DBG_LEVEL >= 2);

    private InCallScreen mInCallScreen;

    private Phone mPhone;

    public boolean manageConferenceVisible;

    public boolean manageConferenceEnabled;

    public boolean canAddCall;

    public boolean canSwap;

    public boolean canMerge;

    public boolean bluetoothEnabled;

    public boolean bluetoothIndicatorOn;

    public boolean speakerEnabled;

    public boolean speakerOn;

    public boolean canMute;

    public boolean muteIndicatorOn;

    public boolean dialpadEnabled;

    public boolean dialpadVisible;

    /** True if the "Hold" function is *ever* available on this device */
    public boolean supportsHold;

    /** True if the call is currently on hold */
    public boolean onHold;

    public boolean canHold;

    public InCallControlState(InCallScreen inCallScreen, Phone phone) {
        if (DBG) log("InCallControlState constructor...");
        mInCallScreen = inCallScreen;
        mPhone = phone;
    }

    /**
     * Updates all our public boolean flags based on the current state of
     * the Phone.
     */
    public void update() {
        final boolean hasRingingCall = !mPhone.getRingingCall().isIdle();
        final Call fgCall = mPhone.getForegroundCall();
        final Call.State fgCallState = fgCall.getState();
        final boolean hasActiveForegroundCall = (fgCallState == Call.State.ACTIVE);
        final boolean hasHoldingCall = !mPhone.getBackgroundCall().isIdle();
        int phoneType = mPhone.getPhoneType();
        if (phoneType == Phone.PHONE_TYPE_GSM) {
            manageConferenceVisible = PhoneUtils.isConferenceCall(fgCall);
            manageConferenceEnabled = manageConferenceVisible && !mInCallScreen.isManageConferenceMode();
        } else if (phoneType == Phone.PHONE_TYPE_CDMA) {
            manageConferenceVisible = false;
            manageConferenceEnabled = false;
        } else {
            throw new IllegalStateException("Unexpected phone type: " + phoneType);
        }
        canAddCall = PhoneUtils.okToAddCall(mPhone);
        canSwap = PhoneUtils.okToSwapCalls(mPhone);
        canMerge = PhoneUtils.okToMergeCalls(mPhone);
        if (mInCallScreen.isBluetoothAvailable()) {
            bluetoothEnabled = true;
            bluetoothIndicatorOn = mInCallScreen.isBluetoothAudioConnectedOrPending();
        } else {
            bluetoothEnabled = false;
            bluetoothIndicatorOn = false;
        }
        speakerEnabled = true;
        speakerOn = PhoneUtils.isSpeakerOn(mInCallScreen);
        if (phoneType == Phone.PHONE_TYPE_CDMA) {
            Connection c = fgCall.getLatestConnection();
            boolean isEmergencyCall = false;
            if (c != null) isEmergencyCall = PhoneNumberUtils.isEmergencyNumber(c.getAddress());
            if (isEmergencyCall) {
                canMute = false;
                muteIndicatorOn = false;
            } else {
                canMute = hasActiveForegroundCall;
                muteIndicatorOn = PhoneUtils.getMute(mPhone);
            }
        } else if (phoneType == Phone.PHONE_TYPE_GSM) {
            canMute = hasActiveForegroundCall;
            muteIndicatorOn = PhoneUtils.getMute(mPhone);
        }
        dialpadEnabled = mInCallScreen.okToShowDialpad();
        dialpadVisible = mInCallScreen.isDialerOpened();
        if (phoneType == Phone.PHONE_TYPE_GSM) {
            supportsHold = true;
            onHold = hasHoldingCall && (fgCallState == Call.State.IDLE);
            boolean okToHold = hasActiveForegroundCall && !hasHoldingCall;
            boolean okToUnhold = onHold;
            canHold = okToHold || okToUnhold;
        } else if (phoneType == Phone.PHONE_TYPE_CDMA) {
            supportsHold = false;
            onHold = false;
            canHold = false;
        }
        if (DBG) dumpState();
    }

    public void dumpState() {
        log("InCallControlState:");
        log("  manageConferenceVisible: " + manageConferenceVisible);
        log("  manageConferenceEnabled: " + manageConferenceEnabled);
        log("  canAddCall: " + canAddCall);
        log("  canSwap: " + canSwap);
        log("  canMerge: " + canMerge);
        log("  bluetoothEnabled: " + bluetoothEnabled);
        log("  bluetoothIndicatorOn: " + bluetoothIndicatorOn);
        log("  speakerEnabled: " + speakerEnabled);
        log("  speakerOn: " + speakerOn);
        log("  canMute: " + canMute);
        log("  muteIndicatorOn: " + muteIndicatorOn);
        log("  dialpadEnabled: " + dialpadEnabled);
        log("  dialpadVisible: " + dialpadVisible);
        log("  onHold: " + onHold);
        log("  canHold: " + canHold);
    }

    private void log(String msg) {
        Log.d(LOG_TAG, msg);
    }
}
