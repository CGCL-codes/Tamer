package agonism.ch.biohazard.impl;

import agonism.ce.AbstractAction;
import agonism.ce.GameState;
import agonism.ce.Debug;

public class Look extends AbstractAction {

    public static int TRACE_ID = Debug.getTraceID();

    public Look(Boid boid) {
        super(boid);
    }

    public void execute(GameState state) {
        throw new RuntimeException("TODO: implement Look.execute");
    }
}
