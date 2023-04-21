package org.onemind.swingweb.client.gwt.widget;

import com.google.gwt.user.client.ui.Widget;

public interface WindowListener {

    public void windowMinimized(Widget sender);

    public void windowMaximized(Widget sender);

    public void windowClosed(Widget sender);
}
