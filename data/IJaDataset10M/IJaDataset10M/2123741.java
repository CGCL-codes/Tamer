package playground.gregor.sims.socialcostII;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.NetworkRoute;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.Population;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.events.AgentMoneyEvent;
import org.matsim.core.events.AgentStuckEvent;
import org.matsim.core.events.LinkEnterEvent;
import org.matsim.core.events.handler.AgentStuckEventHandler;
import org.matsim.core.events.handler.LinkEnterEventHandler;
import org.matsim.core.mobsim.queuesim.QueueSimulation;
import org.matsim.core.mobsim.queuesim.events.QueueSimulationBeforeCleanupEvent;
import org.matsim.core.mobsim.queuesim.listener.QueueSimulationBeforeCleanupListener;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import org.matsim.core.utils.misc.IntegerCache;
import org.matsim.evacuation.socialcost.SocialCostCalculator;

public class SocialCostCalculatorMultiLinkII implements SocialCostCalculator, QueueSimulationBeforeCleanupListener, BeforeMobsimListener, LinkEnterEventHandler, AgentStuckEventHandler {

    private final NetworkLayer network;

    private final int binSize;

    private TravelTimeCalculator travelTimeCalculator;

    private final Population population;

    private Integer maxK;

    private final int minK;

    Map<Id, LinkInfo> linkInfos = new HashMap<Id, LinkInfo>();

    Set<Id> stuckedAgents = new HashSet<Id>();

    public SocialCostCalculatorMultiLinkII(NetworkLayer network, int binSize, TravelTimeCalculator travelTimeCalculator, Population population) {
        this.network = network;
        this.binSize = binSize;
        this.minK = (int) (3 * 3600 / (double) binSize);
        this.travelTimeCalculator = travelTimeCalculator;
        this.population = population;
    }

    public double getSocialCost(Link link, double time) {
        LinkInfo li = this.linkInfos.get(link.getId());
        if (li == null) {
            return 0.;
        }
        HashMap<Integer, LinkTimeCostInfo> ltcs = li.getLinkTimeCostInfos();
        LinkTimeCostInfo ltc = ltcs.get(getTimeBin(time));
        if (ltc == null) {
            return 0.;
        }
        return ltc.cost;
    }

    public void reset(int iteration) {
    }

    public void notifySimulationBeforeCleanup(QueueSimulationBeforeCleanupEvent e) {
        recalculateSocialCosts();
    }

    public void notifyBeforeMobsim(BeforeMobsimEvent event) {
        System.out.println("cleanup");
        this.stuckedAgents.clear();
        for (LinkInfo li : this.linkInfos.values()) {
            li.cleanUp();
        }
    }

    private void recalculateSocialCosts() {
        calcLinkTimeCosts();
        for (Person pers : this.population.getPersons().values()) {
            if (this.stuckedAgents.contains(pers.getId())) {
                continue;
            }
            Plan plan = pers.getSelectedPlan();
            List<Id> links = ((NetworkRoute) plan.getNextLeg(plan.getFirstActivity()).getRoute()).getLinkIds();
            traceAgentsRoute(links, pers.getId());
        }
    }

    private void calcLinkTimeCosts() {
        for (Link link : this.network.getLinks().values()) {
            LinkInfo li = this.linkInfos.get(link.getId());
            if (li == null) {
                continue;
            }
            int kE = this.maxK;
            for (int k = this.maxK; k >= this.minK; k--) {
                Integer kInteger = IntegerCache.getInteger(k);
                double tauAk = this.travelTimeCalculator.getLinkTravelTime(link, k * this.binSize);
                double tauAFree = Math.ceil(link.getFreespeedTravelTime(k * this.binSize)) + 1;
                if (tauAk <= tauAFree) {
                    kE = k;
                    continue;
                }
                LinkTimeCostInfo ltc = li.getLinkTimeCostInfo(kInteger);
                ltc.linkDelay = Math.max(0, (kE - k) * this.binSize - tauAFree);
            }
        }
    }

    private void traceAgentsRoute(List<Id> links, Id agentId) {
        double agentDelay = 0;
        for (int i = links.size() - 1; i >= 0; i--) {
            Id linkId = links.get(i);
            LinkInfo li = this.linkInfos.get(linkId);
            double enterTime = li.getAgentEnterTime(agentId);
            Integer timeBin = getTimeBin(enterTime);
            LinkTimeCostInfo ltc = li.getLinkTimeCostInfo(timeBin);
            agentDelay += ltc.linkDelay;
            ltc.cost += agentDelay / ltc.in;
        }
        AgentMoneyEvent e = new AgentMoneyEvent(this.maxK * this.binSize, agentId, agentDelay / -600);
        QueueSimulation.getEvents().processEvent(e);
    }

    public void handleEvent(LinkEnterEvent event) {
        LinkInfo li = getLinkInfo(event.getLinkId());
        li.incrementInFlow(getTimeBin(event.getTime()));
        li.setAgentEnterTime(event.getPersonId(), event.getTime());
        this.maxK = getTimeBin(event.getTime()) + 1;
    }

    public void handleEvent(AgentStuckEvent event) {
        this.stuckedAgents.add(event.getPersonId());
    }

    private LinkInfo getLinkInfo(Id id) {
        LinkInfo li = this.linkInfos.get(id);
        if (li == null) {
            li = new LinkInfo();
            this.linkInfos.put(id, li);
        }
        return li;
    }

    private Integer getTimeBin(double time) {
        int slice = ((int) time) / this.binSize;
        return IntegerCache.getInteger(slice);
    }

    private static class LinkInfo {

        HashMap<Integer, LinkTimeCostInfo> linkTimeCosts = new HashMap<Integer, LinkTimeCostInfo>();

        HashMap<Id, Double> agentEnterInfos = new HashMap<Id, Double>();

        public void incrementInFlow(Integer timeBin) {
            LinkTimeCostInfo ltc = this.linkTimeCosts.get(timeBin);
            if (ltc == null) {
                ltc = new LinkTimeCostInfo();
                this.linkTimeCosts.put(timeBin, ltc);
            }
            ltc.in++;
        }

        public void setAgentEnterTime(Id agentId, double enterTime) {
            this.agentEnterInfos.put(agentId, enterTime);
        }

        public double getAgentEnterTime(Id agentId) {
            return this.agentEnterInfos.get(agentId);
        }

        public LinkTimeCostInfo getLinkTimeCostInfo(Integer timeBin) {
            LinkTimeCostInfo ltc = this.linkTimeCosts.get(timeBin);
            if (ltc == null) {
                ltc = new LinkTimeCostInfo();
                this.linkTimeCosts.put(timeBin, ltc);
            }
            return ltc;
        }

        public HashMap<Integer, LinkTimeCostInfo> getLinkTimeCostInfos() {
            return this.linkTimeCosts;
        }

        public void cleanUp() {
            this.agentEnterInfos.clear();
            this.linkTimeCosts.clear();
        }
    }

    private static class LinkTimeCostInfo {

        double cost = 0;

        double linkDelay = 0;

        public int in = 0;
    }
}
