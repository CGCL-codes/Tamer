package fr.upmf.animaths.client.events;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.GwtEvent;
import fr.upmf.animaths.client.mvp.MathObject.MOElement;

public class DropEvent extends GwtEvent<DropHandler> {

    private static final Type<DropHandler> TYPE = new Type<DropHandler>();

    private short state;

    private MOElement<?> element;

    private NativeEvent event;

    public static Type<DropHandler> getType() {
        return TYPE;
    }

    public DropEvent(MOElement<?> object, NativeEvent event) {
        if (object != null) this.state = object.getStyleClass();
        this.element = object;
        this.event = event;
    }

    @Override
    protected void dispatch(DropHandler handler) {
        handler.onDrop(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DropHandler> getAssociatedType() {
        return getType();
    }

    public NativeEvent getEvent() {
        return event;
    }

    public short getState() {
        return state;
    }

    public MOElement<?> getElement() {
        return element;
    }
}
