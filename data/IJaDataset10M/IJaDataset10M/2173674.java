package org.jazzteam.jpatterns.utils;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.jazzteam.jpatterns.core.JPConstants;
import org.jazzteam.jpatterns.core.configuration.IPropertiesManager;
import org.jazzteam.jpatterns.core.configuration.PropertiesManagerImpl;
import org.jazzteam.jpatterns.core.configuration.junit.PropertiesManagerMockImpl;
import org.jazzteam.jpatterns.core.configuration.junit.PropertiesManagerMockImplTwo;
import org.jazzteam.jpatterns.utils.junit.JPatternsTestUtils;
import com.zmicer.utils.LoggingUtils;
import com.zmicer.utils.junit.SystemPropsUtils;

/**
 * $Author:: zmicer $<br/>
 * $Rev:: 67 $<br/>
 * * $Date:: 2007-08-28 21:37:07 #$<br/>
 * 
 * @version 1.0.1
 */
public class JPatternsPropsUtilsTest extends TestCase {

    /**
	 * JVM_OVERRIDEN_FILE_NAME_NOT_EXISTED
	 */
    private static final String JVM_OVERRIDEN_FILE_NAME_NOT_EXISTED = "JVMOverriden";

    /**
	 * The test cases for this test.
	 */
    private interface Cases {

        String onlyDefaultContains = "onlyDefaultContains";

        String onlyCustomContains = "onlyCustomContains";

        String noOneContains = "noOneContains";

        String bothContain = "bothContain";

        String wrongImplementation = "wrongImplementation";
    }

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(JPatternsPropsUtilsTest.class);

    /**
	 * Contructor with name of test attribute.
	 * 
	 * @param name
	 *            name of the test
	 */
    public JPatternsPropsUtilsTest(final String name) {
        super(name);
    }

    /**
	 * Perform the set up functionality for the test.
	 * 
	 * @throws Exception
	 *             may occur in the case of some problems
	 */
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * Perform the tear down functionality for the test
	 * 
	 * @throws Exception
	 *             may occur in the case of some problems
	 */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test suite method
	 * 
	 * @return the built test suite
	 */
    public static Test suite() {
        return new TestSuite(JPatternsPropsUtilsTest.class);
    }

    /**
	 * Tests {@link org.jazzteam.jpatterns.core.configuration.PropertiesManagerImpl#getDefaultBundleName}
	 * 
	 * @throws Exception
	 *             in the case smth. wrong occuried.
	 * @forversion 1.0
	 */
    public void testGetDefaultBundleName() throws Exception {
        try {
            JPatternsTestUtils.resetJVMProps();
            Assert.assertNotNull(JPatternsPropsUtils.getDefaultBundleName());
            Assert.assertEquals(JPatternsPropsUtils.getDefaultBundleName(), JPConstants.PropertiesConfigFilesConstants.DEFAULT_PROPERTIES_BASE_NAME);
            System.setProperty(JPConstants.PropertiesConfigFilesConstants.DEFAULT_PROPERTIES_FILE_NAME_JVM_PARAM, JPatternsPropsUtilsTest.JVM_OVERRIDEN_FILE_NAME_NOT_EXISTED);
            Assert.assertNotNull(JPatternsPropsUtils.getDefaultBundleName());
            Assert.assertEquals(JPatternsPropsUtils.getDefaultBundleName(), JPatternsPropsUtilsTest.JVM_OVERRIDEN_FILE_NAME_NOT_EXISTED);
            SystemPropsUtils.clearProps(JPConstants.PropertiesConfigFilesConstants.DEFAULT_PROPERTIES_FILE_NAME_JVM_PARAM);
        } catch (final Exception ex) {
            LoggingUtils.logException(JPatternsPropsUtilsTest.LOG, ex, null, null);
            Assert.fail("Exception should not occur for that case params" + ex.getMessage());
        }
    }

    /**
	 * Tests {@link org.jazzteam.jpatterns.core.configuration.PropertiesManagerImpl#getCustomBundleName}
	 * 
	 * @throws Exception
	 *             in the case smth. wrong occuried.
	 * @forversion 1.0
	 */
    public void testGetCustomBundleName() throws Exception {
        try {
            System.clearProperty(JPConstants.PropertiesConfigFilesConstants.CUSTOM_PROPERTIES_FILE_NAME_JVM_PARAM);
            Assert.assertNotNull(JPatternsPropsUtils.getCustomBundleName());
            Assert.assertEquals(JPatternsPropsUtils.getCustomBundleName(), JPConstants.PropertiesConfigFilesConstants.CUSTOM_PROPERTIES_BASE_NAME);
            System.setProperty(JPConstants.PropertiesConfigFilesConstants.CUSTOM_PROPERTIES_FILE_NAME_JVM_PARAM, JPatternsPropsUtilsTest.JVM_OVERRIDEN_FILE_NAME_NOT_EXISTED);
            Assert.assertNotNull(JPatternsPropsUtils.getCustomBundleName());
            Assert.assertEquals(JPatternsPropsUtils.getCustomBundleName(), JPatternsPropsUtilsTest.JVM_OVERRIDEN_FILE_NAME_NOT_EXISTED);
            SystemPropsUtils.clearProps(JPConstants.PropertiesConfigFilesConstants.CUSTOM_PROPERTIES_FILE_NAME_JVM_PARAM);
        } catch (final Exception ex) {
            LoggingUtils.logException(JPatternsPropsUtilsTest.LOG, ex, null, null);
            Assert.fail("Exception should not occur for that case params" + ex.getMessage());
        }
    }

    /**
	 * Tests {@link JPatternsPropsUtils#getPropertiesManagerImplementation}
	 * 
	 * @throws Exception
	 *             in the case smth. wrong occuried.
	 * @forversion 1.1
	 */
    public void testGetPropertiesManagerImplementation() throws Exception {
        try {
            JPatternsTestUtils.setJVMPropsParams(JPatternsPropsUtilsTest.class, false, Cases.bothContain, null, null, false);
            IPropertiesManager manager = JPatternsPropsUtils.getPropertiesManagerImplementation();
            Assert.assertNotNull(manager);
            Assert.assertTrue(manager instanceof PropertiesManagerMockImplTwo);
            JPatternsTestUtils.setJVMPropsParams(JPatternsPropsUtilsTest.class, false, Cases.onlyDefaultContains, null, null, false);
            manager = JPatternsPropsUtils.getPropertiesManagerImplementation();
            Assert.assertNotNull(manager);
            Assert.assertTrue(manager instanceof PropertiesManagerMockImpl);
            JPatternsTestUtils.setJVMPropsParams(JPatternsPropsUtilsTest.class, false, Cases.onlyCustomContains, null, null, false);
            manager = JPatternsPropsUtils.getPropertiesManagerImplementation();
            Assert.assertNotNull(manager);
            Assert.assertTrue(manager instanceof PropertiesManagerMockImplTwo);
            JPatternsTestUtils.setJVMPropsParams(JPatternsPropsUtilsTest.class, false, Cases.noOneContains, null, null, false);
            manager = JPatternsPropsUtils.getPropertiesManagerImplementation();
            Assert.assertNotNull(manager);
            Assert.assertTrue(manager instanceof PropertiesManagerImpl);
            JPatternsTestUtils.setJVMPropsParams(JPatternsPropsUtilsTest.class, false, Cases.wrongImplementation, null, null, false);
            try {
                manager = JPatternsPropsUtils.getPropertiesManagerImplementation();
                Assert.fail("Exception should have been occuried before this line.");
            } catch (final Exception ex) {
                LoggingUtils.logException(JPatternsPropsUtilsTest.LOG, ex, null, this.getClass());
                Assert.assertTrue(ex instanceof IllegalStateException);
            } finally {
                JPatternsTestUtils.resetJVMProps();
            }
        } catch (final Exception ex) {
            LoggingUtils.logException(JPatternsPropsUtilsTest.LOG, ex, null, null);
            Assert.fail("Exception should not occur for that case " + ex.getMessage());
        }
    }
}
