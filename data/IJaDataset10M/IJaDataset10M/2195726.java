package org.callbackparams.junit4.version;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * @author Henrik Kaipe
 */
class CallbackRunListener_4_5 extends CallbackRunListener_4_4_OrEarlier {

    public CallbackRunListener_4_5(RunNotifier actualNotifier) {
        super(actualNotifier);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        actualNotifier.fireTestAssumptionFailed(modify(failure));
    }
}
