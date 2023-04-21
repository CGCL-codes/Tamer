package org.matsim.contrib.freight.controler;

import java.util.Collection;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.contrib.freight.mobsim.CarrierAgentTracker;
import org.matsim.contrib.freight.mobsim.FreightAgentSource;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.events.SynchronizedEventsManagerImpl;
import org.matsim.core.mobsim.framework.Mobsim;
import org.matsim.core.mobsim.framework.MobsimFactory;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.agents.DefaultAgentFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.DefaultQSimEngineFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.ParallelQNetsimEngineFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zilske
 * Date: 10/31/11
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class City2000WQSimFactory implements MobsimFactory {

    private CarrierAgentTracker carrierAgentTracker;

    public City2000WQSimFactory(CarrierAgentTracker carrierAgentTracker) {
        this.carrierAgentTracker = carrierAgentTracker;
    }

    @Override
    public Mobsim createMobsim(Scenario sc, EventsManager eventsManager) {
        QSimConfigGroup conf = sc.getConfig().getQSimConfigGroup();
        if (conf == null) {
            throw new NullPointerException("There is no configuration set for the QSim. Please add the module 'qsim' to your config file.");
        }
        int numOfThreads = conf.getNumberOfThreads();
        if (numOfThreads > 1) {
            SynchronizedEventsManagerImpl em = new SynchronizedEventsManagerImpl(eventsManager);
            QSim sim = QSim.createQSimAndAddAgentSource(sc, em, new ParallelQNetsimEngineFactory());
            return sim;
        } else {
            final QSim sim = QSim.createQSimAndAddAgentSource(sc, eventsManager, new DefaultQSimEngineFactory());
            Collection<Plan> plans = carrierAgentTracker.createPlans();
            sim.addAgentSource(new FreightAgentSource(plans, new DefaultAgentFactory(sim), sim));
            return sim;
        }
    }
}
