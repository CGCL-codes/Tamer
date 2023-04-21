package com.iver.cit.gvsig.fmap.tools;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PointListener;

/**
 * <p>Listener to select the upper layer with raster data, that has information in the associated <code>MapControl</code> object, down
 *  the position selected by the mouse.</p>
 *
 * @deprecated
 * @author Nacho Brodin <brodin_ign@gva.es>
 */
public class SelectImageListenerImpl implements PointListener {

    /**
	 * The image to display when the cursor is active.
	 */
    private final Image isaveraster = new ImageIcon(MapControl.class.getResource("images/PointSelectCursor.gif")).getImage();

    /**
	 * The cursor used to work with this tool listener.
	 * 
	 * @see #getCursor()
	 */
    private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(isaveraster, new Point(16, 16), "");

    /**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
    protected MapControl mapCtrl;

    /**
	 * World equivalent coordinates of the point 2D 
	 */
    protected Point2D wcPoint = null;

    /**
	 * <p>Creates a new <code>SelectImageListenerImpl</code> object.</p>
	 * 
	 * @param mapCtrl the <code>MapControl</code> where are stored the layers
	 */
    public SelectImageListenerImpl(MapControl mapCtrl) {
        this.mapCtrl = mapCtrl;
    }

    public void point(PointEvent event) {
    }

    public Cursor getCursor() {
        return cur;
    }

    public boolean cancelDrawing() {
        return true;
    }

    public void pointDoubleClick(PointEvent event) {
    }
}
