package org.gamegineer.game.core.system.bindings.xml;

import static org.gamegineer.test.core.DummyFactory.createDummy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.game.core.system.bindings.xml.XmlGameSystemExtensionFactory}
 * class.
 */
public final class XmlGameSystemExtensionFactoryTest {

    /** The XML game system extension factory under test in the fixture. */
    private XmlGameSystemExtensionFactory m_factory;

    /**
     * Initializes a new instance of the
     * {@code XmlGameSystemExtensionFactoryTest} class.
     */
    public XmlGameSystemExtensionFactoryTest() {
        super();
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        m_factory = new XmlGameSystemExtensionFactory();
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_factory = null;
    }

    /**
     * Ensures the {@code parsePath} method returns the correct value when
     * passed extension point data of type {@code Hashtable} in which an entry
     * for the path data is absent.
     */
    @Test
    public void testParsePath_Data_Hashtable_Absent() {
        final Map<String, String> data = new Hashtable<String, String>();
        data.put("key", "value");
        final String actualPath = XmlGameSystemExtensionFactoryFacade.parsePath(data);
        assertNull(actualPath);
    }

    /**
     * Ensures the {@code parsePath} method returns the correct value when
     * passed extension point data of type {@code Hashtable} in which an entry
     * for the path data is present.
     */
    @Test
    public void testParsePath_Data_Hashtable_Present() {
        final String expectedPath = "/my/path/file.xml";
        final Map<String, String> data = new Hashtable<String, String>();
        data.put(XmlGameSystemExtensionFactoryFacade.ATTR_PATH(), expectedPath);
        final String actualPath = XmlGameSystemExtensionFactoryFacade.parsePath(data);
        assertEquals(expectedPath, actualPath);
    }

    /**
     * Ensures the {@code parsePath} method returns the correct value when
     * passed {@code null} extension point data.
     */
    @Test
    public void testParsePath_Data_Null() {
        final String actualPath = XmlGameSystemExtensionFactoryFacade.parsePath(null);
        assertNull(actualPath);
    }

    /**
     * Ensures the {@code parsePath} method returns the correct value when
     * passed extension point data of type {@code String}.
     */
    @Test
    public void testParsePath_Data_String() {
        final String expectedPath = "/my/path/file.xml";
        final String data = expectedPath;
        final String actualPath = XmlGameSystemExtensionFactoryFacade.parsePath(data);
        assertEquals(expectedPath, actualPath);
    }

    /**
     * Ensures the {@code setInitializationData} method throws an exception when
     * passed a {@code null} configuration element.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = NullPointerException.class)
    public void testSetInitializationData_Config_Null() throws Exception {
        m_factory.setInitializationData(null, "propertyName", new Object());
    }

    /**
     * Ensures the {@code setInitializationData} method throws an exception when
     * passed a {@code null} property name.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = NullPointerException.class)
    public void testSetInitializationData_PropertyName_Null() throws Exception {
        m_factory.setInitializationData(createDummy(IConfigurationElement.class), null, new Object());
    }

    /**
     * A class for transparently accessing inaccessible members of the
     * {@code XmlGameSystemExtensionFactory} class for testing purposes.
     */
    private static final class XmlGameSystemExtensionFactoryFacade {

        /**
         * Initializes a new instance of the
         * {@code XmlGameSystemExtensionFactoryFacade} class.
         */
        private XmlGameSystemExtensionFactoryFacade() {
            super();
        }

        static String ATTR_PATH() {
            try {
                final Field field = XmlGameSystemExtensionFactory.class.getDeclaredField("ATTR_PATH");
                field.setAccessible(true);
                return (String) field.get(null);
            } catch (final Exception e) {
                throw new AssertionError("failed to read 'ATTR_PATH'");
            }
        }

        static String parsePath(final Object data) {
            try {
                final Method method = XmlGameSystemExtensionFactory.class.getDeclaredMethod("parsePath", Object.class);
                method.setAccessible(true);
                return (String) method.invoke(null, data);
            } catch (final Exception e) {
                throw new AssertionError("failed to invoke 'parsePath'");
            }
        }
    }
}
