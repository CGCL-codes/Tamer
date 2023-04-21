package org.matsim.core.network;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.testcases.MatsimTestCase;

/**
 * An abstract class that tests if certain features of networks (e.g. specific
 * settings/attributes for links or nodes) are written to file and afterwards
 * correctly read in. The class is abstract so the tests can be reused with
 * different file formats.
 *
 * @author mrieser
 * @param <T>
 */
public abstract class AbstractNetworkWriterReaderTest extends MatsimTestCase {

    /**
	 * Writes the given network to the specified file.
	 *
	 * @param network
	 * @param filename
	 */
    protected abstract void writeNetwork(final NetworkImpl network, final String filename);

    /**
	 * Reads a network from the specified file into the given network data structure.
	 *
	 * @param network
	 * @param filename
	 */
    protected abstract void readNetwork(final Scenario scenario, final String filename);

    public void testAllowedModes_multipleModes() {
        doTestAllowedModes(createHashSet("bus", "train"), getOutputDirectory() + "network.xml");
    }

    public void testAllowedModes_singleMode() {
        doTestAllowedModes(createHashSet("miv"), getOutputDirectory() + "network.xml");
    }

    public void testAllowedModes_noMode() {
        doTestAllowedModes(new HashSet<String>(), getOutputDirectory() + "network.xml");
    }

    private void doTestAllowedModes(final Set<String> modes, final String filename) {
        NetworkImpl network1 = NetworkImpl.createNetwork();
        Node n1 = network1.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
        Node n2 = network1.createAndAddNode(new IdImpl("2"), new CoordImpl(1000, 0));
        Link l1 = network1.createAndAddLink(new IdImpl("1"), n1, n2, 1000.0, 10.0, 3600.0, 1.0);
        l1.setAllowedModes(modes);
        writeNetwork(network1, filename);
        File networkFile = new File(filename);
        assertTrue("written network file doesn't exist.", networkFile.exists());
        assertTrue("written network file seems to be empty.", networkFile.length() > 0);
        Scenario scenario2 = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network2 = scenario2.getNetwork();
        readNetwork(scenario2, filename);
        Link link1 = network2.getLinks().get(new IdImpl("1"));
        assertNotNull("link not found in read-in network.", link1);
        Set<String> modes2 = link1.getAllowedModes();
        assertEquals("wrong number of allowed modes.", modes.size(), modes2.size());
        assertTrue("wrong mode.", modes2.containsAll(modes));
    }

    private static <T> Set<T> createHashSet(T... mode) {
        HashSet<T> set = new HashSet<T>();
        for (T t : mode) {
            set.add(t);
        }
        return set;
    }
}
