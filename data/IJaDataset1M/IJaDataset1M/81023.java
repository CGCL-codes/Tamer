package org.jacp.api.component;

import org.jacp.api.action.IAction;

/**
 * Defines a subcomponent handled by a root component. A subcomponent is running in a perspective environment an can be represented by an visible UI or non visible background component.
 * 
 * @author Andy Moncsek
 * 
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface ISubComponent<L, A, M> extends IComponent<L, A, M>, IHandleable<A, M> {

    /**
	 * Returns the target id where component will be displayed in.
	 * 
	 * @return target id
	 */
    String getExecutionTarget();

    /**
	 * Set the target id where component will be displayed in.
	 * 
	 * @param target
	 */
    void setExecutionTarget(final String target);

    /**
	 * Returns true if component has message in pipe.
	 * 
	 * @return returns true if incoming message is in queue
	 */
    boolean hasIncomingMessage();

    /**
	 * Add new message to component.
	 * 
	 * @param action
	 */
    void putIncomingMessage(final IAction<A, M> action);

    /**
	 * Returns next message in pipe.
	 * 
	 * @return the next action to handle
	 */
    IAction<A, M> getNextIncomingMessage();

    /**
	 * Component is blocked when executed in thread.
	 * 
	 * @return blocked state
	 */
    boolean isBlocked();

    /**
	 * Block component when run in thread.
	 * 
	 * @param blocked
	 */
    void setBlocked(final boolean blocked);

    /**
	 * set the id of parent component
	 * @param parentId
	 */
    void setParentId(final String parentId);

    /**
	 * returns the id of parent component
	 * @return the parent id
	 */
    String getParentId();
}
