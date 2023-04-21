package ua.org.hatu.daos.gwt.client.activity;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import ua.org.hatu.daos.gwt.client.ClientCookies;
import ua.org.hatu.daos.gwt.client.ClientFactory;
import ua.org.hatu.daos.gwt.client.place.SignInPlace;
import ua.org.hatu.daos.gwt.client.place.SignUpPlace;
import ua.org.hatu.daos.gwt.client.presenters.BasicHeaderPresenter;
import ua.org.hatu.daos.gwt.client.view.LogOutView;
import ua.org.hatu.daos.gwt.shared.services.DaosServiceAsync;

/**
 * @author zeus (alex.pogrebnyuk@gmail.com)
 * @author dmytro (pogrebniuk@gmail.com)
 */
public class LogOutActivity extends AbstractActivity implements BasicHeaderPresenter {

    private static final Logger logger = Logger.getLogger(LogOutActivity.class.getName());

    private DaosServiceAsync service;

    private LogOutView view;

    private PlaceController placeController;

    private Place place;

    public LogOutActivity(ClientFactory clientFactory, Place place) {
        this.placeController = clientFactory.getPlaceController();
        this.view = clientFactory.getBasicHeaderView();
        this.service = clientFactory.getDaosService();
        this.place = place;
        changeLogoutAnchor();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        getLogger().log(Level.INFO, "Starting [BasicHeaderActivity] activity ...");
        view.setPresenter(this);
        panel.setWidget(view.asWidget());
    }

    public void onLogoutAnchorClicked() {
        if (place instanceof SignInPlace) {
            placeController.goTo(new SignUpPlace());
        } else {
            service.logout(ClientCookies.getUsernameCookie(), new AsyncCallback<Void>() {

                @Override
                public void onSuccess(Void result) {
                }

                @Override
                public void onFailure(Throwable caught) {
                }
            });
            ClientCookies.removeAllClientCookies();
            placeController.goTo(new SignInPlace());
        }
    }

    Logger getLogger() {
        return logger;
    }

    private void changeLogoutAnchor() {
        if (place instanceof SignInPlace) {
            view.getLogoutPanel().getLogoutAnchor().setText("Not a user? Sign up now!");
        } else if (place instanceof SignUpPlace) {
            view.getLogoutPanel().getLogoutAnchor().setText(null);
        } else {
            view.getLogoutPanel().getLogoutAnchor().setText("Logout");
        }
    }
}
