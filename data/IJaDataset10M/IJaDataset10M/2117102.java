package booksandfilms.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TopicEditEvent extends GwtEvent<TopicEditEventHandler> {

    public static Type<TopicEditEventHandler> TYPE = new Type<TopicEditEventHandler>();

    private final Long id;

    public TopicEditEvent(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public Type<TopicEditEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TopicEditEventHandler handler) {
        handler.onEditTopic(this);
    }
}
