package com.volantis.shared.environment;

/**
 * Test case for the <code>DefaultEnvironmentFactory</code> class
 */
public class DefaultEnvironmentFactoryTestCase extends EnvironmentFactoryTestAbstract {

    /**
     * Creates a new DefaultEnvironmentFactoryTestCase instance
     * @param name the name of the test
     */
    public DefaultEnvironmentFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Tests the createInteractionTracker method
     * @throws Exception if an error occurs
     */
    public void testCreateInteractionTracker() throws Exception {
        EnvironmentFactory factory = new DefaultEnvironmentFactory();
        EnvironmentInteractionTracker tracker = factory.createInteractionTracker();
        assertEquals("createInteractionTracker should return a " + "SimpleEnvironmentInteractionTracker instance", SimpleEnvironmentInteractionTracker.class, tracker.getClass());
    }
}
