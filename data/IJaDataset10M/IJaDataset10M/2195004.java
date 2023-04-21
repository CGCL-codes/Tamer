package org.lbroussal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PureMVC4GWTLoginDemo implements EntryPoint {

    private ApplicationFacade facade;

    public void onModuleLoad() {
        facade = ApplicationFacade.getInst();
        this.facade.start(this);
    }

    public void exitApp() {
        onModuleLoad();
    }

    public void setCurrentDisplay(Composite page) {
        RootPanel.get().clear();
        RootPanel.get().add(page);
    }
}
