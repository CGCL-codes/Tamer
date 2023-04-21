package de.kapsi.net.daap.chunks.impl;

import de.kapsi.net.daap.chunks.UIntChunk;

/**
 * The ID of an item. This value must be unique for a certain set
 * of items (e.g. for all Songs) and must be different from 0
 *
 * @author  Roger Kapsi
 */
public class ItemId extends UIntChunk {

    public ItemId() {
        this(0);
    }

    public ItemId(long itemId) {
        super("miid", "dmap.itemid", itemId);
    }
}
