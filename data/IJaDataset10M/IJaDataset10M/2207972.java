package org.arpenteur.common.math.geometry.primitive;

import org.arpenteur.common.math.geometry.BadGeometryConfigException;
import org.arpenteur.common.math.geometry.Geometry;
import org.arpenteur.common.math.geometry.point.IPoint3D;
import org.arpenteur.common.math.geometry.transformation.ReferenceSystemDefinition;
import org.arpenteur.common.math.geometry.transformation.Transformation3D;

public class Line extends Primitive {

    /**
   * 
   */
    private static final long serialVersionUID = -2479223165508507344L;

    /**
   * Constructeur par defaut d'un objet Droite.
   * Devient prive-paquet : ce n'est pas une droite (ext1=ext2, v=vecteur nul)
   */
    Line() {
        super();
    }

    /**
   * Construction d'une droite par deux points ou un point et un vecteur.
   * Le boolean p2IsVecteur indique si le deuxieme parametre doit etre traite
   * comme point ou comme vecteur directeur.
   * <P>
   * Le booleen keepDir indique si l'on veur conserver la direction du vecteur
   * (pas d'appel e orienterSurDirectionPrincipale)
   */
    public Line(IPoint3D p1, IPoint3D p2, boolean p2isVecteur, boolean keepDir) {
        this();
        if ((p1 == null) || (p2 == null)) {
            fail("Droite: construction e l'aide de points nuls");
        }
        setExt1(p1);
        if (p2isVecteur) {
            setAxis(newPoint3D(p2).normer());
            setExt2(p1.offsetOn(getAxis(), 1000.0));
        } else {
            setAxis(p2.minus(p1).normer());
            setExt2(p2);
        }
        if (!keepDir) {
            getAxis().setOrientationInMainDirection();
        }
        setOri(p1);
        if (getAxis().getNorm() < IPoint3D.EPSILON7) {
            fail("Droite : construction par deux points confondus");
        }
        defineTransformation();
    }

    /**
   * Construction d'une droite par deux points ou un point et un vecteur.
   * Le boolean p2IsVecteur indique si le deuxieme parametre doit etre traite
   * comme point ou comme vecteur directeur.
   */
    public Line(IPoint3D p1, IPoint3D p2, boolean p2isVecteur) {
        this(p1, p2, p2isVecteur, false);
    }

    /**
   * <TT>Constructeur par copie.</TT>
   */
    public Line(Line d) {
        this();
        affect(d);
    }

    public void init() {
        super.init();
        setName("Line");
    }

    public int getPrimitiveType() {
        return LINE;
    }

    protected void defineTransformation() {
        Transformation3D result = new Transformation3D(new ReferenceSystemDefinition(ReferenceSystemDefinition.definitionPlan(ReferenceSystemDefinition.ppXOY, getAxis()), ReferenceSystemDefinition.definitionAxis(IPoint3D.COORD_X, newPoint3D(1, 0, 0)), ReferenceSystemDefinition.definitionOrigin(new IPoint3D[] { getOri(), getOri(), getOri() }, 0, 0, 0)));
        setTransformation(result);
    }

    public double distanceFrom(Line dr) {
        double result = -1;
        if (dr != null) {
            IPoint3D[] pieds = Geometry.intersectionLineLine(getExt1(), getAxis(), dr.getExt1(), dr.getAxis());
            if (pieds != null) {
                result = pieds[0].dist(pieds[1]);
            }
        }
        return result;
    }

    protected boolean canIntersectWith(Primitive primitive) {
        return (primitive instanceof Line);
    }

    protected Object intersectWith(Primitive primitive) {
        return intersectionWith((Line) primitive);
    }

    public Line intersectionWith(Line dr) {
        Line result = null;
        if (dr != null) {
            IPoint3D[] pieds = Geometry.intersectionLineLine2Pt(getExt1(), getAxis(), dr.getExt1(), dr.getAxis());
            if (pieds != null) {
                result = new Line(pieds[0], pieds[1], false);
            }
        }
        return result;
    }

    public IPoint3D projectOn(IPoint3D p) {
        try {
            return Geometry.projectionOnLine2Pt(p, getExt1(), getExt2());
        } catch (BadGeometryConfigException e) {
        }
        return null;
    }

    /**
   */
    public String toString() {
        return ("Line : " + "\n Direction Vector : " + getAxis().toString() + "\n" + "point on the Line  : " + getOri().toString() + "\n");
    }
}
