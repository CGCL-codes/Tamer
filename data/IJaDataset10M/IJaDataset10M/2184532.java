package org.gvsig.topology.topologyrules;

import org.gvsig.topology.TopologyRuleDefinitionException;
import org.gvsig.topology.util.LayerFactory;
import org.gvsig.topology.util.TestTopologyErrorContainer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.io.ParseException;
import junit.framework.TestCase;

public class FMapGeometryMustBeClosedTest extends TestCase {

    public void testUnclosedLines() throws ParseException, TopologyRuleDefinitionException {
        FLyrVect lyr = LayerFactory.createLyrForIGeometryMustBeClosedTest();
        FMapGeometryMustBeClosed rule = new FMapGeometryMustBeClosed(null, lyr, 0.1d);
        TestTopologyErrorContainer errorContainer = new TestTopologyErrorContainer();
        rule.setTopologyErrorContainer(errorContainer);
        rule.checkPreconditions();
        rule.checkRule();
        int numberOfErrors = errorContainer.getNumberOfErrors();
        assertTrue(numberOfErrors == 3);
    }
}
