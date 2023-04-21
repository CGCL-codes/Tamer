package br.upe.dsc.caeto.simulation;

import br.upe.dsc.caeto.core.application.Application;

public class ExaustiveSimulation extends Simulation {

    private SimulationContext context;

    public ExaustiveSimulation(Application application) {
        super(application);
        context = SimulationContext.getInstance();
    }

    public void start() {
    }
}
