package org.fpdev.apps.rtemaster;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BNode;
import org.fpdev.core.basenet.Path;
import org.fpdev.core.transit.SubRoute;
import org.fpdev.core.transit.TransitPath;
import org.fpdev.apps.rtemaster.gui.dialogs.ImportRouteShpAttrDialog;
import org.fpdev.apps.rtemaster.gui.map.HighlightedPoint;
import org.fpdev.apps.rtemaster.gui.map.LinkHoverListener;
import org.fpdev.apps.rtemaster.gui.map.MapDrawItems;
import org.fpdev.apps.rtemaster.gui.map.NodeHoverListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.jgrapht.graph.SimpleGraph;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeType;

/**
 *
 * @author demory
 */
public class RouteShpImporter {

    private RouteMaster av_;

    private Map<String, SimpleFeature> featureLookup_;

    private String resolvedFeatID_;

    private LinkSet linkSet_;

    private BNode startNode_, endNode_;

    private Path mainPath_, lastHighlightedPath_;

    private Set<Path> potentialPaths_;

    private SimpleGraph<BNode, BLink> graph_;

    /** Creates a new instance of RouteShpImporter */
    public RouteShpImporter(RouteMaster av) {
        av_ = av;
        potentialPaths_ = new HashSet<Path>();
        startNode_ = endNode_ = null;
        mainPath_ = new Path(av_.getEngine());
        lastHighlightedPath_ = null;
    }

    public void importRoutesFromShapefile() {
        clearCurrent();
        List<BLink> processedLinks = new LinkedList<BLink>();
        System.out.println("foo");
        try {
            URL shapeURL = null;
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                shapeURL = f.toURI().toURL();
            }
            if (shapeURL == null) {
                shapeURL = new File("c:\\demory\\data\\gis\\marta\\busptn_apr_07.shp").toURI().toURL();
            }
            ShapefileDataStore store = new ShapefileDataStore(shapeURL);
            String name = store.getTypeNames()[0];
            FeatureSource source = store.getFeatureSource(name);
            FeatureCollection fColl = source.getFeatures();
            System.out.println("Feature collection type: " + fColl.getSchema().getName());
            System.out.println("Num features: " + fColl.size());
            List<AttributeType> attrs = store.getSchema().getTypes();
            ImportRouteShpAttrDialog selectAttrsDialog = new ImportRouteShpAttrDialog(av_, attrs);
            if (!selectAttrsDialog.okClicked()) {
                return;
            }
            System.out.println("route " + selectAttrsDialog.getSelectedRouteIndex());
            System.out.println("direction " + selectAttrsDialog.getSelectedDirectionIndex());
            System.out.println("pattern " + selectAttrsDialog.getSelectedPatternIndex());
            featureLookup_ = new HashMap<String, SimpleFeature>();
            FeatureIterator fIter = fColl.features();
            String[][] data = new String[fColl.size()][3];
            int r = 0;
            while (fIter.hasNext()) {
                SimpleFeature f = (SimpleFeature) fIter.next();
                data[r][0] = (String) f.getAttribute(selectAttrsDialog.getSelectedRouteIndex());
                data[r][1] = (String) f.getAttribute(selectAttrsDialog.getSelectedDirectionIndex());
                data[r][2] = (String) f.getAttribute(selectAttrsDialog.getSelectedPatternIndex());
                featureLookup_.put(data[r][0] + "_" + data[r][1] + "_" + data[r][2], f);
                r++;
            }
            av_.getGUI().getImportRouteShpFrame().setData(data);
            av_.fireEvent(new RMEvent(EventTypes.RTEMENU_SHOW_IMPORT_SHP_FRAME));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showImportRouteShpItem(String id) {
        clearCurrent();
        SimpleFeature f = featureLookup_.get(id);
        if (f == null) {
            av_.msg("Invalid routeShape item: " + id);
            return;
        }
        System.out.println("id = " + id);
        ImportGraph ig = new ImportGraph();
        ig.addLink(new ImportGraph.Link(((Geometry) f.getDefaultGeometry()).getCoordinates(), f));
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_PREVIEW, ig, "r");
        av_.getGUI().getMapPanel().zoomRange(ig.getBoundingBox());
        av_.getGUI().getMapPanel().zoomOut(.025);
        av_.getGUI().getMapPanel().refresh(false, true, true);
    }

    public void resolveImportRouteShpItem(String id, double radius) {
        clearCurrent();
        SimpleFeature f = featureLookup_.get(id);
        if (f == null) {
            av_.msg("Invalid routeShape item: " + id);
            return;
        }
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_PREVIEW);
        av_.getGUI().getMapPanel().refresh(false, true, true);
        Geometry buffer = (Geometry) f.getDefaultGeometry();
        av_.getGUI().statusText("Buffering feature...");
        while (radius > 0) {
            double r = radius >= 50 ? 50 : radius;
            buffer = buffer.buffer(r);
            radius -= 50;
        }
        av_.getGUI().statusText("Resolving links against buffer...");
        Iterator<BLink> links = av_.getGUI().getMapPanel().getVisibleLinks();
        GeometryFactory gf = new GeometryFactory();
        linkSet_ = new LinkSet(new Color(0, 127, 0));
        int linkCount = av_.getGUI().getMapPanel().getVisibleLinkCount();
        int linkIndex = 0;
        while (links.hasNext()) {
            if (linkIndex % 50 == 0) {
                double p = 100.0 * (double) linkIndex / (double) linkCount;
                av_.setProgress((int) p);
            }
            BLink link = links.next();
            Geometry linkGeom = gf.createLineString(link.toCoordArray());
            if (link.getID() > 0 && buffer.contains(linkGeom)) {
                linkSet_.add(link);
            }
            linkIndex++;
        }
        av_.setProgress(0);
        av_.getGUI().statusText("Feature resolved successfully");
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_LINKSET, linkSet_, "r");
        av_.getGUI().getMapPanel().refresh(false, true, true);
        resolvedFeatID_ = id;
    }

    public void selectingStart() {
        if (resolvedFeatID_ == null) {
            av_.msg("No active resolved feature");
        } else {
            av_.expectSpecialNodeClick(new StartNodeHandler());
            av_.getGUI().getMapPanel().setNodeHoverListener(new EndpointNodeHoverListener());
            av_.msg("Select Start Node");
        }
    }

    private void selectedStart(BNode node) {
        if (node == null) {
            av_.msg("No node selected; try again");
            return;
        }
        if (resolvedFeatID_ == null) {
            av_.msg("No active resolved feature");
            return;
        }
        startNode_ = node;
        av_.msg("Selected node " + node.getID() + " as start");
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_ENDPOINT);
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_STARTNODE, new HighlightedPoint(node, HighlightedPoint.STARTNODE_COLOR, 8), "r", 10);
        av_.getGUI().clearSpecialClickAction();
        av_.getGUI().getMapPanel().clearNodeHoverListener();
        av_.getGUI().getImportRouteShpFrame().resetStartNodeButton();
        av_.getGUI().getMapPanel().refresh(false, false, true);
    }

    public void selectingEnd() {
        if (resolvedFeatID_ == null) {
            av_.msg("No active resolved feature");
        } else {
            av_.expectSpecialNodeClick(new EndNodeHandler());
            av_.getGUI().getMapPanel().setNodeHoverListener(new EndpointNodeHoverListener());
            av_.msg("Select End Node");
        }
    }

    private void selectedEnd(BNode node) {
        if (node == null) {
            av_.msg("No node selected; try again");
            return;
        }
        if (resolvedFeatID_ == null) {
            av_.msg("No active resolved feature");
            return;
        }
        endNode_ = node;
        av_.msg("Selected node " + node.getID() + " as end");
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_ENDPOINT);
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_ENDNODE, new HighlightedPoint(node, HighlightedPoint.ENDNODE_COLOR, 8), "r", 10);
        av_.getGUI().getMapPanel().clearNodeHoverListener();
        av_.getGUI().clearSpecialClickAction();
        av_.getGUI().getImportRouteShpFrame().resetEndNodeButton();
        av_.getGUI().getMapPanel().refresh(false, false, true);
    }

    public void clearCurrent() {
        resolvedFeatID_ = null;
        linkSet_ = null;
        potentialPaths_ = new HashSet<Path>();
        startNode_ = endNode_ = null;
        mainPath_ = new Path(av_.getEngine());
        lastHighlightedPath_ = null;
        graph_ = null;
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_PREVIEW);
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_STARTNODE);
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_ENDNODE);
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_LINKSET);
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_MAIN_PATH);
        av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_PATH);
    }

    public void editingLinks() {
        if (resolvedFeatID_ == null) {
            av_.msg("No active resolved feature");
            return;
        }
        av_.expectSpecialLinkClick(new EditLinkHandler());
    }

    public void selectedLink(BLink link) {
        if (link.getID() <= 0) {
            av_.msg("Cannot add d-link to linkset");
            return;
        }
        if (linkSet_.contains(link)) {
            linkSet_.remove(link);
        } else {
            linkSet_.add(link);
        }
        av_.getGUI().getMapPanel().refresh(false, false, true);
    }

    public void specifyingPath() {
        if (resolvedFeatID_ == null || startNode_ == null) {
            if (resolvedFeatID_ == null) {
                av_.msg("No active resolved feature");
            }
            if (startNode_ == null) {
                av_.msg("No start node specified");
            }
            av_.getGUI().getImportRouteShpFrame().resetSetPathButton();
            return;
        }
        createGraphFromlinkSet();
        if (mainPath_.linkCount() > 0) {
            findPotentialPaths(mainPath_.endNode(), mainPath_.endLink());
        } else {
            mainPath_ = new Path(av_.getEngine());
            findPotentialPaths(startNode_, null);
        }
        mainPath_.setDefaultDrawArrows(true);
        mainPath_.setDefaultDrawColor(new Color(0, 0, 127));
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_MAIN_PATH, mainPath_, "r", 5);
        av_.getGUI().getMapPanel().setLinkHoverListener(new SpecifyPathLinkHoverListener());
        av_.getGUI().getMapPanel().setIgnoreNodeHover(true);
        av_.expectSpecialLinkClick(new SpecifyPathLinkHandler());
    }

    public void stopSpecifyingPath() {
        av_.getGUI().getMapPanel().clearLinkHoverListener();
        av_.getGUI().getMapPanel().setIgnoreNodeHover(false);
        av_.getGUI().clearSpecialClickAction();
    }

    public void selectedPathLink(BLink link) {
        Iterator<Path> paths = potentialPaths_.iterator();
        Path appendPath = lastHighlightedPath_;
        while (paths.hasNext()) {
            Path path = paths.next();
            if (path.containsLink(link)) {
                appendPath = path;
                break;
            }
        }
        if (appendPath != null) {
            mainPath_.append(appendPath);
            findPotentialPaths(appendPath.endNode(), appendPath.endLink());
            if (!av_.getGUI().getMapPanel().getCanvas().getCC().getRange().contains(appendPath.endNode().getX(), appendPath.endNode().getY())) {
                av_.getGUI().getMapPanel().recenter(appendPath.endNode().getX(), appendPath.endNode().getY());
            } else {
                av_.getGUI().getMapPanel().refresh(false, false, true);
            }
        }
    }

    private void createGraphFromlinkSet() {
        graph_ = new SimpleGraph<BNode, BLink>(BLink.class);
        Iterator<BLink> links = linkSet_.iterator();
        while (links.hasNext()) {
            BLink link = links.next();
            if (!graph_.containsVertex(link.getFNode())) {
                graph_.addVertex(link.getFNode());
            }
            if (!graph_.containsVertex(link.getTNode())) {
                graph_.addVertex(link.getTNode());
            }
            graph_.addEdge(link.getFNode(), link.getTNode(), link);
        }
        System.out.println("created graph");
    }

    private void findPotentialPaths(BNode startNode, BLink ignoreInLink) {
        potentialPaths_ = new HashSet<Path>();
        if (!graph_.containsVertex(startNode_)) {
            av_.msg("Start node not on selected links");
            return;
        }
        Iterator<BLink> outLinks = graph_.edgesOf(startNode).iterator();
        while (outLinks.hasNext()) {
            BLink link = outLinks.next();
            if (link != ignoreInLink) {
                Path path = followLink(link, startNode);
                potentialPaths_.add(path);
            }
        }
        System.out.println("found " + potentialPaths_.size() + " paths");
    }

    private Path followLink(BLink link, BNode start) {
        Path path = new Path(av_.getEngine());
        path.addDirectedFirstLink(link, start);
        BNode node = opposite(start, link);
        while (node != null && graph_.degreeOf(node) == 2 && node != endNode_) {
            link = nextLink(node, link);
            path.addToEnd(link);
            node = opposite(node, link);
        }
        System.out.println("created path, len=" + path.linkCount());
        path.setDefaultDrawWidth(6);
        path.setDefaultDrawArrows(true);
        return path;
    }

    private BLink nextLink(BNode node, BLink inLink) {
        if (graph_.degreeOf(node) != 2) {
            return null;
        }
        Set<BLink> set = new HashSet<BLink>();
        set.addAll(graph_.edgesOf(node));
        set.remove(inLink);
        return set.iterator().next();
    }

    private BNode opposite(BNode node, BLink link) {
        if (graph_.getEdgeSource(link) == node) {
            return graph_.getEdgeTarget(link);
        }
        if (graph_.getEdgeTarget(link) == node) {
            return graph_.getEdgeSource(link);
        }
        return null;
    }

    public void applyToCurrent() {
        if (mainPath_.linkCount() == 0) {
            av_.msg("No active path");
            return;
        }
        SubRoute sub = av_.getRouteOps().getActiveSubRoute();
        if (sub == null) {
            av_.msg("No selected subroute");
            return;
        }
        TransitPath.Type type = TransitPath.Type.BUS;
        TransitPath tpath = new TransitPath(av_.getEngine(), sub, type, mainPath_, av_.getGUI().getTPathOptionsPanel().getAddStops());
        sub.setPath(tpath);
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.RTE_TRANSIT_PATH, sub.getPath(), "r", 20);
        av_.msg("Path applied to " + sub.getFullID());
        this.clearCurrent();
        av_.getGUI().getMapPanel().refresh(false, false, true);
    }

    private class StartNodeHandler implements SpecialNodeClickHandler {

        public void handleNode(BNode node) {
            selectedStart(node);
        }
    }

    private class EndNodeHandler implements SpecialNodeClickHandler {

        public void handleNode(BNode node) {
            selectedEnd(node);
        }
    }

    private class EndpointNodeHoverListener implements NodeHoverListener {

        public void nodeHoverOn(BNode node) {
            if (linkSet_.connectingLinks(node) % 2 == 1) {
                av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_ENDPOINT, new HighlightedPoint(node, Color.BLACK, 10), "r", -1);
                av_.getGUI().getMapPanel().refresh(false, false, true);
            }
        }

        public void nodeHoverOut(BNode node) {
            av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_ENDPOINT);
            av_.getGUI().getMapPanel().refresh(false, false, true);
        }
    }

    private class EditLinkHandler implements SpecialLinkClickHandler {

        public void handleLink(BLink link) {
            selectedLink(link);
        }
    }

    private class SpecifyPathLinkHandler implements SpecialLinkClickHandler {

        public void handleLink(BLink link) {
            selectedPathLink(link);
        }
    }

    private class SpecifyPathLinkHoverListener implements LinkHoverListener {

        public void linkHoverOn(BLink link) {
            Iterator<Path> paths = potentialPaths_.iterator();
            while (paths.hasNext()) {
                Path path = paths.next();
                if (path.containsLink(link)) {
                    av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_PATH, path, "r", -1);
                    av_.getGUI().getMapPanel().refresh(false, false, true);
                    lastHighlightedPath_ = path;
                    return;
                }
            }
        }

        public void linkHoverOut(BLink link) {
            av_.getGUI().getMapPanel().getDrawItems().removeItem(MapDrawItems.IMP_ROUTE_HIGHLIGHT_PATH);
            av_.getGUI().getMapPanel().refresh(false, false, true);
        }
    }
}
