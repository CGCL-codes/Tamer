package jfigure.geom2D;

import jfigure.commons.DisplayableException;
import jfigure.commons.properties.DisplayableProperties;
import jfigure.commons.properties.InformationProperties;
import jfigure.graphics2D.*;

/**
 * Gestion des carres
 *
 * @version 1.0
 **/
public final class Square extends Quadrilatere {

    protected int sens = DIRECT;

    /**
     * Pour le sens direct
     **/
    public static final int DIRECT = 0;

    /**
     * Pour le sens indirect
     **/
    public static final int INDIRECT = 1;

    private double largeur;

    /**
     * Le nombre de carres
     **/
    private static int NOMBRE = 0;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -98997944706554668L;

    /**
     * Costructeur par d�faut d'un carr�
     **/
    public Square() {
        super();
        this.p3.isLocked = true;
        this.p4.isLocked = true;
        this.c2.isLocked = true;
        this.c3.isLocked = true;
        this.c4.isLocked = true;
        this.setLegendDisplayed(false);
        this.setName("(ABCD)");
        this.setDisplayType("Carre");
        this.legend.setText(this.getName());
    }

    /**
     * Constructeur d'un carr� � partir de deux points, d'une largeur et d'un sens
     **/
    public Square(JfigurePoint p1, JfigurePoint p2, int sens) {
        this();
        this.pts[0].x = p1.x;
        this.pts[0].y = p1.y;
        this.pts[1].x = p2.x;
        this.pts[1].y = p2.y;
        this.largeur = this.p1.distance(this.p2);
        this.sens = sens;
        this.reShape();
        this.legend.setText(this.getName());
    }

    /**
     * Recalcul des coordonn�es des sommets
     **/
    private final void reShape() {
        this.largeur = this.p1.distance(this.p2.copy());
        Line d12 = new Line(this.p1.copy(), this.p2.copy());
        Perpendicular p1 = new Perpendicular(d12, this.p2.copy());
        Perpendicular p2 = new Perpendicular(d12, this.p1.copy());
        JfigureVector v1 = p1.getVector();
        JfigureVector v2 = p2.getVector();
        JfigurePoint px = new JfigurePoint(this.p2.x + this.largeur * v1.x, this.p2.y + this.largeur * v1.y);
        this.p3.x = px.x;
        this.p3.y = px.y;
        px = new JfigurePoint(this.p1.x + this.largeur * v2.x, this.p1.y + this.largeur * v2.y);
        this.p4.x = px.x;
        this.p4.y = px.y;
    }

    /**
     * Affichage du carr�
     **/
    protected final void display_(Painter2D afficheur) {
        this.reShape();
    }

    /**
     * Retourne les propri�t�s de ce carr�
     */
    protected DisplayableProperties _getDisplayableProperties() {
        DisplayableProperties prop = new DisplayableProperties();
        prop.addProperty("p1.x", "Abcisse du point 1", "Abcisse de l'origine de la droire", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p1.getX()));
        prop.addProperty("p1.y", "Ordonn�e du point 1", "Ordonn�e de l'origine de la droite", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p1.getY()));
        prop.addProperty("p2.x", "Abcisse du point 2", "Abcisse de l'extr�mit� de la droite", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p2.getX()));
        prop.addProperty("p2.y", "Ordonn�e du point 2", "Ordonn�e de l'extr�mit� de la droite ", DisplayableProperties.DPProperty.DOUBLE_INPUT, new Double(this.p2.getY()));
        prop.addProperty("displayPoint", "Points affich�s", "Si les points sont affich�s", DisplayableProperties.DPProperty.BOOLEAN_INPUT, new Boolean(this.displayPoint));
        return prop;
    }

    /**
     * Retourne les propri�t�s d'informations de l'objet <code>Figure</code>
     */
    public InformationProperties getInformationProperties() {
        InformationProperties props = new InformationProperties();
        return props;
    }

    /**
     * V�rification des caract�ristiques du carr�
     * @return
     */
    public boolean verify() throws DisplayableException {
        if (this.p1.isTheSame(this.p2)) {
            throw new DisplayableException("Le point 1 et le point 2 du quadrilat�re sont confondus");
        }
        return true;
    }
}
