package com.thoughtworks.fireworks.core;

import com.thoughtworks.shadow.TestShadowResult;
import junit.framework.TestListener;
import junit.framework.TestResult;

public class TestResultFactory {

    private final TestListener[] testListeners;

    private final ResultOfTestEndListener[] counterListeners;

    public TestResultFactory(TestListener[] testListeners, ResultOfTestEndListener[] counterListeners) {
        this.testListeners = testListeners == null ? new TestListener[0] : testListeners;
        this.counterListeners = counterListeners == null ? new ResultOfTestEndListener[0] : counterListeners;
    }

    public TestResult createTestResult() {
        TestShadowResult result = new TestShadowResult();
        result.addListener(getTestCounter(result));
        for (int i = 0; i < testListeners.length; i++) {
            result.addListener(testListeners[i]);
        }
        return result;
    }

    private TestResultMonitor getTestCounter(TestShadowResult result) {
        TestResultMonitor resultMonitor = new TestResultMonitor(result);
        for (int i = 0; i < counterListeners.length; i++) {
            resultMonitor.addListener(counterListeners[i]);
        }
        resultMonitor.start();
        return resultMonitor;
    }
}
