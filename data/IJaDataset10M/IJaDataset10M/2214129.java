package dplayer.player.events;

import dplayer.player.Player;

/**
 * Event indicating changes of the position of the current track.
 */
public class TrackPositionChangedEvent extends AbstractPlayerEvent {

    private long mOffset;

    public TrackPositionChangedEvent(final Player player, final long offset) {
        super(player);
        assert offset >= 0;
        mOffset = offset;
    }

    public int getPosition() {
        return mPlayer.getTrack() != null ? (int) ((double) mPlayer.getOffset() / mPlayer.getTrack().getDuration() * 100) : 0;
    }

    public long getOffset() {
        return mOffset;
    }

    /** {@inheritDoc} */
    public String getInfo() {
        return "TrackPositionChangedEvent (position = " + getPosition() + ")";
    }
}
