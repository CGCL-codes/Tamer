package org.gvsig.topology.topologyrules;

import java.awt.geom.Rectangle2D;
import org.gvsig.exceptions.BaseException;
import org.gvsig.fmap.core.NewFConverter;
import org.gvsig.topology.ITopologyErrorFix;
import org.gvsig.topology.Messages;
import org.gvsig.topology.Topology;
import org.gvsig.topology.TopologyError;
import org.gvsig.topology.TopologyRuleDefinitionException;
import org.gvsig.topology.errorfixes.DeleteTopologyErrorFix;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

/**
 * All features of origin layer must TOUCH 
 * (nor intersect or covers, touch) at least one element of destination layer.
 * 
 * @author Alvaro Zabala
 *
 */
public class LyrMustTouch extends AbstractSpatialPredicateTwoLyrRule {

    static final String RULE_NAME = Messages.getText("must_touch");

    static {
        DEFAULT_ERROR_SYMBOL.setDescription(RULE_NAME);
        automaticErrorFixes.add(new DeleteTopologyErrorFix());
    }

    public LyrMustTouch(Topology topology, FLyrVect originLyr, FLyrVect destinationLyr) {
        super(topology, originLyr, destinationLyr);
    }

    public LyrMustTouch() {
        super();
    }

    @Override
    protected boolean acceptsDestinationGeometryType(int shapeType) {
        return true;
    }

    @Override
    protected boolean acceptsOriginGeometryType(int shapeType) {
        return true;
    }

    @Override
    protected void checkWithNeighbourhood(IFeature feature, Rectangle2D extendedBounds, IFeatureIterator neighbourhood) throws BaseException {
        Geometry firstGeometry = NewFConverter.toJtsGeometry(feature.getGeometry());
        while (neighbourhood.hasNext()) {
            IFeature neighbourFeature = neighbourhood.next();
            IGeometry geom2 = neighbourFeature.getGeometry();
            if (acceptsDestinationGeometryType(geom2.getGeometryType())) {
                Rectangle2D rect2 = geom2.getBounds2D();
                if (extendedBounds.intersects(rect2)) {
                    Geometry jtsGeom2 = NewFConverter.toJtsGeometry(geom2);
                    if (checkSpatialPredicate(feature, firstGeometry, neighbourFeature, jtsGeom2)) {
                        return;
                    }
                }
            }
        }
        addTopologyError(createTopologyError(firstGeometry, feature, null));
    }

    @Override
    protected boolean checkSpatialPredicate(IFeature feature, Geometry firstGeometry, IFeature neighbourFeature, Geometry jtsGeom2) {
        return firstGeometry.touches(jtsGeom2);
    }

    @Override
    public void checkPreconditions() throws TopologyRuleDefinitionException {
    }

    @Override
    public String getName() {
        return RULE_NAME;
    }

    public boolean acceptsDestinationLyr(FLyrVect originLyr) {
        return true;
    }

    public boolean acceptsOriginLyr(FLyrVect originLyr) {
        return true;
    }

    public ITopologyErrorFix getDefaultFixFor(TopologyError topologyError) {
        return automaticErrorFixes.get(0);
    }
}
