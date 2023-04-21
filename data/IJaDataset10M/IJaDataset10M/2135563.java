package com.google.code.gwt.iui.client.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;

/**
 * @author bguijt
 */
public class TextListItem extends ListItem implements HasText {

    public TextListItem() {
        super();
    }

    public TextListItem(String text) {
        super();
        setText(text);
    }

    public String getText() {
        return DOM.getInnerText(getElement());
    }

    public void setText(String text) {
        DOM.setInnerText(getElement(), (text == null) ? "" : text);
    }
}
