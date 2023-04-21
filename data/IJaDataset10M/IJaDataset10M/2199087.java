package org.apache.myfaces.trinidadinternal.image.painter;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Interface implemented by objects that paint something.
 * <p>
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/image/painter/Painter.java#0 $) $Date: 10-nov-2005.19:05:02 $
 */
public interface Painter {

    /**
   * Returns the preferred size of the painter.
   * <p>
   * @param context Context for determining the preferred size.
   * <p>
   * @return The preferred size of the Painter.
   */
    public Dimension getPreferredSize(PaintContext context);

    /**
   * Returns the minimum size of the painter.
   * <p>
   * @param context Context for determining the minimum size.
   * <p>
   * @return The minimum size of the Painter.
   */
    public Dimension getMinimumSize(PaintContext context);

    /**
   * Paints the Painter at the given location.
   * <p>
   * @param context Context for painting.
   * @param g       Graphics object to draw into.
   * @param x       X position to draw at.
   * @param y       Y position to draw at.
   * @param width   Width to draw into.
   * @param height  Height to draw into.

   */
    public void paint(PaintContext context, Graphics g, int x, int y, int width, int height);
}
