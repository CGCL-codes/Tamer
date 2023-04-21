package com.luzan.common.gwt.ui.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.DOM;

/**
 * XFileUpload
 *
 * @author Alexander Bondar
 */
public class XFileUpload extends FileUpload {

    ChangeListenerCollection l = new ChangeListenerCollection();

    FocusListenerCollection f = new FocusListenerCollection();

    public XFileUpload() {
        super();
        sinkEvents(Event.ONCHANGE | Event.ONFOCUS | Event.ONBLUR);
    }

    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCHANGE) {
            l.fireChange(this);
        } else if (DOM.eventGetType(event) == Event.ONFOCUS) {
            f.fireFocus(this);
        } else if (DOM.eventGetType(event) == Event.ONBLUR) {
            f.fireLostFocus(this);
        }
        super.onBrowserEvent(event);
    }

    public void addChangeListener(ChangeListener cl) {
        l.add(cl);
    }

    public void addFocusListener(FocusListener fl) {
        f.add(fl);
    }
}
