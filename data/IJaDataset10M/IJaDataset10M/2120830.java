package com.matrixbi.adansi.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.matrixbi.adansi.client.gui.ContentTabController;
import com.matrixbi.adansi.client.gui.GuiController;
import com.matrixbi.adansi.client.gui.GuiEvents;
import com.matrixbi.adansi.client.gui.GuiView;
import com.matrixbi.adansi.client.gui.NavigationController;
import com.matrixbi.adansi.client.service.ServiceFactory;
import com.matrixbi.adansi.ocore.client.ExplorerObject;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AdAnSi implements EntryPoint {

    private Dispatcher dispatcher;

    public static String objects = "objects";

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        init();
    }

    private void init() {
        DOM.setInnerHTML(RootPanel.get("loading-msg").getElement(), "Loading objects...");
        ServiceFactory.getPersistenceService().getObjectsInTree(new AsyncCallback<ExplorerObject>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ExplorerObject result) {
                Registry.register(objects, result);
                DOM.setInnerHTML(RootPanel.get("loading-msg").getElement(), "Loading gui...");
                loadGUI();
            }
        });
    }

    private void loadGUI() {
        dispatcher = Dispatcher.get();
        dispatcher.addController(new GuiController());
        dispatcher.addController(new NavigationController());
        dispatcher.addController(new ContentTabController());
        dispatcher.dispatch(GuiEvents.Init);
        String hash = Window.Location.getHash();
        showPage(new ExplorerObject("welcome", "Welcome", "welcome", "all", "all", "public"));
        Viewport v = Registry.get(GuiView.VIEWPORT);
        v.layout(true);
        ExplorerObject root = (ExplorerObject) Registry.get(objects);
        if (!"".equals(hash)) {
            hash = hash.substring(1);
            ExplorerObject object = root.getExplorerObject(hash.replace("#", ""));
            if (!object.equals(null)) {
                showPage(object);
            }
        }
        DOM.setInnerHTML(RootPanel.get("loading").getElement(), "");
    }

    public static void showPage(ExplorerObject object) {
        AppEvent appEvent = new AppEvent(GuiEvents.ShowPage, object);
        appEvent.setHistoryEvent(true);
        appEvent.setToken(object.getId());
        Dispatcher.forwardEvent(appEvent);
    }
}
