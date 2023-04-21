package playground.yu.travelCost;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.core.router.util.TravelMinDisutility;
import org.matsim.core.router.util.TravelTime;

/**
 * switch TravelCostCalculatorFactory evetually also PersonalizableTravelCost
 * before Replanning only with ReRoute to create diverse routes
 * 
 * @author yu
 * 
 */
public class SpeedWeightedTimeListener implements IterationStartsListener {

    public static class SpeedWeightedTravelCostCalculatorFactoryImpl implements TravelDisutilityFactory {

        public PersonalizableTravelDisutility createTravelDisutility(PersonalizableTravelTime timeCalculator, PlanCalcScoreConfigGroup cnScoringGroup) {
            return new SpeedWeightedTravelTimeCostCalculator(timeCalculator);
        }
    }

    public static class SpeedWeightedTravelTimeCostCalculator implements TravelMinDisutility, PersonalizableTravelDisutility {

        protected final TravelTime timeCalculator;

        public SpeedWeightedTravelTimeCostCalculator(final TravelTime timeCalculator) {
            this.timeCalculator = timeCalculator;
            ;
        }

        @Override
        public double getLinkTravelDisutility(final Link link, final double time) {
            double travelTime = timeCalculator.getLinkTravelTime(link, time);
            return travelTime / (link.getLength() / travelTime);
        }

        @Override
        public double getLinkMinimumTravelDisutility(final Link link) {
            return link.getLength() / link.getFreespeed() / link.getFreespeed();
        }

        @Override
        public void setPerson(Person person) {
        }
    }

    @Override
    public void notifyIterationStarts(IterationStartsEvent event) {
        Controler ctl = event.getControler();
        if (event.getIteration() > ctl.getFirstIteration()) {
            ctl.setTravelDisutilityFactory(new SpeedWeightedTravelCostCalculatorFactoryImpl());
        }
    }
}
