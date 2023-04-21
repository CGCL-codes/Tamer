package es.viewerfree.gwt.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import es.viewerfree.gwt.client.Constants;
import es.viewerfree.gwt.client.service.UserService;
import es.viewerfree.gwt.client.service.UserServiceAsync;

public class SessionTime extends Timer {

    private static final Constants constants = GWT.create(Constants.class);

    private static final UserServiceAsync userService = GWT.create(UserService.class);

    public static void start() {
        new SessionTime();
    }

    private SessionTime() {
        super();
        scheduleRepeating(constants.sessionTime());
    }

    @Override
    public void run() {
        userService.ping(new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void noParam) {
            }

            @Override
            public void onFailure(Throwable throwable) {
                Window.Location.replace(GWT.getHostPageBaseURL() + "?locale=" + LocaleInfo.getCurrentLocale().getLocaleName());
            }
        });
    }
}
