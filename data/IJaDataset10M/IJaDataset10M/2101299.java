package wilos.presentation.assistant.view.panels;

import java.awt.Color;
import java.util.HashMap;
import java.util.Observable;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.spem2.task.Step;
import wilos.presentation.assistant.control.WizardControler;
import wilos.presentation.assistant.view.htmlViewer.HTMLViewer;
import wilos.utils.Constantes;

public class WizardStateMachine extends Observable {

    public static final int STATE_NOTHING = 0;

    public static final int STATE_PARTICIPANT = 1;

    public static final int STATE_TASK_STARTED = 2;

    public static final int STATE_TASK_READY = 3;

    public static final int STATE_TASK_CREATED = 4;

    public static final int STATE_TASK_SUSPENDED = 5;

    public static final int STATE_TASK_FINISHED = 6;

    public static final int STATE_STEP_CREATED = 7;

    public static final int STATE_STEP_READY = 8;

    public static final int STATE_STEP_FINISHED = 9;

    public static final Color COLOR_STARTED = Color.decode("#008800");

    public static final Color COLOR_READY = Color.decode("#3333FF");

    public static final Color COLOR_SUSPENDED = Color.decode("#FF6600");

    public static final Color COLOR_FINISHED = Color.decode("#FF0000");

    public static final Color COLOR_CREATED = Color.black;

    private static int idSteps = 0;

    private int currentState = 0;

    private HashMap<String, Integer> stepState = new HashMap<String, Integer>();

    private static WizardStateMachine wsm = null;

    private WizardStateMachine() {
    }

    /**
	 * give an id for the steps
	 * 
	 * @return the id
	 */
    public static int getIdForStep() {
        return ++idSteps;
    }

    /**
	 * Add a Step and its state.
	 * 
	 * @param s
	 *            step to add
	 * @param state
	 *            state of the Step
	 */
    public void addStep(Step s, int state) {
        this.stepState.put(s.getGuid(), new Integer(state));
    }

    public void deleteStep(Step s) {
        this.stepState.remove(s.getGuid());
    }

    /**
	 * Change the state of the given Step.
	 * 
	 * @param s
	 *            step to change
	 * @param state
	 *            new state of the step
	 */
    public void changeStepState(Step s, int state) {
        if (this.stepState.containsKey(s.getGuid())) {
            this.stepState.put(s.getGuid(), new Integer(state));
            this.currentState = state;
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
	 * Check the current state of the given Step.
	 * 
	 * @param s
	 *            Step to check
	 * @return state of the Step
	 */
    public int getStepState(Step s) {
        return this.stepState.get(s.getGuid()).intValue();
    }

    /**
	 * Delete all the Step of the Step state list.
	 */
    public void deleteAllStep() {
        this.stepState.clear();
    }

    public static WizardStateMachine getInstance() {
        if (wsm == null) {
            wsm = new WizardStateMachine();
        }
        return wsm;
    }

    public void setFocusedObject(Object object, HTMLViewer h) {
        if (h == null) {
            h = WizardControler.getInstance().getDefaultHTML(null);
        }
        WizardControler.getInstance().setLastCtd(object);
        if (object instanceof ConcreteTaskDescriptor) {
            ConcreteTaskDescriptor ctd = (ConcreteTaskDescriptor) object;
            if (ctd.getState().equals(Constantes.State.STARTED)) {
                updateState(STATE_TASK_STARTED);
            } else if (ctd.getState().equals(Constantes.State.READY)) {
                updateState(STATE_TASK_READY);
            } else if (ctd.getState().equals(Constantes.State.CREATED)) {
                updateState(STATE_TASK_CREATED);
            } else if (ctd.getState().equals(Constantes.State.SUSPENDED)) {
                updateState(STATE_TASK_SUSPENDED);
            } else if (ctd.getState().equals(Constantes.State.FINISHED)) {
                updateState(STATE_TASK_FINISHED);
            } else {
                updateState(STATE_NOTHING);
            }
        } else if (object instanceof Step) {
            Step s = (Step) object;
            switch(getStepState(s)) {
                case STATE_STEP_CREATED:
                    updateState(STATE_STEP_CREATED);
                    break;
                case STATE_STEP_FINISHED:
                    updateState(STATE_STEP_FINISHED);
                    break;
                case STATE_STEP_READY:
                    updateState(STATE_STEP_READY);
                    break;
            }
        } else {
            updateState(STATE_NOTHING);
        }
        if (WizardControler.getInstance().isShowInfo() || h != WizardControler.getInstance().getDefaultHTML(null)) {
            h.viewObject(object);
        }
    }

    /**
	 * maj notify all the observers that there was an update
	 */
    public void maj() {
        this.setChanged();
        notifyObservers();
    }

    private void updateState(int newState) {
        this.currentState = newState;
        maj();
    }

    public int getCurrentState() {
        return currentState;
    }
}
