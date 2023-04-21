package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.StringChunk;

/**
 * Unknown purpose.
 * 
 * @author Roger Kapsi
 */
public class StatusString extends StringChunk {

    public StatusString() {
        this(null);
    }

    public StatusString(String statusString) {
        super("msts", "dmap.statusstring", statusString);
    }
}
