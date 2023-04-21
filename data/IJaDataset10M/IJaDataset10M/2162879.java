package com.mathassistant.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ToUserViewEvent extends GwtEvent<ToUserViewEventHandler> {

    public static final Type<ToUserViewEventHandler> TYPE = new Type<ToUserViewEventHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ToUserViewEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ToUserViewEventHandler handler) {
        handler.onTopicListToUserView(this);
    }
}
