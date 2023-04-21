package com.pavco.caribbeanvisit.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.pavco.caribbeanvisit.client.eventhandlers.ReceivedXmlEventHandler;

public class ReceivedXmlEvent extends GwtEvent<ReceivedXmlEventHandler> {

    Widget sender;

    String data;

    public static final GwtEvent.Type<ReceivedXmlEventHandler> TYPE = new GwtEvent.Type<ReceivedXmlEventHandler>();

    @Override
    protected void dispatch(ReceivedXmlEventHandler handler) {
        handler.onReceivedXml(this);
    }

    @Override
    public GwtEvent.Type<ReceivedXmlEventHandler> getAssociatedType() {
        return TYPE;
    }

    public Widget getSender() {
        return sender;
    }

    public void setSender(Widget sender) {
        this.sender = sender;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
