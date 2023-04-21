package de.kapsi.net.daap.chunks.impl;

import de.kapsi.net.daap.chunks.ContainerChunk;

/**
 * This container is used to group Items. An such group repesents
 * for example a Song.
 *
 * @author  Roger Kapsi
 */
public class ListingItem extends ContainerChunk {

    public ListingItem() {
        super("mlit", "dmap.listingitem");
    }
}
