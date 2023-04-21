package org.skycastle.util.view2d.graphics;

import java.awt.Color;

/**
 * An interface that provides various high level 2D drawing commands.
 * <p/>
 * A replacement of the Graphics2D and Graphics in Java, which are not always so practical to use, and lack several
 * useful operations.  Will be expanded bit by bit as needed.
 *
 * @author Hans Haggstrom
 */
public interface GraphicsCanvas {

    /**
     * Clears the canvas to transparent black color (0,0,0,0).
     */
    void clear();

    /**
     * Clears the canvas to the specified color.
     */
    void clearToColor(Color color);
}
