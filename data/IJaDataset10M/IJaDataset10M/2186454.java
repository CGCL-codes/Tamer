package jfigure.geom2D;

import jfigure.graphics2D.*;

/**
 * Gestion des trap�res
 *
 * @version 1.0
 **/
public final class Trapezoid extends Quadrilatere {

    /**
     * Le nombre de trap�ze
     **/
    private static int NOMBRE = 0;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -989470754668L;

    /**
     * Costructeur par d�faut d'un trap�ze
     **/
    public Trapezoid() {
        super();
        this.p4.isLocked = true;
        this.c4.isLocked = true;
    }

    /**
     * Constructeur d'un trap�ze � partir de quatre points
     **/
    public Trapezoid(JfigurePoint p1, JfigurePoint p2, JfigurePoint p3, JfigurePoint p4) {
        this();
        this.pts[0].x = p1.x;
        this.pts[0].y = p1.y;
        this.pts[1].x = p2.x;
        this.pts[1].y = p2.y;
        this.pts[2].x = p3.x;
        this.pts[2].y = p3.y;
        this.pts[3].x = p4.x;
        this.pts[3].y = p4.y;
        this.reShape();
    }

    /**
     * Recalcul des coordonn�es des sommets
     **/
    private final void reShape() {
        Line d12 = new Line(this.p1.copy(), this.p2.copy());
        Line d14 = new Line(this.p1.copy(), this.p4.copy());
        Parallel d34 = new Parallel(d12, this.p3.copy());
        JfigurePoint px = d34.intersect(d14);
        this.p4.x = px.x;
        this.p4.y = px.y;
    }

    /**
     * Affichage du trap�ze
     **/
    protected void display_(Painter2D afficheur) {
        this.reShape();
    }
}
