package booksandfilms.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TopicAddEvent extends GwtEvent<TopicAddEventHandler> {

    public static Type<TopicAddEventHandler> TYPE = new Type<TopicAddEventHandler>();

    @Override
    public Type<TopicAddEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TopicAddEventHandler handler) {
        handler.onAddTopic(this);
    }
}
