package playground.gregor.sims.shelters.linkpenaltyII;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.events.AgentMoneyEventImpl;
import org.matsim.core.events.EventsImpl;
import org.matsim.core.events.LinkEnterEventImpl;
import org.matsim.core.events.LinkLeaveEventImpl;
import org.matsim.core.events.handler.LinkEnterEventHandler;
import org.matsim.core.events.handler.LinkLeaveEventHandler;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.network.NetworkLayer;
import org.matsim.evacuation.base.Building;

public class ShelterInputCounterLinkPenalty implements LinkLeaveEventHandler, LinkEnterEventHandler {

    private static final Logger log = Logger.getLogger(ShelterInputCounterLinkPenalty.class);

    private static final double PENALTY_COEF = 5;

    private final HashMap<Id, LinkInfo> linkInfos = new HashMap<Id, LinkInfo>();

    private final HashMap<Id, Building> shelterLinkMapping;

    private final EventsImpl events;

    private int it;

    public ShelterInputCounterLinkPenalty(NetworkLayer network, HashMap<Id, Building> shelterLinkMapping, EventsImpl events) {
        this.events = events;
        this.shelterLinkMapping = shelterLinkMapping;
        for (LinkImpl link : network.getLinks().values()) {
            if (link.getId().toString().contains("sl") && link.getId().toString().contains("b")) {
                Building b = this.shelterLinkMapping.get(link.getId());
                this.linkInfos.put(link.getId(), new LinkInfo(b.getShelterSpace(), link.getId()));
            }
        }
    }

    public double getLinkTravelCost(Link link, double time) {
        LinkInfo li = this.linkInfos.get(link.getId());
        if (li != null) {
            if (time > li.blockingTime) {
                return 30 * 3600;
            }
        }
        return 0.;
    }

    public void handleEvent(LinkEnterEventImpl event) {
        LinkInfo li = this.linkInfos.get(event.getLinkId());
        if (li != null) {
            li.count++;
            if (li.space == li.count && !li.init) {
                li.init = true;
                double n = this.it;
                li.blockingTime = event.getTime();
            }
            if (li.count > li.space) {
                double pen = 30. * 3600. / -600.;
                AgentMoneyEventImpl e = new AgentMoneyEventImpl(event.getTime(), event.getPersonId(), pen);
                this.events.processEvent(e);
            }
        }
    }

    public void handleEvent(LinkLeaveEventImpl event) {
    }

    public void reset(int iteration) {
        this.it = iteration;
        double maxOverload = 0;
        LinkInfo mxLi = null;
        for (LinkInfo li : this.linkInfos.values()) {
            if (li.space > li.count) {
                double n = this.it;
                li.blockingTime += 10.;
            }
            if (li.space < li.count) {
                li.blockingTime -= 10.;
            }
            log.info("Link:" + li.id + "  count:" + li.count + " should be at most:" + li.space + " overload:" + (double) li.count / li.space + " blocking time:" + (li.blockingTime - 3 * 3600));
            if ((double) li.count / li.space > maxOverload) {
                maxOverload = (double) li.count / li.space;
                mxLi = li;
            }
            li.count = 0;
        }
        if (mxLi != null) {
            log.info("MAX  Link:" + mxLi.id + "  count:" + mxLi.count + " should be at most:" + mxLi.space + " overload:" + maxOverload);
        }
    }

    private static class LinkInfo {

        public boolean init = false;

        double blockingTime = 30 * 3600;

        int count = 0;

        final int space;

        final Id id;

        public LinkInfo(int shelterSpace, Id id) {
            this.id = id;
            this.space = shelterSpace;
        }
    }
}
