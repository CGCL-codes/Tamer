package ch.mimo.swingglue;

import ch.mimo.swingglue.impl.DefaultGlueImpl;
import junit.framework.TestCase;

public class GlueFactoryTestSuite extends TestCase {

    /**
	 * instanciation of factory
	 * test's the instanciation of the default implementation class
	 */
    public void test_CREATE_FACTORY() {
        assertNotNull(GlueFactory.getFactory());
    }

    /**
     * test's the instanciation mechnasim of the Factory if a implementation
     * class is given.
     */
    public void test_CREATE_FACTORY_WITH_SPECIFIC_CLASS() {
        assertNotNull(GlueFactory.getFactory(DefaultGlueImpl.class));
    }

    /**
     * test's the glue getter method on the factory
     */
    public void test_GET_GLUE() {
        GlueFactory factory = GlueFactory.getFactory();
        try {
            assertNotNull(factory.getGlue());
        } catch (ClassNotFoundException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        }
    }

    /**
     * test's the exception handling in case of a invalid implementation class
     */
    public void test_GET_GLUE_FROM_INVALID_CLASS() {
        GlueFactory factory = GlueFactory.getFactory(String.class);
        try {
            factory.getGlue();
        } catch (ClassNotFoundException e) {
            fail();
        } catch (InstantiationException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (ClassCastException e) {
            return;
        }
        fail();
    }
}
