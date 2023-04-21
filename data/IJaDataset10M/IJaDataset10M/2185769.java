package net.deytan.wofee.gwt.ui.event.ui;

import com.google.gwt.event.shared.GwtEvent;

public class UiBuildEvent extends GwtEvent<UiBuildEventHandler> {

    public static Type<UiBuildEventHandler> TYPE = new Type<UiBuildEventHandler>();

    @Override
    public Type<UiBuildEventHandler> getAssociatedType() {
        return UiBuildEvent.TYPE;
    }

    @Override
    protected void dispatch(final UiBuildEventHandler handler) {
        handler.onBuild(this);
    }
}
