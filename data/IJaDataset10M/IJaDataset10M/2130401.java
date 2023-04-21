package com.gwtmobile.ui.kitchensink.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;

public class HeaderPanelPage extends Page {

    private static HeaderPanelPageUiBinder uiBinder = GWT.create(HeaderPanelPageUiBinder.class);

    interface HeaderPanelPageUiBinder extends UiBinder<Widget, HeaderPanelPage> {
    }

    public HeaderPanelPage() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
