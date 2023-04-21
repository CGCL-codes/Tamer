package playground.christoph.energyflows.replanning;

import java.util.Random;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.replanning.PlanStrategy;
import org.matsim.core.replanning.PlanStrategyImpl;
import org.matsim.core.replanning.StrategyManager;
import org.matsim.core.replanning.modules.ReRoute;
import org.matsim.core.replanning.selectors.ExpBetaPlanSelector;
import org.matsim.core.replanning.selectors.RandomPlanSelector;

/**
 *	A subset of the switzerland population are transit agents. They can by identified by
 *	their id (> 1000000000). Their departure times and transport mode is already defined
 *	and should not be changed by a replanning module. The only replanning that we want
 *	to be allowed is re-routing. If another module is selected, we replace it with
 *	"KeepSelected".
 *	
 *	I do not see a way to identify the replanning type of a PlanStrategy. Therefore we
 *	create our own rerouting PlanStrategy.
 * 
 *	@author cdobler
 */
public class TransitStrategyManager extends StrategyManager {

    private PlanStrategy reroutingStrategy;

    private PlanStrategy expBetaSelectorStrategy;

    private double reroutingShare;

    private Random random;

    public TransitStrategyManager(Controler controler, double replanningShare) {
        reroutingStrategy = new PlanStrategyImpl(new RandomPlanSelector());
        reroutingStrategy.addStrategyModule(new ReRoute(controler));
        expBetaSelectorStrategy = new PlanStrategyImpl(new ExpBetaPlanSelector(controler.getConfig().planCalcScore()));
        this.reroutingShare = replanningShare;
        this.random = MatsimRandom.getLocalInstance();
    }

    @Override
    public void beforePopulationRunHook(Population population) {
        this.reroutingStrategy.init();
        this.expBetaSelectorStrategy.init();
    }

    @Override
    public void afterRunHook(Population population) {
        this.reroutingStrategy.finish();
        this.expBetaSelectorStrategy.finish();
    }

    @Override
    public PlanStrategy chooseStrategy(final Person person) {
        int id = Integer.valueOf(person.getId().toString());
        if (id > 1000000000) {
            double rnd = random.nextDouble();
            if (rnd <= reroutingShare) return reroutingStrategy; else return expBetaSelectorStrategy;
        } else return super.chooseStrategy(person);
    }
}
