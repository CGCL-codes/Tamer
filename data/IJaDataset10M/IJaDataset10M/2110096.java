package junit.extensions;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * A Decorator that runs a test in a separate thread.
 *
 * @author Siegfried GOESCHL
 */
public class ActiveTest extends TestDecorator {

    /** Stress level. */
    private int multiplier = 1;

    /** Target test. */
    private Test test;

    /**
     * Thread that executes test.
     */
    private class Executor extends Thread {

        TestResult testResult;

        Executor(TestResult testResult) {
            this.testResult = testResult;
        }

        public void run() {
            test.run(testResult);
        }
    }

    /**
     * Creates active test with default multiplier (1).
     *
     * @param test Target test.
     */
    public ActiveTest(Test test) {
        super(test);
        this.test = test;
    }

    /**
     * Creates active test with default multiplier (1).
     *
     * @param test Target test.
     * @param multiplier Stress level.
     */
    public ActiveTest(Test test, int multiplier) {
        super(test);
        this.test = test;
        this.multiplier = multiplier;
    }

    public void run(TestResult testResult) {
        Executor[] threads = new Executor[multiplier];
        for (int i = 0; i < multiplier; i++) {
            threads[i] = new Executor(testResult);
        }
        for (int i = 0; i < multiplier; i++) {
            threads[i].start();
        }
        for (int i = 0; i < multiplier; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }
        }
    }

    public String toString() {
        return super.toString() + " (active)";
    }
}
