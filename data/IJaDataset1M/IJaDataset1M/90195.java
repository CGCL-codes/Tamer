package playground.yalcin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import net.opengis.kml._2.DocumentType;
import net.opengis.kml._2.FolderType;
import net.opengis.kml._2.KmlType;
import net.opengis.kml._2.ObjectFactory;
import net.opengis.kml._2.ScreenOverlayType;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.KmlNetworkWriter;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.network.NetworkWriter;
import org.matsim.core.network.algorithms.NetworkTransform;
import org.matsim.core.router.Dijkstra;
import org.matsim.core.router.costcalculators.FreespeedTravelTimeAndDisutility;
import org.matsim.core.router.costcalculators.TravelTimeAndDistanceBasedTravelDisutility;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.core.utils.misc.Counter;
import org.matsim.core.utils.misc.StringUtils;
import org.matsim.vis.kml.KMZWriter;
import org.matsim.vis.kml.MatsimKMLLogo;

public class NetworkDistance {

    private static final String bikeCoordsFilename = "../mystudies/yalcin/BikeCoordinates.txt";

    private static final String networkFilename = "../mystudies/yalcin/berlin_wipnet.xml";

    private static final String bikeDistancesFilename = "../mystudies/yalcin/bikeDistances.txt";

    private static final String wgs84NetworkFilename = "../mystudies/yalcin/berlin_wipnet_wgs84b.xml";

    private static final String networkKmzFilename = "../mystudies/yalcin/berlin_wipnet.kmz";

    public static void exportNetwork() {
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(networkFilename);
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.DHDN_GK4, TransformationFactory.WGS84);
        ObjectFactory kmlObjectFactory = new ObjectFactory();
        KmlType mainKml = kmlObjectFactory.createKmlType();
        DocumentType mainDoc = kmlObjectFactory.createDocumentType();
        mainDoc.setId("berlin_wipnet");
        mainKml.setAbstractFeatureGroup(kmlObjectFactory.createDocument(mainDoc));
        FolderType mainFolder = kmlObjectFactory.createFolderType();
        mainFolder.setId("2dnetworklinksfolder");
        mainFolder.setName("Matsim Data");
        mainDoc.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(mainFolder));
        KMZWriter writer = new KMZWriter(networkKmzFilename);
        try {
            ScreenOverlayType logo = MatsimKMLLogo.writeMatsimKMLLogo(writer);
            mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createScreenOverlay(logo));
            KmlNetworkWriter netWriter = new KmlNetworkWriter(network, ct, writer, mainDoc);
            FolderType networkFolder = netWriter.getNetworkFolder();
            mainFolder.getAbstractFeatureGroup().add(kmlObjectFactory.createFolder(networkFolder));
        } catch (IOException e) {
            Gbl.errorMsg("Cannot create kmz or logo because of: " + e.getMessage());
            e.printStackTrace();
        }
        writer.writeMainKml(mainKml);
        writer.close();
        System.out.println("done");
    }

    public static void convertNetwork() {
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(networkFilename);
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.DHDN_GK4, TransformationFactory.WGS84);
        new NetworkTransform(ct).run(network);
        new NetworkWriter(network).write(wgs84NetworkFilename);
    }

    public static void findDistances() {
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        NetworkImpl network = (NetworkImpl) scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(wgs84NetworkFilename);
        Config config = new Config();
        config.addCoreModules();
        config.planCalcScore().setTraveling_utils_hr(0.0);
        config.planCalcScore().setMonetaryDistanceCostRateCar(-0.001);
        config.planCalcScore().setMarginalUtilityOfMoney(1.);
        TravelTime travelTime = new FreespeedTravelTimeAndDisutility(config.planCalcScore());
        TravelDisutility linkCosts = new TravelTimeAndDistanceBasedTravelDisutility(travelTime, config.planCalcScore());
        Dijkstra router = new Dijkstra(network, linkCosts, travelTime);
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, TransformationFactory.DHDN_GK4);
        try {
            BufferedReader reader = IOUtils.getBufferedReader(bikeCoordsFilename);
            BufferedWriter writer = IOUtils.getBufferedWriter(bikeDistancesFilename);
            String header = reader.readLine();
            writer.write("Index\tKundenindex\tcrowflyDist\tnetworkDist\tfromNode\ttoNode\tlinks...\n");
            Counter counter = new Counter("line ");
            String line = reader.readLine();
            while (line != null) {
                counter.incCounter();
                String[] parts = StringUtils.explode(line, '\t');
                Coord fromCoord = new CoordImpl(parts[4], parts[5]);
                Coord toCoord = new CoordImpl(parts[8], parts[9]);
                Node fromNode = network.getNearestNode(fromCoord);
                Node toNode = network.getNearestNode(toCoord);
                Path path = router.calcLeastCostPath(fromNode, toNode, 0);
                double crowflyDistance = CoordUtils.calcDistance(ct.transform(fromCoord), ct.transform(toCoord));
                writer.write(parts[0] + "\t" + parts[1] + "\t" + crowflyDistance + "\t" + getPathDistance(path));
                writer.write("\t" + fromNode.getId().toString());
                writer.write("\t" + toNode.getId().toString());
                for (Link link : path.links) {
                    writer.write("\t" + link.getId().toString());
                }
                writer.write("\n");
                line = reader.readLine();
            }
            counter.printCounter();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private static double getPathDistance(final Path path) {
        double dist = 0;
        for (Link link : path.links) {
            dist += link.getLength();
        }
        return dist;
    }

    public static void main(String[] args) {
        convertNetwork();
        findDistances();
    }
}
