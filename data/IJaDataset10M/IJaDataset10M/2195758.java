package gis2;

import kernel.WorldModelCreator;
import kernel.KernelException;
import rescuecore2.config.Config;
import rescuecore2.worldmodel.WorldModel;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.GeometryTools2D;
import rescuecore2.log.Logger;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.entities.Edge;
import maps.MapReader;
import maps.MapException;
import maps.gml.GMLMap;
import maps.gml.GMLDirectedEdge;
import maps.gml.GMLBuilding;
import maps.gml.GMLRoad;
import maps.gml.GMLShape;
import maps.gml.GMLCoordinates;
import maps.CoordinateConversion;
import maps.ScaleConversion;
import java.util.List;
import java.util.ArrayList;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import java.io.File;

/**
   A WorldModelCreator that reads a GML file and scenario descriptor.
*/
public class GMLWorldModelCreator implements WorldModelCreator {

    private static final String MAP_DIRECTORY_KEY = "gis.map.dir";

    private static final String MAP_FILE_KEY = "gis.map.file";

    private static final String DEFAULT_MAP_FILE = "map.gml";

    private static final String SCENARIO_FILE_KEY = "gis.map.scenario";

    private static final String DEFAULT_SCENARIO_FILE = "scenario.xml";

    private static final double SQ_MM_TO_SQ_M = 0.000001;

    private int nextID;

    @Override
    public String toString() {
        return "GML world model creator";
    }

    @Override
    public WorldModel<? extends Entity> buildWorldModel(Config config) throws KernelException {
        try {
            StandardWorldModel result = new StandardWorldModel();
            File dir = new File(config.getValue(MAP_DIRECTORY_KEY));
            File mapFile = new File(dir, config.getValue(MAP_FILE_KEY, DEFAULT_MAP_FILE));
            File scenarioFile = new File(dir, config.getValue(SCENARIO_FILE_KEY, DEFAULT_SCENARIO_FILE));
            readMapData(mapFile, result);
            readScenarioData(scenarioFile, result, config);
            for (Entity e : result) {
                nextID = Math.max(nextID, e.getID().getValue());
            }
            ++nextID;
            result.index();
            return result;
        } catch (MapException e) {
            throw new KernelException("Couldn't read GML file", e);
        } catch (DocumentException e) {
            throw new KernelException("Couldn't read scenario file", e);
        } catch (ScenarioException e) {
            throw new KernelException("Invalid scenario file", e);
        }
    }

    @Override
    public EntityID generateID() {
        return new EntityID(nextID++);
    }

    private void readMapData(File mapFile, StandardWorldModel result) throws MapException {
        GMLMap map = (GMLMap) MapReader.readMap(mapFile);
        CoordinateConversion conversion = getCoordinateConversion(map);
        Logger.debug("Creating entities");
        Logger.debug(map.getBuildings().size() + " buildings");
        Logger.debug(map.getRoads().size() + " roads");
        for (GMLBuilding next : map.getBuildings()) {
            EntityID id = new EntityID(next.getID());
            Building b = new Building(id);
            List<Point2D> vertices = convertShapeToPoints(next, conversion);
            double area = GeometryTools2D.computeArea(vertices) * SQ_MM_TO_SQ_M;
            Point2D centroid = GeometryTools2D.computeCentroid(vertices);
            b.setFloors(next.getFloors());
            b.setFieryness(0);
            b.setBrokenness(0);
            b.setBuildingCode(next.getCode());
            b.setBuildingAttributes(0);
            b.setGroundArea((int) Math.abs(area));
            b.setTotalArea(((int) Math.abs(area)) * b.getFloors());
            b.setImportance(next.getImportance());
            b.setEdges(createEdges(next, conversion));
            b.setX((int) centroid.getX());
            b.setY((int) centroid.getY());
            result.addEntity(b);
        }
        for (GMLRoad next : map.getRoads()) {
            EntityID id = new EntityID(next.getID());
            Road r = new Road(id);
            List<Point2D> vertices = convertShapeToPoints(next, conversion);
            Point2D centroid = GeometryTools2D.computeCentroid(vertices);
            r.setX((int) centroid.getX());
            r.setY((int) centroid.getY());
            r.setEdges(createEdges(next, conversion));
            result.addEntity(r);
        }
    }

    private void readScenarioData(File scenarioFile, StandardWorldModel result, Config config) throws DocumentException, ScenarioException {
        if (scenarioFile.exists()) {
            SAXReader reader = new SAXReader();
            Logger.debug("Reading scenario");
            Document doc = reader.read(scenarioFile);
            Scenario scenario = new Scenario(doc);
            Logger.debug("Applying scenario");
            scenario.apply(result, config);
        }
    }

    private List<Edge> createEdges(GMLShape s, CoordinateConversion conversion) {
        List<Edge> result = new ArrayList<Edge>();
        for (GMLDirectedEdge edge : s.getEdges()) {
            GMLCoordinates start = edge.getStartCoordinates();
            GMLCoordinates end = edge.getEndCoordinates();
            Integer neighbourID = s.getNeighbour(edge);
            EntityID id = neighbourID == null ? null : new EntityID(neighbourID);
            double sx = conversion.convertX(start.getX());
            double sy = conversion.convertY(start.getY());
            double ex = conversion.convertX(end.getX());
            double ey = conversion.convertY(end.getY());
            result.add(new Edge((int) sx, (int) sy, (int) ex, (int) ey, id));
        }
        return result;
    }

    private List<Point2D> convertShapeToPoints(GMLShape shape, CoordinateConversion conversion) {
        List<Point2D> points = new ArrayList<Point2D>();
        for (GMLCoordinates next : shape.getCoordinates()) {
            points.add(new Point2D(conversion.convertX(next.getX()), conversion.convertY(next.getY())));
        }
        return points;
    }

    private CoordinateConversion getCoordinateConversion(GMLMap map) {
        return new ScaleConversion(map.getMinX(), map.getMinY(), 1000, 1000);
    }
}
