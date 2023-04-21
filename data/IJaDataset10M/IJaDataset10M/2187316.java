package org.jcrpg.space.sidetype;

/**
 * Side type that cannot be passed through.
 * @author illes
 *
 */
public class NotPassable extends SideSubType {

    public NotPassable(String id) {
        super(id);
    }

    public NotPassable(String id, byte[] color) {
        super(id, color);
    }

    public NotPassable(String id, boolean overrideGeneratedTileMiddleHeight) {
        super(id, overrideGeneratedTileMiddleHeight);
    }
}
