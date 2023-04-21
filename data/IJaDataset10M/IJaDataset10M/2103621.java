package gwtmodule1.client;

import gwtmodule1.client.gui.Main;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestModule1 implements EntryPoint {

    public void onModuleLoad() {
        RootPanel.get().add(new Main());
    }

    private void makeRPCCall() {
        TestServiceAsync s = TestService.Util.getInstance();
        AsyncCallback callback = new AsyncCallback() {

            public void onSuccess(Object result) {
                Window.alert("Result:" + result);
            }

            public void onFailure(Throwable caught) {
                Window.alert("Error:" + caught.getMessage());
            }
        };
        s.makeCalc("MeineNachricht", 45, callback);
    }
}
