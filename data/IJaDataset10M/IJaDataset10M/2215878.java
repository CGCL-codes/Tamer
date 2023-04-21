package com.sin.client.activity.west2;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.sin.client.place.StartChatPlace;
import com.sin.client.place.StartVideoPlace;
import com.sin.client.ui.west2.West2StartChatView;
import com.sin.client.ui.west2.West2StartView;

public class West2StartChatActivity extends AbstractActivity implements West2StartChatView.Presenter {

    private West2StartChatView view;

    private PlaceController placeController;

    @Inject
    public West2StartChatActivity(West2StartChatView view, PlaceController placeController) {
        super();
        this.view = view;
        this.placeController = placeController;
        view.setPresenter(this);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
    }
}
