package com.mp3explorer.business.nodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Artist
 */
public class Artist extends AbstractNode {

    private HashMap<String, Node> mapAlbums = new HashMap<String, Node>();

    /**
     * Returns album nodes for the artist
     * @return A map of nodes
     */
    public Map<String, Node> getAlbums() {
        return mapAlbums;
    }

    /**
     * Returns the icon associated to the node
     * @return The icon id
     */
    public int getNodeIcon() {
        return ICON_ARTIST;
    }
}
