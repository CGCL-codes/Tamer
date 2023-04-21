package org.jage.event;

import org.jage.action.Action;
import org.jage.address.IAgentAddress;
import org.jage.workplace.IAggregate;

/**
 * This event is created when an action is to be performed on an aggregate.
 * 
 * @author KrzS
 */
public class AgentActionEvent extends AggregateEvent {

    /**
	 * The action to perform on the aggregate.
	 */
    private final Action _action;

    /**
	 * The address of the entity which invoked the action.
	 * TODO: check if the invoker is essential 
	 */
    private final IAgentAddress _invoker;

    /**
	 * Constructor.
	 * 
	 * @param eventCreator
	 *          the aggregate which creates this event
	 * @param action
	 *          the action to perform on the aggregate
	 * @param invoker
	 *          the address of the entity which invoked the action
	 */
    public AgentActionEvent(IAggregate eventCreator, Action action, IAgentAddress invoker) {
        super(eventCreator);
        _action = action;
        _invoker = invoker;
    }

    /**
	 * Returns the action to perform on the aggregate.
	 * 
	 * @return the action to perform on the aggregate
	 */
    public Action getAction() {
        return _action;
    }

    /**
	 * Returns the address of the entity which invoked the action.
	 * 
	 * @return the address of the entity which invoked the action
	 */
    public IAgentAddress getInvoker() {
        return _invoker;
    }

    /**
	 * Add new agent action event to the events list
	 * @param event 
	 */
    public void addAgentActionEvent(AgentActionEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Cannot add null event to events tree");
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return "Agent action event [creator: " + (_parent == null ? "Workplace" : _parent.getAddress().toString()) + ", action: " + _action + ", invoker: " + _invoker + "]";
    }
}
