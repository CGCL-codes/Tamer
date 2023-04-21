package de.beas.explicanto.client.rcp.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * Box3DFigure
 * Draws a 3d box.
 * @author Lucian Brancovean
 * @version 1.0
 *  
 */
public class Box3DFigure extends BorderFigure {

    /** creates a new Box3dFigure with the specified image */
    public Box3DFigure(Image image) {
        super(image);
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
     */
    protected void paintFigure(Graphics g) {
        Rectangle r = getBounds().getCopy();
        r.crop(new Insets(r.height / 5, 0, r.height / 5, 0));
        r.crop(new Insets(0, 0, r.height / 20, r.width / 20));
        r.crop(new Insets(0, 0, 1, 1));
        Point A = r.getTopLeft();
        Point B = r.getTopRight();
        Point C = r.getBottomLeft();
        Point D = r.getBottomRight();
        Rectangle s = r.getTranslated(r.width / 20, r.height / 20);
        Point E = s.getTopRight();
        Point F = s.getBottomRight();
        Point G = s.getBottomLeft();
        PointList one = new PointList();
        one.addPoint(A);
        one.addPoint(B);
        one.addPoint(D);
        one.addPoint(C);
        PointList two = new PointList();
        two.addPoint(B);
        two.addPoint(E);
        two.addPoint(F);
        two.addPoint(D);
        PointList three = new PointList();
        three.addPoint(D);
        three.addPoint(F);
        three.addPoint(G);
        three.addPoint(C);
        g.fillPolygon(one);
        g.fillPolygon(two);
        g.fillPolygon(three);
        drawBorder(g);
        drawImage(g);
    }

    protected void drawBottomRight(Graphics g, Rectangle r) {
        r.crop(new Insets(r.height / 5, 0, r.height / 5, 0));
        r.crop(new Insets(0, 0, r.height / 20, r.width / 20));
        r.crop(new Insets(0, 0, 1, 1));
        Point A = r.getTopLeft();
        Point B = r.getTopRight();
        Point C = r.getBottomLeft();
        Point D = r.getBottomRight();
        Rectangle s = r.getTranslated(r.width / 20, r.height / 20);
        Point E = s.getTopRight();
        Point F = s.getBottomRight();
        Point G = s.getBottomLeft();
        PointList one = new PointList();
        one.addPoint(A);
        one.addPoint(B);
        one.addPoint(D);
        one.addPoint(C);
        PointList two = new PointList();
        two.addPoint(B);
        two.addPoint(E);
        two.addPoint(F);
        two.addPoint(D);
        PointList three = new PointList();
        three.addPoint(D);
        three.addPoint(F);
        three.addPoint(G);
        three.addPoint(C);
        g.drawLine(B, D);
        g.drawLine(D, C);
        g.drawLine(E, F);
        g.drawLine(F, D);
        g.drawLine(F, G);
        g.drawLine(G, C);
    }

    protected void drawTopLeft(Graphics g, Rectangle r) {
        r.crop(new Insets(r.height / 5, 0, r.height / 5, 0));
        r.crop(new Insets(0, 0, r.height / 20, r.width / 20));
        r.crop(new Insets(0, 0, 1, 1));
        Point A = r.getTopLeft();
        Point B = r.getTopRight();
        Point C = r.getBottomLeft();
        Point D = r.getBottomRight();
        Rectangle s = r.getTranslated(r.width / 20, r.height / 20);
        Point E = s.getTopRight();
        Point F = s.getBottomRight();
        Point G = s.getBottomLeft();
        PointList one = new PointList();
        one.addPoint(A);
        one.addPoint(B);
        one.addPoint(D);
        one.addPoint(C);
        PointList two = new PointList();
        two.addPoint(B);
        two.addPoint(E);
        two.addPoint(F);
        two.addPoint(D);
        PointList three = new PointList();
        three.addPoint(D);
        three.addPoint(F);
        three.addPoint(G);
        three.addPoint(C);
        g.drawLine(A, B);
        g.drawLine(A, C);
        g.drawLine(B, E);
    }
}
