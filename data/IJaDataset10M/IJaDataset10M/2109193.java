package com.iver.cit.gvsig.fmap.tools;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PointListener;

/**
 * <p>Listener for doing a <i>zoom out</i> operation on the extent of the <code>ViewPort</code> of the associated {@link MapControl MapControl}
 *  object, selecting a point of the <code>MapControl</code> by a single click of the third button of the mouse.</p>
 * 
 * <p>Calculates the new extent <i>r</i> with this equations:
 *  <code><br>
 *   ViewPort vp = mapControl.getMapContext().getViewPort();<br>
 *   Point2D p2 = vp.toMapPoint(event.getPoint());<br>
 *   double factor = 1/MapContext.ZOOMOUTFACTOR;<br>
 *   Rectangle2D.Double r = new Rectangle2D.Double();<br>
 *   double nuevoX = p2.getX() - ((vp.getExtent().getWidth() * factor) / 2.0);<br>
 *   double nuevoY = p2.getY() - ((vp.getExtent().getHeight() * factor) / 2.0);<br>
 *   r.x = nuevoX;<br>
 *   r.y = nuevoY;<br>
 *   r.width = vp.getExtent().getWidth() * factor;<br>
 *   r.height = vp.getExtent().getHeight() * factor;<br>
 *   vp.setExtent(r);
 *  </code>
 * </p>
 * 
 * <p>The ultimately extent will be an adaptation from that, calculated by the <code>ViewPort</code>
 *  bearing in mind the ratio of the available rectangle.</p>
 *
 * @see MapContext#ZOOMOUTFACTOR
 * @see ViewPort#setExtent(Rectangle2D)
 * @see ZoomInListenerImpl
 * @see ZoomOutListenerImpl
 *
 * @author Vicente Caballero Navarro
 */
public class ZoomOutRightButtonListener implements PointListener {

    /**
	 * The image to display when the cursor is active.
	 */
    private final Image izoomout = new ImageIcon(MapControl.class.getResource("images/ZoomOutCursor.gif")).getImage();

    /**
	 * The cursor used to work with this tool listener.
	 * 
	 * @see #getCursor()
	 */
    private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(izoomout, new Point(16, 16), "");

    /**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
    private MapControl mapControl;

    /**
	 * <p>Creates a new <code>ZoomOutRightButtonListener</code> object.</p>
	 * 
	 * @param mapControl the <code>MapControl</code> where will be applied the changes
	 */
    public ZoomOutRightButtonListener(MapControl mapControl) {
        this.mapControl = mapControl;
    }

    public void point(PointEvent event) {
        if (event.getEvent().getButton() == MouseEvent.BUTTON3) {
            System.out.println("Zoom out bot�n derecho");
            ViewPort vp = mapControl.getMapContext().getViewPort();
            Point2D p2 = vp.toMapPoint(event.getPoint());
            double nuevoX;
            double nuevoY;
            double factor = 1 / MapContext.ZOOMOUTFACTOR;
            Rectangle2D.Double r = new Rectangle2D.Double();
            if (vp.getExtent() != null) {
                nuevoX = p2.getX() - ((vp.getExtent().getWidth() * factor) / 2.0);
                nuevoY = p2.getY() - ((vp.getExtent().getHeight() * factor) / 2.0);
                r.x = nuevoX;
                r.y = nuevoY;
                r.width = vp.getExtent().getWidth() * factor;
                r.height = vp.getExtent().getHeight() * factor;
                vp.setExtent(r);
            }
        }
    }

    public Cursor getCursor() {
        return cur;
    }

    public boolean cancelDrawing() {
        System.out.println("cancelDrawing del ZoomOutRightButtonListener");
        return true;
    }

    public void pointDoubleClick(PointEvent event) {
    }
}
