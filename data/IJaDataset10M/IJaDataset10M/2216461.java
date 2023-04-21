package imtek.optsuite.acquisition.routine.actions;

import imtek.optsuite.acquisition.routine.MeasurementRoutineStep;

public class MoveRoutineStepDownViewAction extends MeasurementRoutineViewAction {

    public MoveRoutineStepDownViewAction() {
        super();
    }

    public void runAction() {
        MeasurementRoutineStep routineStep = view.getSelectedRoutineStep();
        if (routineStep == null) return;
        view.moveRoutineStepDown(view.getSelectedRoutine(), routineStep);
        view.refreshContent();
    }
}
