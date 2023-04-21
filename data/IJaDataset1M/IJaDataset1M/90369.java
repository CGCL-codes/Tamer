package org.trackplan.app.gui.problems;

import org.trackplan.app.control_table.safety_configuration.ControlTableRoute;
import org.trackplan.app.control_table.verifier.CounterExample;
import org.trackplan.app.gui.control_table.ControlTable;

/**
 * Handles the problems are related to exit signal wrong direction
 * 
 * @author James Mistry
 * 
 *
 */
public class VerifierExitSignalWrongDirectionProblem extends AbstractProblem {

    private static final String PROBLEM_TYPE_UID = CounterExample.CounterExampleType.EXIT_SIGNAL_WRONG_DIRECTION.toString();

    private ControlTableRoute targetRoute;

    public VerifierExitSignalWrongDirectionProblem(Severity severity) {
        super(severity);
    }

    public void setTargetRoute(ControlTableRoute targetRoute) {
        this.targetRoute = targetRoute;
    }

    public ControlTableRoute getTargetRoute() {
        return targetRoute;
    }

    @Override
    public String getProblemTypeUid() {
        return PROBLEM_TYPE_UID;
    }

    @Override
    public void onSelect() {
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractProblem) {
            if (((AbstractProblem) o).getProblemTypeUid().equals(PROBLEM_TYPE_UID)) {
                VerifierDerailmentProblem specObj = (VerifierDerailmentProblem) o;
                if (specObj.getTargetRoute() == getTargetRoute()) {
                    return true;
                } else {
                    return false;
                }
            } else return false;
        } else return false;
    }

    @Override
    public boolean stillExists() {
        if (!ControlTable.getRoutes().contains(targetRoute)) {
            return false;
        } else return true;
    }

    @Override
    public String getExtraDetail() {
        return "Exit signal on route " + targetRoute + " is facing in the wrong direction.";
    }

    @Override
    public String getProblemDescription() {
        return "An exit signal is facing in the wrong direction.";
    }
}
