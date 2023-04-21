package org.echarts;

/**
   Class implementing a timed transition. Currently only a relative
   delay is supported where the lower bound on the delay duration is
   specified by a long int representing the delay in milliseconds.

   When the machine interpreter arrives in a state and a timed transition
   is defined with the same start state and the transition's guard
   condition evalutates to true, then an internal timer is started
   that counts down from the delay value specified for the
   transition. If the delay expires and another transition hasn't
   already fired from the current state then the transition will be
   enabled (fireable). If another non-timed message transition is also
   enabled then the machine interpreter non-deterministically chooses
   which transition to fire. 
   
   When the machine interpreter arrives in a state via a transition from a
   deep/shallow history state, then the semantics of timed transitions
   are slightly different than described above. In the case of a deep
   history state, then the count down on timed transitions proceeds
   without being reset since the last time the state was visited. In
   the case of a shallow history state, the count down proceeds for
   any transitions that were active the last time the state was
   visited, and the count down is initiated for any newly activated
   transitions.  
*/
public final class TimedTransition extends MessageTransition {

    public static final long serialVersionUID = 1;

    static final String rcsid = "$Name:  $ $Id: TimedTransition.java 1516 2009-09-02 18:57:05Z gwbond $";

    public TimedTransition(PortMethod transitionTimerPortEvaluator, TransitionSource source, TransitionTarget target) {
        super(transitionTimerPortEvaluator, TransitionTimeoutMessage.class, source, target, false);
    }
}
