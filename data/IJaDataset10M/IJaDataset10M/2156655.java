package dplayer.lastfm;

import dplayer.scanner.Song;
import dplayer.StringUtil;

public class LastFmController {

    private static final LastFmQueue queue = new LastFmQueueImpl();

    static final String LASTFM_CLIENT_ID = "tst";

    static final String LASTFM_CLIENT_VERSION = "1.0";

    public static void init() {
        getQueue().init();
    }

    public static LastFmQueue getQueue() {
        return queue;
    }

    public static void deinit() {
        getQueue().deinit();
    }

    public static boolean isSongSubmittableToLastFm(Song song) {
        return !StringUtil.isTrimmedEmpty(song.getTitle());
    }
}
