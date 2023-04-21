package net.boogie.calamari.genetic.event;

import net.boogie.calamari.genetic.engine.Simulator;
import net.boogie.calamari.genetic.model.IGenome;
import net.boogie.calamari.genetic.model.IPopulation;

public class PioneerEvent extends SimulatorEvent {

    public static final String EVENT_PIONEER = "pioneer";

    public PioneerEvent(Simulator simulator, IPopulation newPopulation, IGenome refugee) {
        super(simulator, EVENT_PIONEER, newPopulation, refugee);
    }

    public IPopulation getNewPopulation() {
        return (IPopulation) getContextMap().get("0");
    }

    public IGenome getRefugee() {
        return (IGenome) getContextMap().get("1");
    }

    @Override
    public String getContextString() {
        StringBuffer buf = new StringBuffer();
        buf.append("refugee=").append(getRefugee());
        return buf.toString();
    }
}
