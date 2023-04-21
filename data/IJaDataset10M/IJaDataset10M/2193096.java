package playground.thibautd.herbie;

import org.matsim.pt.router.TransitRouter;
import org.matsim.pt.router.TransitRouterConfig;
import org.matsim.pt.router.TransitRouterFactory;
import org.matsim.pt.router.TransitRouterImpl;
import org.matsim.pt.router.TransitRouterNetwork;
import org.matsim.pt.transitSchedule.api.TransitSchedule;
import herbie.running.config.HerbieConfigGroup;
import herbie.running.scoring.TravelScoringFunction;

/**
 * Initialises a TransitRouterFactory, which takes into account the travel distance
 * in the same way the herbie scoring function does.
 * 
 * note: this reveal a poor separation of concerns (the travel disutility
 * implementation makes assumptions on the scoring, which can easily be broken
 * without any error)! Some way to reduce this fact should be found (use a
 * "disutilityCalculator" in both routing and scoring for example... except
 * that the would require any travel time disutility to be additive...)
 *
 * @author thibautd
 */
public class HerbieTransitRouterFactory implements TransitRouterFactory {

    private final TransitSchedule schedule;

    private final TransitRouterConfig config;

    private final TransitRouterNetwork routerNetwork;

    private final HerbieConfigGroup herbieConfig;

    private final TravelScoringFunction travelScoring;

    public HerbieTransitRouterFactory(final TransitSchedule schedule, final TransitRouterConfig config, final HerbieConfigGroup herbieConfig, final TravelScoringFunction travelScoring) {
        this.schedule = schedule;
        this.config = config;
        this.routerNetwork = TransitRouterNetwork.createFromSchedule(this.schedule, this.config.beelineWalkConnectionDistance);
        this.herbieConfig = herbieConfig;
        this.travelScoring = travelScoring;
    }

    @Override
    public TransitRouter createTransitRouter() {
        HerbieTransitTravelTimeAndDisutility ttCalculator = new HerbieTransitTravelTimeAndDisutility(herbieConfig, config, travelScoring);
        return new TransitRouterImpl(this.config, this.routerNetwork, ttCalculator, ttCalculator);
    }
}
