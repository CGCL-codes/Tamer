package playground.benjamin.old.income;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.groups.CharyparNagelScoringConfigGroup;
import org.matsim.core.router.util.PersonalizableTravelCost;
import org.matsim.core.router.util.TravelTime;
import org.matsim.households.Income;
import org.matsim.households.Income.IncomePeriod;

/**
 * @author dgrether
 *
 */
public class BKickIncomeTravelTimeDistanceCostCalculator implements PersonalizableTravelCost {

    private static final Logger log = Logger.getLogger(BKickIncomeTravelTimeDistanceCostCalculator.class);

    private static double betaIncomeCar = 1.31;

    protected TravelTime timeCalculator;

    private double travelCostFactor;

    private double marginalUtlOfDistance;

    private double income;

    public BKickIncomeTravelTimeDistanceCostCalculator(final TravelTime timeCalculator, CharyparNagelScoringConfigGroup charyparNagelScoring) {
        this.timeCalculator = timeCalculator;
        this.travelCostFactor = (-charyparNagelScoring.getTraveling() / 3600.0) + (charyparNagelScoring.getPerforming() / 3600.0);
        this.marginalUtlOfDistance = charyparNagelScoring.getMarginalUtlOfDistanceCar() * 1.31;
        log.info("Using BKickIncomeTravelTimeDistanceCostCalculator...");
    }

    /**
	 * @see org.matsim.core.router.util.TravelCost#getLinkTravelCost(org.matsim.core.network.LinkImpl, double)
	 */
    public double getLinkTravelCost(Link link, double time) {
        double travelTime = this.timeCalculator.getLinkTravelTime(link, time);
        if (this.marginalUtlOfDistance == 0.0) {
            return travelTime * this.travelCostFactor;
        }
        return travelTime * this.travelCostFactor - (this.marginalUtlOfDistance * link.getLength() / this.income);
    }

    public void setIncome(Income income) {
        if (income.getIncomePeriod().equals(IncomePeriod.year)) {
            this.income = income.getIncome() / (240 * 3.5);
        } else {
            throw new UnsupportedOperationException("Can't calculate income per trip");
        }
    }

    @Override
    public void setPerson(Person person) {
    }
}
