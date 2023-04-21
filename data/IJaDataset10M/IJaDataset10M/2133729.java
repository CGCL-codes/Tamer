package org.kalypso.nofdpidss.ui.application.google.earth.handlers;

import org.kalypso.kml.export.interfaces.IPlacemark;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation.MeasureBottomLevelChangeHandler;
import org.kalypso.transformation.GeoTransformer;
import org.kalypsodeegree.model.geometry.GM_Point;

/**
 * @author Dirk Kuch
 */
public class PlacemarkHandlerBottomLevelChange implements IPlacemark {

    private final MeasureBottomLevelChangeHandler m_measure;

    public PlacemarkHandlerBottomLevelChange(final MeasureBottomLevelChangeHandler measure) {
        m_measure = measure;
    }

    /**
   * @see org.kalypso.google.earth.export.interfaces.IPlacemark#getDescription()
   */
    public String getDescription() {
        return "<![CDATA[<div><img src=\"http://nofdp.bafg.de/servlet/is/7543/nofdp.gif\"></div>]]>";
    }

    /**
   * @see org.kalypso.google.earth.export.interfaces.IPlacemark#getName()
   */
    public String getName() {
        return m_measure.getName();
    }

    /**
   * @see org.kalypso.google.earth.export.interfaces.IPlacemark#getX(java.lang.String)
   */
    public String getX(final String targetCRS) throws Exception {
        final GM_Point point = m_measure.getLowerSnapPoint();
        final GeoTransformer transformer = new GeoTransformer(targetCRS);
        final GM_Point transform = (GM_Point) transformer.transform(point);
        return Double.valueOf(transform.getX()).toString();
    }

    /**
   * @see org.kalypso.google.earth.export.interfaces.IPlacemark#getY(java.lang.String)
   */
    public String getY(final String targetCRS) throws Exception {
        final GM_Point point = m_measure.getLowerSnapPoint();
        final GeoTransformer transformer = new GeoTransformer(targetCRS);
        final GM_Point transform = (GM_Point) transformer.transform(point);
        return Double.valueOf(transform.getY()).toString();
    }
}
