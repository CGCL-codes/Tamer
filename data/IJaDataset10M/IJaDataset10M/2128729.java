package playground.christoph.basicmobsim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import org.apache.log4j.Logger;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.network.Node;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.NetworkRoute;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.PlanElement;
import org.matsim.core.api.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.events.Events;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.mobsim.queuesim.Simulation;
import org.matsim.core.network.NetworkLayer;

public class BasicSimulation {

    private final Config config;

    protected final Population plans;

    protected NetworkLayer networkLayer;

    protected static Events events = null;

    /**
	 * Includes all vehicle that have transportation modes unknown to
	 * the QueueSimulation (i.e. != "car") or have two activities on the same link
 	 */
    private static final Logger log = Logger.getLogger(BasicSimulation.class);

    public BasicSimulation(final NetworkLayer network, final Population plans, final Events events) {
        Simulation.reset();
        this.config = Gbl.getConfig();
        setEvents(events);
        this.plans = plans;
        this.networkLayer = network;
    }

    protected void simulatePerson(Person person) {
        log.info("Simulating Person " + person.getId());
        Plan plan = person.getSelectedPlan();
        Random random = new Random(Long.valueOf(person.getId().toString()));
        Activity act = null;
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof Activity) {
                act = (Activity) pe;
            } else if (pe instanceof Leg) {
                ArrayList<Node> routeNodes = new ArrayList<Node>();
                Leg leg = (Leg) pe;
                Activity nextAct = plan.getNextActivity(leg);
                Link destinationLink = nextAct.getLink();
                Link currentLink = act.getLink();
                Node currentNode = currentLink.getToNode();
                while (!currentLink.equals(destinationLink)) {
                    routeNodes.add(currentLink.getToNode());
                    log.info("Current Link: " + currentLink.getId() + " Destination Link: " + destinationLink.getId());
                    Object[] links = currentNode.getOutLinks().values().toArray();
                    int linkCount = links.length;
                    if (linkCount == 0) {
                        log.error("Node has no outgoing links - Stopped search!");
                        break;
                    }
                    int nextLink = random.nextInt(linkCount);
                    if (links[nextLink] instanceof Link) {
                        currentLink = (Link) links[nextLink];
                        currentNode = currentLink.getToNode();
                    } else {
                        log.error("Return object was not from type Link! Class " + links[nextLink] + " was returned!");
                        break;
                    }
                }
                ((NetworkRoute) leg.getRoute()).setNodes(routeNodes);
            }
        }
    }

    public final void run() {
        Collection<Person> persons = plans.getPersons().values();
        Iterator personIterator = persons.iterator();
        while (personIterator.hasNext()) {
            this.simulatePerson((Person) personIterator.next());
        }
        cleanupSim();
    }

    /**
	 * Close any files, etc.
	 */
    protected void cleanupSim() {
        BasicSimulation.events = null;
    }

    public static final Events getEvents() {
        return events;
    }

    private static final void setEvents(final Events events) {
        BasicSimulation.events = events;
    }
}
