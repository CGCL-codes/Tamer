package org.amse.bomberman.client.viewmanager.states.impl;

import org.amse.bomberman.client.control.Controller;
import org.amse.bomberman.client.view.DialogState;
import org.amse.bomberman.client.view.wizard.Wizard;
import org.amse.bomberman.client.viewmanager.State;
import org.amse.bomberman.client.viewmanager.ViewManager;

/**
 * Basic implementation of State interface.
 *
 * @author Kirilchuk V.E.
 */
public abstract class AbstractState implements State {

    protected final ViewManager machine;

    protected State previous;

    protected State next;

    public AbstractState(ViewManager machine) {
        this.machine = machine;
    }

    @Override
    public State setPrevious(State previous) {
        this.previous = previous;
        return this;
    }

    @Override
    public State setNext(State next) {
        this.next = next;
        return this;
    }

    Wizard getWizard() {
        return machine.getWizard();
    }

    Controller getController() {
        return machine.getController();
    }

    /**
     * Override this if you wan`t to release some resourses before changing
     * state. This method is used by ViewManager and must not be called
     * directly in state to release resourses before calling viewManager.setState()
     */
    @Override
    public void release() {
    }
}
