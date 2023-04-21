package org.vizzini.util;

import javax.swing.UIManager;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Provides unit tests for the <code>SystemUtilities</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class SystemUtilitiesTest extends TestCase {

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.1
     */
    public static void main(String[] args) {
        TestRunner.run(SystemUtilitiesTest.class);
    }

    /**
     * Test the <code>isLinuxPlatform()</code> method.
     *
     * @since  v0.1
     */
    public void testIsLinuxPlatform() {
        boolean expected = System.getProperty("os.name").toLowerCase().startsWith("linux");
        SystemUtilities systemUtils = new SystemUtilities();
        assertEquals(expected, systemUtils.isLinuxPlatform());
    }

    /**
     * Test the <code>isMacPlatform()</code> method.
     *
     * @since  v0.1
     */
    public void testIsMacPlatform() {
        boolean expected = System.getProperty("os.name").toLowerCase().startsWith("mac os");
        SystemUtilities systemUtils = new SystemUtilities();
        assertEquals(expected, systemUtils.isMacPlatform());
    }

    /**
     * Test the <code>isSystemLAF()</code> method.
     *
     * @since  v0.1
     */
    public void testIsSystemLAF() {
        String systemLAFName = UIManager.getSystemLookAndFeelClassName();
        String currentLAFName = UIManager.getLookAndFeel().getClass().getName();
        boolean expected = systemLAFName.equals(currentLAFName);
        SystemUtilities systemUtils = new SystemUtilities();
        assertEquals(expected, systemUtils.isSystemLAF());
    }

    /**
     * Test the <code>isWindowsPlatform()</code> method.
     *
     * @since  v0.1
     */
    public void testIsWindowsPlatform() {
        boolean expected = System.getProperty("os.name").toLowerCase().startsWith("windows");
        SystemUtilities systemUtils = new SystemUtilities();
        assertEquals(expected, systemUtils.isWindowsPlatform());
    }
}
