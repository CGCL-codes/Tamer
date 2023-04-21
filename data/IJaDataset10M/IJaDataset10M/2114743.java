package org.matsim.contrib.evacuation.riskaversion;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.contrib.evacuation.riskaversion.RiskCostFromNetworkChangeEvents;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.network.NetworkChangeEvent.ChangeType;
import org.matsim.core.network.NetworkChangeEvent.ChangeValue;
import org.matsim.core.network.NetworkFactoryImpl;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.network.TimeVariantLinkFactory;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.misc.Time;
import org.matsim.testcases.MatsimTestCase;

public class RiskCostFromNetworkChangeEventsTest extends MatsimTestCase {

    public void testRiskCostCalculatorNoCostsForEqualRiskLinks() {
        NetworkImpl net = NetworkImpl.createNetwork();
        NetworkFactoryImpl nf = new NetworkFactoryImpl(net);
        nf.setLinkFactory(new TimeVariantLinkFactory());
        net.setFactory(nf);
        Node n0 = net.createAndAddNode(new IdImpl(0), new CoordImpl(0., 0.));
        Node n1 = net.createAndAddNode(new IdImpl(1), new CoordImpl(0., 1.));
        Node n2 = net.createAndAddNode(new IdImpl(2), new CoordImpl(1., 0.));
        Node n3 = net.createAndAddNode(new IdImpl(3), new CoordImpl(1., 1.));
        Node n4 = net.createAndAddNode(new IdImpl(4), new CoordImpl(.5, .5));
        Link l0 = net.createAndAddLink(new IdImpl(0), n0, n1, 10., 5., 8., 5.4321);
        Link l1 = net.createAndAddLink(new IdImpl(1), n1, n2, 10., 5., 8., 5.4321);
        Link l2 = net.createAndAddLink(new IdImpl(2), n2, n3, 10., 5., 8., 5.4321);
        Link l3 = net.createAndAddLink(new IdImpl(3), n3, n4, 10., 5., 8., 5.4321);
        Link l4 = net.createAndAddLink(new IdImpl(4), n4, n0, 2.5, 5., 8., 5.4321);
        List<NetworkChangeEvent> nc = new ArrayList<NetworkChangeEvent>();
        NetworkChangeEvent e0 = nf.createNetworkChangeEvent(3 * 3600 + 60 * 20);
        e0.addLink(l0);
        e0.addLink(l1);
        e0.setFreespeedChange(new ChangeValue(ChangeType.ABSOLUTE, 0.));
        nc.add(e0);
        NetworkChangeEvent e1 = nf.createNetworkChangeEvent(3 * 3600 + 60 * 30);
        e1.addLink(l1);
        e1.addLink(l2);
        e1.setFreespeedChange(new ChangeValue(ChangeType.ABSOLUTE, 0.));
        nc.add(e1);
        NetworkChangeEvent e2 = nf.createNetworkChangeEvent(3 * 3600 + 60 * 10);
        e2.addLink(l2);
        e2.setFreespeedChange(new ChangeValue(ChangeType.ABSOLUTE, 0.));
        nc.add(e2);
        net.setNetworkChangeEvents(nc);
        RiskCostFromNetworkChangeEvents rcc = new RiskCostFromNetworkChangeEvents(net, false, ((EventsManager) EventsUtils.createEventsManager()));
        assertEquals("Risk cost:", 0., rcc.getLinkTravelDisutility(l0, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 107400. * 10., rcc.getLinkTravelDisutility(l1, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 0., rcc.getLinkTravelDisutility(l2, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 0., rcc.getLinkTravelDisutility(l3, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 106800. * 2.5, rcc.getLinkTravelDisutility(l4, Time.UNDEFINED_TIME));
    }

    public void testRiskCostCalculatorChargeEqualRiskLinks() {
        NetworkImpl net = NetworkImpl.createNetwork();
        NetworkFactoryImpl nf = new NetworkFactoryImpl(net);
        nf.setLinkFactory(new TimeVariantLinkFactory());
        net.setFactory(nf);
        Node n0 = net.createAndAddNode(new IdImpl(0), new CoordImpl(0., 0.));
        Node n1 = net.createAndAddNode(new IdImpl(1), new CoordImpl(0., 1.));
        Node n2 = net.createAndAddNode(new IdImpl(2), new CoordImpl(1., 0.));
        Node n3 = net.createAndAddNode(new IdImpl(3), new CoordImpl(1., 1.));
        Node n4 = net.createAndAddNode(new IdImpl(4), new CoordImpl(.75, .75));
        Node n5 = net.createAndAddNode(new IdImpl(5), new CoordImpl(.25, .25));
        Link l0 = net.createAndAddLink(new IdImpl(0), n0, n1, 2.5, 5., 8., 5.4321);
        Link l1 = net.createAndAddLink(new IdImpl(1), n1, n2, 3.5, 5., 8., 5.4321);
        Link l2 = net.createAndAddLink(new IdImpl(2), n2, n3, 4.5, 5., 8., 5.4321);
        Link l3 = net.createAndAddLink(new IdImpl(3), n3, n4, 10., 5., 8., 5.4321);
        Link l4 = net.createAndAddLink(new IdImpl(4), n4, n5, 10., 5., 8., 5.4321);
        Link l5 = net.createAndAddLink(new IdImpl(5), n5, n0, 5.5, 5., 8., 5.4321);
        List<NetworkChangeEvent> nc = new ArrayList<NetworkChangeEvent>();
        NetworkChangeEvent e0 = nf.createNetworkChangeEvent(3 * 3600 + 60 * 20);
        e0.addLink(l0);
        e0.addLink(l1);
        e0.setFreespeedChange(new ChangeValue(ChangeType.ABSOLUTE, 0.));
        nc.add(e0);
        NetworkChangeEvent e1 = nf.createNetworkChangeEvent(3 * 3600 + 60 * 30);
        e1.addLink(l1);
        e1.addLink(l2);
        e1.setFreespeedChange(new ChangeValue(ChangeType.ABSOLUTE, 0.));
        nc.add(e1);
        NetworkChangeEvent e2 = nf.createNetworkChangeEvent(3 * 3600 + 60 * 10);
        e2.addLink(l2);
        e2.setFreespeedChange(new ChangeValue(ChangeType.ABSOLUTE, 0.));
        nc.add(e2);
        net.setNetworkChangeEvents(nc);
        RiskCostFromNetworkChangeEvents rcc = new RiskCostFromNetworkChangeEvents(net, true, ((EventsManager) EventsUtils.createEventsManager()));
        assertEquals("Risk cost:", 106800. * 2.5, rcc.getLinkTravelDisutility(l0, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 107400. * 3.5, rcc.getLinkTravelDisutility(l1, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 107400. * 4.5, rcc.getLinkTravelDisutility(l2, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 0., rcc.getLinkTravelDisutility(l3, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 0., rcc.getLinkTravelDisutility(l4, Time.UNDEFINED_TIME));
        assertEquals("Risk cost:", 106800. * 5.5, rcc.getLinkTravelDisutility(l5, Time.UNDEFINED_TIME));
    }
}
