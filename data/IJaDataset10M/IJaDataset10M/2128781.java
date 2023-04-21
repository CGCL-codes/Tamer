package org.auramp.event.events;

import org.auramp.mplayer.Track;

public class PlayTrackEvent extends Event {

    private Track track;

    public PlayTrackEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
}
