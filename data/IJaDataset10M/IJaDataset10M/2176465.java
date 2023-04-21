package de.kapsi.net.daap.chunks.impl;

import de.kapsi.net.daap.chunks.UIntChunk;

/**
 * The start time of the Song in milliseconds. I.e. you can use it
 * to skip n-milliseconds at the beginning.
 *
 * @author  Roger Kapsi
 */
public class SongStartTime extends UIntChunk {

    /**
     * Creates a new SongStartTime which starts at the
     * beginning of the song.
     * You can change this value with {@see #setValue(int)}.
     */
    public SongStartTime() {
        this(0);
    }

    /**
     * Creates a new SongStartTime at the assigned time.
     * You can change this value with {@see #setValue(int)}.
     * @param <tt>time</tt> the start time of this song in milliseconds.
     */
    public SongStartTime(long time) {
        super("asst", "daap.songstarttime", time);
    }
}
