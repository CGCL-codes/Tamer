package br.com.klis.batendoumabola.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UploadEvent extends GwtEvent<UploadEventHandler> {

    public static Type<UploadEventHandler> TYPE = new Type<UploadEventHandler>();

    @Override
    public Type<UploadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UploadEventHandler handler) {
        handler.onUpload(this);
    }
}
