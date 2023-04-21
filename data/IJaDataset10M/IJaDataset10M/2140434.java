package playground.christoph.withinday.replanning.replanners;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.ptproject.qsim.agents.WithinDayAgent;
import playground.christoph.withinday.replanning.replanners.InitialReplanner;
import playground.christoph.withinday.replanning.replanners.interfaces.WithinDayInitialReplanner;

public class InitialReplanner extends WithinDayInitialReplanner {

    InitialReplanner(Id id, Scenario scenario) {
        super(id, scenario);
    }

    public boolean doReplanning(WithinDayAgent withinDayAgent) {
        if (this.routeAlgo == null) return false;
        if (withinDayAgent == null) return false;
        Person person = withinDayAgent.getPerson();
        routeAlgo.run(person.getSelectedPlan());
        return true;
    }
}
