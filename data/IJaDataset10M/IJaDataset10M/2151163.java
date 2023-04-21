package org.hip.kernel.workflow;

/**
 * This interface is used to describe a state. 
 * A state has transitions to other states.
 * 
 * Created on 03.07.2003
 * @author Benno Luthiger
 * (inspired by itools by Juan David Ib��ez Palomar <jdavid@itaapy.com>)
 */
public interface State {

    /**
	 * Adds the transition with the specified name to the state instance.
	 * 
	 * @param inTransitionName java.lang.String
	 * @param inTransition org.hip.kernel.workflow.Transition
	 */
    public void addTransition(String inTransitionName, Transition inTransition);

    /**
	 * Returns the transition with the specified name.
	 * 
	 * @param inTransitionName java.lang.String
	 * @return org.hip.kernel.workflow.Transition
	 */
    public Transition getTransition(String inTransitionName);
}
