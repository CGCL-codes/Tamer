package de.rowbuddy.client.events;

import com.google.gwt.event.shared.GwtEvent;
import de.rowbuddy.client.model.StatusMessage;

public class StatusMessageEvent extends GwtEvent<StatusMessageHandler> {

    public static Type<StatusMessageHandler> TYPE = new Type<StatusMessageHandler>();

    private StatusMessage message;

    public StatusMessageEvent(StatusMessage message) {
        this.message = message;
    }

    @Override
    protected void dispatch(StatusMessageHandler arg0) {
        arg0.onNewStatusMessage(this);
    }

    public StatusMessage getMessage() {
        return message;
    }

    @Override
    public Type<StatusMessageHandler> getAssociatedType() {
        return TYPE;
    }
}
