package com.jpark.jamse.player.api;

import java.io.Serializable;

/**
 * Song info
 *  
 * @author wklum
 *
 */
public class SongInfo implements Serializable {

    private static final long serialVersionUID = -1027087789863767927L;

    /**
         * Current Song time
         */
    private int currentTime;

    /**
         * Total Song time
         */
    private int totalTime;

    /**
         * Song title
         */
    private String songTitle;

    /**
         * Flag indicating play status
         */
    private boolean isPlaying;

    /**
         * Flag indicating paused state
         */
    private boolean isPaused;

    /**
         * Public Constructor
         * @param currentTime current song time
         * @param totalTime total song time
         * @param songTitle title
         * @param isPlaying
         * @param isPaused
         */
    public SongInfo(int currentTime, int totalTime, String songTitle, boolean isPlaying, boolean isPaused) {
        super();
        this.currentTime = currentTime;
        this.totalTime = totalTime;
        this.songTitle = songTitle;
        this.isPlaying = isPlaying;
        this.isPaused = isPaused;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
