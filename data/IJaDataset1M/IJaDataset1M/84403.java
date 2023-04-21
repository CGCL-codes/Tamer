package org.mvz.gwt.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;

public class ActionLink extends Label {

    public ActionLink(String text, ClickListener listener) {
        super(text);
        addClickListener(listener);
        setStyleName("actionLink");
    }
}
