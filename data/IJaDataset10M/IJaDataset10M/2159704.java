package com.google.gwt.debug.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Test Case for {@link DebugInfo} when <code>gwt.enableDebugId</code> is enabled.
 */
public class DebugInfoTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.google.gwt.user.DebugTest";
    }

    /**
   * Test that the {@link DebugInfo#isDebugIdEnabled()} method works correctly.
   */
    public void testIsDebugIdEnabled() {
        assertTrue(DebugInfo.isDebugIdEnabled());
    }
}
