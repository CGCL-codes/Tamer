package fm.radiostation.player;

import fm.radiostation.Track;

/**
 * RadioPlayerEvent defines a set of higher level events fired by the RSFM
 * implementation of the Radio Player
 * <p>
 * Additionally, also adds events that more relevant to the use of
 * RadioStation.ForMe. Such as the notification of empty playlists.
 * 
 * @author kaiyi
 * 
 */
public class RadioPlayerEvent {

    /**
	 * Event occurs when the playlist is empty.
	 */
    public static final int OUT_OF_TRACKS = 1;

    /**
	 * Event occurs at the start of a new track.
	 */
    public static final int TRACK_STARTED = 2;

    /**
	 * Event occurs when a track stops playing, regardless whether it reached
	 * the end or stopped by the user.
	 */
    public static final int TRACK_STOPPED = 4;

    /**
	 * Event occurs when the radio is turned off.
	 */
    public static final int RADIO_OFF = 8;

    private int event;

    private Track track;

    /**
	 * Construct an event object given the event type.
	 * 
	 * @param event
	 *            one of the event types defined as a constant of this class
	 */
    public RadioPlayerEvent(int event) {
        this.event = event;
    }

    public RadioPlayerEvent(int event, Track track) {
        this.event = event;
        this.track = track;
    }

    public int getEvent() {
        return event;
    }

    public Track getTrack() {
        return track;
    }
}
