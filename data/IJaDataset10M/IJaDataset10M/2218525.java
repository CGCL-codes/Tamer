package org.eesgmbh.gimv.client.event;

import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Signals a mouse move over the viewport.
 *
 * @author Christian Seewald - EES GmbH - c.seewald@ees-gmbh.de
 *
 */
public class ViewportMouseMoveEvent extends FilteredDispatchGwtEvent<ViewportMouseMoveEventHandler> {

    public static Type<ViewportMouseMoveEventHandler> TYPE = new Type<ViewportMouseMoveEventHandler>();

    private final MouseMoveEvent gwtEvent;

    public ViewportMouseMoveEvent(MouseMoveEvent gwtEvent, ViewportMouseMoveEventHandler... blockedHandlers) {
        super(blockedHandlers);
        this.gwtEvent = gwtEvent;
    }

    public MouseMoveEvent getGwtEvent() {
        return this.gwtEvent;
    }

    @Override
    public Type<ViewportMouseMoveEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void onDispatch(ViewportMouseMoveEventHandler handler) {
        handler.onMouseMove(this);
    }
}
