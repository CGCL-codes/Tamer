package playground.christoph.knowledge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import net.opengis.kml._2.AbstractFeatureType;
import net.opengis.kml._2.DocumentType;
import net.opengis.kml._2.FolderType;
import net.opengis.kml._2.KmlType;
import net.opengis.kml._2.ObjectFactory;
import net.opengis.kml._2.PlacemarkType;
import net.opengis.kml._2.ScreenOverlayType;
import net.opengis.kml._2.StyleType;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.KmlNetworkWriter;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.IdentityTransformation;
import org.matsim.core.utils.misc.RouteUtils;
import org.matsim.vis.kml.KMZWriter;
import org.matsim.vis.kml.MatsimKMLLogo;
import org.matsim.vis.kml.MatsimKmlStyleFactory;
import org.matsim.vis.kml.NetworkFeatureFactory;
import org.matsim.vis.kml.NetworkKmlStyleFactory;

public class KMLPersonWriter {

    private static final Logger log = Logger.getLogger(KMLPersonWriter.class);

    protected String netFileName;

    protected String kmzFileName;

    protected String outputDirectory;

    protected PersonImpl person;

    protected ArrayList<Link> activityLinks;

    protected ArrayList<Node> routeNodes;

    protected Network network;

    protected Map<Id, Node> nodes;

    protected NetworkRoute route;

    protected boolean writeKnownNodes = true;

    protected boolean writeActivityLinks = true;

    protected boolean writeRouteNodes = true;

    protected boolean writeNetwork = false;

    private NetworkKmlStyleFactory styleFactory;

    private final ObjectFactory kmlObjectFactory = new ObjectFactory();

    private StyleType networkLinkStyle;

    private NetworkFeatureFactory networkFeatureFactory;

    private StyleType networkNodeStyle;

    private CoordinateTransformation coordinateTransform = new IdentityTransformation();

    public KMLPersonWriter(Network network, PersonImpl person) {
        setNetwork(network);
        setPerson(person);
        collectKnownNodes();
    }

    public KMLPersonWriter() {
    }

    public void writeFile() {
        String outputFile;
        if (kmzFileName == null || kmzFileName.equals("")) {
            outputFile = outputDirectory + "/" + this.person.getId() + ".kmz";
        } else {
            outputFile = outputDirectory + "/" + kmzFileName;
        }
        ObjectFactory kmlObjectFactory = new ObjectFactory();
        KmlType mainKml = kmlObjectFactory.createKmlType();
        DocumentType mainDoc = kmlObjectFactory.createDocumentType();
        mainDoc.setId("mainDoc");
        mainKml.setAbstractFeatureGroup(kmlObjectFactory.createDocument(mainDoc));
        FolderType mainFolder = kmlObjectFactory.createFolderType();
        mainFolder.setId("2dnetworklinksfolder");
        mainFolder.setName("Matsim Data");
        mainDoc.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(mainFolder));
        KMZWriter writer = new KMZWriter(outputFile);
        this.styleFactory = new MatsimKmlStyleFactory(writer, mainDoc);
        this.networkFeatureFactory = new NetworkFeatureFactory(coordinateTransform, network);
        try {
            ScreenOverlayType logo = MatsimKMLLogo.writeMatsimKMLLogo(writer);
            mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createScreenOverlay(logo));
            if (writeNetwork && this.network != null) {
                KmlNetworkWriter netWriter = new KmlNetworkWriter(network, coordinateTransform, writer, mainDoc);
                FolderType networkFolder = netWriter.getNetworkFolder();
                mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(networkFolder));
            }
            if (writeKnownNodes) {
                FolderType nodesFolder = getNodesFolder();
                if (nodesFolder != null) {
                    mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(nodesFolder));
                }
            }
            if (writeRouteNodes) {
                FolderType routeFolder = getRouteNodesFolder();
                if (routeFolder != null) {
                    mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(routeFolder));
                }
            }
            if (writeActivityLinks) {
                FolderType activityFolder = getActivityLinksFolder();
                if (activityFolder != null) {
                    mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(activityFolder));
                }
            }
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
        writer.writeMainKml(mainKml);
        writer.close();
    }

    private FolderType getNodesFolder() throws IOException {
        if (nodes == null) return null;
        FolderType folder = this.kmlObjectFactory.createFolderType();
        folder.setName("Activity Room of Person " + this.person.getId());
        this.networkLinkStyle = this.styleFactory.createDefaultNetworkLinkStyle();
        this.networkNodeStyle = this.styleFactory.createDefaultNetworkNodeStyle();
        FolderType nodeFolder = kmlObjectFactory.createFolderType();
        nodeFolder.setName("Activity Room");
        for (Node node : this.nodes.values()) {
            AbstractFeatureType abstractFeature = this.networkFeatureFactory.createNodeFeature(node, this.networkNodeStyle);
            if (abstractFeature.getClass().equals(PlacemarkType.class)) {
                nodeFolder.getAbstractFeatureGroup().add(this.kmlObjectFactory.createPlacemark((PlacemarkType) abstractFeature));
            } else {
                log.warn("Not yet implemented: Adding node KML features of type " + abstractFeature.getClass());
            }
        }
        folder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(nodeFolder));
        return folder;
    }

    private FolderType getRouteNodesFolder() throws IOException {
        if (routeNodes == null) return null;
        FolderType folder = this.kmlObjectFactory.createFolderType();
        folder.setName("Route of Person " + this.person.getId());
        this.networkLinkStyle = this.styleFactory.createDefaultNetworkLinkStyle();
        this.networkNodeStyle = this.styleFactory.createDefaultNetworkNodeStyle();
        FolderType nodeFolder = kmlObjectFactory.createFolderType();
        nodeFolder.setName("Route");
        for (Node node : routeNodes) {
            AbstractFeatureType abstractFeature = this.networkFeatureFactory.createNodeFeature(node, this.networkNodeStyle);
            if (abstractFeature.getClass().equals(PlacemarkType.class)) {
                nodeFolder.getAbstractFeatureGroup().add(this.kmlObjectFactory.createPlacemark((PlacemarkType) abstractFeature));
            } else {
                log.warn("Not yet implemented: Adding node KML features of type " + abstractFeature.getClass());
            }
        }
        folder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(nodeFolder));
        return folder;
    }

    private FolderType getActivityLinksFolder() throws IOException {
        if (routeNodes == null) return null;
        FolderType folder = this.kmlObjectFactory.createFolderType();
        folder.setName("Activity Links of Person " + this.person.getId());
        this.networkLinkStyle = this.styleFactory.createDefaultNetworkLinkStyle();
        this.networkNodeStyle = this.styleFactory.createDefaultNetworkNodeStyle();
        FolderType linkFolder = kmlObjectFactory.createFolderType();
        linkFolder.setName("Activity Links");
        for (Link link : activityLinks) {
            AbstractFeatureType abstractFeature = this.networkFeatureFactory.createLinkFeature(link, this.networkLinkStyle);
            if (abstractFeature.getClass().equals(PlacemarkType.class)) {
                linkFolder.getAbstractFeatureGroup().add(this.kmlObjectFactory.createPlacemark((PlacemarkType) abstractFeature));
            } else {
                log.warn("Not yet implemented: Adding node KML features of type " + abstractFeature.getClass());
            }
        }
        folder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(linkFolder));
        return folder;
    }

    private void createRouteNodes() {
        this.routeNodes = new ArrayList<Node>();
        if (this.person != null) {
            Plan selectedPlan = this.person.getSelectedPlan();
            if (selectedPlan != null) {
                for (PlanElement pe : selectedPlan.getPlanElements()) {
                    if (pe instanceof Leg) {
                        Leg leg = (Leg) pe;
                        if (leg.getRoute() instanceof NetworkRoute) {
                            NetworkRoute route = (NetworkRoute) leg.getRoute();
                            for (Node node : RouteUtils.getNodes(route, this.network)) {
                                routeNodes.add(node);
                            }
                        } else {
                            log.error("Unknown Route Type found!");
                        }
                    }
                }
            }
        }
    }

    private void createActivityLinks() {
        this.activityLinks = new ArrayList<Link>();
        if (this.person != null) {
            Plan selectedPlan = this.person.getSelectedPlan();
            if (selectedPlan != null) {
                for (PlanElement pe : selectedPlan.getPlanElements()) {
                    if (pe instanceof Activity) {
                        Activity act = (Activity) pe;
                        activityLinks.add(network.getLinks().get(act.getLinkId()));
                    }
                }
            }
        }
    }

    private void collectKnownNodes() {
        if (this.person != null) {
            Map<String, Object> customAttributes = person.getCustomAttributes();
            if (customAttributes != null) {
                if (customAttributes.containsKey("Nodes")) {
                    nodes = (Map<Id, Node>) customAttributes.get("Nodes");
                } else nodes = null;
            } else nodes = null;
        } else nodes = null;
    }

    public void writeKnownNodes(boolean value) {
        this.writeKnownNodes = value;
    }

    public void writeRouteNodes(boolean value) {
        this.writeRouteNodes = value;
    }

    public void writeActivityLinks(boolean value) {
        this.writeActivityLinks = value;
    }

    public void writeNetwork(boolean value) {
        this.writeNetwork = value;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setCoordinateTransformation(CoordinateTransformation coordinateTransform) {
        this.coordinateTransform = coordinateTransform;
    }

    public CoordinateTransformation getCoordinateTransformation() {
        return this.coordinateTransform;
    }

    public void setOutputDirectory(String directory) {
        this.outputDirectory = directory;
    }

    public String getOutputDirectory() {
        return this.outputDirectory;
    }

    public void setPerson(PersonImpl person) {
        this.person = person;
        if (person != null) {
            createRouteNodes();
            createActivityLinks();
        } else {
            routeNodes = null;
            activityLinks = null;
        }
    }

    public PersonImpl getPerson() {
        return this.person;
    }

    public void setKmzFileName(String name) {
        kmzFileName = name;
    }

    public String getKmzFileName() {
        return this.kmzFileName;
    }
}
