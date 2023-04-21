package bg.obs.internal.jnetplayer.player;

public interface PlaybackPolicy {

    public static enum SearchMode {

        BACKWORDS, FORWARD
    }

    int applyPolicy(PlayList playlist);

    boolean hasNextTrack(PlayList playlist);

    boolean hasPrevTrack(PlayList playlist);

    void searchMode(SearchMode mode);
}
