package org.bs.sm.tests.examples;

import org.bs.sm.*;
import org.bs.sm.tests.infra.SMAbstractTest;
import org.bs.sm.tests.infra.SMExampleRunner;

/**
 * @author Boaz Nahum
 * @version x.5
 */
public class Devices extends SMAbstractTest {

    public static class TheDevices {

        private boolean opXFailed;

        public boolean isOpXFailed() {
            return opXFailed;
        }

        public void setOpXFailed(boolean opXFailed) {
            this.opXFailed = opXFailed;
        }
    }

    public Devices() {
        super("Devices");
        int nDevices = 5;
        final TheDevices theDevices = new TheDevices();
        theDevices.setOpXFailed(false);
        SMUTrigger doX = new SMSimpleTrigger("DoX");
        SMUTrigger[] devicesOKTriggers = buildOkTriggers(nDevices);
        SMUTrigger[] devicesFailedTriggers = buildFailedTriggers(nDevices);
        sm.defineTriggers(doX);
        sm.defineTriggers(devicesOKTriggers);
        sm.defineTriggers(devicesFailedTriggers);
        SMCompositeState topLevel = sm.getTopLevel();
        SMState idle = topLevel.addSimpleState("Idle");
        idle.addEntryHandler(new SMStateHandler() {

            @Override
            public void handle(SMEventContext eventContext, SMStateHandlerInfo info) {
                theDevices.setOpXFailed(false);
            }
        });
        final SMState sXOk = topLevel.addSimpleState("XOk");
        final SMState sXFailed = topLevel.addSimpleState("XFailed");
        SMConcurrentState sDoingX = topLevel.addConcurrentState("DoingX");
        SMStateVertex sDoingXJoin = sDoingX.addJoin();
        SMStateVertex sDoingFork = sDoingX.addFork();
        topLevel.addInitialState().addTransition(idle);
        idle.addTransition(doX, sDoingFork);
        final SMGuard guard = new SMNaryGuard() {

            @Override
            public int select(SMTransitionGuardInfo info) {
                if (theDevices.isOpXFailed()) {
                    return info.resolve(sXFailed);
                } else {
                    return info.resolve(sXOk);
                }
            }
        };
        sDoingXJoin.addTransition(guard, sXOk, sXFailed);
        buildDevicesDoingStates(theDevices, sDoingX, sDoingFork, sDoingXJoin, devicesOKTriggers, devicesFailedTriggers);
    }

    private void buildDevicesDoingStates(final TheDevices theDevices, SMConcurrentState sDoingX, SMStateVertex fork, SMStateVertex join, SMTrigger[] devicesOKTriggers, SMTrigger[] devicesFailedTriggers) {
        int n = devicesOKTriggers.length;
        for (int i = 0; i < n; ++i) {
            int id = i + 1;
            SMCompositeState doingContainer = sDoingX.addCompositeState("P" + id);
            SMState doingX = doingContainer.addSimpleState("Doing" + id);
            fork.addTransition(doingX);
            doingX.addTransition(devicesOKTriggers[i], join);
            SMTransition t = doingX.addTransition(devicesFailedTriggers[i], join);
            t.addHandler(new SMTransitionHandler() {

                @Override
                public void handle(SMEventContext eventContext, SMTransitionHandlerInfo info) {
                    theDevices.setOpXFailed(true);
                }
            });
        }
    }

    private SMUTrigger[] buildOkTriggers(int nDevices) {
        SMUTrigger[] triggers = new SMUTrigger[nDevices];
        for (int i = 0; i < nDevices; ++i) {
            int id = i + 1;
            triggers[i] = new SMSimpleTrigger("" + id + "OK");
        }
        return triggers;
    }

    private SMUTrigger[] buildFailedTriggers(int nDevices) {
        SMUTrigger[] triggers = new SMUTrigger[nDevices];
        for (int i = 0; i < nDevices; ++i) {
            int id = i + 1;
            triggers[i] = new SMSimpleTrigger("" + id + "Failed");
        }
        return triggers;
    }

    public static void main(String[] args) {
        Devices test = new Devices();
        SMExampleRunner.runGUI(test);
    }
}
