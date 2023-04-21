package repast.simphony.dataLoader.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.junit.Before;
import org.junit.Test;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterType;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.AttributeFactory;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ContextFileReader;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.StickyBorders;
import repast.simphony.space.continuous.StrictBorders;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.projection.Projection;

/**
 * @author Nick Collier
 */
public class AutoBuilderTest {

    private ContextData sContext;

    private Context context;

    private Parameters params;

    private Object[][] gridResults = new Object[][] { { "grid3DB", 3, 10, 20, 30, repast.simphony.space.grid.BouncyBorders.class, false }, { "grid3DSt", 3, 30, 10, 20, repast.simphony.space.grid.StickyBorders.class, false }, { "grid3DStr", 3, 10, 70, 30, repast.simphony.space.grid.StrictBorders.class, true }, { "grid3DP", 3, 10, 20, 100, repast.simphony.space.grid.WrapAroundBorders.class, false }, { "grid2DP", 2, 10, 20, 0, repast.simphony.space.grid.WrapAroundBorders.class, true } };

    private Object[][] spaceResults = new Object[][] { { "space3DB", 3, 10, 20, 30, BouncyBorders.class }, { "space3DSt", 3, 30, 10, 20, StickyBorders.class }, { "space3DStr", 3, 10, 70, 30, StrictBorders.class }, { "space3DP", 3, 10, 20, 100, WrapAroundBorders.class }, { "space2DP", 2, 10, 20, 0, WrapAroundBorders.class } };

    private Object[][] netResults = new Object[][] { { "directedNet", true }, { "undirectedNet", false } };

    @Before
    public void init() throws IOException, ParameterFormatException, XMLStreamException {
        sContext = new ContextFileReader().read(new File("./test/repast/simphony/dataLoader/engine/context.xml"), new Classpath());
        context = new DefaultContext();
        ParametersCreator creator = new ParametersCreator();
        for (ProjectionData proj : sContext.projections()) {
            for (Attribute attribute : proj.attributes()) {
                ParameterType type = AttributeFactory.toParameterType(attribute);
                creator.addParameter(proj.getId() + attribute.getId(), attribute.getDisplayName(), type.getJavaClass(), type.getValue(attribute.getValue()), false);
            }
        }
        params = creator.createParameters();
        RunEnvironment.init(null, null, params, false);
    }

    @Test
    public void gridCreation() {
        ContextXMLBuilder builder = new ContextXMLBuilder(sContext);
        builder.build(context);
        Object obj1 = new Object();
        Object obj2 = new Object();
        context.add(obj1);
        context.add(obj2);
        for (int i = 0; i < gridResults.length; i++) {
            Object[] results = gridResults[i];
            Projection proj = context.getProjection((String) results[0]);
            assertTrue(proj instanceof Grid);
            Grid grid = (Grid) proj;
            GridDimensions dims = grid.getDimensions();
            assertEquals(results[1], dims.size());
            assertEquals(results[2], dims.getWidth());
            assertEquals(results[3], dims.getHeight());
            if (dims.size() > 2) assertEquals(results[4], dims.getDepth());
            assertEquals(results[5], grid.getGridPointTranslator().getClass());
            GridPoint location = grid.getLocation(obj1);
            assertNotNull(location);
            assertNotNull(grid.getLocation(obj2));
            assertEquals(results[6], grid.moveTo(obj2, location.toIntArray(null)));
        }
    }

    @Test
    public void ContinousSpaceCreation() {
        ContextXMLBuilder builder = new ContextXMLBuilder(sContext);
        builder.build(context);
        Object obj1 = new Object();
        Object obj2 = new Object();
        context.add(obj1);
        context.add(obj2);
        for (int i = 0; i < spaceResults.length; i++) {
            Object[] results = spaceResults[i];
            Projection proj = context.getProjection((String) results[0]);
            assertTrue(proj instanceof ContinuousSpace);
            ContinuousSpace cs = (ContinuousSpace) proj;
            Dimensions dims = cs.getDimensions();
            assertEquals(results[1], dims.size());
            assertEquals(results[2], (int) dims.getWidth());
            assertEquals(results[3], (int) dims.getHeight());
            if (dims.size() > 2) assertEquals(results[4], (int) dims.getDepth());
            assertEquals(results[5], cs.getPointTranslator().getClass());
            assertNotNull(cs.getLocation(obj1));
            assertNotNull(cs.getLocation(obj2));
        }
    }

    @Test
    public void netCreation() {
        ContextXMLBuilder builder = new ContextXMLBuilder(sContext);
        builder.build(context);
        Object obj1 = new Object();
        Object obj2 = new Object();
        context.add(obj1);
        context.add(obj2);
        for (int i = 0; i < netResults.length; i++) {
            Object[] results = netResults[i];
            Projection proj = context.getProjection((String) results[0]);
            assertTrue(proj instanceof Network);
            Network net = (Network) proj;
            assertEquals(results[1], net.isDirected());
            assertEquals(2, net.size());
        }
    }

    @Test
    public void geogCreation() {
        ContextXMLBuilder builder = new ContextXMLBuilder(sContext);
        builder.build(context);
        Geography geog = (Geography) context.getProjection("geography");
        assertTrue(geog != null);
    }
}
