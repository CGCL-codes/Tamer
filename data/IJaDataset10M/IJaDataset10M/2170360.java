package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.UIntChunk;

/**
 * The stop time of the Song in milliseconds. I.e. you can use it to stop
 * playing this song n-milliseconds before end.
 * 
 * @author Roger Kapsi
 */
public class SongStopTime extends UIntChunk {

    /**
     * Creates a new SongStopTime where stop time is not set. You can change
     * this value with {@see #setValue(int)}.
     */
    public SongStopTime() {
        this(0);
    }

    /**
     * Creates a new SongStopTime at the assigned time. Use 0 to disable this
     * property. You can change this value with {@see #setValue(int)}.
     * 
     * @param <tt>time</tt> the stop time of this song in milliseconds.
     */
    public SongStopTime(long time) {
        super("assp", "daap.songstoptime", time);
    }
}
