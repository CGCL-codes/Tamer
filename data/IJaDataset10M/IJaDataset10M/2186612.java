package org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation;

import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDikeRelocation;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.measures.MCUtils.MEASURE;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.geometry.GM_Curve;
import org.kalypsodeegree.model.geometry.GM_Surface;

/**
 * @author Dirk Kuch
 */
public class MeasureDikeRelocationHandler extends MeasureDikeConstructionHandler implements IMeasureDikeRelocation {

    public MeasureDikeRelocationHandler(final IProjectModel model, final Feature feature) {
        super(model, feature);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDykeRelocation#getOldDykeFootBoundary()
   */
    public GM_Surface<?> getOldDikeFootBoundary() {
        return (GM_Surface<?>) getProperty(IMeasureDikeRelocation.QN_GEOMETRY_OLD_DIKE_FOOT_BOUNDARY);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDykeRelocation#getOldDykeGeometry()
   */
    public GM_Curve getOldDikeGeometry() {
        return (GM_Curve) getProperty(IMeasureDikeRelocation.QN_GEOMETRY_OLD_DIKE);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasure#getType()
   */
    @Override
    public MEASURE getType() {
        return MEASURE.eAraDykeRelocation;
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDikeRelocation#getDikeVolume()
   */
    public Double getDikeVolume() {
        return (Double) getProperty(QN_DIKE_VOLUME);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDikeRelocation#getOldDikeFootWidth()
   */
    public Double getOldDikeFootWidth() {
        return (Double) getProperty(QN_OLD_DIKE_FOOT_WIDTH);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureDikeRelocation#getOldDikeVolume()
   */
    public Double getOldDikeVolume() {
        return (Double) getProperty(QN_OLD_DIKE_VOLUME);
    }
}
