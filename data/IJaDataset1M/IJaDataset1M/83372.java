package playground.yu.newPlans;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
 * writes new Plansfile, in which every person will has 2 plans, one with type
 * "iv" and the other with type "oev", whose leg mode will be "pt" and who will
 * have only a blank <Route></Rout>
 *
 * @author ychen
 *
 */
public class NewPtWalkPlan extends NewPopulation implements PlanAlgorithm {

    private Person person;

    private final List<PlanImpl> copyPlans = new ArrayList<PlanImpl>();

    public NewPtWalkPlan(final Network network, final Population population, final String filename) {
        super(network, population, filename);
    }

    @Override
    public void run(final Person person) {
        if (Integer.parseInt(person.getId().toString()) < 1000000000) {
            this.person = person;
            for (Plan pl : person.getPlans()) {
                run(pl);
            }
            for (PlanImpl copyPlan : this.copyPlans) {
                person.addPlan(copyPlan);
            }
            this.copyPlans.clear();
        }
        this.pw.writePerson(person);
    }

    @Override
    public void run(final Plan plan) {
        PlanImpl ptPlan = new PlanImpl(this.person);
        PlanImpl walkPlan = new PlanImpl(this.person);
        List<PlanElement> actsLegs = plan.getPlanElements();
        for (int i = 0; i < actsLegs.size(); i++) {
            Object o = actsLegs.get(i);
            if (o instanceof Activity) {
                ptPlan.addActivity((Activity) o);
                walkPlan.addActivity((Activity) o);
            } else if (o instanceof Leg) {
                Leg leg = (Leg) o;
                LegImpl ptLeg = new org.matsim.core.population.LegImpl((LegImpl) leg);
                ptLeg.setMode(TransportMode.pt);
                ptLeg.setRoute(null);
                ptPlan.addLeg(ptLeg);
                LegImpl walkLeg = new org.matsim.core.population.LegImpl((LegImpl) leg);
                walkLeg.setMode(TransportMode.walk);
                walkLeg.setRoute(null);
                walkPlan.addLeg(walkLeg);
                if (!leg.getMode().equals(TransportMode.car)) {
                    leg.setRoute(null);
                    leg.setMode(TransportMode.car);
                }
            }
        }
        this.copyPlans.add(ptPlan);
        this.copyPlans.add(walkPlan);
    }

    public static void main(final String[] args) {
        Gbl.startMeasurement();
        final String netFilename = "../matsimTests/scoringTest/network.xml";
        final String plansFilename = "../matsimTests/scoringTest/plans100.xml";
        final String outputFilename = "../matsimTests/scoringTest/plans100_pt_walk.xml";
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(netFilename);
        Population population = scenario.getPopulation();
        new MatsimPopulationReader(scenario).readFile(plansFilename);
        NewPtWalkPlan npwp = new NewPtWalkPlan(network, population, outputFilename);
        npwp.run(population);
        npwp.writeEndPlans();
        System.out.println("--> Done!");
        Gbl.printElapsedTime();
        System.exit(0);
    }
}
