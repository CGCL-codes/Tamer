package org.ist.contract.monitor.atn.transitiontests;

import org.ist.contract.monitor.atn.ATN;
import org.ist.contract.monitor.atn.Message;
import org.ist.contract.monitor.atn.TransitionTest;

/**
 *
 * @author noren
 */
public class NegatedTransitionTest implements TransitionTest {

    TransitionTest t;

    public NegatedTransitionTest(TransitionTest t) {
        this.t = t;
    }

    public boolean canTransition(ATN atn, Message m) {
        return !t.canTransition(atn, m);
    }

    public String getExplanation(ATN atn, Message m) {
        if (canTransition(atn, m)) return "it was not the case that " + t.getExplanation(atn, m);
        return null;
    }
}
