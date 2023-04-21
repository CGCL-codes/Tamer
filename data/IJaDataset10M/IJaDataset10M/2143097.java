package com.hellomvp.client.activity;

import com.hellomvp.client.ClientFactory;
import com.hellomvp.client.place.HelloPlace;
import com.hellomvp.client.ui.HelloLeftPanel;
import com.hellomvp.client.ui.PresenterNavigator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LeftPanelHelloActivity extends AbstractActivity implements PresenterNavigator {

    private ClientFactory clientFactory;

    private String name;

    public LeftPanelHelloActivity(HelloPlace place, ClientFactory clientFactory) {
        this.name = place.getHelloName();
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        HelloLeftPanel helloLeftPanel = clientFactory.getHelloLeftPanel();
        helloLeftPanel.setName(name);
        helloLeftPanel.setNavigator(this);
        panel.setWidget(helloLeftPanel.asWidget());
    }

    @Override
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
