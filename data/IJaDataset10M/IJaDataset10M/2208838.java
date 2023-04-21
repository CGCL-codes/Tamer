package org.bs.sm.tests.examples;

import org.bs.sm.*;
import org.bs.sm.tests.infra.SMAbstractTest;
import org.bs.sm.tests.infra.SMExampleRunner;
import org.bs.sm.tests.infra.SMTestsCommonTriggers;
import static org.bs.sm.tests.infra.SMTestsCommonTriggers.*;

public class Example0 extends SMAbstractTest {

    public Example0() {
        super("Example0");
        SMTransition t;
        sm.defineTriggers(SMTestsCommonTriggers.class);
        SMCompositeState tl = sm.getTopLevel();
        final SMState s1 = tl.addSimpleState("S1");
        addStateHandlers(s1, SMTestsCommonTriggers.values());
        final SMState s2 = tl.addSimpleState("S2");
        addStateHandlers(s2, SMTestsCommonTriggers.values());
        t = tl.addInitialState().addTransition(s1);
        addTranHandlers(t);
        SMTransition t1_2 = s1.addTransition(E1, s2);
        addTranHandlers(t1_2);
        SMTransition t2_1 = s2.addTransition(E2, s1);
        addTranHandlers(t2_1);
        t = s1.addTransition(E2, s1);
        addTranHandlers(t);
        t = s2.addTransition(E1, s2);
        addTranHandlers(t);
        t1_2.addHandler(new SMTransitionHandler() {

            @Override
            public void handle(SMEventContext eventContext, SMTransitionHandlerInfo info) {
                s1.addToLocalEventQueue(L1);
                s2.addToLocalEventQueue(L2);
            }
        }, null);
        t2_1.addHandler(new SMTransitionHandler() {

            @Override
            public void handle(SMEventContext eventContext, SMTransitionHandlerInfo info) {
                s1.addToLocalEventQueue(L3);
                s2.addToLocalEventQueue(L4);
            }
        }, null);
    }

    public static void main(String[] args) {
        final Example0 test = new Example0();
        SMExampleRunner.runGUI(test);
    }
}
