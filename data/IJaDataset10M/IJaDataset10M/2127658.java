package fr.hd3d.html5.video.client.events;

import com.google.gwt.event.shared.GwtEvent;
import fr.hd3d.html5.video.client.handlers.VideoDurationChangeHandler;

/**
 * The duration attribute has just been updated.
 * 
 * @author michael.guiral
 * 
 */
public class VideoDurationChangeEvent extends GwtEvent<VideoDurationChangeHandler> {

    private static final Type<VideoDurationChangeHandler> TYPE = new Type<VideoDurationChangeHandler>();

    public static Type<VideoDurationChangeHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(VideoDurationChangeHandler handler) {
        handler.onDurationChange(this);
    }

    @Override
    public Type<VideoDurationChangeHandler> getAssociatedType() {
        return TYPE;
    }
}
