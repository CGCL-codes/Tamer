package org.trackplan.app.gui.problems;

import org.trackplan.app.control_table.safety_configuration.ControlTableRoute;
import org.trackplan.app.control_table.verifier.CounterExample;
import org.trackplan.app.gui.control_table.ControlTable;

/**
 * Handles the problems are related to too few tracks
 * 
 * @author James Mistry
 * 
 *
 */
public class VerifierTooFewTracksProblem extends AbstractProblem {

    private static final String PROBLEM_TYPE_UID = CounterExample.CounterExampleType.TOO_FEW_TRACK_SECTIONS.toString();

    private ControlTableRoute targetRoute;

    public VerifierTooFewTracksProblem(Severity severity) {
        super(severity);
    }

    @Override
    public String getExtraDetail() {
        return "The route " + targetRoute.getRouteName().getStoredName() + " must consist of at least 2 track segments.";
    }

    @Override
    public String getProblemDescription() {
        return "A route contains too few track segments.";
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
                VerifierBadExitSignalAttachmentProblem specObj = (VerifierBadExitSignalAttachmentProblem) o;
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

    public void setTargetRoute(ControlTableRoute targetRoute) {
        this.targetRoute = targetRoute;
    }

    public ControlTableRoute getTargetRoute() {
        return targetRoute;
    }
}
