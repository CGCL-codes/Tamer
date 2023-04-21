package playground.thibautd.jointtripsoptimizer.population;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PopulationFactory;

/**
 * @author thibautd
 */
public class PopulationOfCliquesFactory implements PopulationFactory {

    private final PopulationWithCliquesFactory factoryDelegate;

    public PopulationOfCliquesFactory(ScenarioWithCliques sc) {
        this.factoryDelegate = new PopulationWithCliquesFactory(sc);
    }

    public Clique createClique(Id id) {
        return new Clique(id);
    }

    /**
	 * @return a clique
	 */
    @Override
    public Person createPerson(Id id) {
        return this.createClique(id);
    }

    /**
	 * @return a Joint plan, with clique initialized to null
	 */
    @Override
    public Plan createPlan() {
        return new JointPlan(null);
    }

    @Override
    public Activity createActivityFromCoord(String actType, Coord coord) {
        return this.factoryDelegate.createActivityFromCoord(actType, coord);
    }

    @Override
    public Activity createActivityFromLinkId(String actType, Id linkId) {
        return this.factoryDelegate.createActivityFromLinkId(actType, linkId);
    }

    @Override
    public Leg createLeg(String legMode) {
        return this.factoryDelegate.createLeg(legMode);
    }
}
