package org.columba.core.gui.action;

import org.columba.api.gui.frame.IFrameMediator;

/**
 * This class subclasses AbstractColumbaAction to encapsulate a selection
 * state. State changes are propagated to registered PropertyChangeListener
 * instances.
 */
public abstract class AbstractSelectableAction extends AbstractColumbaAction {

    protected boolean state = false;

    /**
     * Creates a new action instance with a default selection state of false.
     */
    public AbstractSelectableAction(IFrameMediator controller, String name) {
        super(controller, name);
    }

    /**
     * Returns the action's selection state.
     */
    public boolean getState() {
        return state;
    }

    /**
     * Sets the action's selection state and notifies registered listeners.
     */
    public void setState(boolean state) {
        if (this.state != state) {
            Boolean oldValue = this.state ? Boolean.TRUE : Boolean.FALSE;
            this.state = state;
            firePropertyChange("selected", oldValue, state ? Boolean.TRUE : Boolean.FALSE);
        }
    }
}
