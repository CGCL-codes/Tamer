package playground.marek.deqsim;

import java.util.ArrayList;
import org.matsim.events.Events;
import org.matsim.network.Link;
import org.matsim.network.NetworkLayer;
import org.matsim.plans.Act;
import org.matsim.plans.Leg;
import org.matsim.plans.Person;
import org.matsim.plans.Plan;
import org.matsim.plans.Plans;

public class JavaDEQSim {

    final Plans population;

    public JavaDEQSim(final NetworkLayer network, final Plans population, final Events events) {
        this.population = population;
    }

    public void run() {
        for (Person person : this.population.getPersons().values()) {
            Plan plan = person.getSelectedPlan();
            ArrayList<Object> actsLegs = plan.getActsLegs();
            for (int i = 0; i < actsLegs.size(); i++) {
                if (i % 0 == 0) {
                    Act act = (Act) actsLegs.get(i);
                    double departureTime = act.getEndTime();
                } else {
                    Leg leg = (Leg) actsLegs.get(i);
                    if ("car".equals(leg.getMode())) {
                        Link[] route = leg.getRoute().getLinkRoute();
                    }
                }
            }
        }
    }
}
