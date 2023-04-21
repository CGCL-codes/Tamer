package org.kalypso.nofdpidss.geodata.raster;

import javax.xml.namespace.QName;
import org.apache.commons.lang.ArrayUtils;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategories;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataSetTypes;
import org.kalypsodeegree.model.feature.Feature;

public class ColorHelper {

    private static QName[] WATER = new QName[] { IGeodataSetTypes.QN_INUNDATION_AREA, IGeodataSetTypes.QN_INUNDATION_DEPTH, IGeodataSetTypes.QN_INUNDATION_DURATION, IGeodataSetTypes.QN_INUNDATION_FREQ };

    public static IColorDelegate getDelegate(final Feature gds, final int colors) {
        final QName qn = gds.getFeatureType().getQName();
        if (ArrayUtils.contains(WATER, qn)) return new WaterDepthColorDelegate(colors);
        final QName qparent = gds.getParent().getFeatureType().getQName();
        if (IGeodataCategories.QN_CATEGORY_DEM_TYPE.equals(qparent)) return new MountainRangeColorDelegate(colors);
        return new GroovyColorDelegate(colors);
    }
}
