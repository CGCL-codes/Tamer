package com.eventbusapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ClearEvent extends GwtEvent<ClearEventHandler> {

    public static Type<ClearEventHandler> TYPE = new Type<ClearEventHandler>();

    @Override
    public Type<ClearEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClearEventHandler handler) {
        handler.clear(this);
    }
}
