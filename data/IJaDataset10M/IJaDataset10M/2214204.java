package org.systemsbiology.PIPE2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import org.systemsbiology.PIPE2.client.Controllers.PIPEController;
import org.systemsbiology.PIPE2.client.view.*;

public class PIPE2 implements EntryPoint {

    PIPEController pcontroller;

    PIPEViewController vcontroller;

    public void onModuleLoad() {
        vcontroller = new MobileWindowsViewController();
        pcontroller = new PIPEController(vcontroller);
        pcontroller.init();
        pcontroller.setupDevEnv();
        if (vcontroller.getRoot() != null) RootPanel.get().add(vcontroller.getRoot());
        AjaxLoader.AjaxLoaderOptions options = AjaxLoader.AjaxLoaderOptions.newInstance();
        options.setPackages("Table");
        AjaxLoader.loadApi("visualization", "1", new Runnable() {

            public void run() {
                GWT.log("Google Visualizations loaded", null);
            }
        }, options);
    }
}
