package com.ncs.crm.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.layout.VLayout;

public class Hello implements EntryPoint {

    public void onModuleLoad() {
        VLayout layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setBackgroundColor("yellow");
        layout.draw();
    }
}
