package org.antdepo.common;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.antdepo.tools.AntdepoTest;
import org.apache.tools.ant.Project;

/**
 * ControlTier Software Inc. User: alexh Date: Jul 22, 2005 Time: 2:01:00 PM
 */
public class TestFramework extends AntdepoTest {

    /**
     * Constructor for test
     *
     * @param name name of test
     */
    public TestFramework(final String name) {
        super(name);
    }

    /**
     * main method
     *
     * @param args cli args
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Junit suite
     *
     * @return test suite
     */
    public static Test suite() {
        return new TestSuite(TestFramework.class);
    }

    protected void setUp() {
        super.setUp();
    }

    /**
     * Test creation
     */
    public void testConstruction() {
        try {
            Framework.getInstance(getAntdepoBase(), getModulesBase(), getDepotsBase());
            assertTrue("Framework.getInstance should have thrown an exception for missing framework.properties", true);
        } catch (Exception e) {
            assertNotNull(e);
        }
        final Framework framework = Framework.getInstance(getAntdepoBase(), getModulesBase(), getDepotsBase());
        assertNotNull("Framework.getInstance returned null", framework);
        assertTrue("framework.node property was not set", framework.existsProperty("framework.node"));
        assertEquals("basedir did not match", framework.getBaseDir().getAbsolutePath(), getAntdepoBase());
        assertNotNull("authorization manager was null", framework.getAuthorizationMgr());
        assertNotNull("authentication manager was null", framework.getAuthenticationMgr());
        assertNotNull("DepotResourceMgr was null", framework.getDepotResourceMgr());
        assertNotNull("ModuleMgr was null", framework.getModuleLookup());
        assertNotNull("ExtensionMgr was null", framework.getExtensionMgr());
    }

    /**
     * Test the allowUserInput property of Framework class
     */
    public void testAllowUserInput() {
        final Framework newfw = Framework.getInstance(getAntdepoBase());
        assertTrue("User input should be enabled by default", newfw.isAllowUserInput());
        newfw.setAllowUserInput(false);
        assertFalse("User input should be disabled", newfw.isAllowUserInput());
        {
            final Project p = new Project();
            assertNull("property should not be set", p.getProperty("framework.userinput.disabled"));
            newfw.configureProject(p);
            assertEquals("Ant property not set to disable user input", "true", p.getProperty("framework.userinput.disabled"));
            assertNotNull("Input Handler should be configured", p.getInputHandler());
            assertEquals("Input Handler isn't expected type", Framework.FailInputHandler.class, p.getInputHandler().getClass());
            newfw.setAllowUserInput(true);
            newfw.configureProject(p);
            assertTrue("Ant property not set to enable user input", "false".equals(p.getProperty("framework.userinput.disabled")) || null == p.getProperty("framework.userinput.disabled"));
            assertTrue("Input Handler shouldn't be configured", null == p.getInputHandler() || !(p.getInputHandler() instanceof Framework.FailInputHandler));
        }
        {
            final Project p = new Project();
            p.setProperty("framework.userinput.disabled", "true");
            p.setProperty("antdepo.base", getAntdepoBase());
            final Framework ftest1 = Framework.getInstanceOrCreate(p);
            assertNotNull("instance should be found from PRoject", ftest1);
            assertFalse("framework input should be disabled", ftest1.isAllowUserInput());
            assertNotNull("Input Handler should be configured", p.getInputHandler());
            assertEquals("Input Handler isn't expected type", Framework.FailInputHandler.class, p.getInputHandler().getClass());
        }
        {
            final Project p = new Project();
            p.setProperty("framework.userinput.disabled", "false");
            p.setProperty("antdepo.base", getAntdepoBase());
            final Framework ftest1 = Framework.getInstanceOrCreate(p);
            assertNotNull("instance should be found from PRoject", ftest1);
            assertTrue("framework input should be enabled", ftest1.isAllowUserInput());
            assertTrue("Input Handler shouldn't be configured", null == p.getInputHandler() || !(p.getInputHandler() instanceof Framework.FailInputHandler));
        }
        {
            final Project p = new Project();
            p.setProperty("antdepo.base", getAntdepoBase());
            final Framework ftest1 = Framework.getInstanceOrCreate(p);
            assertNotNull("instance should be found from PRoject", ftest1);
            assertTrue("framework input should be enabled", ftest1.isAllowUserInput());
            assertTrue("Input Handler shouldn't be configured", null == p.getInputHandler() || !(p.getInputHandler() instanceof Framework.FailInputHandler));
        }
    }
}
