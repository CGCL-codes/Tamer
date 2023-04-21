package playground.christoph.knowledge.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.NetworkRoute;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.PlanElement;
import org.matsim.core.network.NetworkLayer;

public class GetAllLinks {

    public ArrayList<Link> getAllLinks(NetworkLayer n) {
        return getLinks(n);
    }

    public void getAllNodes(NetworkLayer n, ArrayList<Link> links) {
        getLinks(n, links);
    }

    public ArrayList<Link> getAllLinks(Person p) {
        return getLinks(p);
    }

    public void getAllLinks(Person p, ArrayList<Link> links) {
        getLinks(p, links);
    }

    public ArrayList<Link> getAllLinks(Plan p) {
        return getLinks(p);
    }

    public void getAllLinks(Plan p, ArrayList<Link> links) {
        getLinks(p, links);
    }

    protected void getLinks(Person person, ArrayList<Link> links) {
        Plan plan = person.getSelectedPlan();
        getLinks(plan, links);
    }

    protected ArrayList<Link> getLinks(Person person) {
        Plan plan = person.getSelectedPlan();
        return getLinks(plan);
    }

    protected ArrayList<Link> getLinks(Plan plan) {
        ArrayList<Link> links = new ArrayList<Link>();
        getLinks(plan, links);
        return links;
    }

    protected void getLinks(Plan plan, ArrayList<Link> links) {
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof Activity) {
                Activity act = (Activity) pe;
                if (!links.contains(act.getLink())) links.add(act.getLink());
            }
        }
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof Leg) {
                Leg leg = (Leg) pe;
                NetworkRoute route = (NetworkRoute) leg.getRoute();
                for (Link link : route.getLinks()) {
                    if (!links.contains(link)) links.add(link);
                }
            }
        }
    }

    protected ArrayList<Link> getLinks(NetworkLayer n) {
        ArrayList<Link> links = new ArrayList<Link>();
        getLinks(n, links);
        return links;
    }

    protected void getLinks(NetworkLayer n, ArrayList<Link> links) {
        TreeMap<Id, Link> linkMap = (TreeMap<Id, Link>) n.getLinks();
        Iterator<Map.Entry<Id, Link>> linkIterator = linkMap.entrySet().iterator();
        while (linkIterator.hasNext()) {
            Map.Entry<Id, Link> nextLink = linkIterator.next();
            Link link = nextLink.getValue();
            if (!links.contains(link)) links.add(link);
        }
    }
}
