package edu.jrous.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import edu.jrous.ui.Constantes;
import edu.jrous.ui.Interface;

/**
 * <p>Clase que origina toda la rama de link</p>
 * @author Manuel Sako
 * @version 1.0
 */
public abstract class FigLink extends AbstractFig implements Serializable, MouseListener, MouseMotionListener {

    /**
	 * Constructor de la clase FigLink
	 * @param _owner
	 * @param _idDevice
	 * @param _name
	 * @param _iconname
	 */
    public FigLink(Interface _owner, long _idLink, int drawX1, int drawY1, int drawX2, int drawY2, String _interBegin, String _interSecond) {
        super(_owner, _idLink, "", "");
        if (drawX1 < drawX2) {
            x2 = drawX2 - drawX1;
            x1 = 0;
        } else {
            x1 = drawX1 - drawX2;
            x2 = 0;
        }
        if (drawY1 < drawY2) {
            y2 = drawY2 - drawY1 + 10;
            y1 = 10;
        } else {
            y1 = drawY1 - drawY2 + 10;
            y2 = 10;
        }
        addMouseListener(this);
        addMouseMotionListener(this);
        if (x2 == 0 && y2 == 0) {
            x2 = x1;
            x1 = 10;
            y2 = y1;
            y1 = 10;
            a1 = interBegin = _interSecond;
            a2 = interSecond = _interBegin;
        }
        if (x1 != 0) {
            y1 = y2;
            y2 = 10;
            x2 = x1;
            x1 = 10;
            a1 = interBegin = _interSecond;
            a2 = interSecond = _interBegin;
        }
        a1 = interBegin = _interBegin;
        a2 = interSecond = _interSecond;
        e2 = y2 - 2;
        g1 = x2 - 90;
        e1 = x2 - 30;
    }

    /**
	 * La posicion x de la grafica
	 */
    public int x1;

    /**
	 * La posicion x final de la grafica
	 */
    public int x2;

    /**
	 * La posicion y de la grafica
	 */
    public int y1;

    /**
	 * La posicion y final de la grafica
	 */
    public int y2;

    /**
	 * La primera Interface 
	 */
    public String interBegin;

    /**
	 * La segunda Interface 
	 */
    public String interSecond;

    /**
	 * La primera IP 
	 */
    public String ip1 = "";

    /**
	 * La segunda IP 
	 */
    public String ip2 = "";

    /**
	 * Nombre a interface a mostrar
	 */
    private String a1;

    /**
	 * Nombre a interface a mostrar
	 */
    private String a2;

    /**
     * Color
     */
    public Color so = Constantes.COLOR_GREEN;

    /**
     * Punto de Y en la primera interface el nombre
     */
    public int d2 = 22;

    /**
     * Punto de Y en la segunda interface del nombre
     */
    public int e2;

    public int e1;

    /**
     * D1 posicion X de primer interface
     */
    public int d1 = 5;

    public int f1 = 6;

    public int f2 = 8;

    public int g1;

    public int g2 = 22;

    /**
     * <p>Dibuja en el sistema</p>
     */
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(so);
        g.drawLine(x1, y1, x2, y2);
        g.setFont(Constantes.FONT_SHELL);
        g.drawString(a1, d1, d2);
        g.drawString(a2, e1, e2);
        g.setColor(Constantes.COLOR_BLUE);
        g.setFont(Constantes.FONT_IPs);
        g.drawString(ip1, f1, f2);
        g.drawString(ip2, g1, g2);
    }

    public void repaintSelected() {
        if (!selected) {
            so = Constantes.COLOR_GREEN;
            a1 = "";
            a2 = "";
        }
    }

    public void selectDevice() {
        viewCaracter();
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseDragged(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    private void viewCaracter() {
        so = Constantes.COLOR_ROJON;
        a1 = interBegin;
        a2 = interSecond;
        repaint();
    }
}
