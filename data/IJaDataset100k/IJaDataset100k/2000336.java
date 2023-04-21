package ArianneEditor;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * ChartShapeBean � il bean che compone l'oggetto ChartShape
 * <p>
 *
 * @author      Andrea Annibali
 * @version     1.0
 */
public class EditorChartShapeBean extends ArianneUtil.ChartShapeBean {

    private EditorChartShape owner = null;

    public EditorChartShapeBean(EditorChartShape ow, Hashtable cvs, java.sql.Connection c, int x, int y, double minYR, double maxYR, Dimension minSize, Dimension prefSize, Color bckCol, Color brdCol, String ct, String yrl, String xrl, String ctfn, int ctsty, int ctsz, int ctc, Font fnt, Color fntColor, int hc, boolean st, boolean as, JPopupMenu jp, int msec, ImageIcon bckImg, float alpha, boolean sl, boolean vori) {
        super(ow, cvs, c, x, y, minSize, prefSize, bckCol, brdCol, ct, yrl, xrl, ctfn, ctsty, ctsz, ctc, fnt, fntColor, hc, st, as, jp, msec, bckImg, alpha, minYR, maxYR, sl, vori);
        setOwner(ow);
        try {
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public EditorChartShape getOwner() {
        return owner;
    }

    public void setOwner(EditorChartShape ow) {
        owner = ow;
    }

    /**
   * Inizializzazione dei componenti grafici
   *
   * @throws java.lang.Exception
   */
    public void init() {
        int len = getMouseMotionListeners().length;
        for (int i = 0; i < len; i++) {
            removeMouseListener(getMouseListeners()[0]);
            removeMouseMotionListener(getMouseMotionListeners()[0]);
        }
        len = getMouseListeners().length;
        for (int i = 0; i < len; i++) {
            removeMouseListener(getMouseListeners()[0]);
        }
        addMouseListener(new EditorChartShapeBean_chartPanel_mouseAdapter(this));
        addMouseMotionListener(new EditorChartShapeBean_chartPanel_mouseMotionAdapter(this));
        setDoubleBuffered(false);
    }

    public void chartPanel_mouseClicked(MouseEvent e) {
        this_mouseClicked(e);
    }

    public void chartPanel_mouseDragged(MouseEvent e) {
        this_mouseDragged(e);
    }

    public void chartPanel_mousePressed(MouseEvent e) {
        this_mousePressed(e);
    }

    public void chartPanel_mouseReleased(MouseEvent e) {
        this_mouseReleased(e);
    }

    public void this_mouseClicked(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mouseClicked(e);
    }

    public void this_mouseDragged(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mouseDragged(e);
    }

    public void this_mouseEntered(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mouseEntered(e);
    }

    public void this_mousePressed(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mousePressed(e);
    }

    public void this_mouseReleased(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mouseReleased(e);
    }

    public void chartPanel_mouseEntered(MouseEvent e) {
        this_mouseEntered(e);
    }

    public void chartPanel_mouseExited(MouseEvent e) {
        this_mouseExited(e);
    }

    public void chartPanel_mouseMoved(MouseEvent e) {
        this_mouseMoved(e);
    }

    public void this_mouseExited(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mouseExited(e);
    }

    public void this_mouseMoved(MouseEvent e) {
        e.translatePoint(getOwner().getChartPosX(), getOwner().getChartPosY());
        getOwner().getFatherPanel().this_mouseMoved(e);
    }
}

class EditorChartShapeBean_chartPanel_mouseAdapter extends MouseAdapter {

    private EditorChartShapeBean adaptee;

    EditorChartShapeBean_chartPanel_mouseAdapter(EditorChartShapeBean adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.chartPanel_mouseClicked(e);
    }

    public void mousePressed(MouseEvent e) {
        adaptee.chartPanel_mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        adaptee.chartPanel_mouseReleased(e);
    }

    public void mouseEntered(MouseEvent e) {
        adaptee.chartPanel_mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        adaptee.chartPanel_mouseExited(e);
    }
}

class EditorChartShapeBean_chartPanel_mouseMotionAdapter extends MouseMotionAdapter {

    private EditorChartShapeBean adaptee;

    EditorChartShapeBean_chartPanel_mouseMotionAdapter(EditorChartShapeBean adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseDragged(MouseEvent e) {
        adaptee.chartPanel_mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        adaptee.chartPanel_mouseMoved(e);
    }
}
