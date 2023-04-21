package org.noranj.formak.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditBusinessDocumentEvent extends GwtEvent<EditBusinessDocumentEventHandler> {

    public static Type<EditBusinessDocumentEventHandler> TYPE = new Type<EditBusinessDocumentEventHandler>();

    private final String id;

    public EditBusinessDocumentEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Type<EditBusinessDocumentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditBusinessDocumentEventHandler handler) {
        handler.onEditBusinessDocument(this);
    }
}
