package net.sourceforge.simpleworklog.client.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author: Ignat Alexeyenko
 * Date: 12/11/10
 */
public class ShowMenuEvent extends GwtEvent<ShowMenuHandler> {

    public static final Type<ShowMenuHandler> TYPE = new Type<ShowMenuHandler>();

    @Override
    public Type<ShowMenuHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowMenuHandler handler) {
        handler.onMenuShow(this);
    }
}
