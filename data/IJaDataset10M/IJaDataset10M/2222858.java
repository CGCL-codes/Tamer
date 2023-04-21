package com.enoram.training.gwt.admin.client.activity;

import com.enoram.training.gwt.admin.client.ClientFactory;
import com.enoram.training.gwt.admin.client.place.AboutPlace;
import com.enoram.training.gwt.admin.client.ui.AboutView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AboutActivity extends AbstractActivity implements AboutView.Presenter {

    private ClientFactory clientFactory;

    private String name = "";

    public AboutActivity(AboutPlace place, ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
        this.name = place.getToken();
    }

    @Override
    public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
        final AboutView view = clientFactory.getAboutView();
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
