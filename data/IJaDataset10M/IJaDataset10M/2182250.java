package com.simconomy.twitter.web.client.commons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class Paragraph extends AbsolutePanel {

    public Paragraph() {
        super(DOM.createElement("p"));
    }
}
