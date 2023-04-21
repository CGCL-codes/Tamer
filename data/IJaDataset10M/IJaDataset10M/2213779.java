package playground.dgrether;

import org.matsim.basic.v01.Id;
import org.matsim.events.EventLinkEnter;
import org.matsim.events.handler.EventHandlerLinkEnterI;
import org.matsim.trafficmonitoring.LinkSensorManager;
import org.matsim.utils.vis.netvis.NetVis;
import org.matsim.withinday.coopers.CoopersControler;

/**
 * @author dgrether
 *
 */
public class MyControlerBasicFunctionality {

    private static final String inputdir = "test/input/org/matsim/integration/withinday/CoopersImplementationTest/testBasicFunctionality/";

    private static final String outputdir = "test/output/org/matsim/integration/withinday/CoopersImplementationTest/testBasicFunctionality/";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CoopersControler controler = new CoopersControler("./test/input/org/matsim/integration/withinday/CoopersImplementationTest/testBasicFunctionality/config.xml");
        controler.setCreateGraphs(false);
        controler.setOverwriteFiles(true);
        controler.getEvents().addHandler(new EventHandlerLinkEnterI() {

            public void handleEvent(EventLinkEnter event) {
                if (event.link.getId().equals(new Id("6"))) {
                    System.out.println("enter link 6");
                }
            }

            public void reset(int iteration) {
            }
        });
        LinkSensorManager lsm = new LinkSensorManager();
        lsm.addLinkSensor("5");
        lsm.addLinkSensor("7");
        controler.getEvents().addHandler(lsm);
        controler.run();
        System.out.println("Traffic on link 5: " + lsm.getLinkTraffic("5"));
        System.out.println("Traffic on link 7: " + lsm.getLinkTraffic("7"));
        String[] visargs = { controler.getConfig().controler().getOutputDirectory() + "/ITERS/it.0/Snapshot" };
        NetVis.main(visargs);
    }
}
