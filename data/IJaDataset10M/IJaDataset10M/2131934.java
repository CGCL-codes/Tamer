package playground.mrieser;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.router.TransitRouter;
import org.matsim.pt.router.TransitRouterConfig;
import org.matsim.pt.router.TransitRouterFactory;
import org.matsim.pt.router.TransitRouterImpl;
import org.matsim.pt.router.TransitRouterNetwork;
import org.matsim.pt.router.TransitRouterNetworkTravelTimeAndDisutility;
import org.matsim.pt.transitSchedule.api.TransitSchedule;

public class DemoPeter {

    public static void main(final String[] args) {
        Config config = ConfigUtils.loadConfig(args[0]);
        Scenario scenario = ScenarioUtils.createScenario(config);
        Controler controler = new Controler(scenario);
        TransitRouterConfig routerConfig = new TransitRouterConfig(config.planCalcScore(), config.plansCalcRoute(), config.transitRouter(), config.vspExperimental());
        routerConfig.searchRadius = 2000;
        MyTransitRouterFactory transitRouterFactory = new MyTransitRouterFactory(((ScenarioImpl) scenario).getTransitSchedule(), routerConfig);
        controler.setTransitRouterFactory(transitRouterFactory);
        controler.run();
    }

    public static class MyTransitRouterFactory implements TransitRouterFactory {

        private final TransitSchedule schedule;

        private final TransitRouterConfig config;

        private final TransitRouterNetwork routerNetwork;

        public MyTransitRouterFactory(final TransitSchedule schedule, final TransitRouterConfig config) {
            this.schedule = schedule;
            this.config = config;
            this.routerNetwork = TransitRouterNetwork.createFromSchedule(this.schedule, this.config.beelineWalkConnectionDistance);
        }

        @Override
        public TransitRouter createTransitRouter() {
            MyTransitRouterNetworkTravelTimeCost ttcalc = new MyTransitRouterNetworkTravelTimeCost(this.config);
            return new TransitRouterImpl(this.config, this.routerNetwork, ttcalc, ttcalc);
        }
    }

    public static class MyTransitRouterNetworkTravelTimeCost extends TransitRouterNetworkTravelTimeAndDisutility {

        public MyTransitRouterNetworkTravelTimeCost(final TransitRouterConfig config) {
            super(config);
        }

        @Override
        public double getLinkTravelDisutility(final Link link, final double time) {
            return super.getLinkTravelDisutility(link, time);
        }
    }
}
