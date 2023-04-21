package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.BooleanChunk;

/**
 * Unknown purpose.
 * 
 * @author Roger Kapsi
 */
public class SupportsPersistentIds extends BooleanChunk {

    public SupportsPersistentIds() {
        this(false);
    }

    public SupportsPersistentIds(boolean supports) {
        super("mspi", "dmap.supportspersistentids", supports);
    }
}
