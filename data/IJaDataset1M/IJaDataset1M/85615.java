package playground.andreas.intersection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import org.matsim.config.Config;
import org.matsim.controler.ScenarioImpl;
import org.matsim.events.ActEndEvent;
import org.matsim.events.ActStartEvent;
import org.matsim.events.AgentArrivalEvent;
import org.matsim.events.AgentDepartureEvent;
import org.matsim.events.AgentWait2LinkEvent;
import org.matsim.events.Events;
import org.matsim.events.LinkEnterEvent;
import org.matsim.events.LinkLeaveEvent;
import org.matsim.events.handler.ActEndEventHandler;
import org.matsim.events.handler.ActStartEventHandler;
import org.matsim.events.handler.AgentArrivalEventHandler;
import org.matsim.events.handler.AgentDepartureEventHandler;
import org.matsim.events.handler.AgentWait2LinkEventHandler;
import org.matsim.events.handler.LinkEnterEventHandler;
import org.matsim.events.handler.LinkLeaveEventHandler;
import org.matsim.mobsim.queuesim.QueueNetwork;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.utils.CRCChecksum;
import org.matsim.utils.io.IOUtils;
import playground.andreas.intersection.sim.QSim;

/**
 * @author aneumann
 *
 */
public class TravelTimeTestFourWay extends MatsimTestCase implements LinkLeaveEventHandler, LinkEnterEventHandler, ActEndEventHandler, ActStartEventHandler, AgentArrivalEventHandler, AgentDepartureEventHandler, AgentWait2LinkEventHandler {

    BufferedWriter writer = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        QueueNetwork.setSimulateAllLinks(true);
        QueueNetwork.setSimulateAllNodes(true);
    }

    public void testTrafficLightIntersection4arms() {
        System.setProperty("line.separator", "\n");
        Config conf = loadConfig("test/input/playground/andreas/intersection/fourways/config.xml");
        String newLSADef = "test/input/playground/andreas/intersection/fourways/lsa.xml";
        String newLSADefCfg = "test/input/playground/andreas/intersection/fourways/lsa_config.xml";
        ScenarioImpl data = new ScenarioImpl(conf);
        Events events = new Events();
        events.addHandler(this);
        try {
            this.writer = IOUtils.getBufferedWriter("temp.txt.gz", true);
            new QSim(events, data.getPopulation(), data.getNetwork(), false, newLSADef, newLSADefCfg).run();
            this.writer.flush();
            this.writer.close();
            assertEquals(CRCChecksum.getCRCFromFile("temp.txt.gz"), CRCChecksum.getCRCFromFile("test/input/playground/andreas/intersection/fourways/reference.txt.gz"));
            new File("temp.txt.gz").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testTrafficLightIntersection4armsWithUTurn() {
        System.setProperty("line.separator", "\n");
        Config conf = loadConfig("test/input/playground/andreas/intersection/fourways/config.xml");
        conf.plans().setInputFile("test/input/playground/andreas/intersection/fourways/plans_uturn.xml.gz");
        String newLSADef = "test/input/playground/andreas/intersection/fourways/lsa.xml";
        String newLSADefCfg = "test/input/playground/andreas/intersection/fourways/lsa_config.xml";
        ScenarioImpl data = new ScenarioImpl(conf);
        Events events = new Events();
        events.addHandler(this);
        try {
            this.writer = IOUtils.getBufferedWriter("temp.txt.gz", true);
            new QSim(events, data.getPopulation(), data.getNetwork(), false, newLSADef, newLSADefCfg).run();
            this.writer.flush();
            this.writer.close();
            assertEquals(CRCChecksum.getCRCFromFile("temp.txt.gz"), CRCChecksum.getCRCFromFile("test/input/playground/andreas/intersection/fourways/reference_uturn.txt.gz"));
            new File("temp.txt.gz").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(LinkEnterEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(LinkLeaveEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset(@SuppressWarnings("unused") int iteration) {
    }

    public void handleEvent(ActEndEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(ActStartEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(AgentArrivalEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(AgentDepartureEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(AgentWait2LinkEvent event) {
        try {
            this.writer.write(event.toString());
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
