package org.gvsig.rastertools.geolocation.behavior;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.util.RasterToolsUtil;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Listeners.RectangleListener;
import com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener;

/**
 * Comportamiento que se aplica a la herramienta de scale. El cursor del rat�n cambiar� cuando
 * se encuentre en los bordes del raster, permitiendo el escalar la imagen al pinchar y 
 * arrastrar.
 * 
 * Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class ScaleBehavior extends TransformationBehavior {

    private int PX_SELEC_BASE = 6;

    private int LONG_CORNER_BASE = 12;

    private int PX_SELEC = PX_SELEC_BASE;

    private int LONG_CORNER = LONG_CORNER_BASE;

    private RectangleListener listener;

    private final Image iconHoriz = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaHorizCursor.gif")).getImage();

    private final Image iconVert = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaVertCursor.gif")).getImage();

    private final Image iconInclDer = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaInclDerCursor.gif")).getImage();

    private final Image iconInclIzq = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaInclIzqCursor.gif")).getImage();

    /**
	 * Variable que si est� a true permite que se pinte el marco de la imagen. Se activa al
	 * comenzar el redimensionado y se desactiva al terminar
	 */
    private boolean isResizable = false;

    /**
	 * Escala entre la imagen inicial y la final. Esta es la que sea aplicar� al raster visualizado al
	 * terminar la operaci�n de reescalado. Esta podemos conocerla cuando levantamos el bot�n
	 * del rat�n dividiendo el nuevo ancho por el inicial
	 */
    private double escale = 1D;

    /**
	 * Lista de flags de redimensionado para cada lado del raster
	 * [0]-arriba
	 * [1]-abajo
	 * [2]-derecha
	 * [3]-izquierda
	 * [4]-esquina superior derecha
	 * [5]-esquina superior izquierda
	 * [6]-esquina inferior derecha
	 * [7]-esquina inferior izquierda
	 */
    private boolean[] sideActive = { false, false, false, false, false, false, false, false };

    private boolean init = false;

    private Point2D tmpLr, tmpUl;

    private double initWidth, endWidth;

    /**
	 * Crea un nuevo RectangleBehavior.
	 *
	 * @param zili listener.
	 */
    public ScaleBehavior(GeoRasterBehavior grb, Cursor cur, ITransformIO windowIO) {
        grBehavior = grb;
        defaultCursor = cur;
        this.trIO = windowIO;
    }

    /**
	 * Inicializaci�n. Calcula la longitud de la esquina y el ancho en p�xeles
	 * de la zona de detecci�n del puntero de rat�n. Inicialmente esto est� calculado en 
	 * unidades pixel de la vista pero la detecci�n de puntero, debido a la rotaci�n posible
	 * del raster ha de hacerse en unidades del raster en disco por ello ha de calcularse
	 * una escala entre ambas unidades cuando se carga la aplicaci�n.
	 *
	 */
    private void init() {
        ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
        lyr = grBehavior.getLayer();
        if (lyr == null) return;
        Extent ext = lyr.getDataSource().getExtent();
        AffineTransform atImg = lyr.getAffineTransform();
        Point2D ul = new Point2D.Double(ext.getULX(), ext.getULY());
        Point2D lr = new Point2D.Double(ext.getLRX(), ext.getLRY());
        Point2D ulPx = new Point2D.Double();
        Point2D lrPx = new Point2D.Double();
        Point2D ulVp = new Point2D.Double();
        Point2D lrVp = new Point2D.Double();
        double esc = 1;
        try {
            atImg.inverseTransform(ul, ulPx);
            atImg.inverseTransform(lr, lrPx);
            ulVp = vp.fromMapPoint(ul);
            lrVp = vp.fromMapPoint(lr);
            esc = Math.abs(lrPx.getX() - ulPx.getX()) / Math.abs(lrVp.getX() - ulVp.getX());
        } catch (NoninvertibleTransformException e1) {
            return;
        }
        PX_SELEC = (int) (PX_SELEC_BASE * esc);
        LONG_CORNER = (int) (LONG_CORNER_BASE * esc);
        init = true;
    }

    /**
	 * <P>
	 * Cuando se produce un evento de pintado dibujamos el marco de la imagen para
	 * que el usuario pueda seleccionar y redimensionar. Pintamos un marco a la imagen para saber 
	 * donde la movemos. 
	 * </P>
	 * <P>
	 * Para dibujar el marco alrededor del raster hacemos lo mismo que para pintar el raster
	 * rotado. En realidad dibujamos un cuadrado y luego le aplicamos las transformaciones necesarias
	 * para que se vea con la misma forma del raster al que representa.
	 * </P> 
	 */
    public void paintComponent(Graphics g) {
        if (isResizable && lyr != null) {
            try {
                ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
                Point2D ul = new Point2D.Double();
                Point2D lr = new Point2D.Double();
                AffineTransform tr = lyr.getAffineTransform();
                tr.transform(tmpUl, ul);
                tr.transform(tmpLr, lr);
                AffineTransform at = new AffineTransform(tr.getScaleX(), tr.getShearY(), tr.getShearX(), tr.getScaleY(), ul.getX(), ul.getY());
                at.preConcatenate(vp.getAffineTransform());
                vp.getAffineTransform().transform(ul, ul);
                at.inverseTransform(ul, ul);
                vp.getAffineTransform().transform(lr, lr);
                at.inverseTransform(lr, lr);
                ((Graphics2D) g).transform(at);
                g.setColor(rectangleColor);
                ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g.fillRect((int) ul.getX(), (int) ul.getY(), (int) lr.getX(), (int) lr.getY());
                ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                g.drawRect((int) ul.getX(), (int) ul.getY(), (int) lr.getX(), (int) lr.getY());
                ((Graphics2D) g).transform(at.createInverse());
            } catch (NoninvertibleTransformException e1) {
                RasterToolsUtil.messageBoxError("error_transformacion1", this, e1);
            }
        }
    }

    /**
	 * Reimplementaci�n del m�todo mousePressed de Behavior. Cuando se pulsa
	 * estando sobre el marco de la imagen activamos la posibilidad de arrastrar
	 * para redimensionar la imagen.
	 *
	 * @param e MouseEvent
	 */
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            setActiveTool(true);
            isResizable = true;
            ;
            ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
            if (lyr == null) lyr = grBehavior.getLayer();
            if (vp != null && lyr != null) {
                Extent ext = lyr.getFullRasterExtent();
                tmpUl = new Point2D.Double(ext.getULX(), ext.getULY());
                tmpLr = new Point2D.Double(ext.getLRX(), ext.getLRY());
                try {
                    lyr.getAffineTransform().inverseTransform(tmpUl, tmpUl);
                    lyr.getAffineTransform().inverseTransform(tmpLr, tmpLr);
                } catch (NoninvertibleTransformException e1) {
                    e1.printStackTrace();
                }
                initWidth = Math.abs(tmpUl.getX() - tmpLr.getX());
            }
        }
    }

    /**
	 * Reimplementaci�n del m�todo mouseReleased de Behavior.
	 *
	 * @param e MouseEvent
	 *
	 * @throws BehaviorException Excepci�n lanzada cuando el Behavior.
	 */
    public void mouseReleased(MouseEvent e) throws BehaviorException {
        resetBorderSelected();
        if (e.getButton() == MouseEvent.BUTTON1 && isResizable && lyr != null) {
            endWidth = Math.abs(tmpUl.getX() - tmpLr.getX());
            escale = endWidth / initWidth;
            AffineTransform tr = (AffineTransform) lyr.getAffineTransform().clone();
            Point2D ul = new Point2D.Double();
            Point2D lr = new Point2D.Double();
            tr.transform(tmpUl, ul);
            tr.transform(tmpLr, lr);
            AffineTransform esc = new AffineTransform(escale, 0, 0, escale, 0, 0);
            AffineTransform at = new AffineTransform(tr.getScaleX(), tr.getShearY(), tr.getShearX(), tr.getScaleY(), ul.getX(), ul.getY());
            esc.preConcatenate(at);
            lyr.setAffineTransform(esc);
            grBehavior.getMapControl().getMapContext().invalidate();
            isResizable = false;
            super.mouseReleased(e);
        }
    }

    /**
	 * Al arrastrar cuando se ha pulsado sobre el marco de la imagen recalculamos
	 * el marco de la imagen 
	 *
	 * @param e MouseEvent
	 */
    public void mouseDragged(MouseEvent ev) {
        if (isResizable) {
            if (tmpLr == null || tmpUl == null || lyr == null) return;
            setActiveTool(true);
            ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
            Point2D e = new Point2D.Double(ev.getX(), ev.getY());
            e = vp.toMapPoint(e);
            try {
                lyr.getAffineTransform().inverseTransform(e, e);
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }
            double longLadoX = Math.abs(tmpLr.getX() - tmpUl.getX());
            double longLadoY = Math.abs(tmpLr.getY() - tmpUl.getY());
            double coordCentroV = tmpUl.getY() + (Math.abs(tmpLr.getY() - tmpUl.getY()) / 2);
            double coordCentroH = tmpUl.getX() + (Math.abs(tmpLr.getX() - tmpUl.getX()) / 2);
            if (sideActive[2]) {
                double newLadoX = (e.getX() - tmpUl.getX());
                double newLadoY = (newLadoX * longLadoY) / longLadoX;
                tmpUl = new Point2D.Double(tmpUl.getX(), coordCentroV - (newLadoY / 2));
                tmpLr = new Point2D.Double(e.getX(), coordCentroV + (newLadoY / 2));
            } else if (sideActive[3]) {
                double newLadoX = (e.getX() - tmpLr.getX());
                double newLadoY = (newLadoX * longLadoY) / longLadoX;
                tmpLr = new Point2D.Double(tmpLr.getX(), coordCentroV - (newLadoY / 2));
                tmpUl = new Point2D.Double(e.getX(), coordCentroV + (newLadoY / 2));
            } else if (sideActive[1]) {
                double newLadoY = (e.getY() - tmpUl.getY());
                double newLadoX = (newLadoY * longLadoX) / longLadoY;
                tmpLr = new Point2D.Double(coordCentroH + (newLadoX / 2), e.getY());
                tmpUl = new Point2D.Double(coordCentroH - (newLadoX / 2), tmpUl.getY());
            } else if (sideActive[0]) {
                double newLadoY = (tmpLr.getY() - e.getY());
                double newLadoX = (newLadoY * longLadoX) / longLadoY;
                tmpUl = new Point2D.Double(coordCentroH - (newLadoX / 2), e.getY());
                tmpLr = new Point2D.Double(coordCentroH + (newLadoX / 2), tmpLr.getY());
            } else if (sideActive[6]) {
                double rel = longLadoX / longLadoY;
                double difY = (e.getY() - tmpLr.getY());
                double difX = (e.getX() - tmpLr.getX());
                if (difX > difY) {
                    difY = difX / rel;
                } else difX = difY * rel;
                tmpUl = new Point2D.Double(tmpUl.getX(), tmpUl.getY());
                tmpLr = new Point2D.Double(tmpLr.getX() + difX, tmpLr.getY() + difY);
            } else if (sideActive[5]) {
                double rel = longLadoX / longLadoY;
                double difY = (tmpUl.getY() - e.getY());
                double difX = (tmpUl.getX() - e.getX());
                if (difX > difY) {
                    difY = difX / rel;
                } else difX = difY * rel;
                tmpUl = new Point2D.Double(tmpUl.getX() - difX, tmpUl.getY() - difY);
                tmpLr = new Point2D.Double(tmpLr.getX(), tmpLr.getY());
            } else if (sideActive[4]) {
                double rel = longLadoX / longLadoY;
                double difY = (tmpUl.getY() - e.getY());
                double difX = (e.getX() - tmpLr.getX());
                if (difX > difY) {
                    difY = difX / rel;
                } else difX = difY * rel;
                tmpUl = new Point2D.Double(tmpUl.getX(), tmpUl.getY() - difY);
                tmpLr = new Point2D.Double(tmpLr.getX() + difX, tmpLr.getY());
            } else if (sideActive[7]) {
                double rel = longLadoX / longLadoY;
                double difY = (e.getY() - tmpLr.getY());
                double difX = (tmpUl.getX() - e.getX());
                if (difX > difY) difY = difX / rel; else difX = difY * rel;
                tmpUl = new Point2D.Double(tmpUl.getX() - difX, tmpUl.getY());
                tmpLr = new Point2D.Double(tmpLr.getX(), tmpLr.getY() + difY);
            }
            assignTransformToDialog();
            grBehavior.getMapControl().repaint();
        }
    }

    /**
	 * Asigna las coordenadas temporales en el dialogo.
	 */
    public void assignTransformToDialog() {
        Point2D ul = new Point2D.Double();
        Point2D lr = new Point2D.Double();
        AffineTransform tr = grBehavior.getLayer().getAffineTransform();
        tr.transform(tmpUl, ul);
        tr.transform(tmpLr, lr);
        endWidth = Math.abs(tmpUl.getX() - tmpLr.getX());
        escale = endWidth / initWidth;
        AffineTransform esc = new AffineTransform(escale, 0, 0, escale, 0, 0);
        AffineTransform at = new AffineTransform(tr.getScaleX(), tr.getShearY(), tr.getShearX(), tr.getScaleY(), ul.getX(), ul.getY());
        esc.preConcatenate(at);
        trIO.loadTransform(esc);
    }

    /**
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#setListener(org.gvsig.georeferencing.fmap.tools.ToolListener)
	 */
    public void setListener(ToolListener listener) {
        this.listener = (RectangleListener) listener;
    }

    /**
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
    public ToolListener getListener() {
        return listener;
    }

    /**
	 * Cuando movemos el rat�n detecta si estamos en el marco de la 
	 * imagen y pone el icono del cursor del rat�n adecuado.
	 */
    public boolean mouseMoved(MouseEvent ev) throws BehaviorException {
        if (!init) init();
        ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
        resetBorderSelected();
        lyr = grBehavior.getLayer();
        if (lyr == null) {
            setActiveTool(false);
            return false;
        }
        AffineTransform atImg = lyr.getAffineTransform();
        Point2D e = vp.toMapPoint(ev.getX(), ev.getY());
        try {
            atImg.inverseTransform(e, e);
        } catch (NoninvertibleTransformException e1) {
            return false;
        }
        Point2D p1 = new Point2D.Double(0, 0);
        Point2D p2 = new Point2D.Double(lyr.getDataSource().getWidth(), lyr.getDataSource().getHeight());
        if (e.getX() < p1.getX() || e.getX() > p2.getX() || e.getY() < p1.getY() || e.getY() > p2.getY()) {
            setActiveTool(false);
            grBehavior.getMapControl().repaint();
            grBehavior.getMapControl().setCursor(defaultCursor);
            return false;
        }
        if (e.getX() > p1.getX() && e.getX() <= (p1.getX() + PX_SELEC) && e.getY() > (p1.getY() + LONG_CORNER) && e.getY() < (p2.getY() - LONG_CORNER)) {
            setCursor(iconHoriz);
            setActiveTool(true);
            sideActive[3] = true;
            return true;
        }
        if (e.getX() >= (p2.getX() - PX_SELEC) && e.getX() < p2.getX() && e.getY() > (p1.getY() + LONG_CORNER) && e.getY() < (p2.getY() - LONG_CORNER)) {
            setCursor(iconHoriz);
            setActiveTool(true);
            sideActive[2] = true;
            return true;
        }
        if (e.getY() > p1.getY() && e.getY() <= (p1.getY() + PX_SELEC) && e.getX() > (p1.getX() + LONG_CORNER) && e.getX() < (p2.getX() - LONG_CORNER)) {
            setCursor(iconVert);
            setActiveTool(true);
            sideActive[0] = true;
            return true;
        }
        if (e.getY() >= (p2.getY() - PX_SELEC) && e.getY() < p2.getY() && e.getX() > (p1.getX() + LONG_CORNER) && e.getX() < (p2.getX() - LONG_CORNER)) {
            setCursor(iconVert);
            setActiveTool(true);
            sideActive[1] = true;
            return true;
        }
        if ((e.getX() > p1.getX() && e.getX() <= (p1.getX() + LONG_CORNER) && e.getY() > p1.getY() && e.getY() < (p1.getY() + PX_SELEC)) || (e.getX() > p1.getX() && e.getX() <= (p1.getX() + PX_SELEC) && e.getY() > p1.getY() && e.getY() <= (p1.getY() + LONG_CORNER))) {
            if ((atImg.getScaleX() * atImg.getScaleY()) < 0) setCursor(iconInclIzq); else setCursor(iconInclDer);
            setActiveTool(true);
            sideActive[5] = true;
            return true;
        }
        if ((e.getX() >= (p2.getX() - LONG_CORNER) && e.getX() < p2.getX() && e.getY() > p1.getY() && e.getY() < (p1.getY() + PX_SELEC)) || (e.getX() >= (p2.getX() - PX_SELEC) && e.getX() < p2.getX() && e.getY() > p1.getY() && e.getY() <= (p1.getY() + LONG_CORNER))) {
            if ((atImg.getScaleX() * atImg.getScaleY()) < 0) setCursor(iconInclDer); else setCursor(iconInclIzq);
            setActiveTool(true);
            sideActive[4] = true;
            return true;
        }
        if ((e.getX() > p1.getX() && e.getX() <= (p1.getX() + LONG_CORNER) && e.getY() >= (p2.getY() - PX_SELEC) && e.getY() < p2.getY()) || (e.getX() > p1.getX() && e.getX() <= (p1.getX() + PX_SELEC) && e.getY() < p2.getY() && e.getY() >= (p2.getY() - LONG_CORNER))) {
            if ((atImg.getScaleX() * atImg.getScaleY()) < 0) setCursor(iconInclDer); else setCursor(iconInclIzq);
            setActiveTool(true);
            sideActive[7] = true;
            return true;
        }
        if ((e.getX() < p2.getX() && e.getX() >= (p2.getX() - LONG_CORNER) && e.getY() >= (p2.getY() - PX_SELEC) && e.getY() < p2.getY()) || (e.getX() < p2.getX() && e.getX() >= (p2.getX() - PX_SELEC) && e.getY() < p2.getY() && e.getY() >= (p2.getY() - LONG_CORNER))) {
            if ((atImg.getScaleX() * atImg.getScaleY()) < 0) setCursor(iconInclIzq); else setCursor(iconInclDer);
            setActiveTool(true);
            sideActive[6] = true;
            return true;
        }
        grBehavior.getMapControl().repaint();
        grBehavior.getMapControl().setCursor(defaultCursor);
        return false;
    }

    /**
	 * Pone a false todos los elementos del array sideActive. Esto es equivalente 
	 * a eliminar cualquier selecci�n de borde.
	 */
    private void resetBorderSelected() {
        for (int i = 0; i < sideActive.length; i++) sideActive[i] = false;
    }
}
