package org.axed.user.client.impl;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import org.axed.user.client.InputCatcher;

/**
 * General purpose implementation for InputCatcher. 
 */
public class InputCatcherImplStandard extends InputCatcherImpl {

    /**
	 * TODO
	 */
    public int onKeyEvent(Event event) {
        int type = DOM.eventGetType(event);
        int keycode = DOM.eventGetKeyCode(event);
        switch(type) {
            case Event.ONKEYDOWN:
                if (InputCatcher.isSpezial(keycode)) {
                    return -keycode;
                }
                if (DOM.eventGetCtrlKey(event)) {
                    return keycode;
                }
                return 0;
            case Event.ONKEYPRESS:
                if (keycode < 32) {
                    return 0;
                }
                return keycode;
            case Event.ONKEYUP:
                return 0;
            default:
                assert false;
                return 0;
        }
    }
}
