package com.matrixbi.adansi.client.gui;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

public class GuiController extends Controller {

    private GuiView view;

    public GuiController() {
        view = new GuiView(this);
        registerEventTypes(GuiEvents.Init);
    }

    @Override
    public void handleEvent(AppEvent event) {
        if (event.getType() == GuiEvents.Init) {
            forwardToView(view, event);
        }
    }
}
