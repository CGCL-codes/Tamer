package org.jage.action;

/**
 * The interface of actions created by agents, passed on to local environment
 * and executed by aggregates.
 * 
 * @author KrzS
 */
public interface IActionContext {

    /**
	 * Returns information about the action in string format.
	 */
    public String toString();
}
