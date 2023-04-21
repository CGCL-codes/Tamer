package metier.outils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import metier.modele.Figure;

/**
 * Figure => Rectangle
 * @author Quentin, Vincent, Charlie
 *
 */
public class Tools_Rect extends Figure {

    private static final long serialVersionUID = 1L;

    /**
	 * Tracé du rectangle
	 */
    public void tracer(Graphics g, boolean couleursVraies, boolean edit, Color color) {
        Color cbg, cfg;
        if (couleursVraies == true) {
            cbg = getBg();
            cfg = getFg();
        } else {
            cbg = color;
            cfg = color;
        }
        if (getBg() != null) {
            g.setColor(cbg);
            g.fillRect(getX1(), getY1(), Math.abs(getX2() - getX1()), Math.abs(getY2() - getY1()));
        }
        g.setColor(cfg);
        g.drawRect(getX1(), getY1(), Math.abs(getX2() - getX1()), Math.abs(getY2() - getY1()));
        if (edit == true) addEditRect(g);
        if (getStatus() == Figure.SUPPRESSION) addDeleteRect((Graphics2D) g);
    }

    /**
	 * Ajout du rectangle de suppression
	 * @param g
	 */
    public void addDeleteRect(Graphics2D g) {
        int offset = Tools.EDIT_RECT_SIZE;
        float epaisseur = 1;
        float[] style = { 10, 5 };
        g.setStroke(new BasicStroke(epaisseur, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, style, 0));
        g.drawRect(getX1() - offset, getY1() - offset, Math.abs(getX2() - getX1()) + 2 * offset, Math.abs(getY2() - getY1()) + 2 * offset);
        g.setStroke(new BasicStroke(1.0f));
    }

    /**
	 * Ajout des rectangle de redimensionnement
	 * @param g
	 */
    public void addEditRect(Graphics g) {
        int offset = Tools.EDIT_RECT_SIZE;
        g.fillRect(getX1() - offset / 2, getY1() - offset / 2, offset, offset);
        g.fillRect(getX2() - offset / 2, getY2() - offset / 2, offset, offset);
    }

    public void setDonnees(int x1, int y1, int x2, int y2) {
        if (x1 < x2) {
            setX1(x1);
            setX2(x2);
        } else {
            setX1(x2);
            setX2(x1);
        }
        if (y1 < y2) {
            setY1(y1);
            setY2(y2);
        } else {
            setY1(y2);
            setY2(y1);
        }
    }

    /**
	 * Est-ce que le point 'p' est dans la figure ?
	 */
    public boolean pointInFigure(Point p) {
        int x1 = getX1(), x2 = getX2(), y1 = getY1(), y2 = getY2();
        int x = p.x, y = p.y;
        if ((x >= x1 && x <= x2) && (y >= y1 && y <= y2)) return true;
        return false;
    }

    /**
	 * Est-ce que le point 'p' est sur la figure (périmètre) ?
	 * -----1-----
	 * |         |
	 * 2         3
	 * |         |
	 * -----4-----
	 */
    public boolean pointOnFigure(Point p) {
        Point pe = new Point();
        Point ps = new Point();
        double dist;
        int x2 = Math.abs(getX2() - getX1());
        int y2 = Math.abs(getY2() - getY1());
        pe.x = getX1();
        pe.y = getY1();
        ps.x = getX1() + x2;
        ps.y = getY1();
        dist = distanceToSegment(pe, ps, p);
        if (dist < Tools.MIN_DISTANCE) return true;
        pe.x = getX1();
        pe.y = getY1();
        ps.x = getX1();
        ps.y = getY1() + y2;
        dist = distanceToSegment(pe, ps, p);
        if (dist < Tools.MIN_DISTANCE) return true;
        pe.x = getX1() + x2;
        pe.y = getY1();
        ps.x = getX1() + x2;
        ps.y = getY1() + y2;
        dist = distanceToSegment(pe, ps, p);
        if (dist < Tools.MIN_DISTANCE) return true;
        pe.x = getX1();
        pe.y = getY1() + y2;
        ps.x = getX1() + x2;
        ps.y = getY1() + y2;
        dist = distanceToSegment(pe, ps, p);
        if (dist < Tools.MIN_DISTANCE) return true;
        return false;
    }

    /**
	 * Est-ce que le point 'p' est dans les petits
	 * rectangle de redimensionnement ?
	 */
    public int pointInEditFigure(Point p) {
        int x = p.x;
        int y = p.y;
        int offset = Tools.EDIT_RECT_SIZE / 2;
        if ((x >= getX1() - offset && x <= getX1() + offset) && (y >= getY1() - offset && y <= getY1() + offset)) return 2;
        if ((x >= getX2() - offset && x <= getX2() + offset) && (y >= getY2() - offset && y <= getY2() + offset)) return 1; else return 0;
    }
}
