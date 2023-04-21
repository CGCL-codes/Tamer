package com.thoughtworks.turtlemock.internal;

import com.thoughtworks.turtlemock.Turtle;
import com.thoughtworks.turtlemock.constraint.CheckResult;
import com.thoughtworks.turtlemock.constraint.Constraint;
import junit.framework.TestCase;

public class InvokeLogWithConstraintTest extends TestCase {

    private String[] params;

    private InvokeMethodResultLog invokeMethodResultLog;

    private ProxyTypeMock constrain;

    protected void setUp() throws Exception {
        params = new String[] { "param1", "param2" };
        invokeMethodResultLog = new OnceInvokeMethodResultLog("operation", params);
        constrain = Turtle.mock(Constraint.class);
        constrain.ifCall("check").willReturn(CheckResult.PASS);
    }

    public void testShouldConstrainAllArgs() throws Exception {
        invokeMethodResultLog.withAllArgsConstraint((Constraint) constrain.mockTarget());
        constrain.assertDid("check").with(params);
    }

    public void testShouldConstrainFirstArg() throws Exception {
        invokeMethodResultLog.withFirstArgConstraint((Constraint) constrain.mockTarget());
        constrain.assertDid("check").with(params[0]);
    }

    public void testShouldConstrainArgWithIndexFromZero() throws Exception {
        int index = 0;
        invokeMethodResultLog.withArgConstraint((Constraint) constrain.mockTarget(), index);
        constrain.assertDid("check").with(params[index]);
    }

    public void testShouldFailIfParamIndexOutOfBounds() throws Exception {
        int index = 100;
        InvokeWithArgsResultLog invokeResultLog = invokeMethodResultLog.withArgConstraint((Constraint) constrain.mockTarget(), index);
        assertEquals(0, invokeResultLog.timesNotAsserted());
    }

    public void testShouldConstrainArgsOneByOne() throws Exception {
        invokeMethodResultLog.withArgsConstraint(new Constraint[] { (Constraint) constrain.mockTarget(), (Constraint) constrain.mockTarget() });
        constrain.assertDid("check").with(params[0]);
        constrain.assertDid("check").with(params[1]);
    }

    public void testShouldAlwaysPassWithNoneConstrains() throws Exception {
        invokeMethodResultLog.withArgsConstraint(new Constraint[0]);
    }
}
