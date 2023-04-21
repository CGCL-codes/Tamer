package org.matsim.ptproject.qsim.multimodalsimengine;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimFactory;
import org.matsim.core.mobsim.framework.Simulation;
import org.matsim.ptproject.qsim.QSim;
import org.matsim.ptproject.qsim.multimodalsimengine.router.util.MultiModalTravelTimeFactory;

public class MultiModalMobsimFactory implements MobsimFactory {

    private static final Logger log = Logger.getLogger(MultiModalMobsimFactory.class);

    private final MobsimFactory delegate;

    private final MultiModalTravelTimeFactory timeFactory;

    public MultiModalMobsimFactory(MobsimFactory mobsimFactory, MultiModalTravelTimeFactory timeFactory) {
        this.delegate = mobsimFactory;
        this.timeFactory = timeFactory;
    }

    @Override
    public Simulation createMobsim(Scenario sc, EventsManager eventsManager) {
        Simulation sim = delegate.createMobsim(sc, eventsManager);
        if (sim instanceof QSim) {
            log.info("Using MultiModalMobsim...");
            QSim qSim = (QSim) sim;
            MultiModalSimEngine multiModalEngine = new MultiModalSimEngineFactory().createMultiModalSimEngine(qSim, timeFactory);
            qSim.addMobsimEngine(multiModalEngine);
            qSim.addDepartureHandler(new MultiModalDepartureHandler(qSim, multiModalEngine, sc.getConfig().multiModal()));
        } else {
            log.error("Simulation Object is not from type QSim - cannot use MultiModalMobsim!");
        }
        return sim;
    }
}
