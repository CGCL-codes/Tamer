package playground.fabrice.primloc;

import org.matsim.config.ConfigWriter;
import org.matsim.facilities.Facilities;
import org.matsim.facilities.FacilitiesWriter;
import org.matsim.facilities.MatsimFacilitiesReader;
import org.matsim.gbl.Gbl;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkLayer;
import org.matsim.network.NetworkWriter;
import org.matsim.plans.MatsimPlansReader;
import org.matsim.plans.Plans;
import org.matsim.plans.PlansReaderI;
import org.matsim.plans.PlansWriter;
import org.matsim.world.MatsimWorldReader;
import org.matsim.world.WorldWriter;

public class PrimlocValidationTest {

    public static void testRun01() {
        System.out.println("TEST RUN 01:");
        System.out.println("  reading world xml file... ");
        final MatsimWorldReader worldReader = new MatsimWorldReader(Gbl.getWorld());
        worldReader.readFile(Gbl.getConfig().world().getInputFile());
        System.out.println("  done.");
        System.out.println("  creating network layer... ");
        NetworkLayer network = (NetworkLayer) Gbl.getWorld().createLayer(NetworkLayer.LAYER_TYPE, null);
        System.out.println("  done.");
        System.out.println("  reading network xml file... ");
        new MatsimNetworkReader(network).readFile(Gbl.getConfig().network().getInputFile());
        System.out.println("  done.");
        System.out.println("  reading facilities xml file... ");
        Facilities facilities = (Facilities) Gbl.getWorld().createLayer(Facilities.LAYER_TYPE, null);
        new MatsimFacilitiesReader(facilities).readFile(Gbl.getConfig().facilities().getInputFile());
        System.out.println("  done.");
        System.out.println("  creating plans object... ");
        Plans plans = new Plans();
        System.out.println("  done.");
        System.out.println("  reading plans xml file... ");
        PlansReaderI plansReader = new MatsimPlansReader(plans);
        plansReader.readFile(Gbl.getConfig().plans().getInputFile());
        System.out.println("  done.");
        System.out.println("  adding plan algorithms");
        System.out.println("  ** adding primary location choice");
        PrimlocDriver plcm = new PrimlocDriver();
        plcm.setup(plans);
        plans.addAlgorithm(plcm);
        System.out.println("  done.");
        System.out.println("  running plan algorithms");
        plans.runAlgorithms();
        System.out.println("  writing plans xml file... ");
        new PlansWriter(plans).write();
        System.out.println("  done.");
        System.out.println("  writing facilities xml file... ");
        new FacilitiesWriter(facilities).write();
        System.out.println("  done.");
        System.out.println("  writing network xml file... ");
        NetworkWriter network_writer = new NetworkWriter(network);
        network_writer.write();
        System.out.println("  done.");
        System.out.println("  writing world xml file... ");
        WorldWriter world_writer = new WorldWriter(Gbl.getWorld());
        world_writer.write();
        System.out.println("  done.");
        System.out.println("  writing config xml file... ");
        ConfigWriter config_writer = new ConfigWriter(Gbl.getConfig());
        config_writer.write();
        System.out.println("  done.");
        System.out.println("TEST SUCCEEDED.");
        System.out.println();
    }

    public static void main(String[] args) {
        Gbl.startMeasurement();
        Gbl.createConfig(args);
        testRun01();
        Gbl.printElapsedTime();
    }
}
