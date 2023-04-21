package org.truxingame.input.xinput;

public class XINPUT_GAMEPAD {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected XINPUT_GAMEPAD(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(XINPUT_GAMEPAD obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            XInputJNI.delete_XINPUT_GAMEPAD(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setWButtons(int value) {
        XInputJNI.XINPUT_GAMEPAD_wButtons_set(swigCPtr, this, value);
    }

    public int getWButtons() {
        return XInputJNI.XINPUT_GAMEPAD_wButtons_get(swigCPtr, this);
    }

    public void setBLeftTrigger(short value) {
        XInputJNI.XINPUT_GAMEPAD_bLeftTrigger_set(swigCPtr, this, value);
    }

    public short getBLeftTrigger() {
        return XInputJNI.XINPUT_GAMEPAD_bLeftTrigger_get(swigCPtr, this);
    }

    public void setBRightTrigger(short value) {
        XInputJNI.XINPUT_GAMEPAD_bRightTrigger_set(swigCPtr, this, value);
    }

    public short getBRightTrigger() {
        return XInputJNI.XINPUT_GAMEPAD_bRightTrigger_get(swigCPtr, this);
    }

    public void setSThumbLX(short value) {
        XInputJNI.XINPUT_GAMEPAD_sThumbLX_set(swigCPtr, this, value);
    }

    public short getSThumbLX() {
        return XInputJNI.XINPUT_GAMEPAD_sThumbLX_get(swigCPtr, this);
    }

    public void setSThumbLY(short value) {
        XInputJNI.XINPUT_GAMEPAD_sThumbLY_set(swigCPtr, this, value);
    }

    public short getSThumbLY() {
        return XInputJNI.XINPUT_GAMEPAD_sThumbLY_get(swigCPtr, this);
    }

    public void setSThumbRX(short value) {
        XInputJNI.XINPUT_GAMEPAD_sThumbRX_set(swigCPtr, this, value);
    }

    public short getSThumbRX() {
        return XInputJNI.XINPUT_GAMEPAD_sThumbRX_get(swigCPtr, this);
    }

    public void setSThumbRY(short value) {
        XInputJNI.XINPUT_GAMEPAD_sThumbRY_set(swigCPtr, this, value);
    }

    public short getSThumbRY() {
        return XInputJNI.XINPUT_GAMEPAD_sThumbRY_get(swigCPtr, this);
    }

    public XINPUT_GAMEPAD() {
        this(XInputJNI.new_XINPUT_GAMEPAD(), true);
    }
}
