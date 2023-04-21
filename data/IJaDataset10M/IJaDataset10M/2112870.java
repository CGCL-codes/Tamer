package org.mockserver.integration.spring;

import org.mockserver.integration.BaseContextRunner;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class MockServerExecutionListener extends BaseContextRunner implements TestExecutionListener {

    public void prepareTestInstance(TestContext testContext) throws Exception {
    }

    public void afterTestMethod(TestContext testContext) throws Exception {
        this.stopServers(testContext.getTestMethod());
    }

    public void beforeTestMethod(TestContext testContext) throws Exception {
        this.startServers(testContext.getTestMethod());
    }
}
