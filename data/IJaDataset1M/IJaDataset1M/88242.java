package org.springframework.richclient.command.support;

import junit.framework.TestCase;

public class MethodInvokingActionCommandExecutorTests extends TestCase {

    private MethodInvokingActionCommandExecutor e = new MethodInvokingActionCommandExecutor();

    private int invokeCount;

    protected void setUp() throws Exception {
        e = new MethodInvokingActionCommandExecutor();
        e.setTargetObject(this);
        e.setArguments(new Object[0]);
        e.setTargetMethod("targetMethod");
        e.afterPropertiesSet();
    }

    public void testExceuteInvokesMethod() {
        e.execute();
        assertEquals("Execute should have invoked targetMethod", 1, invokeCount);
        e.execute();
        assertEquals("Execute should have invoked targetMethod", 2, invokeCount);
    }

    public void targetMethod() {
        invokeCount++;
    }
}
