package org.arpenteur.common.gui.reflet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.arpenteur.common.gui.Reflet;

public class DataPoint extends RefletRectangulaire {

    /** Couleur par defaut = Color.RED */
    protected static final Color defaultColor = Color.red;

    /** Ce point est-il exprime en coordonnees ecran ?
   *  <P> e priori non : les points sont exprimes en coodonnees pixel
   *  i.e. coordonnees sur l'image e l'echelle 1 (pas de zoom)
   */
    private boolean isCoordEcran = false;

    /**
   * Construit un point de donnee e l'aide de la position du centre et de la
   * demi-largeur de la croix.
   */
    public DataPoint(double x, double y, double halfWidth, Color aColor, boolean isScaled, boolean isEnabled, int type) {
        super(x, y, halfWidth, aColor, isScaled, isEnabled, type);
    }

    /**
   * Construit un point de donnee e l'aide de la position du centre et de la
   * demi-largeur de la croix.
   */
    public DataPoint(double x, double y, double halfWidth, Color aColor, boolean isScaled, boolean isEnabled) {
        this(x, y, halfWidth, aColor, isScaled, isEnabled, Reflet.TYPE_DATA);
    }

    public DataPoint(double x, double y, double halfWidth) {
        this(x, y, halfWidth, defaultColor, false, true);
    }

    public DataPoint(double halfWidth) {
        this(0, 0, halfWidth * 2);
    }

    protected DataPoint(double x, double y, double halfWidth, Color aColor, boolean isScaled, boolean isEnabled, boolean exprimeSurEcran) {
        this(x, y, halfWidth, aColor, isScaled, isEnabled);
        isCoordEcran = exprimeSurEcran;
    }

    public Point2D getDataPoint() {
        return new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    /** Origine du reflet */
    @Override
    public Point2D getLocation() {
        return getDataPoint();
    }

    /** Dessin d'une croix avec les branches paralleles aux axes */
    public static void draw(Graphics g, Rectangle2D bounds) {
        g.drawLine((int) Math.round(bounds.getX()), (int) Math.round(bounds.getY() + bounds.getHeight() / 2), (int) Math.round(bounds.getX() + bounds.getWidth()), (int) Math.round(bounds.getY() + bounds.getHeight() / 2));
        g.drawLine((int) Math.round(bounds.getX() + bounds.getWidth() / 2), (int) Math.round(bounds.getY()), (int) Math.round(bounds.getX() + bounds.getWidth() / 2), (int) Math.round(bounds.getY() + bounds.getHeight()));
    }

    protected static Point2D milieu(Point2D p1, Point2D p2) {
        return new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
    }

    /** Dessin d'une croix avec les branches paralleles aux axes */
    public static void draw(Graphics g, Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        Point2D c1 = milieu(p1, p4);
        Point2D c2 = milieu(p2, p3);
        g.drawLine((int) Math.round(c1.getX()), (int) Math.round(c1.getY()), (int) Math.round(c2.getX()), (int) Math.round(c2.getY()));
        c1 = milieu(p1, p2);
        c2 = milieu(p3, p4);
        g.drawLine((int) Math.round(c1.getX()), (int) Math.round(c1.getY()), (int) Math.round(c2.getX()), (int) Math.round(c2.getY()));
    }

    @Override
    protected void drawReflet(Graphics g, Rectangle2D newBounds, AffineTransform transform) {
        if (newBounds instanceof RotatableRect) {
            RotatableRect r = (RotatableRect) newBounds;
            draw(g, r.getFirstCorner(), r.getSecondCorner(), r.getThirdCorner(), r.getFourthCorner());
        } else {
            DataPoint.draw(g, newBounds);
        }
    }

    @Override
    public void setLocation(double x, double y) {
        super.setLocation(x, y);
        double halfWidth = getWidth() / 2;
        translate(-halfWidth, -halfWidth);
    }

    /** Renvoie les sommets de ce Reflet, exprime dans un ref. neutre */
    @Override
    public Point2D[] getVertices() {
        return new Point2D[] { getLocation() };
    }
}
