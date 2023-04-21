package org.scribble.model;

/**
 * This class represents the base class for all Scribble behavioural
 * activities.
 */
public abstract class Behaviour extends Activity {

    private static final long serialVersionUID = -487404402751133649L;

    /**
	 * This method returns whether the behaviour is a grouping
	 * construct.
	 * 
	 * @return Whether the behaviour is a grouping construct 
	 */
    public boolean isGroupingConstruct() {
        return (false);
    }

    /**
	 * This method returns whether the behaviour is a wait
	 * state.
	 * 
	 * @return Whether the behaviour is a wait state
	 */
    public boolean isWaitState() {
        return (false);
    }

    /**
	 * This method returns the list of roles that are
	 * associated with the behaviour.
	 * 
	 * @return The list of associated roles
	 */
    public java.util.List<Role> getAssociatedRoles() {
        return (new java.util.Vector<Role>());
    }
}
