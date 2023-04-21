package com.moesol.gwt.maps.client.touch;

public class TouchStartEvent extends TouchEvent<TouchStartHandler> {

    private static final Type<TouchStartHandler> TYPE = new Type<TouchStartHandler>("touchstart", new TouchStartEvent());

    public static Type<TouchStartHandler> getType() {
        return TYPE;
    }

    @Override
    public com.google.gwt.event.dom.client.DomEvent.Type<TouchStartHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TouchStartHandler handler) {
        handler.onTouchStart(this);
    }
}
