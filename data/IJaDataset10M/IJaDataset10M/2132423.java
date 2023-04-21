package common.state;

import java.util.TimerTask;

/**
 * This class is used to change states once a timer
 * has expired.
 * 
 *  @see StateMachine#changeState(State, State, long, StateChangeTimeoutHandler)
 */
public class StateChangeTimerTask extends TimerTask {

    private State newState;

    private StateMachine machine;

    private StateChangeTimeoutHandler handler;

    /**
	 * Creates a new timer task that, when executed, will call 
	 * change the state of the machine.
	 * 
	 * @param newState the state to potentially move to
	 * @param machine the state machine to alter
	 * @param handler the object who's <code>handleTimeout()</code>
	 *                method should be called upon state change
	 */
    protected StateChangeTimerTask(State newState, StateMachine machine, StateChangeTimeoutHandler handler) {
        this.newState = newState;
        this.machine = machine;
        this.handler = handler;
    }

    @Override
    public void run() {
        machine.changeState(newState);
        handler.handleTimeout();
    }
}
