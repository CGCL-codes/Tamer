package ro.codemart.installer.operation;

import junit.framework.TestCase;
import ro.codemart.installer.core.InstallerException;
import ro.codemart.installer.core.operation.LongOperation;
import ro.codemart.installer.core.operation.OperationObserver;

/**
 *
 *
 */
public class LongOperationTest extends TestCase {

    private SumOperation sum1000 = new SumOperation(1000);

    private InvocationCounterObserver counter1 = new InvocationCounterObserver();

    private InvocationCounterObserver counter2 = new InvocationCounterObserver();

    public void testSimpleExecution() throws Exception {
        sum1000.execute();
        assertEquals("Invalid execution", 1001 * 1000 / 2, sum1000.getSum());
    }

    public void testObserverForProperty() throws InstallerException {
        String property = LongOperation.PROGRESS_PROPERTY;
        sum1000.addObserver(property, counter1);
        sum1000.addObserver(counter2);
        sum1000.execute();
        assertEquals("Wrong invocations to the observer", 1000, counter1.getInvocationsForProperty(property));
        assertEquals("Wrong total invocations count", 1000, counter1.getTotalInvocations());
        assertEquals("Wrong invocations for observer", 1000, counter2.getInvocationsForProperty(property));
        assertEquals("Wrong invocations for observer", 1000, counter2.getInvocationsForProperty(SumOperation.SUM_PROPERTY));
        assertEquals("Wrong invocations for observer", 1000, counter2.getInvocationsForProperty(LongOperation.INFO_PROPERTY));
        assertEquals("Wrong total invocations count", 3000, counter2.getTotalInvocations());
    }

    public void testUpdateOfCompletedPercentage() {
        sum1000.addObserver(LongOperation.PROGRESS_PROPERTY, new InvocationCounterObserver() {

            public void onPropertyChange(String property) {
                super.onPropertyChange(property);
                if (getTotalInvocations() == 100) {
                    assertEquals("invalid percentage", 0.1, sum1000.getCompletedPercentage());
                }
            }
        });
    }

    public void testUpdateOfCompletedPercentageForFineUpdatesOfProgress() throws InstallerException {
        SumOperation op = new SumOperation(9999);
        op.addObserver(LongOperation.PROGRESS_PROPERTY, counter1);
        op.execute();
        assertEquals("Invalid number of calls for progress update", 9999, counter1.getTotalInvocations());
        counter1.clear();
        op = new SumOperation(300000);
        op.addObserver(LongOperation.PROGRESS_PROPERTY, counter1);
        op.execute();
        assertTrue("Invalid number of calls for progress update", 10000 > counter1.getTotalInvocations());
    }

    public void testDelegationObserver() throws InstallerException {
        final OuterOperation outer = new OuterOperation(sum1000);
        outer.addObserver(SumOperation.SUM_PROPERTY, counter1);
        outer.addObserver(new OperationObserver() {

            public void onPropertyChange(String property) {
                assertTrue("Percentage not copied", Math.abs(outer.getCompletedPercentage() - sum1000.getCompletedPercentage()) < 0.01);
                assertEquals("Description not copied", sum1000.getDescription(), outer.getDescription());
                assertEquals("Info not copied", sum1000.getInfo(), outer.getInfo());
            }
        });
        outer.execute();
        assertEquals("Wrong total invocations count", 1000, counter1.getTotalInvocations());
    }

    public void testDelegationWithDifferentSpan() throws InstallerException {
        SumOperation sum3 = new SumOperation(3);
        final OuterOperation outer = new OuterOperation(sum3, 0.3, 0.7);
        outer.addObserver(SumOperation.SUM_PROPERTY, counter1);
        InvocationCounterObserver checker = new InvocationCounterObserver() {

            private int step = 0;

            private static final double incrementStep = 0.7 / 3;

            public void onPropertyChange(String property) {
                super.onPropertyChange(property);
                assertEquals("Percentage not copied", 0.3 + (step++) * incrementStep, outer.getCompletedPercentage());
            }
        };
        outer.addObserver(LongOperation.PROGRESS_PROPERTY, checker);
        outer.execute();
        assertEquals("Wrong total invocations count", 4, checker.getTotalInvocations());
    }
}
