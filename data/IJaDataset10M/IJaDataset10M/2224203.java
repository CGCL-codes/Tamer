package edu.ucla.stat.SOCR.touchgraph.graphlayout;

import java.awt.Point;
import edu.ucla.stat.SOCR.touchgraph.graphlayout.interaction.HVScroll;
import edu.ucla.stat.SOCR.touchgraph.graphlayout.interaction.HyperScroll;
import edu.ucla.stat.SOCR.touchgraph.graphlayout.interaction.LocalityScroll;
import edu.ucla.stat.SOCR.touchgraph.graphlayout.interaction.RotateScroll;
import edu.ucla.stat.SOCR.touchgraph.graphlayout.interaction.ZoomScroll;

/** TGScrollPane is a Java interface for a user interface using scrollbars
  * to set TouchGraph navigation and editing properties such as zoom, rotate
  * and locality. If a particular UI doesn't use a specific scrollbar, the 
  * corresponding method should return a null.
  *
  * @author   Murray Altheim  
  * @author   Alex Shapiro
  * @version  1.22-jre1.1  $Id: TGScrollPane.java,v 1.1 2010/01/20 20:38:32 jiecui Exp $
  */
public interface TGScrollPane {

    /** Return the TGPanel used with this TGScrollPane. */
    public TGPanel getTGPanel();

    /** Return the HVScroll used with this TGScrollPane. */
    public HVScroll getHVScroll();

    /** Return the HyperScroll used with this TGScrollPane. */
    public HyperScroll getHyperScroll();

    /** Sets the horizontal offset to p.x, and the vertical offset to p.y
      * given a Point <tt>p<tt>. 
      */
    public void setOffset(Point p);

    /** Return the horizontal and vertical offset position as a Point. */
    public Point getOffset();

    /** Return the RotateScroll used with this TGScrollPane. */
    public RotateScroll getRotateScroll();

    /** Set the rotation angle of this TGScrollPane (allowable values between 0 to 359). */
    public void setRotationAngle(int angle);

    /** Return the rotation angle of this TGScrollPane. */
    public int getRotationAngle();

    /** Return the LocalityScroll used with this TGScrollPane. */
    public LocalityScroll getLocalityScroll();

    /** Set the locality radius of this TGScrollPane  
      * (allowable values between 0 to 4, or LocalityUtils.INFINITE_LOCALITY_RADIUS). 
      */
    public void setLocalityRadius(int radius);

    /** Return the locality radius of this TGScrollPane. */
    public int getLocalityRadius();

    /** Return the ZoomScroll used with this TGScrollPane. */
    public ZoomScroll getZoomScroll();

    /** Set the zoom value of this TGScrollPane (allowable values between -100 to 100). */
    public void setZoomValue(int zoomValue);

    /** Return the zoom value of this TGScrollPane. */
    public int getZoomValue();
}
