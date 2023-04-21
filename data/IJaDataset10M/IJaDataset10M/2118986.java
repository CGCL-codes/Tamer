package com.ohua.tests.operators;

import org.junit.Test;
import com.ohua.tests.AbstractFlowTestCase;

public class testDataColoringOperator extends AbstractFlowTestCase {

    /**
   * Simple test making sure the operator is working properly.
   */
    @Test
    public void test1() throws Throwable {
        runFlow(getTestMethodInputDirectory() + "data-coloring-test-flow.xml");
    }
}
