package com.customwars.client.action;

import com.customwars.client.ui.state.InGameContext;

/**
 * An action that is fired after a delay
 * Subclasses should set actionCompleted to true when the action has finished.
 *
 * Subclass methods are invoked in this order:
 * init(inGameContext)
 * invokeAction ()
 *
 * @author stefan
 */
public abstract class DelayedAction extends AbstractCWAction {

    private int time;

    private final int delay;

    private boolean running;

    protected DelayedAction(String name, int delay) {
        super(name);
        this.delay = delay;
    }

    public final void invoke(InGameContext context) {
        if (!running) {
            init(context);
            running = true;
        }
    }

    /**
   * Init the delayed action, called once before updating this action
   */
    protected abstract void init(InGameContext inGameContext);

    public final void update(int elapsedTime) {
        if (running) {
            time += elapsedTime;
            if (time >= delay) {
                updateAction();
                time = 0;
            }
        }
    }

    /**
   * Keep invoking the action until it is complete
   */
    public final void updateAction() {
        if (!isCompleted()) invokeAction();
        if (isCompleted()) {
            running = false;
        }
    }

    /**
   * This method is called every time
   * the delay has passed and the action is not fully completed
   */
    protected abstract void invokeAction();
}
