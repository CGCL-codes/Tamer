package org.mobicents.slee.resource.mediacontrol.wrapper.mediagroup;

import java.net.URI;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.Recorder;
import javax.media.mscontrol.mediagroup.RecorderEvent;
import javax.media.mscontrol.resource.RTC;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.Wrapper;

/**
 * @author baranowb
 * 
 */
public class RecorderWrapper implements Recorder, Wrapper {

    protected final Recorder wrappedRecorder;

    protected final MediaGroupWrapper mediaGroup;

    protected final MediaSessionWrapper mediaSession;

    protected final MsResourceAdaptor ra;

    protected final RecorderEventWrapperListener recorderListener = new RecorderEventWrapperListener(this);

    /**
	 * @param mediaGroup
	 * @param mediaSession
	 */
    public RecorderWrapper(Recorder recorder, MediaGroupWrapper mediaGroup, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
        if (recorder == null) {
            throw new IllegalArgumentException("Recorder must not be null.");
        }
        if (mediaGroup == null) {
            throw new IllegalArgumentException("MediaGroup must not be null.");
        }
        if (mediaSession == null) {
            throw new IllegalArgumentException("MediaSession must not be null.");
        }
        if (ra == null) {
            throw new IllegalArgumentException("MsResourceAdaptor must not be null.");
        }
        this.mediaGroup = mediaGroup;
        this.mediaSession = mediaSession;
        this.wrappedRecorder = recorder;
        this.ra = ra;
        this.wrappedRecorder.addListener(this.recorderListener);
    }

    public void record(URI uri, RTC[] rtc, Parameters parameters) throws MsControlException {
        this.wrappedRecorder.record(uri, rtc, parameters);
    }

    public void stop() {
        this.wrappedRecorder.stop();
    }

    public MediaGroup getContainer() {
        return this.mediaGroup;
    }

    public MediaSession getMediaSession() {
        return this.mediaSession;
    }

    public void removeListener(MediaEventListener<RecorderEvent> arg0) {
        throw new SecurityException();
    }

    public void addListener(MediaEventListener<RecorderEvent> arg0) {
        throw new SecurityException();
    }

    public Object getWrappedObject() {
        return this.wrappedRecorder;
    }

    private class RecorderEventWrapperListener implements MediaEventListener<RecorderEvent> {

        private final RecorderWrapper wrapper;

        /**
		 * @param wrapper
		 */
        public RecorderEventWrapperListener(RecorderWrapper wrapper) {
            super();
            this.wrapper = wrapper;
        }

        public void onEvent(RecorderEvent event) {
            RecorderEventWrapper localEvent = new RecorderEventWrapper(event, wrapper);
            if (event.getEventType().equals(RecorderEvent.PAUSED)) {
                ra.fireEvent(localEvent.PAUSED, mediaGroup.getActivityHandle(), localEvent);
            } else if (event.getEventType().equals(RecorderEvent.RECORD_COMPLETED)) {
                ra.fireEvent(localEvent.RECORD_COMPLETED, mediaGroup.getActivityHandle(), localEvent);
            } else if (event.getEventType().equals(RecorderEvent.RESUMED)) {
                ra.fireEvent(localEvent.RESUMED, mediaGroup.getActivityHandle(), localEvent);
            } else if (event.getEventType().equals(RecorderEvent.STARTED)) {
                ra.fireEvent(localEvent.STARTED, mediaGroup.getActivityHandle(), localEvent);
            }
        }
    }
}
