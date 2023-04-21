package us.gibb.dev.gwt.event;

import java.util.HashMap;
import java.util.Map;
import us.gibb.dev.gwt.location.Location;
import us.gibb.dev.gwt.location.LocationManager;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class DefaultEventBus extends HandlerManager implements EventBus {

    Map<Object, EventType<?>> typeRegistry = new HashMap<Object, EventType<?>>();

    private LocationManager locationManager;

    private class EventType<H extends com.google.gwt.event.shared.EventHandler> extends GwtEvent.Type<H> {
    }

    private class EventWrapper extends GwtEvent<EventHandler<?>> {

        private Event<?, EventHandler<?>> event;

        @SuppressWarnings("unchecked")
        public EventWrapper(Event<?, ?> event) {
            this.event = (Event<?, EventHandler<?>>) event;
        }

        protected void dispatch(EventHandler<?> handler) {
            event.dispatch(handler);
        }

        @SuppressWarnings("unchecked")
        public GwtEvent.Type<EventHandler<?>> getAssociatedType() {
            return (GwtEvent.Type<EventHandler<?>>) typeRegistry.get(event.getTypeObject());
        }
    }

    public DefaultEventBus(LocationManager locationManager) {
        super(null);
        this.locationManager = locationManager;
        locationManager.addValueChangeHandler(new ValueChangeHandler<String>() {

            public void onValueChange(ValueChangeEvent<String> event) {
                fire(Location.fromString(event.getValue()));
            }
        });
    }

    public <E extends Event<T, H>, H extends EventHandler<E>, T> HandlerRegistration add(EventHandler<E> handler) {
        EventType<EventHandler<E>> type = getType(handler.getTypeObject());
        if (type == null) {
            type = new EventType<EventHandler<E>>();
            typeRegistry.put(handler.getTypeObject(), type);
        }
        return addHandler(type, handler);
    }

    public void fire(Event<?, ?> event) {
        super.fireEvent(new EventWrapper(event));
    }

    public boolean isHandled(Class<?> typeClass) {
        EventType<?> type = getType(typeClass);
        if (type != null) {
            return super.isEventHandled(type);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Event> EventType<EventHandler<E>> getType(Object typeObject) {
        return (EventType<EventHandler<E>>) typeRegistry.get(typeObject);
    }

    public void changeLocation(String location, String... params) {
        locationManager.changeLocation(location, params);
    }

    public void changeLocationIfNotCurrent(String location, String... params) {
        locationManager.changeLocationIfNotCurrent(location, params);
    }

    public Location currentLocation() {
        return locationManager.currentLocation();
    }

    public Location currentLocation(String requiredLocation) {
        return locationManager.currentLocation(requiredLocation);
    }

    public void addValueChangeHandler(ValueChangeHandler<String> handler) {
        locationManager.addValueChangeHandler(handler);
    }

    public void fireCurrentLocation() {
        Location currentLocation = locationManager.currentLocation();
        if (currentLocation != null) {
            fire(currentLocation);
        }
    }

    @Override
    public void failure(String msg) {
        fire(new FailureEvent(msg));
    }

    @Override
    public void failure(Throwable t) {
        fire(new FailureEvent(t));
    }

    @Override
    public void failure(String msg, Throwable t) {
        fire(new FailureEvent(msg, t));
    }
}
