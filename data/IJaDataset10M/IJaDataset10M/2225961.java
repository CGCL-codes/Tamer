package jfigure.geom2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import jfigure.graphics2D.*;
import jfigure.util.GraphicsUtils;
import jfigure.util.StringUtilities;

/**
 * Gestion des hauteurs issues d'un point sur un segment
 *
 * @version 1.0
 **/
public final class Hight extends Line {

    private final Segment cote;

    private final JfigurePoint point;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6869794470754662L;

    /**
     * Constructeur d'une hauteur � partir d'un segment et d'un point
     **/
    public Hight(Segment s, JfigurePoint p) {
        this.cote = s;
        this.point = p;
        this.isLocked = true;
        Line dx = new Line(this.cote.getP1(), this.cote.getP2());
        Line d = new Perpendicular(dx, this.point);
        this.p1.setCoordonnates(d.getP1());
        this.p2.setCoordonnates(d.getP2());
    }

    /**
     * Affichage de la hauteur sur une afficheur avec des propri�ts graphiques
     **/
    public final void display_(Painter2D afficheur) {
        Line dx = new Line(this.cote.getP1(), this.cote.getP2());
        Line d = new Perpendicular(dx, this.point);
        JfigurePoint inter = d.intersect(dx);
        this.p1.setCoordonnates(d.getP1());
        this.p2.setCoordonnates(d.getP2());
        if (inter != null) {
            GraphicsUtils.drawSquareCode(this.point, inter, this.cote.getP1(), afficheur);
        }
        super.display_(afficheur);
    }

    /**
     * Retourne le noeud xml repr�sentant cette hauteur
     **/
    public Element getXmlRepresentation_(Document docXml, Element racine, Painter2D afficheur) {
        Line dx = new Line(this.cote.getP1(), this.cote.getP2());
        Line d = new Perpendicular(dx, this.point);
        JfigurePoint inter = d.intersect(dx);
        super.getXmlRepresentation_(docXml, racine, afficheur);
        JfigurePoint pxx[] = GraphicsUtils.drawSquareCodeForXml(this.point, inter, this.cote.getP1(), afficheur);
        Element codeElement = docXml.createElement("LineCode");
        racine.appendChild(codeElement);
        codeElement.setAttribute("X1", StringUtilities.formatDouble(pxx[0].x));
        codeElement.setAttribute("Y1", StringUtilities.formatDouble(pxx[0].y));
        codeElement.setAttribute("X2", StringUtilities.formatDouble(pxx[1].x));
        codeElement.setAttribute("Y2", StringUtilities.formatDouble(pxx[1].y));
        codeElement.setAttribute("X3", StringUtilities.formatDouble(pxx[2].x));
        codeElement.setAttribute("Y3", StringUtilities.formatDouble(pxx[2].y));
        return racine;
    }
}
