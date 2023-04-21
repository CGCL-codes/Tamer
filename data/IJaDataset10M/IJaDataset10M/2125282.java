package com.enoram.training.gwt.admin.client.activity;

import com.enoram.training.gwt.admin.client.ClientFactory;
import com.enoram.training.gwt.admin.client.place.DesktopPlace;
import com.enoram.training.gwt.admin.client.place.MessagePlace;
import com.enoram.training.gwt.admin.client.ui.DesktopView;
import com.enoram.training.gwt.admin.client.ui.MessageView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MessageActivity extends AbstractActivity implements MessageView.Presenter {

    private ClientFactory clientFactory;

    private String name = "";

    public MessageActivity(MessagePlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.name = place.getToken();
    }

    @Override
    public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
        final MessageView view = clientFactory.getMessageView();
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
    }

    /**
	 * Navigate to a new Place in the browser
	 */
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
