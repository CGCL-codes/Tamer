package org.synthful.smartgwt.client.widgets;

import com.google.gwt.user.client.ui.HasHTML;
import com.smartgwt.client.widgets.HTMLFlow;

public class UIHTMLFlow extends HTMLFlow implements HasHTML {

    @Override
    public String getText() {
        return super.getContents();
    }

    @Override
    public void setText(String text) {
        super.setContents(text);
    }

    @Override
    public String getHTML() {
        return super.getContents();
    }

    @Override
    public void setHTML(String html) {
        super.setContents(html);
    }
}
