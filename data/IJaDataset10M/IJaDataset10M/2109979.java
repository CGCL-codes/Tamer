package net.sipvip.client.activity;

import net.sipvip.client.ClientFactory;
import net.sipvip.client.place.HelloPlace;
import net.sipvip.client.ui.HelloNordPanel;
import net.sipvip.client.ui.PresenterNavigator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class NordPanelHelloActivity extends AbstractActivity implements PresenterNavigator {

    private ClientFactory clientFactory;

    private String name;

    public NordPanelHelloActivity(HelloPlace place, ClientFactory clientFactory) {
        this.name = place.getHelloName();
        this.clientFactory = clientFactory;
    }

    @Override
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        HelloNordPanel helloNordPanel = clientFactory.getHelloNordPanel();
        helloNordPanel.setName(name);
        helloNordPanel.setNavigator(this);
        panel.setWidget(helloNordPanel.asWidget());
    }

    public String mayStop() {
        GWT.log("mayStop Nord");
        return null;
    }

    public void onStop() {
        GWT.log("onStop Nord");
    }
}
