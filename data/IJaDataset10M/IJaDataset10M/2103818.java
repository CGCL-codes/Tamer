package maps.gml.formats;

import maps.gml.GMLMap;
import maps.gml.GMLCoordinates;
import maps.gml.GMLShape;
import maps.gml.GMLBuilding;
import maps.gml.GMLRoad;
import maps.gml.GMLNode;
import maps.gml.GMLEdge;
import maps.gml.GMLDirectedEdge;
import maps.gml.GMLTools;
import maps.gml.GMLMapFormat;
import maps.ConstantConversion;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Attribute;
import org.dom4j.QName;
import org.dom4j.Namespace;
import org.dom4j.XPath;
import org.dom4j.DocumentHelper;
import org.jaxen.SimpleVariableContext;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import rescuecore2.misc.Pair;
import rescuecore2.log.Logger;

/**
   A MapFormat that can handle GML maps from Meijo University.
 */
public final class MeijoFormat extends GMLMapFormat {

    /** Singleton instance. */
    public static final MeijoFormat INSTANCE = new MeijoFormat();

    private static final String MEIJO_NAMESPACE_URI = "http://sakura.meijo-u.ac.jp/rcrs";

    private static final String GML_APP_NAMESPACE_URI = "http://www.opengis.net/app";

    private static final Namespace RCRS_NAMESPACE = DocumentHelper.createNamespace("rcrs", MEIJO_NAMESPACE_URI);

    private static final Namespace GML_APP_NAMESPACE = DocumentHelper.createNamespace("app", GML_APP_NAMESPACE_URI);

    private static final QName ROOT_QNAME = DocumentHelper.createQName("Topology", GML_APP_NAMESPACE);

    private static final QName VERSION_QNAME = DocumentHelper.createQName("Version", RCRS_NAMESPACE);

    private static final QName DESCRIPTION_QNAME = DocumentHelper.createQName("Description", RCRS_NAMESPACE);

    private static final QName AREA_QNAME = DocumentHelper.createQName("Area", RCRS_NAMESPACE);

    private static final QName NODE_LIST_QNAME = DocumentHelper.createQName("NodeList", RCRS_NAMESPACE);

    private static final QName EDGE_LIST_QNAME = DocumentHelper.createQName("EdgeList", RCRS_NAMESPACE);

    private static final QName FACE_LIST_QNAME = DocumentHelper.createQName("FaceList", RCRS_NAMESPACE);

    private static final QName FACE_QNAME = DocumentHelper.createQName("Face", RCRS_NAMESPACE);

    private static final QName BUILDING_PROPERTY_QNAME = DocumentHelper.createQName("BuildingProperty", RCRS_NAMESPACE);

    private static final Map<String, String> URIS = new HashMap<String, String>();

    private static final XPath NODE_XPATH = DocumentHelper.createXPath("//app:Topology/rcrs:Area/rcrs:NodeList/gml:Node");

    private static final XPath EDGE_XPATH = DocumentHelper.createXPath("//app:Topology/rcrs:Area/rcrs:EdgeList/gml:Edge");

    private static final XPath FACE_XPATH = DocumentHelper.createXPath("//app:Topology/rcrs:Area/rcrs:FaceList/rcrs:Face");

    private static final XPath NODE_COORDINATES_XPATH = DocumentHelper.createXPath("gml:pointProperty/gml:Point/gml:coordinates");

    private static final XPath EDGE_COORDINATES_XPATH = DocumentHelper.createXPath("gml:centerLineOf/gml:LineString/gml:coordinates");

    private static final XPath FACE_COORDINATES_XPATH = DocumentHelper.createXPath("gml:polygon/gml:LinearRing/gml:coordinates");

    private static final XPath EDGE_START_XPATH = DocumentHelper.createXPath("gml:directedNode[1]//@xlink:href");

    private static final XPath EDGE_END_XPATH = DocumentHelper.createXPath("gml:directedNode[2]/@xlink:href");

    private static final SimpleVariableContext FACE_NEIGHBOUR_XPATH_CONTEXT = new SimpleVariableContext();

    private static final String FACE_NEIGHBOUR_XPATH_STRING = "//rcrs:EdgeList/gml:Edge[@gml:id=\"$edgeid\"]/gml:directedFace[@xlink:href!=\"#$faceid\"]/@xlink:href";

    private static final double THRESHOLD = 0.0001;

    static {
        URIS.put("gml", Common.GML_NAMESPACE_URI);
        URIS.put("app", GML_APP_NAMESPACE_URI);
        URIS.put("xlink", Common.XLINK_NAMESPACE_URI);
        URIS.put("rcrs", MEIJO_NAMESPACE_URI);
        NODE_XPATH.setNamespaceURIs(URIS);
        EDGE_XPATH.setNamespaceURIs(URIS);
        FACE_XPATH.setNamespaceURIs(URIS);
        NODE_COORDINATES_XPATH.setNamespaceURIs(URIS);
        EDGE_COORDINATES_XPATH.setNamespaceURIs(URIS);
        FACE_COORDINATES_XPATH.setNamespaceURIs(URIS);
        EDGE_START_XPATH.setNamespaceURIs(URIS);
        EDGE_END_XPATH.setNamespaceURIs(URIS);
    }

    private MeijoFormat() {
    }

    @Override
    public Map<String, String> getNamespaces() {
        return Collections.unmodifiableMap(URIS);
    }

    @Override
    public String toString() {
        return "Meijo";
    }

    @Override
    public boolean isCorrectRootElement(String uri, String localName) {
        return MEIJO_NAMESPACE_URI.equals(uri) && "Topology".equals(localName);
    }

    @Override
    public GMLMap read(Document doc) {
        GMLMap result = new GMLMap();
        readNodes(doc, result);
        result.convertCoordinates(new ConstantConversion(0.001));
        readEdges(doc, result);
        readFaces(doc, result);
        splitMultipleEdges(result);
        return result;
    }

    @Override
    public Document write(GMLMap map) {
        Element root = DocumentHelper.createElement(ROOT_QNAME);
        Document result = DocumentHelper.createDocument(root);
        writeNodes(map, root.addElement(NODE_LIST_QNAME));
        writeEdges(map, root.addElement(EDGE_LIST_QNAME));
        writeFaces(map, root.addElement(FACE_LIST_QNAME));
        return result;
    }

    private void writeNodes(GMLMap map, Element parent) {
        for (GMLNode next : map.getNodes()) {
            Element e = parent.addElement(Common.GML_NODE_QNAME);
            e.addAttribute(Common.GML_ID_QNAME, String.valueOf(next.getID()));
            e.addElement(Common.GML_POINT_PROPERTY_QNAME).addElement(Common.GML_POINT_QNAME).addElement(Common.GML_COORDINATES_QNAME).setText(next.getCoordinates().toString());
        }
    }

    private void writeEdges(GMLMap map, Element parent) {
        for (GMLEdge next : map.getEdges()) {
            Element e = parent.addElement(Common.GML_EDGE_QNAME);
            e.addAttribute(Common.GML_ID_QNAME, String.valueOf(next.getID()));
            e.addElement(Common.GML_DIRECTED_NODE_QNAME).addAttribute(Common.GML_ORIENTATION_QNAME, "+").addAttribute(Common.XLINK_HREF_QNAME, "#" + next.getStart().getID());
            e.addElement(Common.GML_DIRECTED_NODE_QNAME).addAttribute(Common.GML_ORIENTATION_QNAME, "+").addAttribute(Common.XLINK_HREF_QNAME, "#" + next.getEnd().getID());
            for (GMLShape shape : map.getAllShapes()) {
                for (GMLDirectedEdge edge : shape.getEdges()) {
                    if (edge.getEdge() == next) {
                        e.addElement(Common.GML_DIRECTED_FACE_QNAME).addAttribute(Common.GML_ORIENTATION_QNAME, "+").addAttribute(Common.XLINK_HREF_QNAME, "#" + shape.getID());
                    }
                }
            }
        }
    }

    private void writeFaces(GMLMap map, Element parent) {
        for (GMLShape next : map.getAllShapes()) {
            Element e = parent.addElement(FACE_QNAME);
            if (next instanceof GMLBuilding) {
                parent.addElement(BUILDING_PROPERTY_QNAME);
            }
            e = e.addElement(Common.GML_FACE_QNAME);
            e.addAttribute(Common.GML_ID_QNAME, String.valueOf(next.getID()));
            for (GMLDirectedEdge edge : next.getEdges()) {
                String orientation = "-";
                if (edge.getEdge().isPassable()) {
                    orientation = "+";
                }
                e.addElement(Common.GML_DIRECTED_EDGE_QNAME).addAttribute(Common.GML_ORIENTATION_QNAME, orientation).addAttribute(Common.XLINK_HREF_QNAME, "#" + edge.getEdge().getID());
            }
            e.addElement(Common.GML_POLYGON_QNAME).addElement(Common.GML_LINEAR_RING_QNAME).addElement(Common.GML_COORDINATES_QNAME).setText(GMLTools.getCoordinatesString(next.getCoordinates()));
        }
    }

    private void readNodes(Document doc, GMLMap result) {
        for (Object next : NODE_XPATH.selectNodes(doc)) {
            Element e = (Element) next;
            int id = readID(e);
            String coordinates = ((Element) NODE_COORDINATES_XPATH.evaluate(e)).getText();
            GMLCoordinates c = new GMLCoordinates(coordinates);
            GMLNode node = new GMLNode(id, c);
            result.addNode(node);
            Logger.debug("Read node " + node);
        }
    }

    private void readEdges(Document doc, GMLMap result) {
        for (Object next : EDGE_XPATH.selectNodes(doc)) {
            Element e = (Element) next;
            int id = readID(e);
            int startID = Integer.parseInt(((Attribute) EDGE_START_XPATH.evaluate(e)).getValue().substring(1));
            int endID = Integer.parseInt(((Attribute) EDGE_END_XPATH.evaluate(e)).getValue().substring(1));
            GMLEdge edge = new GMLEdge(id, result.getNode(startID), result.getNode(endID), false);
            edge.setPoints(GMLTools.getCoordinatesList(((Element) EDGE_COORDINATES_XPATH.evaluate(e)).getText()));
            result.addEdge(edge);
            Logger.debug("Read edge " + edge);
        }
    }

    private void readFaces(Document doc, GMLMap result) {
        for (Object next : FACE_XPATH.selectNodes(doc)) {
            Element e = (Element) next;
            String type = e.attributeValue("type");
            Element gmlFace = e.element(Common.GML_FACE_QNAME);
            int id = readID(gmlFace);
            Pair<List<GMLDirectedEdge>, List<Integer>> edges = readEdges(gmlFace, result, id);
            GMLShape shape = null;
            if ("building".equals(type)) {
                shape = new GMLBuilding(id, edges.first(), edges.second());
            } else {
                shape = new GMLRoad(id, edges.first(), edges.second());
            }
            String coordsString = ((Element) FACE_COORDINATES_XPATH.evaluate(gmlFace)).getText();
            List<GMLCoordinates> coords = GMLTools.getCoordinatesList(coordsString);
            shape.setCoordinates(coords);
            result.add(shape);
            Logger.debug("Read shape: " + shape);
        }
    }

    private Pair<List<GMLDirectedEdge>, List<Integer>> readEdges(Element e, GMLMap map, int faceID) {
        List<GMLDirectedEdge> edges = new ArrayList<GMLDirectedEdge>();
        List<Integer> neighbours = new ArrayList<Integer>();
        for (Object next : e.elements(Common.GML_DIRECTED_EDGE_QNAME)) {
            Element dEdge = (Element) next;
            boolean passable = "+".equals(dEdge.attributeValue(Common.GML_ORIENTATION_QNAME));
            int edgeID = Integer.parseInt(dEdge.attributeValue(Common.XLINK_HREF_QNAME).substring(1));
            edges.add(new GMLDirectedEdge(map.getEdge(edgeID), true));
            XPath xpath = makeFaceNeighbourXPath(edgeID, faceID);
            Object o = xpath.evaluate(e);
            if (o == null) {
                neighbours.add(null);
            } else if (o instanceof Collection && ((Collection) o).isEmpty()) {
                neighbours.add(null);
            } else {
                int neighbourID = Integer.parseInt(((Attribute) o).getValue().substring(1));
                neighbours.add(neighbourID);
            }
        }
        return new Pair<List<GMLDirectedEdge>, List<Integer>>(edges, neighbours);
    }

    private int readID(Element e) {
        return Integer.parseInt(e.attributeValue(Common.GML_ID_QNAME));
    }

    private XPath makeFaceNeighbourXPath(int edgeID, int faceID) {
        String path = FACE_NEIGHBOUR_XPATH_STRING.replace("$edgeid", String.valueOf(edgeID)).replace("$faceid", String.valueOf(faceID));
        XPath result = DocumentHelper.createXPath(path);
        result.setNamespaceURIs(URIS);
        return result;
    }

    private void splitMultipleEdges(GMLMap map) {
        for (GMLEdge edge : map.getEdges()) {
            if (edge.getPoints().size() != 2) {
                Iterator<GMLCoordinates> it = edge.getPoints().iterator();
                GMLCoordinates first = it.next();
                List<GMLEdge> newEdges = new ArrayList<GMLEdge>();
                while (it.hasNext()) {
                    GMLCoordinates second = it.next();
                    GMLNode n1 = map.createNode(first);
                    GMLNode n2 = map.createNode(second);
                    GMLEdge newEdge = map.createEdge(n1, n2);
                    newEdges.add(newEdge);
                    first = second;
                }
                for (GMLShape shape : map.getAllShapes()) {
                    replaceEdge(shape, edge, newEdges);
                }
                map.removeEdge(edge);
            }
        }
    }

    private void replaceEdge(GMLShape shape, GMLEdge oldEdge, List<GMLEdge> newEdges) {
        List<GMLDirectedEdge> newShapeEdges = new ArrayList<GMLDirectedEdge>();
        List<Integer> newShapeNeighbours = new ArrayList<Integer>();
        boolean found = false;
        for (GMLDirectedEdge e : shape.getEdges()) {
            if (e.getEdge().equals(oldEdge)) {
                found = true;
                GMLNode start = e.getStartNode();
                Integer neighbour = shape.getNeighbour(e);
                for (GMLEdge next : newEdges) {
                    GMLDirectedEdge newDEdge = new GMLDirectedEdge(next, start);
                    newShapeEdges.add(newDEdge);
                    newShapeNeighbours.add(neighbour);
                    start = newDEdge.getEndNode();
                }
            } else {
                newShapeEdges.add(e);
                newShapeNeighbours.add(shape.getNeighbour(e));
            }
        }
        if (found) {
            shape.setEdges(newShapeEdges);
            Iterator<GMLDirectedEdge> it = newShapeEdges.iterator();
            Iterator<Integer> ix = newShapeNeighbours.iterator();
            while (it.hasNext() && ix.hasNext()) {
                shape.setNeighbour(it.next(), ix.next());
            }
        }
    }

    private boolean closeEnough(GMLNode n1, GMLNode n2) {
        if (n1 == n2) {
            return true;
        }
        double dx = n1.getX() - n2.getX();
        double dy = n1.getY() - n2.getY();
        return (dx > -THRESHOLD && dx < THRESHOLD && dy > -THRESHOLD && dy < THRESHOLD);
    }
}
