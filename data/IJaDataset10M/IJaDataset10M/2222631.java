package com.pavco.caribbeanvisit.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class CaribbeanVisit implements EntryPoint {

    public static final MyResources resources = GWT.create(MyResources.class);

    @Override
    public void onModuleLoad() {
        Window.setMargin("0px");
        MyResources.INSTANCE.caribbeanVisitCss().ensureInjected();
        HandlerManager eventBus = new HandlerManager(null);
        RpcServiceAsync rpcService = GWT.create(RpcService.class);
        AppController appViewer = new AppController(rpcService, eventBus);
        appViewer.go(RootLayoutPanel.get());
    }
}
