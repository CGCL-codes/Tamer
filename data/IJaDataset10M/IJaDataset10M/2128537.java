package playground.mzilske.teach;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.network.NetworkWriter;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.io.OsmNetworkReader;
import org.xml.sax.SAXException;

public class Zurich {

    public static void main(String[] args) {
        String osm = "./inputs/schweiz-2/merged-network.osm";
        Scenario sc = new ScenarioImpl();
        Network net = sc.getNetwork();
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, TransformationFactory.WGS84_UTM35S);
        OsmNetworkReader onr = new OsmNetworkReader(net, ct);
        onr.setHierarchyLayer(48.15, 5.71, 45.41, 11, 6);
        try {
            onr.parse(osm);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Converted.");
        new NetworkCleaner().run(net);
        new NetworkWriter(net).write("./inputs/schweiz-2/network.xml");
    }
}
