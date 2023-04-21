package org.openremote.web.console.event.press;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;

/**
 * This event defines the end of a press event
 * @author rich
 *
 */
public class PressCancelEvent extends PressEvent<PressCancelHandler> {

    private static final Type<PressCancelHandler> TYPE = new Type<PressCancelHandler>();

    public PressCancelEvent(MouseEvent<MouseOutHandler> sourceEvent) {
        super(sourceEvent);
        clientXPos = sourceEvent.getClientX();
        clientYPos = sourceEvent.getClientY();
        screenXPos = sourceEvent.getScreenX();
        screenYPos = sourceEvent.getScreenY();
    }

    @Override
    public Type<PressCancelHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PressCancelHandler handler) {
        handler.onPressCancel(this);
    }

    public static Type<PressCancelHandler> getType() {
        return TYPE;
    }
}
