package org.matsim.pt;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.pt.router.TransitRouterConfig;
import org.matsim.pt.router.TransitRouterImplFactory;

/**
 * @author mrieser
 */
public class TransitControlerListener implements StartupListener {

    @Override
    public void notifyStartup(final StartupEvent event) {
        final Scenario scenario = event.getControler().getScenario();
        if (event.getControler().getTransitRouterFactory() == null) {
            TransitRouterConfig transitRouterConfig = new TransitRouterConfig(scenario.getConfig().planCalcScore(), scenario.getConfig().plansCalcRoute(), scenario.getConfig().transitRouter(), scenario.getConfig().vspExperimental());
            event.getControler().setTransitRouterFactory(new TransitRouterImplFactory(scenario.getTransitSchedule(), transitRouterConfig));
        }
    }
}
