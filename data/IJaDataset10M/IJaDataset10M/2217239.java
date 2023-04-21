package org.frapuccino.action;

/**
 * This class subclasses AbstractColumbaAction to encapsulate a selection state.
 * State changes are propagated to registered PropertyChangeListener instances.
 *
 * @author fdietz
 */
public abstract class AbstractSelectableAction extends AbstractFAction {

    protected boolean selectionState = false;

    /**
	 * Creates a new action instance with a default selection state of false.
	 */
    public AbstractSelectableAction(String name) {
        super(name);
    }

    /**
	 * Returns the action's selection state.
	 */
    public boolean getSelected() {
        return selectionState;
    }

    /**
	 * Sets the action's selection state and notifies registered listeners.
	 */
    public void setSelected(boolean state) {
        if (this.selectionState != state) {
            Boolean oldValue = this.selectionState ? Boolean.TRUE : Boolean.FALSE;
            this.selectionState = state;
            firePropertyChange("selected", oldValue, state ? Boolean.TRUE : Boolean.FALSE);
        }
    }
}
