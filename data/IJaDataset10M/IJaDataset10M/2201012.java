package fr.ign.cogit.geoxygene.spatial.coordgeom;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IOffsetCurve;

/**
 * NON IMPLEMENTE. Courbe qui est à une distance constante d'une courbe de base.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 * 
 */
class GM_OffsetCurve extends GM_CurveSegment implements IOffsetCurve {

    /**
   * Courbe de base à partir de laquelle est générée self.
   */
    protected GM_CurveSegment baseCurve;

    @Override
    public GM_CurveSegment getBaseCurve() {
        return this.baseCurve;
    }

    protected void setBaseCurve(GM_CurveSegment value) {
        this.baseCurve = value;
    }

    @Override
    public int cardBaseCurve() {
        return (this.baseCurve == null) ? 0 : 1;
    }

    /**
   * Distance de self à la courbe de base. (NORME : cet attribut est de type
   * Length.)
   */
    protected double distance;

    @Override
    public double getDistance() {
        return this.distance;
    }

    @Override
    public DirectPositionList coord() {
        return null;
    }

    @Override
    public GM_CurveSegment reverse() {
        return null;
    }

    @Override
    public String getInterpolation() {
        return this.getBaseCurve().getInterpolation();
    }
}
