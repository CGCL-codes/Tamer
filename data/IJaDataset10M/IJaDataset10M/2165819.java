package jfigure.geom2D;

import java.awt.*;
import java.util.ArrayList;
import jfigure.geom2D.transformation.Application;
import jfigure.geom2D.transformation.Rotation;
import jfigure.graphics2D.*;
import jfigure.util.*;
import jfigure.alg.*;
import jfigure.commons.DisplayableException;
import jfigure.commons.properties.DisplayableProperties;
import jfigure.commons.properties.InformationProperties;
import org.w3c.dom.*;

/**
 * Gestion des droites
 *
 * @version 1.0
 **/
public class Line extends Figure {

    protected JfigurePoint p1, p2;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754661L;

    private double theta;

    /**
     * Constructeur par d�faut d'une droite
     **/
    public Line() {
        this.p1 = new JfigurePoint();
        this.p2 = new JfigurePoint();
        this.setName("(AB)");
        this.setDisplayType("Droite");
        this.type = "Line";
        this.legend.setVisible(false);
    }

    /**
     * Constructeur d'une droite � partir de deux points
     **/
    public Line(JfigurePoint p1, JfigurePoint p2) {
        this();
        this.p1 = p1;
        this.p2 = p2;
        this.setName("(" + p1.getName() + p2.getName() + ")");
    }

    /**
     * Cr�ation d'une droite par un point et un vecteur directeur
     **/
    public Line(JfigurePoint pt, JfigureVector v) {
        this(pt, new JfigurePoint(pt.getX() + v.getX(), pt.getY() + v.getY()));
    }

    /**
     * Retourne la distance � la l�gende
     */
    public double[] getLegendRelativeLocation(Painter2D painter) {
        double r[] = new double[2];
        JfigurePoint m = this.getSegment().getMiddle();
        r[0] = m.x;
        r[1] = m.y + 0.1;
        return r;
    }

    /**
     * Retourne le segment port� par la droite
     **/
    public final Segment getSegment() {
        return new Segment(this.p1, this.p2);
    }

    /**
     * Retourne le vecteur directeur unitaire de cette droite
     **/
    public final JfigureVector getVector() {
        Segment seg = this.getSegment();
        double l = seg.length();
        if (l != 0.0) return new JfigureVector((seg.getP2().getX() - seg.getP1().getX()) / l, (seg.getP2().getY() - seg.getP1().getY()) / l); else return new JfigureVector(0.0, 0.0);
    }

    /**
     * Retourne les points extremes de la droite par rapport � un afficheur
     **/
    public final JfigurePoint[] getExtremPoints(Painter2D painter) {
        JfigurePoint pe1 = new JfigurePoint(painter.getScale().cx, painter.getScale().cy);
        JfigurePoint pe2 = new JfigurePoint(painter.getScale().cx, painter.getScale().cy + painter.getScale().ly);
        JfigurePoint pe3 = new JfigurePoint(painter.getScale().cx + painter.getScale().lx, painter.getScale().cy + painter.getScale().ly);
        JfigurePoint pe4 = new JfigurePoint(painter.getScale().cx + painter.getScale().lx, painter.getScale().cy);
        Segment s1 = new Segment(pe1, pe2);
        Segment s2 = new Segment(pe2, pe3);
        Segment s3 = new Segment(pe3, pe4);
        Segment s4 = new Segment(pe4, pe1);
        ArrayList points = new ArrayList();
        JfigurePoint se1 = this.intersect(s1);
        if (se1 != null) points.add(se1);
        JfigurePoint se2 = this.intersect(s2);
        if (se2 != null) {
            if (se1 != null && !se2.isTheSame(se1)) points.add(se2);
            if (se1 == null) points.add(se2);
        }
        JfigurePoint se3 = this.intersect(s3);
        if (se3 != null) points.add(se3);
        JfigurePoint se4 = this.intersect(s4);
        if (se4 != null) {
            if (se3 != null && !se3.isTheSame(se4)) points.add(se4);
            if (se3 == null) points.add(se4);
        }
        JfigurePoint p1 = new JfigurePoint();
        JfigurePoint p2 = new JfigurePoint();
        if (points.size() >= 2) {
            p1.setCoordonnates((JfigurePoint) points.get(0));
            p2.setCoordonnates((JfigurePoint) points.get(1));
        }
        return new JfigurePoint[] { p1, p2 };
    }

    /**
     * Affichage de la droite sur un afficheur avec une s�rie de propri�t�s graphiques
     **/
    public void display_(Painter2D painter) {
        JfigurePoint ptsX[] = this.getExtremPoints(painter);
        Point p1 = painter.scaleTransform(ptsX[0]);
        Point p2 = painter.scaleTransform(ptsX[1]);
        painter.getGraphics2D().drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Retourne si la droite est s�lectionn�e.
     * @param sel Le point de s�lection de la souris dans l'afficheur
     * @param afficheur L'afficheur
     **/
    public final boolean mustSelect(Point sel, Painter2D afficheur) {
        JfigurePoint px = afficheur.scaleTransform(sel);
        JfigureVector v1 = new JfigureVector(this.getSegment().getP1(), px);
        JfigureVector v2 = new JfigureVector(this.getSegment().getP1(), this.getSegment().getP2());
        double pv = v1.vectorProduct(v2);
        double distance = pv / v2.getNorm();
        if (Math.abs(distance) < 0.1) {
            return true;
        }
        return false;
    }

    /**
     * Retourne la distance entre un point et une droite
     **/
    public final double distance(JfigurePoint px) {
        JfigureVector v1 = new JfigureVector(this.getSegment().getP1(), px);
        JfigureVector v2 = new JfigureVector(this.getSegment().getP1(), this.getSegment().getP2());
        double pv = v1.vectorProduct(v2);
        double distance = pv / v2.getNorm();
        return Math.abs(distance);
    }

    /**
     * Bouge la droite en question. Il s'agit de bouger le milieu
     * du segment port� par la droite au point en param�tre.
     **/
    public JfigureVector move(JfigurePoint pt, Painter2D painter) {
        JfigurePoint m = this.getSegment().getMiddle();
        double vx = pt.getX() - m.getX();
        double vy = pt.getY() - m.getY();
        JfigureVector t = new JfigureVector(vx, vy);
        this.p1.x += vx;
        this.p1.y += vy;
        this.p2.x += vx;
        this.p2.y += vy;
        return t;
    }

    /**
     * Retourne si un point donn� appartient � la droite
     **/
    public final boolean pointIsIn(JfigurePoint pt) {
        JfigureVector v1 = new JfigureVector(this.getP1(), pt);
        JfigureVector v2 = this.getVector();
        return v1.isColinear(v2);
    }

    /**
     * Retourne si deux droites sont paral�lles non confondues
     **/
    public final boolean isParallelButNotSameAs(Line d2) {
        JfigureVector v1 = this.getVector();
        JfigureVector v2 = d2.getVector();
        if (!v1.isColinear(v2)) return false;
        return !this.pointIsIn(d2.getP2());
    }

    /**
     * Retourne si deux droites sontconfondus
     **/
    public final boolean isSameAs(Line d2) {
        JfigureVector v1 = this.getVector();
        JfigureVector v2 = d2.getVector();
        if (!v1.isColinear(v2)) return false;
        return this.pointIsIn(d2.getP1());
    }

    /**
     * Intersection de deux droites. S'il y a un point d'intersection retourne
     * ce point sinon (droites parall�les  ou confondues) retourne null;
     **/
    public final JfigurePoint intersect(Line d2) {
        if (this.isSameAs(d2) || this.isParallelButNotSameAs(d2)) return null;
        JfigureVector v1 = this.getVector();
        JfigureVector v2 = d2.getVector();
        JfigurePoint a = this.getP1();
        JfigurePoint b = d2.getP2();
        Matrix2 m = new Matrix2(v2.x, -v1.x, v2.y, -v1.y);
        double[] r = m.getInverse().multiplie(new double[] { a.x - b.x, a.y - b.y });
        double xx = b.x + r[0] * v2.x;
        double yy = b.y + r[0] * v2.y;
        return new JfigurePoint(xx, yy);
    }

    /**
     * Intersection de la droite avec un segment. S'il y a un point d'intersection retourne
     * ce point sinon (droites parall�les  ou confondues) retourne null;
     **/
    public final JfigurePoint intersect(Segment s2) {
        Line d2 = new Line(s2.getP1(), s2.getP2());
        JfigurePoint p = this.intersect(d2);
        if (p == null) return null;
        if (s2.isIn(p)) return p; else return null;
    }

    /**
     * Retourne le noeud xml repr�sentant cette droite
     **/
    public Element getXmlRepresentation_(Document docXml, Element racine, Painter2D afficheur) {
        Element droiteElement = docXml.createElement("Coord");
        racine.appendChild(droiteElement);
        JfigurePoint pts[] = this.getExtremPoints(afficheur);
        droiteElement.setAttribute("X1", StringUtilities.formatDouble(pts[0].getX()));
        droiteElement.setAttribute("Y1", StringUtilities.formatDouble(pts[0].getY()));
        droiteElement.setAttribute("X2", StringUtilities.formatDouble(pts[1].getX()));
        droiteElement.setAttribute("Y2", StringUtilities.formatDouble(pts[1].getY()));
        return racine;
    }

    /**
     * @return Returns the p1.
     */
    public final JfigurePoint getP1() {
        return p1;
    }

    /**
     * @param p1 The p1 to set.
     */
    public final void setP1(JfigurePoint p1) {
        this.p1.setCoordonnates(p1);
    }

    /**
     * @return Returns the p2.
     */
    public final JfigurePoint getP2() {
        return p2;
    }

    /**
     * @param p2 The p2 to set.
     */
    public final void setP2(JfigurePoint p2) {
        this.p2.setCoordonnates(p2);
    }

    /**
     * Retourne les propri�t�s de cette droite
     */
    protected DisplayableProperties _getDisplayableProperties() {
        DisplayableProperties prop = new DisplayableProperties();
        prop.addProperty("p1.x", "Abcisse de l'origine", "Abcisse de l'origine de la droire", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p1.getX()));
        prop.addProperty("p1.y", "Ordonn�e de l'origine", "Ordonn�e de l'origine de la droite", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p1.getY()));
        prop.addProperty("p2.x", "Abcisse de l'extr�mit�", "Abcisse de l'extr�mit� de la droite", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p2.getX()));
        prop.addProperty("p2.y", "Ordonn�e de l'extr�mit�", "Ordonn�e de l'extr�mit� de la droite ", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p2.getY()));
        prop.addProperty("theta", "Angle de la rotation", "Angle de la rotation", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.theta));
        return prop;
    }

    /**
     * V�rification des caract�ristiques de la droite
     * @return
     */
    public boolean verify() throws DisplayableException {
        if (this.p1.isTheSame(this.p2)) {
            throw new DisplayableException("L'origine et l'extr�mit� de la droite sont confondues");
        }
        return true;
    }

    /**
     * Retourne les propri�t�s d'informations de l'objet <code>Figure</code>
     */
    public InformationProperties getInformationProperties() {
        InformationProperties props = new InformationProperties();
        JfigureVector v = this.getVector();
        props.addBaseProperty("direct", "Vecteur directeur", "Vecteur directeur : (" + StringUtilities.formatDouble(v.x) + "," + StringUtilities.formatDouble(v.y) + ")");
        return props;
    }

    /**
     * Copie d'une ligne
     * 
     * @return
     */
    public final Line copy() {
        return new Line(this.p1.copy(), this.p2.copy());
    }

    /**
     * Copie d'une ligne
     * 
     * @return
     */
    public final Object clone() {
        return this.copy();
    }

    /**
     * Valicaton � partir d'une autre figure
     * @param other
     */
    public void validate(Figure other) {
        if (other instanceof Line) {
            Line lx = (Line) other;
            this.p1.setCoordonnates(lx.getP1());
            this.p2.setCoordonnates(lx.getP2());
        }
    }

    /**
     * Applique une transformation � cette droite
     **/
    public void transform(Application transformation) {
        this.p1.setCoordonnates(transformation.transforme(this.p1));
        this.p2.setCoordonnates(transformation.transforme(this.p2));
    }

    /**
     * @return Returns the theta.
     */
    public final double getTheta() {
        return theta;
    }

    /**
     * @param theta The theta to set.
     */
    public final void setTheta(double theta) {
        this.theta = theta;
        Rotation r = new Rotation(this.theta, this.p1);
        r.reShape();
        JfigurePoint px = r.transforme(this.p2);
        this.p2.setCoordonnates(px);
        this.theta = 0.0;
    }
}
