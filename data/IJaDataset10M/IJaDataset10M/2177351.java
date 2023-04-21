package playground.jhackney;

import org.matsim.facilities.Facilities;
import org.matsim.facilities.MatsimFacilitiesReader;
import org.matsim.facilities.algorithms.FacilitiesMakeSample;
import org.matsim.gbl.Gbl;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkLayer;
import org.matsim.plans.MatsimPlansReader;
import org.matsim.plans.Plans;
import org.matsim.plans.PlansReaderI;
import org.matsim.plans.algorithms.PersonRemoveReferences;
import org.matsim.plans.algorithms.XY2Links;
import org.matsim.router.PlansCalcRoute;
import org.matsim.router.costcalculators.FreespeedTravelTimeCost;
import org.matsim.plans.algorithms.PlansScenarioCut;
import org.matsim.utils.geometry.CoordI;
import org.matsim.utils.geometry.shared.Coord;
import org.matsim.world.algorithms.WorldBottom2TopCompletion;
import org.matsim.world.MatsimWorldReader;
import org.matsim.world.World;
import org.matsim.world.WorldWriter;
import playground.jhackney.algorithms.*;

public class MakeScenario {

    public static void run() {
        System.out.println("Make Scenario SAMPLE OF FACILITIES:");
        System.out.println("Uses output of a CUT. Samples 100x\"pct\"% of the facilities and moves Acts to take place at these");
        System.out.println("  reading facilities xml file... ");
        Facilities facilities = (Facilities) Gbl.getWorld().createLayer(Facilities.LAYER_TYPE, null);
        new MatsimFacilitiesReader(facilities).readFile(Gbl.getConfig().facilities().getInputFile());
        System.out.println("  done.");
        System.out.println("  reading the network xml file...");
        NetworkLayer network = (NetworkLayer) Gbl.getWorld().createLayer(NetworkLayer.LAYER_TYPE, null);
        new MatsimNetworkReader(network).readFile(Gbl.getConfig().network().getInputFile());
        System.out.println("  done.");
        System.out.println("  reading plans xml file... ");
        Plans plans = new Plans();
        PlansReaderI plansReader = new MatsimPlansReader(plans);
        plansReader.readFile(Gbl.getConfig().plans().getInputFile());
        System.out.println("  done.");
        System.out.println("  running plans modules... ");
        System.out.println("  done.");
        System.out.println("  Completing World ... ");
        new WorldBottom2TopCompletion().run(Gbl.getWorld());
        System.out.println("  done.");
        System.out.println("  running facilities modules... ");
        System.out.println("  done.");
        System.out.println("  running plans modules... ");
        new PersonSetActToLinkWithNonNullFacility(facilities).run(plans);
        FreespeedTravelTimeCost timeCostCalc = new FreespeedTravelTimeCost();
        new PlansCalcRoute(network, timeCostCalc, timeCostCalc).run(plans);
        System.out.println("  done.");
        Scenario.writePlans(plans);
        Scenario.writeNetwork(network);
        Scenario.writeFacilities(facilities);
        Scenario.writeWorld(Gbl.getWorld());
        Scenario.writeConfig();
        System.out.println("TEST SUCCEEDED.");
        System.out.println();
    }

    public static void main(final String[] args) {
        Gbl.startMeasurement();
        Gbl.createConfig(args);
        Gbl.createWorld();
        run();
        Gbl.printElapsedTime();
    }
}
