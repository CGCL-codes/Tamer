package org.mcarthur.sandy.gwt.plugin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * JSNI between GWT and Flash method calls.
 *
 * @author Sandy McArthur
 */
class FlashMethods implements Flash.Methods {

    private final Flash flash;

    private final Element flashElement;

    FlashMethods(final Flash flash) {
        this.flash = flash;
        flashElement = flash.getElement();
    }

    public native void gotoFrame(int frame);

    public native boolean isPlaying();

    public void pan(final int x, final int y, final PanMode mode) {
        callPan(x, y, mode.getMode());
    }

    private native void callPan(int x, int y, int mode);

    public native int percentLoaded();

    public native void play();

    public native void rewind();

    public native void setZoomRect(int left, int top, int right, int bottom);

    public native void stopPlay();

    public native int totalFrames();

    public native void zoom(int zoom);

    public native void loadMovie(int layer, String url);

    public native int tCurrentFrame(String target);

    public native String tCurrentLabel(String target);

    public native void tGotoFrame(String target, int frame);

    public native void tGotoLabel(String target, String frameLabel);

    public native void tPlay(String target);

    public native void tStopPlay(String target);

    public native String getVariable(String varName);

    public native void setVariable(String varName, String value);

    public native void tCallFrame(String target, int frame);

    public native void tCallLabel(String target, String frameLabel);

    public String tGetProperty(final String target, final Property property) {
        return callTGetProperty(target, property.getPropertyNumber());
    }

    private native String callTGetProperty(String target, int propertyNumber);

    public int tGetPropertyAsNumber(final String target, final Property property) {
        return callTGetPropertyAsNumber(target, property.getPropertyNumber());
    }

    private native int callTGetPropertyAsNumber(String target, int propertyNumber);

    public void tSetProperty(final String target, final Property property, final String value) {
        callTSetProperty(target, property.getPropertyNumber(), value);
    }

    private native void callTSetProperty(String target, int propertyNumber, String value);

    public void tSetProperty(final String target, final Property property, final int value) {
        callTSetProperty(target, property.getPropertyNumber(), value);
    }

    private native void callTSetProperty(String target, int propertyNumber, int value);

    /**
     * This is just here to make unused inspections happy.
     */
    protected void discardedByTheCompiler() {
        if (flash == null || flashElement == null) {
            GWT.log("This should never be called", null);
        }
    }
}
