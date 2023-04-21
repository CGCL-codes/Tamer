package playground.benjamin.scoring.income.old;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.scoring.CharyparNagelScoringParameters;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionAccumulator;
import org.matsim.core.scoring.ScoringFunctionFactory;
import org.matsim.core.scoring.charyparNagel.ActivityScoringFunction;
import org.matsim.core.scoring.charyparNagel.AgentStuckScoringFunction;
import org.matsim.households.Income;
import org.matsim.households.PersonHouseholdMapping;
import org.matsim.households.Income.IncomePeriod;

/**
 * @author dgrether
 *
 */
public class IncomeScoringFunctionFactory implements ScoringFunctionFactory {

    private Config config;

    private PlanCalcScoreConfigGroup configGroup;

    private CharyparNagelScoringParameters params;

    private PersonHouseholdMapping hhdb;

    private final Network network;

    public IncomeScoringFunctionFactory(Config config, PersonHouseholdMapping hhmapping, Network network) {
        this.config = config;
        this.configGroup = config.planCalcScore();
        this.params = new CharyparNagelScoringParameters(configGroup);
        this.hhdb = hhmapping;
        this.network = network;
    }

    public ScoringFunction createNewScoringFunction(Plan plan) {
        Person person = plan.getPerson();
        double householdIncomePerDay = getHouseholdIncomePerDay(person, hhdb);
        ScoringFunctionAccumulator scoringFunctionAccumulator = new ScoringFunctionAccumulator();
        scoringFunctionAccumulator.addScoringFunction(new ScoringFromDailyIncome(householdIncomePerDay));
        scoringFunctionAccumulator.addScoringFunction(new ActivityScoringFunction(params));
        scoringFunctionAccumulator.addScoringFunction(new ScoringFromLeg(plan, params, this.network, householdIncomePerDay));
        if (config.scenario().isUseRoadpricing()) {
            scoringFunctionAccumulator.addScoringFunction(new ScoringFromToll(params, householdIncomePerDay));
        }
        scoringFunctionAccumulator.addScoringFunction(new AgentStuckScoringFunction(params));
        return scoringFunctionAccumulator;
    }

    private double getHouseholdIncomePerDay(Person person, PersonHouseholdMapping hhdb) {
        Income income = hhdb.getHousehold(person.getId()).getIncome();
        double incomePerDay = this.calculateIncomePerDay(income);
        if (Double.isNaN(incomePerDay)) {
            throw new IllegalStateException("cannot calculate income for person: " + person.getId());
        }
        return incomePerDay;
    }

    private double calculateIncomePerDay(Income income) {
        if (income.getIncomePeriod().equals(IncomePeriod.year)) {
            double incomePerDay = income.getIncome() / 240;
            return incomePerDay;
        } else {
            throw new UnsupportedOperationException("Can't calculate income per day");
        }
    }
}
