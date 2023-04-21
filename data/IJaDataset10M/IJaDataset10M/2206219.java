package playground.gregor.gis.shapefiletransformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.matsim.api.basic.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.network.Network;
import org.matsim.core.api.network.Node;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.network.NetworkWriter;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.core.utils.misc.Time;
import playground.gregor.MY_STATIC_STUFF;

public class NetworkTransform {

    private final String net;

    private final String outFile;

    private final String nodesFile;

    public NetworkTransform(String net, String outFile, String nodesFile) {
        this.net = net;
        this.outFile = outFile;
        this.nodesFile = nodesFile;
    }

    private void run() throws IOException {
        Map<String, Coord> map = new HashMap<String, Coord>();
        FeatureSource fts = ShapeFileReader.readDataFile(this.nodesFile);
        Iterator it = fts.getFeatures().iterator();
        while (it.hasNext()) {
            Feature ft = (Feature) it.next();
            Integer id = (Integer) ft.getAttribute("ID");
            map.put(id.toString(), MGC.coordinate2Coord(ft.getDefaultGeometry().getCentroid().getCoordinate()));
        }
        Scenario sc = new ScenarioImpl();
        Network net = sc.getNetwork();
        new MatsimNetworkReader(net).readFile(this.net);
        NetworkLayer nl = new NetworkLayer();
        for (Node node : net.getNodes().values()) {
            Coord c = map.get(node.getId().toString());
            if (node.getId().toString().contains("en")) {
                c = node.getCoord();
            }
            if (c == null) {
                c = node.getCoord();
            }
            nl.createNode(node.getId(), c);
        }
        for (Link link : net.getLinks().values()) {
            nl.createLink(link.getId(), nl.getNode(link.getFromNode().getId()), nl.getNode(link.getToNode().getId()), link.getLength(), link.getFreespeed(Time.UNDEFINED_TIME), link.getCapacity(Time.UNDEFINED_TIME), link.getNumberOfLanes(Time.UNDEFINED_TIME));
        }
        nl.setEffectiveCellSize(net.getEffectiveCellSize());
        nl.setEffectiveLaneWidth(net.getEffectiveLaneWidth());
        nl.setCapacityPeriod(net.getCapacityPeriod());
        new NetworkWriter(nl, this.outFile).write();
    }

    public static void main(String[] args) {
        String nodesFile = MY_STATIC_STUFF.CVS_GIS + "/network_v20080618/nodes.shp";
        List<String> list = new ArrayList<String>();
        list.add("/home/laemmel/devel/outputs/output/output_network.xml.gz");
        for (String net : list) {
            try {
                new NetworkTransform(net, net, nodesFile).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
