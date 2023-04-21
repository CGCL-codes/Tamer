package com.google.gdt.eclipse.suite.launch.processors;

import com.google.appengine.eclipse.core.projects.GaeEnablingProjectCreationParticipant;
import com.google.appengine.eclipse.core.sdk.GaeSdkTestUtilities;
import com.google.gdt.eclipse.core.TestUtilities;
import com.google.gdt.eclipse.core.WebAppUtilities;
import com.google.gdt.eclipse.core.launch.LaunchConfigurationProcessorTestingHelper;
import com.google.gdt.eclipse.core.launch.LaunchConfigurationProcessorUtilities;
import com.google.gdt.eclipse.core.properties.WebAppProjectProperties;
import com.google.gwt.eclipse.core.launch.processors.GwtLaunchConfigurationProcessorUtilities;
import com.google.gwt.eclipse.core.projects.GwtEnablingProjectCreationParticipant;
import com.google.gwt.eclipse.core.runtime.GwtRuntimeTestUtilities;
import junit.framework.TestCase;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import java.util.List;

/**
 * Tests the {@link WarArgumentProcessor}.
 */
public class WarArgumentProcessorTest extends TestCase {

    private final LaunchConfigurationProcessorTestingHelper helper = new LaunchConfigurationProcessorTestingHelper();

    @Override
    public void setUp() throws Exception {
        TestUtilities.setUp();
        GaeSdkTestUtilities.addDefaultSdk();
        GwtRuntimeTestUtilities.addDefaultRuntime();
        helper.setUp(WarArgumentProcessorTest.class.getSimpleName(), new GwtEnablingProjectCreationParticipant(), new GaeEnablingProjectCreationParticipant());
    }

    public void testWarArgPresenceForNonWebAppProject() throws Exception {
        WebAppProjectProperties.setWarSrcDir(helper.getProject(), new Path(""));
        assertFalse(WebAppUtilities.isWebApp(helper.getProject()));
        List<String> args = LaunchConfigurationProcessorUtilities.parseProgramArgs(helper.getLaunchConfig());
        assertTrue(args.indexOf("-war") >= 0);
        new WarArgumentProcessor().update(helper.getLaunchConfig(), JavaCore.create(helper.getProject()), args, null);
        assertTrue(args.indexOf("-war") >= 0);
        helper.getLaunchConfig().setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, GwtLaunchConfigurationProcessorUtilities.GWT_SHELL_MAIN_TYPE);
        assertTrue(args.indexOf("-war") >= 0);
        new WarArgumentProcessor().update(helper.getLaunchConfig(), JavaCore.create(helper.getProject()), args, null);
        assertFalse(args.indexOf("-war") >= 0);
    }

    @Override
    protected void tearDown() throws Exception {
        helper.tearDown();
    }
}
