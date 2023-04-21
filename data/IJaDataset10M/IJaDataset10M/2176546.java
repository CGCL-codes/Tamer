package edu.rice.cs.drjava.model;

import edu.rice.cs.drjava.DrJavaTestCase;
import edu.rice.cs.drjava.model.GlobalModelTestCase.TestListener;

/**
 * Tests the functionality of the class that notifies listeners
 * of a global model.
 * @version $Id: EventNotifierTest.java 5175 2010-01-20 08:46:32Z mgricken $
 */
public final class EventNotifierTest extends DrJavaTestCase {

    protected GlobalEventNotifier _notifier;

    public void setUp() throws Exception {
        super.setUp();
        _notifier = new GlobalEventNotifier();
    }

    public void tearDown() throws Exception {
        _notifier = null;
        super.tearDown();
    }

    /** Checks that the notifier adds and removes listeners correctly,
   * notifying the correct ones on a particular event.
   */
    public void testAddAndRemoveListeners() {
        TestListener listener1 = new TestListener() {

            public void junitSuiteStarted(int numTests) {
                junitSuiteStartedCount++;
            }

            public void interpreterExited(int status) {
                interpreterExitedCount++;
            }
        };
        TestListener listener2 = new TestListener() {

            public void junitSuiteStarted(int numTests) {
                junitSuiteStartedCount++;
            }
        };
        _notifier.addListener(listener1);
        _notifier.addListener(listener2);
        _notifier.junitSuiteStarted(1);
        listener1.assertJUnitSuiteStartedCount(1);
        listener2.assertJUnitSuiteStartedCount(1);
        _notifier.removeListener(listener2);
        _notifier.interpreterExited(1);
        listener1.assertInterpreterExitedCount(1);
        listener2.assertInterpreterExitedCount(0);
    }

    /** Checks that the notifier can poll multiple listeners.
   */
    public void testPollListeners() {
        TestListener trueListener = new TestListener() {

            public boolean canAbandonFile(OpenDefinitionsDocument doc) {
                return true;
            }
        };
        TestListener falseListener = new TestListener() {

            public boolean canAbandonFile(OpenDefinitionsDocument doc) {
                return false;
            }
        };
        _notifier.addListener(trueListener);
        boolean result = _notifier.canAbandonFile(null);
        assertTrue("should be able to abandon file", result);
        _notifier.addListener(falseListener);
        result = _notifier.canAbandonFile(null);
        assertTrue("should not be able to abandon file", !result);
    }
}
