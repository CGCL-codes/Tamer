package playground.johannes.networks;

import java.util.List;
import org.matsim.config.Config;
import org.matsim.gbl.Gbl;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkChangeEvent;
import org.matsim.network.NetworkChangeEventsWriter;
import org.matsim.network.NetworkChangeEventsParser;
import org.matsim.network.NetworkLayer;

/**
 * @author illenberger
 *
 */
public class ParserTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Config config = Gbl.createConfig(new String[] { "/Users/fearonni/vsp-work/eut/corridor/config/config.xml" });
        String networkFile = "/Users/fearonni/vsp-work/eut/corridor/data/net.2.xml";
        NetworkLayer network = new NetworkLayer();
        new MatsimNetworkReader(network).readFile(networkFile);
        NetworkChangeEventsParser parser = new NetworkChangeEventsParser(network);
        List<NetworkChangeEvent> events = parser.parseEvents("/Users/fearonni/workspace/matsim_sf/test/input/org/matsim/network/testNetworkChangeEvents.xml");
        NetworkChangeEventsWriter writer = new NetworkChangeEventsWriter();
        writer.write("output/netevents.xml", events);
    }
}
