package jfigure.geom2D.transformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import jfigure.geom2D.*;
import jfigure.graphics2D.Painter2D;

/**
 * Gestion des figures transform�es
 */
public class TransformedFigure extends Figure {

    private final Application application;

    private final Figure figure;

    private Figure transformedFigure;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754680L;

    /**
     *  Cr�ation d'une figure transform�e
     */
    public TransformedFigure(Figure fig, Application app) {
        this.figure = fig;
        this.application = app;
        this.isLocked = true;
        this.setName(this.application.getName() + "(" + this.figure.getName() + ")");
        this.type = "TransformedFigure";
        this.transformedFigure = (Figure) this.figure.clone();
        this.addChild(this.transformedFigure, false);
        this.removeLegend();
    }

    /**
     * Affichage de la figure transform�e
     **/
    public void display_(Painter2D painter) {
        Object o = this.figure.clone();
        if (o != null) {
            Figure fig = (Figure) o;
            this.application.reShape();
            fig.transform(this.application);
            this.transformedFigure.setName(this.application.getName() + "(" + this.figure.getName() + ")");
            this.transformedFigure.validate(fig);
        }
    }

    /**
     * Retourne le noeud xml repr�sentant cette figure transform�e
     **/
    public Element getXmlRepresentation_(Document docXml, Element racine, Painter2D afficheur) {
        return this.figure.getXmlRepresentation(docXml, racine, afficheur);
    }
}
