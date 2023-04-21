package de.rowbuddy.client.events;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import de.rowbuddy.boundary.dtos.MemberDTO;
import de.rowbuddy.client.FadeAnimation;
import de.rowbuddy.client.Presenter;

public abstract class PresenterChanger implements EventHandler, ValueChangeHandler<String> {

    public interface EventListener {

        void eventProcessed(AbstractEvent<?> event);
    }

    private final HasWidgets targetWidget;

    private static final Logger logger = Logger.getLogger(PresenterChanger.class.getName());

    private final List<EventListener> listeners = new LinkedList<EventListener>();

    protected final EventBus eventBus;

    protected static MemberDTO member;

    public PresenterChanger(HasWidgets targetWidget, EventBus eventBus, MemberDTO member) {
        this.member = member;
        this.targetWidget = targetWidget;
        this.eventBus = eventBus;
        this.eventBus.addHandler(getType(), this);
        History.addValueChangeHandler(this);
    }

    public void processEvent(AbstractEvent<?> event) {
        History.newItem(event.toHistoryItem());
    }

    public void processHistoryEntry(String historyEntry) {
        AbstractEvent<?> event = toEvent(historyEntry);
        Presenter presenter = createPresenter(event);
        for (EventListener listener : listeners) {
            listener.eventProcessed(event);
        }
        FadeAnimation fade = new FadeAnimation(targetWidget, presenter);
        fade.run(400);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> arg0) {
        String historyToken = arg0.getValue();
        if (historyToken.startsWith(getHistoryIdentifier())) {
            logger.info("History event processed: " + historyToken);
            processHistoryEntry(historyToken);
        }
    }

    public void addObserver(EventListener observer) {
        listeners.add(observer);
    }

    public abstract AbstractEvent<?> toEvent(String historyItem);

    public abstract Presenter createPresenter(AbstractEvent<?> event);

    protected abstract <T extends PresenterChanger> Type<T> getType();

    protected abstract String getHistoryIdentifier();
}
