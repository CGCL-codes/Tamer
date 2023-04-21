package org.matsim.ptproject.qsim;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.ptproject.qsim.netsimengine.ParallelQSimEngineFactory;

public class ParallelQSimulation extends QSim {

    public ParallelQSimulation(final Scenario scenario, final EventsManager eventsManager) {
        super(scenario, eventsManager, new ParallelQSimEngineFactory());
    }
}
