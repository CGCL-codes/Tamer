package com.threerings.gwt.ui;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Helper class that centers a single widget horizontally and vertically. Using a TABLE!
 */
public class CenteredBox extends HorizontalPanel {

    public CenteredBox(Widget widget, String styleName) {
        super();
        if (styleName != null) {
            setStyleName(styleName);
        }
        setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        if (widget != null) {
            add(widget);
        }
    }

    public CenteredBox(Widget widget, String styleName, int width, int height) {
        this(widget, styleName);
        if (width > 0) {
            setWidth(width + "px");
        }
        if (height > 0) {
            setHeight(height + "px");
        }
    }
}
