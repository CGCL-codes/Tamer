package org.gstreamer.media.event;

import org.gstreamer.ClockTime;
import org.gstreamer.State;
import org.gstreamer.media.MediaPlayer;

/**
 * Based on code from FMJ by Ken Larson
 */
public class StopEvent extends TransitionEvent {

    private static final long serialVersionUID = -1646275975260781455L;

    private ClockTime mediaTime;

    public StopEvent(MediaPlayer from, State previous, State current, State target, ClockTime mediaTime) {
        super(from, previous, current, target);
        this.mediaTime = mediaTime;
    }

    public ClockTime getMediaTime() {
        return mediaTime;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[source=" + getSource() + ",previousState=" + getPreviousState() + ",currentState=" + getCurrentState() + ",targetState=" + getPendingState() + ",mediaTime=" + mediaTime + "]";
    }
}
