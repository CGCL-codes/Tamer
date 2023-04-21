package net.sf.structure.paint;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * <p>
 * A <code>GraphicsPainter</code> is responsible for providing a graphical representation
 * of a <code>Structure</code> on a <code>Graphics2D</code> context. This representation may be used
 * within a particular user interface framework such as AWT or Swing. Alternatively,
 * <code>GraphicsPainter</code> may be used in other contexts, such as those involving the
 * direct generation of graphics files.
 * </p>
 * 
 * @author Richard Apodaca
 * @see net.sf.structure.util.ImageKit
 */
public interface GraphicsPainter extends Painter {

    /**
   * Paints this <code>GraphicsPainter</code> onto the specified <code>java.awt.Graphics
   * </code> context.
   * 
   * @param g the <code>Graphics2D</code> context
   * @param bounds the bounding rectangle into which context should be painted
   */
    public void paint(Graphics2D g, Rectangle2D bounds);
}
