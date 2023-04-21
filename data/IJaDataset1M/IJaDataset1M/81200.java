package org.timothyb89.jtelirc.ircrpg.map.zone;

import java.awt.Rectangle;

/**
 *
 * @author tim
 */
public class ZoneImpl extends Zone {

    private String name;

    private Rectangle bounds;

    private String description;

    public ZoneImpl(String name, Rectangle bounds, String description) {
        this.name = name;
        this.bounds = bounds;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getDescription() {
        return description;
    }
}
