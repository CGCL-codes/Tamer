package eolus.client;

import java.util.Date;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EolusUI implements EntryPoint {

    private ContactServerServiceAsync serverCall = GWT.create(ContactServerService.class);

    private final UserInfo user = UserInfo.getUserInfo();

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        EntityManager.init();
        setTimeandIp();
        setUserAccess();
        setUserData();
    }

    private void SetUp() {
        Element e = RootPanel.get("username").getElement();
        if (user.isAdmin() == true) {
            e.setInnerHTML("Welcome Admin");
            RootPanel.get("tab6").removeStyleName("hidden");
            RootPanel.get("tab5").removeStyleName("hidden");
            RootPanel.get("tab1").removeStyleName("hidden");
        } else {
            e.setInnerHTML("Welcome User");
        }
        final PageLoader loader = new PageLoader();
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            public void onValueChange(ValueChangeEvent<String> event) {
                loader.Load(event.getValue());
            }
        });
        if (History.getToken().length() == 0) {
            History.newItem("home");
        } else History.fireCurrentHistoryState();
    }

    private void setUserAccess() {
        Button button = new Button("Logout");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                serverCall.logout(new AsyncCallback<Boolean>() {

                    public void onFailure(Throwable caught) {
                        Window.alert("Could not logout");
                    }

                    public void onSuccess(Boolean result) {
                        if (!result) Window.alert("Could not logout"); else {
                            String url = Window.Location.getHref();
                            int last_sep = url.lastIndexOf("/");
                            String new_url = url.substring(0, last_sep);
                            Window.Location.assign(new_url);
                        }
                    }
                });
            }
        });
        RootPanel.get("user_access").add(button);
    }

    void setTimeandIp() {
        Timer refreshTimer = new Timer() {

            @Override
            public void run() {
                refreshTime();
            }
        };
        refreshTimer.scheduleRepeating(1000);
        refreshTime();
        serverCall.getRemoteIp(new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                RootPanel.get("ip").getElement().setInnerHTML(caught.getMessage());
            }

            public void onSuccess(String reply) {
                RootPanel.get("ip").getElement().setInnerHTML(reply);
            }
        });
    }

    void refreshTime() {
        RootPanel.get("time").getElement().setInnerHTML(DateTimeFormat.getMediumTimeFormat().format(new Date()));
    }

    void setUserData() {
        final AsyncCallback<Boolean> logout_callback = new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {
                Window.alert("Could not logout!");
            }

            public void onSuccess(Boolean result) {
                String url = Window.Location.getHref();
                int last_sep = url.lastIndexOf("/");
                String new_url = url.substring(0, last_sep);
                Window.Location.assign(new_url);
            }
        };
        final Element e = RootPanel.get("username").getElement();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                e.setInnerHTML("Could not get username.");
                serverCall.logout(logout_callback);
            }

            public void onSuccess(String reply) {
                e.setInnerHTML("Welcome <strong>" + reply + "</strong>");
                user.setUsername(reply);
            }
        };
        serverCall.getUsername(callback);
        serverCall.isAdmin(new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {
                Element e = RootPanel.get("username").getElement();
                e.setInnerHTML("Could not get user role.");
                serverCall.logout(logout_callback);
            }

            public void onSuccess(Boolean admin) {
                user.setIsAdmin(admin);
                SetUp();
            }
        });
    }
}
