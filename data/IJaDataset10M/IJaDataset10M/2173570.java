package org.matsim.evacuation.socialcost;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.AgentDepartureEvent;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.api.experimental.events.LinkEnterEvent;
import org.matsim.core.api.experimental.events.LinkLeaveEvent;
import org.matsim.core.api.experimental.events.handler.AgentDepartureEventHandler;
import org.matsim.core.api.experimental.events.handler.LinkEnterEventHandler;
import org.matsim.core.api.experimental.events.handler.LinkLeaveEventHandler;
import org.matsim.core.controler.events.AfterMobsimEvent;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.AfterMobsimListener;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.events.AgentMoneyEventImpl;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.router.util.TravelCost;
import org.matsim.evacuation.config.EvacuationConfigGroup;

public class SocialCostCalculatorSingleLinkII implements TravelCost, BeforeMobsimListener, AfterMobsimListener, AgentDepartureEventHandler, LinkEnterEventHandler, LinkLeaveEventHandler {

    private final Map<Id, LinkInfo> linkInfos = new HashMap<Id, LinkInfo>();

    private final EventsManager events;

    private final int timeSliceSize;

    private final SocialCostAggregator aggregator;

    private final Network network;

    private final int msaOffset;

    /**
	 * @param network
	 * @param timeSliceSize
	 * @param eventsManager
	 */
    public SocialCostCalculatorSingleLinkII(Scenario sc, EventsManager eventsManager) {
        this.events = eventsManager;
        this.timeSliceSize = sc.getConfig().travelTimeCalculator().getTraveltimeBinSize();
        this.aggregator = new SocialCostAggregator();
        this.network = sc.getNetwork();
        EvacuationConfigGroup ecg = (EvacuationConfigGroup) sc.getConfig().getModule("evacuation");
        this.msaOffset = ecg.getMSAOffset();
    }

    @Override
    public void notifyAfterMobsim(AfterMobsimEvent event) {
        int msaIteration = event.getIteration() - this.msaOffset;
        this.aggregator.updateSocialCosts(msaIteration);
    }

    @Override
    public double getLinkGeneralizedTravelCost(Link link, double time) {
        int slot = getTimeSlotIndex(time);
        double ret = this.aggregator.getSocialCosts(link.getId(), slot);
        return ret;
    }

    @Override
    public void notifyBeforeMobsim(BeforeMobsimEvent event) {
        this.linkInfos.clear();
    }

    @Override
    public void handleEvent(AgentDepartureEvent event) {
        agentEnters(event.getLinkId(), event.getTime(), event.getPersonId());
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        agentEnters(event.getLinkId(), event.getTime(), event.getPersonId());
    }

    /**
	 * @param linkId
	 * @param time
	 * @param personId
	 */
    private void agentEnters(Id linkId, double enterTime, Id personId) {
        LinkInfo li = this.linkInfos.get(linkId);
        if (li == null) {
            li = new LinkInfo(Math.ceil(((LinkImpl) this.network.getLinks().get(linkId)).getFreespeedTravelTime()));
            this.linkInfos.put(linkId, li);
        }
        AgentInfo ai = new AgentInfo(personId, enterTime);
        li.agentsOnLink.add(ai);
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        LinkInfo li = this.linkInfos.get(event.getLinkId());
        AgentInfo ai = li.agentsOnLink.poll();
        ai.exitTime = event.getTime();
        double travelTime = ai.exitTime - ai.enterTime;
        if (li.agentsOnLink.size() == 0 || travelTime <= li.freespeedTravelTime) {
            computeSocialCosts(li, event.getLinkId(), event.getTime());
        } else {
            li.agentsLeftLink.add(ai);
        }
    }

    /**
	 * 
	 * @param li
	 * @param linkId
	 * @param congestionEndtime
	 * @param freeSpeedEnterTime
	 */
    private void computeSocialCosts(LinkInfo li, Id linkId, double congestionEndTime) {
        if (li.agentsLeftLink.size() <= 0) {
            return;
        }
        int lb = getTimeSlotIndex(li.agentsLeftLink.peek().enterTime) + 1;
        int ub = getTimeSlotIndex(congestionEndTime - li.freespeedTravelTime) - 1;
        while (li.agentsLeftLink.size() > 0) {
            AgentInfo ai = li.agentsLeftLink.poll();
            double sc = congestionEndTime - ai.exitTime;
            int enterTimeSlot = getTimeSlotIndex(ai.enterTime);
            if (enterTimeSlot >= lb && enterTimeSlot <= ub) {
                this.aggregator.addSocialCosts(linkId, enterTimeSlot, sc);
            }
            double money = sc / -600;
            AgentMoneyEventImpl am = new AgentMoneyEventImpl(congestionEndTime, ai.id, money);
            this.events.processEvent(am);
        }
    }

    @Override
    public void reset(int iteration) {
    }

    private int getTimeSlotIndex(final double time) {
        int slice = ((int) time) / this.timeSliceSize;
        return slice;
    }

    private static class LinkInfo {

        Queue<AgentInfo> agentsOnLink = new ConcurrentLinkedQueue<AgentInfo>();

        Queue<AgentInfo> agentsLeftLink = new ConcurrentLinkedQueue<AgentInfo>();

        private final double freespeedTravelTime;

        public LinkInfo(double freespeedTravelTime) {
            this.freespeedTravelTime = freespeedTravelTime;
        }
    }

    private static class AgentInfo {

        final Id id;

        final double enterTime;

        double exitTime;

        public AgentInfo(Id id, double enterTime) {
            this.id = id;
            this.enterTime = enterTime;
        }
    }
}
