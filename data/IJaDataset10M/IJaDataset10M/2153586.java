package org.grailrtls.server.event;

import java.util.EventObject;
import org.grailrtls.server.Solver;

public class SolverEvent extends EventObject {

    public static final int RESULT_EVENT = 0;

    public final Solver solver;

    public SolverEvent(Solver solver) {
        super(solver);
        this.solver = solver;
    }
}
