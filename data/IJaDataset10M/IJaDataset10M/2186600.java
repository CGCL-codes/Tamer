package de.rowbuddy.client.events;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HasWidgets;
import de.rowbuddy.boundary.dtos.MemberDTO;
import de.rowbuddy.client.Presenter;
import de.rowbuddy.client.boat.AddDamagePresenter;
import de.rowbuddy.client.boat.AddDamageView;
import de.rowbuddy.client.services.BoatRemoteServiceAsync;
import de.rowbuddy.client.boat.AddDamagePresenter.Display;

public class AddDamagePresenterChanger extends PresenterChanger {

    private final BoatRemoteServiceAsync boatService;

    public AddDamagePresenterChanger(HasWidgets targetWidget, EventBus eventBus, BoatRemoteServiceAsync boatService, MemberDTO member) {
        super(targetWidget, eventBus, member);
        this.boatService = boatService;
    }

    @Override
    public AbstractEvent<?> toEvent(String historyItem) {
        return new AddDamageEvent();
    }

    @Override
    public Presenter createPresenter(AbstractEvent<?> event) {
        return new AddDamagePresenter(boatService, eventBus, new AddDamageView());
    }

    @Override
    protected <T extends PresenterChanger> Type<T> getType() {
        return (Type<T>) AddDamageEvent.TYPE;
    }

    @Override
    protected String getHistoryIdentifier() {
        return AddDamageEvent.HISTORY_IDENTIFIER;
    }
}
