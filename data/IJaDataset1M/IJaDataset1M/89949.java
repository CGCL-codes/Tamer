package org.kalypso.nofdpidss.core.base.gml.model.geodata;

import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.geometry.GM_Object;

/**
 * @author Dirk Kuch
 */
public interface IShapeFeature extends Feature {

    public GM_Object getGeometry();
}
