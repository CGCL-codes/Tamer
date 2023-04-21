package org.gvsig.topology.topologyrules;

import junit.framework.TestCase;
import org.gvsig.jts.GeometryCollapsedException;
import org.gvsig.jts.GeometrySnapper;
import org.gvsig.jts.JtsUtil;
import org.gvsig.topology.TopologyRuleDefinitionException;
import org.gvsig.topology.util.LayerFactory;
import org.gvsig.topology.util.TestTopologyErrorContainer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class MustBeGreaterThanClusterToleranceTest extends TestCase {

    public void testLineLayerWithCollapsedCoords() throws ParseException {
        FLyrVect lyr = LayerFactory.getLineLayerWithCollapsedCoords();
        MustBeLargerThanClusterTolerance rule = new MustBeLargerThanClusterTolerance(lyr, 11d);
        TestTopologyErrorContainer errorContainer = new TestTopologyErrorContainer();
        rule.setTopologyErrorContainer(errorContainer);
        try {
            rule.checkPreconditions();
            rule.checkRule();
            assertTrue(errorContainer.getNumberOfErrors() == 1);
        } catch (TopologyRuleDefinitionException e) {
            e.printStackTrace();
        }
        GeometrySnapper snapper = new GeometrySnapper(11d);
        WKTReader reader = new WKTReader(JtsUtil.GEOMETRY_FACTORY);
        Geometry jtsGeo = reader.read("POLYGON((10 10, 15 10, 15 15, 10 15, 10 10))");
        try {
            Geometry newGeo = snapper.snap(jtsGeo);
        } catch (GeometryCollapsedException e) {
            e.printStackTrace();
        }
        snapper = new GeometrySnapper(4.5d);
        try {
            snapper.snap(jtsGeo);
        } catch (GeometryCollapsedException e) {
            e.printStackTrace();
        }
    }
}
