package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.annotation.DissectableArea;

/**
 * This class contains the shards that have been selected for each dissectable
 * area.
 * <p>
 * A dissectable area is identified by its index within the list of areas in
 * the document. The shard is identified by its index with the list of shards
 * in the shard area.
 */
public class SelectedShards {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The index in this array is dissectable areas and the values are the
     * shard indexes within the associated area.
     */
    private Shard[] shards;

    /**
     * Create a new <code>SelectedShard</code> that supports the specified
     * number of dissectable areas.
     * @param count The number of dissectable areas.
     */
    public SelectedShards(int count) {
        shards = new Shard[count];
    }

    public int getCount() {
        return shards.length;
    }

    /**
     * Set the specified shard for the specified dissectable area.
     * @param dissectableArea
     * @param shard
     */
    public void setShard(DissectableArea dissectableArea, Shard shard) {
        shards[dissectableArea.getIndex()] = shard;
    }

    public Shard getShard(DissectableArea dissectableArea) {
        return shards[dissectableArea.getIndex()];
    }

    public Shard getShard(int dissectableAreaIndex) {
        return shards[dissectableAreaIndex];
    }
}
