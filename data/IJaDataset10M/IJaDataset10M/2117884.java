package org.gamegineer.common.internal.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.internal.core.NullDebugOptions} class.
 */
public final class NullDebugOptionsTest {

    /** The default option name to be used by the fixture. */
    private static final String OPTION = "bundle/option";

    /** The debug options under test in the fixture. */
    private DebugOptions m_debugOptions;

    /**
     * Initializes a new instance of the {@code NullDebugOptionsTest} class.
     */
    public NullDebugOptionsTest() {
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
        m_debugOptions = new NullDebugOptions();
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_debugOptions = null;
    }

    /**
     * Ensures the {@code getBooleanOption} method throws an exception when
     * passed a {@code null} option.
     */
    @Test(expected = NullPointerException.class)
    public void testGetBooleanOption_Option_Null() {
        m_debugOptions.getBooleanOption(null, false);
    }

    /**
     * Ensures the {@code getBooleanOption} method always returns the default
     * value.
     */
    @Test
    public void testGetBooleanOption_ReturnsDefaultValue() {
        assertTrue(m_debugOptions.getBooleanOption(OPTION, true));
        assertFalse(m_debugOptions.getBooleanOption(OPTION, false));
    }

    /**
     * Ensures the {@code getIntegerOption} method throws an exception when
     * passed a {@code null} option.
     */
    @Test(expected = NullPointerException.class)
    public void testGetIntegerOption_Option_Null() {
        m_debugOptions.getIntegerOption(null, 0);
    }

    /**
     * Ensures the {@code getIntegerOption} method always returns the default
     * value.
     */
    @Test
    public void testGetIntegerOption_ReturnsDefaultValue() {
        assertEquals(1, m_debugOptions.getIntegerOption(OPTION, 1));
        assertEquals(0, m_debugOptions.getIntegerOption(OPTION, 0));
        assertEquals(-1, m_debugOptions.getIntegerOption(OPTION, -1));
    }

    /**
     * Ensures the {@code getOption(String)} method throws an exception when
     * passed a {@code null} option.
     */
    @Test(expected = NullPointerException.class)
    public void testGetOption_Option_Null() {
        m_debugOptions.getOption(null);
    }

    /**
     * Ensures the {@code getOption(String)} method always returns {@code null}.
     */
    @Test
    public void testGetOption_ReturnsNull() {
        assertNull(m_debugOptions.getOption(OPTION));
    }

    /**
     * Ensures the {@code getOption(String, String)} method throws an exception
     * when passed a {@code null} default value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetOptionWithDefault_DefaultValue_Null() {
        m_debugOptions.getOption(OPTION, null);
    }

    /**
     * Ensures the {@code getOption(String, String)} method throws an exception
     * when passed a {@code null} option.
     */
    @Test(expected = NullPointerException.class)
    public void testGetOptionWithDefault_Option_Null() {
        m_debugOptions.getOption(null, "defaultValue");
    }

    /**
     * Ensures the {@code getOption(String, String)} method always returns the
     * default value.
     */
    @Test
    public void testGetOptionWithDefault_ReturnsDefaultValue() {
        final String value = "defaultValue";
        assertEquals(value, m_debugOptions.getOption(OPTION, value));
    }

    /**
     * Ensures the {@code newDebugTrace(String)} method throws an exception when
     * passed a {@code null} bundle symbolic name.
     */
    @Test(expected = NullPointerException.class)
    public void testNewDebugTrace_BundleSymbolicName_Null() {
        m_debugOptions.newDebugTrace(null);
    }

    /**
     * Ensures the {@code newDebugTrace(String,Class)} method throws an
     * exception when passed a {@code null} bundle symbolic name.
     */
    @Test(expected = NullPointerException.class)
    public void testNewDebugTraceWithTraceEntryClass_BundleSymbolicName_Null() {
        m_debugOptions.newDebugTrace(null, Object.class);
    }

    /**
     * Ensures the {@code newDebugTrace(String,Class)} method throws an
     * exception when passed a {@code null} trace entry class.
     */
    @Test(expected = NullPointerException.class)
    public void testNewDebugTraceWithTraceEntryClass_TraceEntryClass_Null() {
        m_debugOptions.newDebugTrace("", null);
    }

    /**
     * Ensures the {@code removeOption} method throws an exception when passed a
     * {@code null} option.
     */
    @Test(expected = NullPointerException.class)
    public void testRemoveOption_Option_Null() {
        m_debugOptions.removeOption(null);
    }

    /**
     * Ensures the {@code setOption} method throws an exception when passed a
     * {@code null} option.
     */
    @Test(expected = NullPointerException.class)
    public void testSetOption_Option_Null() {
        m_debugOptions.setOption(null, "");
    }
}
