package de.rowbuddy.client.events;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HasWidgets;
import de.rowbuddy.boundary.dtos.MemberDTO;
import de.rowbuddy.client.Presenter;
import de.rowbuddy.client.boat.ListDamagesPresenter;
import de.rowbuddy.client.boat.ListDamagesView;
import de.rowbuddy.client.services.BoatRemoteServiceAsync;

public class ListDamagePresenterChanger extends PresenterChanger {

    private final BoatRemoteServiceAsync boatService;

    public ListDamagePresenterChanger(HasWidgets targetWidget, EventBus eventBus, BoatRemoteServiceAsync boatService, MemberDTO member) {
        super(targetWidget, eventBus, member);
        this.boatService = boatService;
    }

    @Override
    public AbstractEvent<?> toEvent(String historyItem) {
        return new ListDamageEvent();
    }

    @Override
    public Presenter createPresenter(AbstractEvent<?> event) {
        return new ListDamagesPresenter(new ListDamagesView(), boatService, eventBus);
    }

    @Override
    protected <T extends PresenterChanger> Type<T> getType() {
        return (Type<T>) ListDamageEvent.TYPE;
    }

    @Override
    protected String getHistoryIdentifier() {
        return ListDamageEvent.HISTORY_IDENTIFIER;
    }
}
