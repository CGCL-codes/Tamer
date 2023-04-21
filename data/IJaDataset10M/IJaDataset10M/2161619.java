package org.matsim.contrib.evacuation.riskaversion;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.evacuation.config.EvacuationConfigGroup;
import org.matsim.contrib.evacuation.flooding.FloodingReader;
import org.matsim.core.api.experimental.events.AgentMoneyEvent;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.api.experimental.events.handler.AgentMoneyEventHandler;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigReaderMatsimV1;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.Module;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.LinkEnterEventImpl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.misc.Time;
import org.matsim.testcases.MatsimTestCase;

public class RiskCostFromFloodingDataTest extends MatsimTestCase {

    public void testRiskCostFromFloodingData() {
        String config = getInputDirectory() + "config.xml";
        ScenarioImpl sc = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        new ConfigReaderMatsimV1(sc.getConfig()).readFile(config);
        Module m = sc.getConfig().getModule("evacuation");
        EvacuationConfigGroup ec = new EvacuationConfigGroup(m);
        sc.getConfig().getModules().put("evacuation", ec);
        new MatsimNetworkReader(sc).readFile(sc.getConfig().network().getInputFile());
        double offsetEast = ec.getSWWOffsetEast();
        double offsetNorth = ec.getSWWOffsetNorth();
        List<FloodingReader> frs = new ArrayList<FloodingReader>();
        for (int i = 0; i < ec.getSWWFileCount(); i++) {
            String netcdf = ec.getSWWRoot() + "/" + ec.getSWWFilePrefix() + i + ec.getSWWFileSuffix();
            FloodingReader fr = new FloodingReader(netcdf);
            fr.setReadTriangles(true);
            fr.setOffset(offsetEast, offsetNorth);
            frs.add(fr);
        }
        EventsManager events = (EventsManager) EventsUtils.createEventsManager();
        RiskCostFromFloodingData rcf = new RiskCostFromFloodingData(sc.getNetwork(), frs, events, ec.getBufferSize());
        double delta = Math.pow(10, -6);
        Link l0 = sc.getNetwork().getLinks().get(new IdImpl("11288"));
        double l0Cost = rcf.getLinkTravelDisutility(l0, Time.UNDEFINED_TIME);
        assertEquals(28044.9329790229, l0Cost, delta);
        Link l0Inverse = sc.getNetwork().getLinks().get(new IdImpl("111288"));
        double l0InverseCost = rcf.getLinkTravelDisutility(l0Inverse, Time.UNDEFINED_TIME);
        assertEquals(0, l0InverseCost, delta);
        Link l1 = sc.getNetwork().getLinks().get(new IdImpl("9204"));
        double l1Cost = rcf.getLinkTravelDisutility(l1, Time.UNDEFINED_TIME);
        assertEquals(124.44247096468449, l1Cost, delta);
        Link l1Inverse = sc.getNetwork().getLinks().get(new IdImpl("109204"));
        double l1InverseCost = rcf.getLinkTravelDisutility(l1Inverse, Time.UNDEFINED_TIME);
        assertEquals(0, l1InverseCost, delta);
        Link l2 = sc.getNetwork().getLinks().get(new IdImpl("6798"));
        double l2Cost = rcf.getLinkTravelDisutility(l2, Time.UNDEFINED_TIME);
        assertEquals(497.60526643476226, l2Cost, delta);
        Link l2Inverse = sc.getNetwork().getLinks().get(new IdImpl("106798"));
        double l2InverseCost = rcf.getLinkTravelDisutility(l2Inverse, Time.UNDEFINED_TIME);
        assertEquals(0, l2InverseCost, delta);
        Id id = sc.createId("agent");
        AgentPenaltyCalculator apc = new AgentPenaltyCalculator();
        events.addHandler(apc);
        events.addHandler(rcf);
        double refCost = 0.;
        events.processEvent(new LinkEnterEventImpl(0., id, l0.getId(), null));
        refCost += 28044.9329790229 / -600.;
        events.processEvent(new LinkEnterEventImpl(0., id, l0Inverse.getId(), null));
        refCost += 0.;
        events.processEvent(new LinkEnterEventImpl(0., id, l1.getId(), null));
        refCost += 124.44247096468449 / -600.;
        events.processEvent(new LinkEnterEventImpl(0., id, l1Inverse.getId(), null));
        refCost += 0.;
        events.processEvent(new LinkEnterEventImpl(0., id, l2.getId(), null));
        refCost += 497.60526643476226 / -600.;
        events.processEvent(new LinkEnterEventImpl(0., id, l2Inverse.getId(), null));
        refCost += 0.;
        assertEquals(refCost, apc.penalty, delta);
    }

    private static class AgentPenaltyCalculator implements AgentMoneyEventHandler {

        double penalty = 0.;

        public void handleEvent(AgentMoneyEvent event) {
            this.penalty += event.getAmount();
        }

        public void reset(int iteration) {
        }
    }
}
