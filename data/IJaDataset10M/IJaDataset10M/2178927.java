package playground.benjamin.income2;

import org.matsim.core.config.Config;
import org.matsim.core.router.util.TravelCost;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scoring.ScoringFunctionFactory;
import org.matsim.households.PersonHouseholdMapping;
import org.matsim.population.algorithms.PlanAlgorithm;
import playground.benjamin.BKickControler;

/**
 * @author dgrether
 *
 */
public class BKickIncome2Controler extends BKickControler {

    private PersonHouseholdMapping hhdb;

    public BKickIncome2Controler(String arg) {
        super(arg);
    }

    public BKickIncome2Controler(String[] args) {
        super(args);
    }

    public BKickIncome2Controler(Config config) {
        super(config);
    }

    private void setHouseholdDb(PersonHouseholdMapping hhdb) {
        this.hhdb = hhdb;
    }

    @Override
    protected void setUp() {
        this.hhdb = new PersonHouseholdMapping(this.getScenarioData().getHouseholds());
        ScoringFunctionFactory scoringFactory = new BKickIncome2ScoringFunctionFactory(this.getScenarioData().getConfig().charyparNagelScoring(), hhdb);
        setTravelCostCalculatorFactory(new Income2TravelCostCalculatorFactory());
        this.setScoringFunctionFactory(scoringFactory);
        super.setUp();
    }

    @Override
    public PlanAlgorithm getRoutingAlgorithm(final TravelCost travelCosts, final TravelTime travelTimes) {
        return new Income2PlansCalcRoute(this.config.plansCalcRoute(), this.network, travelCosts, travelTimes, this.getLeastCostPathCalculatorFactory(), this.hhdb);
    }

    public static void main(String[] args) {
        if ((args == null) || (args.length == 0)) {
            System.out.println("No argument given!");
            System.out.println("Usage: Controler config-file [dtd-file]");
            System.out.println();
        } else {
            final BKickIncome2Controler controler = new BKickIncome2Controler(args);
            controler.run();
        }
    }
}
