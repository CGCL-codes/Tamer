package de.rowbuddy.client.events;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HasWidgets;
import de.rowbuddy.boundary.dtos.MemberDTO;
import de.rowbuddy.client.Presenter;
import de.rowbuddy.client.boat.DetailDamagePresenter;
import de.rowbuddy.client.boat.DetailsDamageView;
import de.rowbuddy.client.services.BoatRemoteServiceAsync;

public class DetailsDamagePresenterChanger extends PresenterChanger {

    private final BoatRemoteServiceAsync boatService;

    public DetailsDamagePresenterChanger(HasWidgets targetWidget, EventBus eventBus, BoatRemoteServiceAsync boatService, MemberDTO member) {
        super(targetWidget, eventBus, member);
        this.boatService = boatService;
    }

    @Override
    public AbstractEvent<?> toEvent(String historyItem) {
        Long id = IdEvent.getIdFromHistoryItem(historyItem);
        return new DetailsDamageEvent(id);
    }

    @Override
    public Presenter createPresenter(AbstractEvent<?> event) {
        DetailsDamageEvent e = (DetailsDamageEvent) event;
        return new DetailDamagePresenter(e.getId(), new DetailsDamageView(), boatService, eventBus);
    }

    @Override
    protected <T extends PresenterChanger> Type<T> getType() {
        return (Type<T>) DetailsDamageEvent.TYPE;
    }

    @Override
    protected String getHistoryIdentifier() {
        return DetailsDamageEvent.HISTORY_IDENTIFIER;
    }
}
