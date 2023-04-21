package fr.hd3d.html5.video.client.events;

import com.google.gwt.event.shared.GwtEvent;
import fr.hd3d.html5.video.client.handlers.VideoProgressHandler;

/**
 * The user agent is fetching media data.
 * 
 * @author michael.guiral
 * 
 */
public class VideoProgressEvent extends GwtEvent<VideoProgressHandler> {

    private static final Type<VideoProgressHandler> TYPE = new Type<VideoProgressHandler>();

    public static Type<VideoProgressHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(VideoProgressHandler handler) {
        handler.onProgress(this);
    }

    @Override
    public Type<VideoProgressHandler> getAssociatedType() {
        return TYPE;
    }
}
