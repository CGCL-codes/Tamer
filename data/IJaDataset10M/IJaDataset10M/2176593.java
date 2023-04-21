package playground.ciarif.flexibletransports.router;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.TravelMinDisutility;
import org.matsim.core.router.util.TravelTime;
import playground.ciarif.flexibletransports.config.FtConfigGroup;

public class FtTravelTimeDistanceCostCalculator implements TravelMinDisutility, PersonalizableTravelDisutility {

    protected final TravelTime timeCalculator;

    private final double travelCostFactor;

    private final double marginalUtlOfDistance;

    public FtTravelTimeDistanceCostCalculator(TravelTime timeCalculator, PlanCalcScoreConfigGroup cnScoringGroup, FtConfigGroup ftConfigGroup) {
        this.timeCalculator = timeCalculator;
        this.travelCostFactor = (-cnScoringGroup.getTraveling_utils_hr() / 3600.0D + cnScoringGroup.getPerforming_utils_hr() / 3600.0D);
        this.marginalUtlOfDistance = (ftConfigGroup.getDistanceCostCar() / 1000.0D * cnScoringGroup.getMonetaryDistanceCostRateCar() * cnScoringGroup.getMarginalUtilityOfMoney());
    }

    public double getLinkMinimumTravelDisutility(Link link) {
        return (link.getLength() / link.getFreespeed() * this.travelCostFactor - (this.marginalUtlOfDistance * link.getLength()));
    }

    public double getLinkTravelDisutility(Link link, double time) {
        double travelTime = this.timeCalculator.getLinkTravelTime(link, time);
        return (travelTime * this.travelCostFactor - (this.marginalUtlOfDistance * link.getLength()));
    }

    protected double getTravelCostFactor() {
        return this.travelCostFactor;
    }

    protected double getMarginalUtlOfDistance() {
        return this.marginalUtlOfDistance;
    }

    public void setPerson(Person person) {
    }
}
