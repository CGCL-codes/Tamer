package test.de.hpi.eworld.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import test.de.hpi.eworld.EWorldTest;
import test.de.hpi.eworld.util.FileSysUtils;
import de.hpi.eworld.core.ModelManager;
import de.hpi.eworld.core.PersistenceManager;
import de.hpi.eworld.model.db.data.EdgeModel;
import de.hpi.eworld.model.db.data.GlobalPosition;
import de.hpi.eworld.model.db.data.LaneModel;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.model.db.data.NodeModel;
import de.hpi.eworld.model.db.data.event.CircleLocationModel;
import de.hpi.eworld.model.db.data.event.EdgeLocationModel;
import de.hpi.eworld.model.db.data.event.EnvironmentEventModel;
import de.hpi.eworld.model.db.data.event.PolygonLocationModel;
import de.hpi.eworld.model.db.data.event.RoadEventModel;

public class TestModelEvent extends EWorldTest {

    private static String EWD_FILE = "./resources/model_test/modelEvent.ewd";

    /**
	 * Tests initialization (check attributes) and methods of 
	 * de.hpi.eworld.model.db.data.event.RoadEvent
	 */
    @Test
    public void testRoadEvent() {
        NodeModel fromNode = new NodeModel(2.3, 45, 6);
        NodeModel toNode = new NodeModel(45.22, 78.899);
        EdgeModel e = new EdgeModel("testModelID", fromNode, toNode);
        EdgeLocationModel el1 = new EdgeLocationModel(e, -12.3, -55.67);
        EdgeLocationModel el2 = new EdgeLocationModel(e, 12.3, 55.67);
        RoadEventModel re1 = new RoadEventModel(RoadEventModel.Type.Accident, el1);
        RoadEventModel re2 = new RoadEventModel(RoadEventModel.Type.Roadwork, el1);
        RoadEventModel re3 = new RoadEventModel(RoadEventModel.Type.Accident, el2);
        RoadEventModel re4 = new RoadEventModel(RoadEventModel.Type.Roadwork, el2);
        RoadEventModel re5 = new RoadEventModel(RoadEventModel.Type.Accident);
        RoadEventModel re6 = new RoadEventModel(RoadEventModel.Type.Roadwork);
        Assert.assertEquals(RoadEventModel.Type.Accident, re1.getEventType());
        Assert.assertEquals(RoadEventModel.Type.Accident, re3.getEventType());
        Assert.assertEquals(RoadEventModel.Type.Accident, re5.getEventType());
        Assert.assertEquals(RoadEventModel.Type.Roadwork, re2.getEventType());
        Assert.assertEquals(RoadEventModel.Type.Roadwork, re4.getEventType());
        Assert.assertEquals(RoadEventModel.Type.Roadwork, re6.getEventType());
        Assert.assertEquals(1.0, re1.getEventEffect());
        Assert.assertNotSame(0.5, re1.getEventEffect());
        Assert.assertEquals(0.5, re2.getEventEffect());
        Assert.assertNotSame(1.0, re2.getEventEffect());
        Assert.assertFalse(re1.isFullBlock());
        re1.setFullBlock(true);
        Assert.assertTrue(re1.isFullBlock());
        ArrayList<LaneModel> ll = new ArrayList<LaneModel>();
        LaneModel l1 = new LaneModel(50, 70);
        ll.add(l1);
        LaneModel l2 = new LaneModel(-23, -75);
        ll.add(l2);
        re1.setBlockedLanes(ll);
        Assert.assertEquals(2, re1.getBlockedLanes().size());
        Assert.assertEquals(l1, re1.getBlockedLanes().get(0));
        Assert.assertEquals(l2, re1.getBlockedLanes().get(1));
    }

    /**
	 * Tests initialization (check attributes) and methods of 
	 * de.hpi.eworld.model.db.data.event.RoadEvent
	 */
    @Test
    public void testEnvironmentEvent() {
        CircleLocationModel cl = new CircleLocationModel();
        Point2D positionOnScreen = new Point2D.Double(3, 4);
        GlobalPosition centerPosition = GlobalPosition.from(positionOnScreen);
        cl.setCenter(centerPosition);
        Point2D circlePoint = new Point2D.Double(0, -25);
        GlobalPosition circlePosition = GlobalPosition.from(circlePoint);
        cl.setCirclePoint(circlePosition);
        cl.setRadius(centerPosition.distanceTo(circlePosition));
        EnvironmentEventModel ee1 = new EnvironmentEventModel(EnvironmentEventModel.Type.CO2, 12, cl);
        PolygonLocationModel pl = new PolygonLocationModel();
        List<GlobalPosition> positions = new ArrayList<GlobalPosition>();
        Point2D p1 = new Point2D.Double(3, 4);
        GlobalPosition gp1 = GlobalPosition.from(p1);
        positions.add(gp1);
        Point2D p2 = new Point2D.Double(13, 14);
        GlobalPosition gp2 = GlobalPosition.from(p2);
        positions.add(gp2);
        Point2D p3 = new Point2D.Double(22, 23);
        GlobalPosition gp3 = GlobalPosition.from(p3);
        positions.add(gp3);
        Point2D p4 = new Point2D.Double(12, 9);
        GlobalPosition gp4 = GlobalPosition.from(p4);
        positions.add(gp4);
        Point2D p5 = new Point2D.Double(3, 34);
        GlobalPosition gp5 = GlobalPosition.from(p5);
        positions.add(gp5);
        pl.setPoints(positions);
        EnvironmentEventModel ee2 = new EnvironmentEventModel(EnvironmentEventModel.Type.Fog, -23, pl);
        NodeModel fromNode = new NodeModel(2.3, 45, 6);
        NodeModel toNode = new NodeModel(45.22, 78.899);
        EdgeModel e = new EdgeModel("testModelID", fromNode, toNode);
        EdgeLocationModel el = new EdgeLocationModel(e, -12.3, -55.67);
        EnvironmentEventModel ee3 = new EnvironmentEventModel(EnvironmentEventModel.Type.Snow, 0, el);
        Assert.assertEquals(12, ee1.getStrength());
        Assert.assertEquals(-23, ee2.getStrength());
        Assert.assertEquals(0, ee3.getStrength());
        Assert.assertEquals(EnvironmentEventModel.Type.CO2, ee1.getEventType());
        Assert.assertEquals(EnvironmentEventModel.Type.Fog, ee2.getEventType());
        Assert.assertEquals(EnvironmentEventModel.Type.Snow, ee3.getEventType());
        ee1.setEventType(EnvironmentEventModel.Type.Ice);
        Assert.assertEquals(EnvironmentEventModel.Type.Ice, ee1.getEventType());
        Assert.assertEquals(0.5, ee1.getEventEffect());
        Assert.assertEquals(0.7, ee2.getEventEffect());
        Assert.assertEquals(0.8, ee3.getEventEffect());
    }

    /**
	 * Saves each model element to an ewd file via ModelManager.saveToFile and Loads it afterwards to
	 * check if all attributes are restored correctly 
	 */
    @Test
    public void testSaveAndRestore() {
        NodeModel fromNode = new NodeModel(2.3, 45, 6);
        NodeModel toNode = new NodeModel(45.22, 78.899);
        EdgeModel e = new EdgeModel("testModelID", fromNode, toNode);
        EdgeLocationModel el1 = new EdgeLocationModel(e, -12.3, -55.67);
        EdgeLocationModel el2 = new EdgeLocationModel(e, 12.3, 55.67);
        RoadEventModel re1 = new RoadEventModel(RoadEventModel.Type.Accident, el1);
        RoadEventModel re2 = new RoadEventModel(RoadEventModel.Type.Roadwork, el1);
        RoadEventModel re3 = new RoadEventModel(RoadEventModel.Type.Accident, el2);
        RoadEventModel re4 = new RoadEventModel(RoadEventModel.Type.Roadwork, el2);
        RoadEventModel re5 = new RoadEventModel(RoadEventModel.Type.Accident);
        RoadEventModel re6 = new RoadEventModel(RoadEventModel.Type.Roadwork);
        CircleLocationModel cl = new CircleLocationModel();
        Point2D positionOnScreen = new Point2D.Double(3, 4);
        GlobalPosition centerPosition = GlobalPosition.from(positionOnScreen);
        cl.setCenter(centerPosition);
        Point2D circlePoint = new Point2D.Double(0, -25);
        GlobalPosition circlePosition = GlobalPosition.from(circlePoint);
        cl.setCirclePoint(circlePosition);
        cl.setRadius(centerPosition.distanceTo(circlePosition));
        EnvironmentEventModel ee1 = new EnvironmentEventModel(EnvironmentEventModel.Type.CO2, 12, cl);
        PolygonLocationModel pl = new PolygonLocationModel();
        List<GlobalPosition> positions = new ArrayList<GlobalPosition>();
        Point2D p1 = new Point2D.Double(3, 4);
        GlobalPosition gp1 = GlobalPosition.from(p1);
        positions.add(gp1);
        Point2D p2 = new Point2D.Double(13, 14);
        GlobalPosition gp2 = GlobalPosition.from(p2);
        positions.add(gp2);
        Point2D p3 = new Point2D.Double(22, 23);
        GlobalPosition gp3 = GlobalPosition.from(p3);
        positions.add(gp3);
        Point2D p4 = new Point2D.Double(12, 9);
        GlobalPosition gp4 = GlobalPosition.from(p4);
        positions.add(gp4);
        Point2D p5 = new Point2D.Double(3, 34);
        GlobalPosition gp5 = GlobalPosition.from(p5);
        positions.add(gp5);
        pl.setPoints(positions);
        EnvironmentEventModel ee2 = new EnvironmentEventModel(EnvironmentEventModel.Type.Fog, -23, pl);
        NodeModel fromNode2 = new NodeModel(2.3, 45, 6);
        NodeModel toNode2 = new NodeModel(45.22, 78.899);
        EdgeModel e2 = new EdgeModel("testModelID", fromNode2, toNode2);
        EdgeLocationModel el = new EdgeLocationModel(e2, -12.3, -55.67);
        EnvironmentEventModel ee3 = new EnvironmentEventModel(EnvironmentEventModel.Type.Snow, 0, el);
        ModelManager mm = ModelManager.getInstance();
        mm.clearModel();
        mm.addModelElement(re1);
        mm.addModelElement(re2);
        mm.addModelElement(re3);
        mm.addModelElement(re4);
        mm.addModelElement(re5);
        mm.addModelElement(re6);
        mm.addModelElement(ee1);
        mm.addModelElement(ee2);
        mm.addModelElement(ee3);
        PersistenceManager.getInstance().saveToFile(EWD_FILE);
        mm.clearModel();
        PersistenceManager.getInstance().loadFromFile(EWD_FILE);
        Collection<ModelElement> modelElements = mm.getAllModelElements();
        Assert.assertEquals(9, modelElements.size());
        Assert.assertTrue(FileSysUtils.deleteFile(EWD_FILE));
    }
}
