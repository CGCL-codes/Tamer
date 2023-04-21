package jtinymediav1.jtinymedia.core.decoder;

import java.util.EventObject;
import jtinymediav1.jtinymedia.structure.library.MusicTrack;

/**
 *
 * @author Administrator
 */
public class Mp3PlayerEvent extends EventObject {

    private MusicTrack track;

    public Mp3PlayerEvent(Object s, MusicTrack t) {
        super(s);
        track = t;
    }

    /**
     * @return the track
     */
    public MusicTrack getTrack() {
        return track;
    }
}
