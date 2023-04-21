package org.thechiselgroup.choosel.workbench.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Provider;

public class RootPanelProvider implements Provider<AbsolutePanel> {

    @Override
    public AbsolutePanel get() {
        return RootPanel.get();
    }
}
