package org.gvsig.topology.errorfixes;

import java.util.ArrayList;
import java.util.List;
import org.gvsig.exceptions.BaseException;
import org.gvsig.fmap.core.FeatureUtil;
import org.gvsig.fmap.core.NewFConverter;
import org.gvsig.jts.JtsUtil;
import org.gvsig.topology.Messages;
import org.gvsig.topology.TopologyError;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * Automatic fix to 'Polygon must not overlaps' topology error.
 * 
 * It removes from the two overlappings polygons their common area, leaving
 * a gap or void.
 * 
 * @author Alvaro Zabala
 *
 */
public class SubstractOverlapPolygonFix extends AbstractTopologyErrorFix {

    public List<IFeature>[] fixAlgorithm(TopologyError error) throws BaseException {
        Geometry errorJts = NewFConverter.toJtsGeometry(error.getGeometry());
        IFeature firstFeature = error.getFeature1();
        Geometry firstJts = NewFConverter.toJtsGeometry(firstFeature.getGeometry());
        IFeature secondFeature = error.getFeature2();
        Geometry secondJts = NewFConverter.toJtsGeometry(secondFeature.getGeometry());
        List<IFeature> editedFeatures = new ArrayList<IFeature>();
        if (secondJts.covers(firstJts)) {
            secondFeature = FeatureUtil.removeOverlappingArea(secondFeature, secondJts, errorJts);
            editedFeatures.add(secondFeature);
        } else if (secondJts.coveredBy(firstJts)) {
            firstFeature = FeatureUtil.removeOverlappingArea(firstFeature, firstJts, errorJts);
            editedFeatures.add(firstFeature);
        } else {
            firstFeature = FeatureUtil.removeOverlappingArea(firstFeature, firstJts, errorJts);
            secondFeature = FeatureUtil.removeOverlappingArea(secondFeature, secondJts, errorJts);
            editedFeatures.add(secondFeature);
            editedFeatures.add(firstFeature);
        }
        return (List<IFeature>[]) new List[] { editedFeatures };
    }

    public String getEditionDescription() {
        return Messages.getText("SUBSTRACT_OVERLAP_AREA_FIX");
    }
}
