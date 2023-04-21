package playground.christoph.network.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.opengis.kml._2.DocumentType;
import net.opengis.kml._2.FolderType;
import net.opengis.kml._2.KmlType;
import net.opengis.kml._2.ObjectFactory;
import net.opengis.kml._2.ScreenOverlayType;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.CH1903LV03toWGS84;
import org.matsim.core.utils.misc.Time;
import org.matsim.vis.kml.KMZWriter;
import org.matsim.vis.kml.MatsimKMLLogo;
import playground.christoph.knowledge.container.MapKnowledge;
import playground.christoph.knowledge.container.NodeKnowledge;
import playground.christoph.network.KmlSubNetworkWriter;
import playground.christoph.network.SubNetwork;
import playground.christoph.network.mapping.MappingInfo;
import playground.christoph.network.util.SubNetworkCreator;

public class NetworkThinning {

    private static final Logger log = Logger.getLogger(NetworkThinning.class);

    private Network network;

    private SubNetwork subNetwork;

    private Map<Id, Node> transformableNodes;

    private Map<Id, Node> nodesToTransform;

    private List<SubNetwork> subNetworks = new ArrayList<SubNetwork>();

    private int numOfThreads = 2;

    int idCounter = 0;

    public static void main(String[] args) {
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        String networkFile = "mysimulations/kt-zurich/input/network.xml";
        Network nw = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(networkFile);
        log.info("Network has " + nw.getNodes().size() + " Nodes.");
        log.info("Network has " + nw.getLinks().size() + " Links.");
        NetworkThinning ntfd = new NetworkThinning();
        ntfd.setNetwork(nw);
        PrepareNetwork prepareNetwork = new PrepareNetwork(nw);
        prepareNetwork.checkAndAddLinks();
        ntfd.setSubNetwork((SubNetwork) prepareNetwork.createMappedNetwork());
        ntfd.getSubNetworks().add(ntfd.getSubNetwork());
        log.info("Network has " + nw.getNodes().size() + " Nodes.");
        log.info("Network has " + nw.getLinks().size() + " Links.");
        for (int i = 0; i < 10; i++) {
            log.info("Iteration " + (i + 1));
            prepareNetwork = new PrepareNetwork(ntfd.getSubNetworks().get(i));
            SubNetwork subNetwork = (SubNetwork) prepareNetwork.createMappedNetwork();
            ntfd.getSubNetworks().add(subNetwork);
            ntfd.setSubNetwork(subNetwork);
            for (Node node : ntfd.getSubNetwork().getNodes().values()) {
                if (node.getInLinks().size() != node.getOutLinks().size()) {
                    log.warn("Different Link Count!");
                }
            }
            TransformTriNodes tft = new TransformTriNodes(ntfd.getSubNetwork());
            tft.printStatistics(true);
            tft.findTransformableStructures();
            tft.selectTransformableStructures();
            tft.doTransformation();
            TransformLoops tl = new TransformLoops(ntfd.getSubNetwork());
            tl.printStatistics(true);
            tl.setMaxCosts(5000.0 * Math.pow(1 + (i / 8), 2));
            tl.findTransformableStructures();
            tl.selectTransformableStructures();
            tl.doTransformation();
            TransformDuoNodes tfn = new TransformDuoNodes(ntfd.getSubNetwork());
            tfn.printStatistics(true);
            tfn.findTransformableStructures();
            tfn.selectTransformableStructures();
            tfn.doTransformation();
            TransformDuplicatedLinks tdl = new TransformDuplicatedLinks(ntfd.getSubNetwork());
            tdl.printStatistics(true);
            tdl.findTransformableStructures();
            tdl.selectTransformableStructures();
            tdl.doTransformation();
            TransformDeadEndNodes tden = new TransformDeadEndNodes(ntfd.getSubNetwork());
            tden.printStatistics(true);
            tden.findTransformableStructures();
            tden.selectTransformableStructures();
            tden.doTransformation();
        }
        log.info("After Network Thinning");
        log.info("Network has " + ntfd.getSubNetwork().getNodes().size() + " Nodes.");
        log.info("Network has " + ntfd.getSubNetwork().getLinks().size() + " Links.");
        ntfd.writeKmlFile();
        SubNetwork subNw = ntfd.getSubNetworks().get(0);
        MappingInfo info1 = (MappingInfo) subNw.getNodes().get(new IdImpl("1000"));
        while (info1.getUpMapping() != null) {
            info1 = (MappingInfo) info1.getUpMapping().getOutput();
            log.info(info1);
        }
        if (info1 instanceof Node) log.info("Node " + subNw.getNodes().containsKey(((Node) info1).getId())); else if (info1 instanceof Link) log.info("Link " + subNw.getLinks().containsKey(((Link) info1).getId()));
        log.info("");
        MappingInfo info2 = (MappingInfo) subNw.getLinks().get(new IdImpl("129227"));
        while (info2.getUpMapping() != null) {
            info2 = (MappingInfo) info2.getUpMapping().getOutput();
            log.info(info2);
        }
        if (info2 instanceof Node) log.info("Node " + subNw.getNodes().containsKey(((Node) info2).getId())); else if (info2 instanceof Link) log.info("Link " + subNw.getLinks().containsKey(((Link) info2).getId()));
        for (Node node : ntfd.getSubNetwork().getNodes().values()) {
            MappingInfo mappingInfo = (MappingInfo) node;
            List<MappingInfo> mappings = mappingInfo.getDownMapping().getMappedObjects();
            for (MappingInfo mapping : mappings) {
                if (mapping instanceof Node) {
                    if (!nw.getNodes().containsKey(((Node) mapping).getId())) {
                        log.error("Node not found!");
                    }
                } else if (mapping instanceof Link) {
                    if (!nw.getLinks().containsKey(((Link) mapping).getId())) {
                        log.error("Link not found!");
                    }
                }
            }
            log.info("Size: " + mappings.size());
        }
        for (Link link : ntfd.getSubNetwork().getLinks().values()) {
            MappingInfo mappingInfo = (MappingInfo) link;
            List<MappingInfo> mappings = mappingInfo.getDownMapping().getMappedObjects();
            for (MappingInfo mapping : mappings) {
                if (mapping instanceof Node) {
                    if (!nw.getNodes().containsKey(((Node) mapping).getId())) {
                        log.error("Node not found!");
                    }
                } else if (mapping instanceof Link) {
                    if (!nw.getLinks().containsKey(((Link) mapping).getId())) {
                        log.error("Link not found!");
                    }
                }
            }
            log.info("Size: " + mappings.size());
        }
        log.info("Done!");
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setSubNetwork(SubNetwork subNetwork) {
        this.subNetwork = subNetwork;
    }

    public SubNetwork getSubNetwork() {
        return this.subNetwork;
    }

    public List<SubNetwork> getSubNetworks() {
        return this.subNetworks;
    }

    public void createSubNetwork() {
        SubNetworkCreator snc = new SubNetworkCreator(network);
        NodeKnowledge nodeKnowledge = new MapKnowledge();
        nodeKnowledge.setNetwork(network);
        nodeKnowledge.setKnownNodes((Map<Id, Node>) network.getNodes());
        subNetwork = snc.createSubNetwork(nodeKnowledge);
    }

    public void writeKmlFile() {
        ObjectFactory kmlObjectFactory = new ObjectFactory();
        KmlType mainKml = kmlObjectFactory.createKmlType();
        DocumentType mainDoc = kmlObjectFactory.createDocumentType();
        mainKml.setAbstractFeatureGroup(kmlObjectFactory.createDocument(mainDoc));
        FolderType mainFolder = kmlObjectFactory.createFolderType();
        mainFolder.setName("Matsim Data");
        mainDoc.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(mainFolder));
        KMZWriter writer = new KMZWriter("ThinnedNetwork.kml");
        try {
            ScreenOverlayType logo = MatsimKMLLogo.writeMatsimKMLLogo(writer);
            mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createScreenOverlay(logo));
            CoordinateTransformation coordTransformation;
            coordTransformation = new CH1903LV03toWGS84();
            KmlSubNetworkWriter netWriter = new KmlSubNetworkWriter(subNetwork, coordTransformation, writer, mainDoc);
            FolderType networkFolder = netWriter.getNetworkFolder();
            mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(networkFolder));
        } catch (IOException e) {
            Gbl.errorMsg("Cannot create kmz or logo cause: " + e.getMessage());
            e.printStackTrace();
        }
        writer.writeMainKml(mainKml);
        writer.close();
        log.info("SubNetwork written to kmz!");
    }

    public void findNodesMultiThread() {
        ThinningThread[] thinningThreads = new ThinningThread[numOfThreads];
        List<List<Node>> nodeLists = new ArrayList<List<Node>>();
        for (int i = 0; i < numOfThreads; i++) {
            nodeLists.add(new ArrayList<Node>());
        }
        int i = 0;
        for (Node node : this.network.getNodes().values()) {
            nodeLists.get(i % numOfThreads).add(node);
            i++;
        }
        for (int j = 0; j < thinningThreads.length; j++) {
            thinningThreads[j] = new ThinningThread(network, nodeLists.get(j));
            thinningThreads[j].setName("ThinningThread#" + i);
        }
        for (ThinningThread thinningThread : thinningThreads) {
            thinningThread.start();
        }
        try {
            for (ThinningThread thinningThread : thinningThreads) {
                thinningThread.join();
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        transformableNodes = new TreeMap<Id, Node>();
        for (ThinningThread thinningThread : thinningThreads) {
            transformableNodes.putAll(thinningThread.getTransformableNodes());
        }
        log.info("Network has " + network.getLinks().size() + " Links.");
        log.info("Not used links: " + transformableNodes.size());
    }

    private boolean subNodesConnected(Node a, Node b, Node c) {
        for (Link link : a.getOutLinks().values()) {
            Node node = link.getToNode();
            if (node.equals(b) || node.equals(c)) return true;
        }
        for (Link link : b.getOutLinks().values()) {
            Node node = link.getToNode();
            if (node.equals(a) || node.equals(c)) return true;
        }
        for (Link link : c.getOutLinks().values()) {
            Node node = link.getToNode();
            if (node.equals(a) || node.equals(b)) return true;
        }
        return false;
    }

    private int calcInTriangle(Node a, Node b, Node c, Node point) {
        double det1 = calcDeterminant(a, b, point);
        double det2 = calcDeterminant(b, c, point);
        double det3 = calcDeterminant(c, a, point);
        if (det1 == 0.0 || det2 == 0.0 || det3 == 0.0) return 0;
        if (det1 > 0 && det2 > 0 && det3 > 0) return 1;
        if (det1 < 0 && det2 < 0 && det3 < 0) return 1;
        return -1;
    }

    private double calcDeterminant(Node start, Node end, Node point) {
        double[] row1 = new double[] { start.getCoord().getX(), start.getCoord().getY(), 1 };
        double[] row2 = new double[] { end.getCoord().getX(), end.getCoord().getY(), 1 };
        double[] row3 = new double[] { point.getCoord().getX(), point.getCoord().getY(), 1 };
        double det = row1[0] * row2[1] * row3[2] + row1[1] * row2[2] * row3[0] + row1[2] * row2[0] * row3[1] - row1[2] * row2[1] * row3[0] - row1[1] * row2[0] * row3[2] - row1[0] * row2[2] * row3[1];
        return det;
    }

    /**
	 * The thread class that really handles the persons.
	 */
    private static class ThinningThread extends Thread {

        private Network network;

        private Map<Id, Node> nodesToTransform;

        private List<Node> nodes;

        private double time = Time.UNDEFINED_TIME;

        private int thread;

        private static int threadCounter = 0;

        public ThinningThread(Network network, List<Node> nodes) {
            this.network = network;
            this.nodes = nodes;
            this.thread = threadCounter++;
        }

        @Override
        public void run() {
            findNodes();
        }

        public Map<Id, Node> getTransformableNodes() {
            return this.nodesToTransform;
        }

        private void findNodes() {
            nodesToTransform = new HashMap<Id, Node>();
            int nodeCount = 0;
            for (Node node : nodes) {
                List<Node> outNodes = new ArrayList<Node>();
                for (Link outLink : node.getOutLinks().values()) {
                    outNodes.add(outLink.getToNode());
                }
                nodeCount++;
                if (nodeCount % 1000 == 0) {
                    log.info("Thread: " + thread + ", NodeCount: " + nodeCount + ", not used Links: " + nodesToTransform.size());
                }
            }
        }
    }
}
