package cc.w3d.jawos.GPageBuilder.client;

import cc.w3d.jawos.GPageBuilder.client.structure.Xlet;
import cc.w3d.jawos.GPageBuilder.client.structure.XletData;
import cc.w3d.jawos.GPageBuilder.client.xlets.horizontalPanelXlet.HorizontalPanelXlet;
import cc.w3d.jawos.GPageBuilder.client.xlets.htmlXlet.HtmlXlet;
import cc.w3d.jawos.GPageBuilder.client.xlets.verticalPanelXlet.VerticalPanelXlet;
import cc.w3d.jawos.gpageBuilderService.client.rpc.GPageBuilderService;
import cc.w3d.jawos.gpageBuilderService.client.rpc.GPageBuilderServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class App implements EntryPoint {

    public static GPageBuilderServiceAsync service = null;

    public void initService() {
        VerticalPanelXlet.initialize();
        HorizontalPanelXlet.initialize();
        HtmlXlet.initialize();
        service = (GPageBuilderServiceAsync) GWT.create(GPageBuilderService.class);
    }

    public void onModuleLoad() {
        initService();
        service.getRoot(new AsyncCallback<XletData>() {

            public void onSuccess(XletData root) {
                Xlet child = Xlet.getInstance(root, RootPanel.get());
            }

            public void onFailure(Throwable arg0) {
                service.initFakeData(new AsyncCallback<Void>() {

                    public void onSuccess(Void arg0) {
                        Window.alert("Fake content added");
                    }

                    public void onFailure(Throwable arg0) {
                        Window.alert(arg0.getMessage());
                    }
                });
            }
        });
    }
}
