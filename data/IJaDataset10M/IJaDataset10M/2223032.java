package com.tapina.robe.swi.wimp;

import com.tapina.robe.runtime.ByteArray;

/**
 * This class represents an event which is queued by the Wimp and returned in response to a call to {@link com.tapina.robe.swi.Wimp#Poll(com.tapina.robe.runtime.Environment)}
 */
public abstract class WimpEvent {

    public static final int NO_REASON = 0;

    public static final int REDRAW_WINDOW = 1;

    public static final int OPEN_WINDOW = 2;

    public static final int CLOSE_WINDOW = 3;

    public static final int LEAVING_WINDOW = 4;

    public static final int ENTERING_WINDOW = 5;

    public static final int MOUSE_CLICK = 6;

    public static final int USER_DRAG_BOX = 7;

    public static final int KEY_PRESSED = 8;

    public static final int MENU_SELECTION = 9;

    public static final int SCROLL_REQUEST = 10;

    public static final int LOSE_CARET = 11;

    public static final int GAIN_CARET = 12;

    public static final int POLLWORD_NON_ZERO = 13;

    public static final int USER_MESSAGE = 17;

    public static final int USER_MESSAGE_RECORDED = 18;

    public static final int USER_MESSAGE_ACKNOWLEDGE = 19;

    public static final int TOOLBOX_EVENT = 512;

    WimpTask target;

    int reasonCode;

    public WimpEvent(WimpTask target, int reasonCode) {
        this.target = target;
        this.reasonCode = reasonCode;
    }

    public WimpTask getTarget() {
        return target;
    }

    public int getReasonCode() {
        return reasonCode;
    }

    public abstract void storeData(ByteArray byteArray);
}
