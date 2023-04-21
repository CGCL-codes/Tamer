package com.google.gwt.eclipse.core.runtime;

import com.google.gdt.eclipse.core.TestUtilities;
import com.google.gwt.eclipse.core.preferences.GWTPreferences;
import junit.framework.TestCase;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * Tests for the {@link GwtCapabilityChecker}.
 */
public class GwtCapabilityCheckerTest extends TestCase {

    public void testCapabilitiesOfGwtProjectsRuntime() throws CoreException {
        GwtRuntimeTestUtilities.importGwtSourceProjects();
        try {
            GWTRuntime gwtRuntime = GWTRuntime.getFactory().newInstance("GWT Projects", ResourcesPlugin.getWorkspace().getRoot().getLocation());
            assertLatestGwtCapabilities(gwtRuntime);
        } finally {
            GwtRuntimeTestUtilities.removeGwtSourceProjects();
        }
    }

    public void testCapabilitiesOfLatestGwtSdk() throws Exception {
        GwtRuntimeTestUtilities.addDefaultRuntime();
        try {
            assertLatestGwtCapabilities(GWTPreferences.getDefaultRuntime());
        } finally {
            GwtRuntimeTestUtilities.removeDefaultRuntime();
        }
    }

    @Override
    protected void setUp() throws Exception {
        TestUtilities.setUp();
    }

    private void assertLatestGwtCapabilities(GWTRuntime gwtRuntime) {
        GwtCapabilityChecker checker = new GwtCapabilityChecker(gwtRuntime);
        assertTrue(checker.doesCompilerAllowMultipleModules());
    }
}
