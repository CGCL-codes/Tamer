package es.us.isw2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import es.us.isw2.client.forms.LoginForm;
import es.us.isw2.client.viewsAndPanels.MainPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainModule implements EntryPoint {

    public void onModuleLoad() {
        GWT.log("Welcolme , Your session ID is: " + ClientSession.getSessionID());
        GWT.log("Welcolme , Your session mailUser is: " + ClientSession.getUserMailInSession());
        if (ClientSession.getSessionID() == "" || ClientSession.getSessionID() == null) {
            LoginForm l = new LoginForm();
            l.go();
        } else {
            RootPanel.get("loginForm").setVisible(false);
            MainPanel mainForm = new MainPanel();
            RootPanel.get().add(mainForm);
        }
    }
}
