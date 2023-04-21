package com.neolab.crm.client.fwk.containers;

import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class VerticalContainer extends Container<VerticalPanel> {

    public VerticalContainer(boolean render) {
        super(new VerticalPanel());
        if (render) render();
    }
}
