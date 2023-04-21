package playground.benjamin.income;

import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.TransportMode;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.Route;
import org.matsim.core.basic.v01.households.BasicIncome;
import org.matsim.core.basic.v01.households.BasicIncome.IncomePeriod;
import org.matsim.core.scoring.CharyparNagelScoringParameters;
import org.matsim.core.scoring.charyparNagel.LegScoringFunction;

/**
 * @author dgrether
 * 
 */
public class BKickLegScoring extends LegScoringFunction {

    private static final Logger log = Logger.getLogger(BKickLegScoring.class);

    private static double betaIncomeCar = 1.31;

    private static double betaIncomePt = 1.31;

    private double incomePerTrip;

    public BKickLegScoring(final Plan plan, final CharyparNagelScoringParameters params) {
        super(plan, params);
        BasicIncome income = plan.getPerson().getHousehold().getIncome();
        this.incomePerTrip = this.calculateIncomePerTrip(income);
    }

    @Override
    protected double calcLegScore(final double departureTime, final double arrivalTime, final Leg leg) {
        double tmpScore = 0.0;
        double travelTime = arrivalTime - departureTime;
        double dist = 0.0;
        if (TransportMode.car.equals(leg.getMode())) {
            Route route = leg.getRoute();
            dist = route.getDistance();
            if (Double.isNaN(dist)) {
                throw new IllegalStateException("Route distance is NaN for person: " + this.plan.getPerson().getId());
            }
            tmpScore += travelTime * this.params.marginalUtilityOfTraveling + this.params.marginalUtilityOfDistanceCar * dist * betaIncomeCar / this.incomePerTrip + betaIncomeCar * Math.log(this.incomePerTrip);
        } else if (TransportMode.pt.equals(leg.getMode())) {
            dist = leg.getRoute().getDistance();
            if (Double.isNaN(dist)) {
                throw new IllegalStateException("Route distance is NaN for person: " + this.plan.getPerson().getId());
            }
            tmpScore += travelTime * this.params.marginalUtilityOfTravelingPT + this.params.marginalUtilityOfDistancePt * dist * betaIncomePt / this.incomePerTrip + betaIncomePt * Math.log(this.incomePerTrip);
        } else {
            throw new IllegalStateException("Scoring funtion not defined for other modes than pt and car!");
        }
        if (Double.isNaN(tmpScore)) {
            throw new IllegalStateException("Leg score is NaN for person: " + this.plan.getPerson().getId());
        }
        return tmpScore;
    }

    private double calculateIncomePerTrip(BasicIncome income) {
        double ipt = Double.NaN;
        if (income.getIncomePeriod().equals(IncomePeriod.year)) {
            ipt = income.getIncome() / (240 * 3.5);
            if (Double.isNaN(ipt)) {
                throw new IllegalStateException("cannot calculate income for person: " + this.plan.getPerson().getId());
            }
        } else {
            throw new UnsupportedOperationException("Can't calculate income per trip");
        }
        return ipt;
    }
}
