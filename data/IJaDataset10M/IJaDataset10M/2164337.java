package org.unitilsnew.core.context;

import org.junit.Before;
import org.junit.Test;
import org.unitils.core.UnitilsException;
import org.unitils.mock.Mock;
import org.unitilsnew.UnitilsJUnit4;
import org.unitilsnew.core.config.Configuration;
import static org.junit.Assert.fail;

/**
 * @author Tim Ducheyne
 */
public class ContextGetInstanceOfTypeCyclicDependencyTest extends UnitilsJUnit4 {

    private Context context;

    private Mock<Configuration> configurationMock;

    @Before
    public void initialize() {
        context = new Context(configurationMock.getMock());
    }

    @Test
    public void cyclicDependency() {
        try {
            context.getInstanceOfType(TestClassA.class);
            fail("UnitilsException expected");
        } catch (UnitilsException e) {
            System.out.println("e = " + e);
        }
    }

    protected static interface TestInterface {
    }

    protected static class TestClassA {

        public TestClassA(TestClassB testClassB) {
        }
    }

    protected static class TestClassB {

        public TestClassB(TestClassC testClassC) {
        }
    }

    protected static class TestClassC {

        public TestClassC(TestClassA testClassA) {
        }
    }
}
