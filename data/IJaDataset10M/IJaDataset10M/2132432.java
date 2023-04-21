package playground.david.vis;

import org.matsim.gbl.Gbl;
import org.matsim.mobsim.QueueNetworkLayer;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.world.World;

public class PadangEventConverter {

    public static void main(String[] args) {
        if (args.length == 0) args = new String[] { "./test/dstrippgen/myconfig.xml" };
        Gbl.createConfig(args);
        Gbl.startMeasurement();
        World world = Gbl.createWorld();
        String netFileName = Gbl.getConfig().getParam("network", "inputNetworkFile");
        netFileName = "../../tmp/studies/padang/evacuation_net.xml";
        QueueNetworkLayer net = new QueueNetworkLayer();
        new MatsimNetworkReader(net).readFile(netFileName);
        world.setNetworkLayer(net);
        String eventFile = Gbl.getConfig().getParam("events", "outputFile");
        eventFile = "../../tmp/studies/padang/0.events.txt.gz";
        OTFNetEventFileHandler test = new OTFNetEventFileHandler(10, net, "../../tmp/studies/padang/ds_fromEvent.vis");
        test.run(eventFile);
    }
}
