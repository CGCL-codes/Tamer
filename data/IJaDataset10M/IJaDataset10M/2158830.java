package att.grappa;

/**
 * An interface for defining an image drawing method to be used for
 * painting the background of the graph.
 *
 * @version 1.2, 04 Mar 2008; Copyright 1996 - 2008 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public interface GrappaBacker {

    /**
   * The method for drawing the background image.
   *
   * @param g2d the graphics context.
   * @param graph the graph being drawn in the foreground.
   * @param bbox the bounding box of the graph.
   * @param clip the clipping shape defining the limits of the area to be drawn.
   */
    public void drawBackground(java.awt.Graphics2D g2d, Graph graph, java.awt.geom.Rectangle2D bbox, java.awt.Shape clip);
}
