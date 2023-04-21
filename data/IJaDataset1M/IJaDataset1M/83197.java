package com.iver.cit.gvsig.drivers.dwg.debug;

import java.awt.Cursor;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JOptionPane;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.drivers.dwg.DwgMemoryDriver;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.VectorialDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SingleLayerIterator;
import com.iver.cit.gvsig.fmap.operations.strategies.FeatureVisitor;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PointListener;
import com.iver.cit.jdwglib.dwg.DwgHandleReference;
import com.iver.cit.jdwglib.dwg.DwgObject;

/**
 * <p>Listens events produced by the selection of a <i>control point</i> of any graphical geometry
 *  in layers of the associated {@link MapControl MapControl} object.</p>
 *
 * <p>Listens a single or double click of any mouse's button, and also the events produced when
 *  the position of the cursor has changed on the associated <code>MapControl</code> object.</p>
 *
 * <p>Uses {@link Cursor#CROSSHAIR_CURSOR Cursor#CROSSHAIR_CURSOR} as mouse's cursor image.</p>
 */
public class DwgEntityListener implements PointListener {

    /**
	 * Used to calculate the pixel at the associated <code>MapControl</code> down the
	 *  position of the mouse.
	 */
    public static int pixelTolerance = 3;

    /**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
    private MapControl mapCtrl;

    /**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
    private Cursor cur = java.awt.Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

    /**
     * <p>Creates a new <code>DwgEntityListener</code> object.</p>
     *
     * @param mc the <code>MapControl</code> where will be applied the changes
     */
    public DwgEntityListener(MapControl mc) {
        this.mapCtrl = mc;
    }

    public void point(PointEvent event) throws BehaviorException {
        Point2D pReal = mapCtrl.getMapContext().getViewPort().toMapPoint(event.getPoint());
        SingleLayerIterator it = new SingleLayerIterator(mapCtrl.getMapContext().getLayers());
        while (it.hasNext()) {
            FLayer aux = it.next();
            if (!aux.isActive()) continue;
            if (!(aux instanceof FLyrVect)) return;
            FLyrVect vectLyr = (FLyrVect) aux;
            VectorialDriver driver = vectLyr.getSource().getDriver();
            if (!(driver instanceof com.iver.cit.gvsig.fmap.drivers.AbstractCadMemoryDriver)) return;
            if (!(driver instanceof DwgMemoryDriver)) {
                JOptionPane.showConfirmDialog(null, "Esta herramienta s�lo funciona con libFMap del HEAD.\nVer comentarios en el codigo de esta clase.");
                return;
            }
            final com.iver.cit.gvsig.fmap.drivers.AbstractCadMemoryDriver dwgDriver = (com.iver.cit.gvsig.fmap.drivers.AbstractCadMemoryDriver) driver;
            double realTol = mapCtrl.getViewPort().toMapDistance(pixelTolerance);
            Rectangle2D recPoint = new Rectangle2D.Double(pReal.getX() - (realTol / 2), pReal.getY() - (realTol / 2), realTol, realTol);
            try {
                vectLyr.process(new FeatureVisitor() {

                    public void visit(IGeometry g, int index) throws VisitorException {
                        DwgObject dwgObj = (DwgObject) ((DwgMemoryDriver) dwgDriver).getCadSource(index);
                        DwgHandleReference handle = dwgObj.getLayerHandle();
                        int lyrHdlCode = handle.getCode();
                        int lyrHdl = handle.getOffset();
                        JOptionPane.showConfirmDialog(null, "hdlCode=" + lyrHdlCode + ",hdl=" + lyrHdl + "entity=" + dwgObj.getClass().getName());
                    }

                    public String getProcessDescription() {
                        return "";
                    }

                    public void stop(FLayer layer) {
                    }

                    public boolean start(FLayer layer) {
                        return true;
                    }
                }, recPoint);
            } catch (VisitorException e) {
                e.printStackTrace();
            } catch (ReadDriverException e) {
                e.printStackTrace();
            }
        }
    }

    public Cursor getCursor() {
        return cur;
    }

    public boolean cancelDrawing() {
        return false;
    }

    public void pointDoubleClick(PointEvent event) throws BehaviorException {
        point(event);
    }
}
