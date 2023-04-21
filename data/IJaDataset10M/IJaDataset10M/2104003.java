package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.DateChunk;

/**
 * The date when the Song was modified. Date is the difference between the
 * current time and midnight, January 1, 1970 UTC in <u>seconds</u>!
 * 
 * <code>int date = (int)(System.currentTimeMillis()/1000);</code>
 * 
 * @author Roger Kapsi
 */
public class SongDateModified extends DateChunk {

    public SongDateModified() {
        this(0l);
    }

    public SongDateModified(long date) {
        super("asdm", "daap.songdatemodified", date);
    }
}
