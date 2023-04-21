package com.rednels.ofcgwt.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ChartReadyEvent extends GwtEvent<ChartReadyHandler> {

    private static final Type<ChartReadyHandler> TYPE = new Type<ChartReadyHandler>();

    @Override
    public Type<ChartReadyHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChartReadyHandler handler) {
        handler.onReady(this);
    }
}
