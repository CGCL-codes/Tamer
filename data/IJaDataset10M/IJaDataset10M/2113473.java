package uk.co.caprica.vlcj.oop;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;

/**
 * Default implementation of an "out-of-process" media player.
 * <p>
 * An application creates a local video surface component and associates it with
 * the remote media player instance.
 */
public class DefaultOutOfProcessMediaPlayer extends DefaultMediaPlayer implements OutOfProcessMediaPlayer {

    /**
     * Has the video surface been associated with the native media player?
     */
    private boolean videoSurfaceSet;

    /**
     * Video surface component.
     */
    private ComponentIdVideoSurface videoSurface;

    /**
     * Create a media player.
     * 
     * @param libvlc native interface
     * @param instance libvlc instance
     */
    public DefaultOutOfProcessMediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        super(libvlc, instance);
    }

    @Override
    public final void setVideoSurface(ComponentIdVideoSurface videoSurface) {
        Logger.debug("setVideoSurface(videoSurface=" + videoSurface + ")");
        this.videoSurface = videoSurface;
    }

    @Override
    protected final void onBeforePlay() {
        Logger.debug("onBeforePlay()");
        Logger.debug("videoSurfaceSet={}", videoSurfaceSet);
        if (!videoSurfaceSet && videoSurface != null) {
            Logger.debug("Attaching video surface...");
            videoSurface.attach(libvlc, this);
            videoSurfaceSet = true;
            Logger.debug("Video surface attached.");
        }
    }
}
