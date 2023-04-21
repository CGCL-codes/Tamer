package org.echarts;

import java.io.Serializable;

/**
   Abstract superclass representing a max priority enabled transition
   for a machine.
*/
abstract class EnabledTransition implements Serializable {

    public static final long serialVersionUID = 1;

    static final String rcsid = "$Name:  $ $Id: EnabledTransition.java 556 2007-05-18 19:06:37Z gwbond $";

    /**
	   The machine in which the transition is defined.
	*/
    final TransitionMachine machine;

    /**
	   The enabled transition.  
	*/
    final Transition transition;

    /**
	   The satisfied target for this transition
	*/
    final BasicTransitionTarget target;

    /**
	   Nesting depth of machine relative to top-level machine. Top
	   level is 0.
	*/
    final int depth;

    /**
	   Source configuration of transition relative to the top level
	   machine. This is used to compute the relative priority of an
	   enabled transition.
	*/
    StateConfiguration relativeSource;

    /**
	   Target configuration of transition relative to the top level
	   machine. This is used to determine whether or not to deactivate
	   a timed transition after a transition fires.
	*/
    StateConfiguration relativeTarget;

    EnabledTransition(TransitionMachine machine, Transition transition, BasicTransitionTarget target, StateConfiguration relativeSource, StateConfiguration relativeTarget, int depth) {
        this.machine = machine;
        this.transition = transition;
        this.target = target;
        this.relativeSource = relativeSource;
        this.relativeTarget = relativeTarget;
        this.depth = depth;
    }
}
