package com.google.gwt.eclipse.core.launch.processors;

import com.google.gdt.eclipse.core.TestUtilities;
import com.google.gdt.eclipse.core.WebAppUtilities;
import com.google.gdt.eclipse.core.launch.LaunchConfigurationProcessorTestingHelper;
import com.google.gdt.eclipse.core.launch.LaunchConfigurationProcessorUtilities;
import com.google.gdt.eclipse.core.properties.WebAppProjectProperties;
import com.google.gwt.eclipse.core.projects.GwtEnablingProjectCreationParticipant;
import com.google.gwt.eclipse.core.runtime.GwtRuntimeTestUtilities;
import junit.framework.TestCase;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import java.util.List;

/**
 * Tests the {@link RemoteUiArgumentProcessor}.
 */
public class RemoteUiArgumentProcessorTest extends TestCase {

    private final LaunchConfigurationProcessorTestingHelper helper = new LaunchConfigurationProcessorTestingHelper();

    @Override
    public void setUp() throws Exception {
        TestUtilities.setUp();
        GwtRuntimeTestUtilities.addDefaultRuntime();
        helper.setUp(RemoteUiArgumentProcessorTest.class.getSimpleName(), new GwtEnablingProjectCreationParticipant());
    }

    public void testGwtShellSupportsRemoteUi() throws Exception {
        List<String> args = LaunchConfigurationProcessorUtilities.parseProgramArgs(helper.getLaunchConfig());
        WebAppProjectProperties.setWarSrcDir(helper.getProject(), new Path(""));
        assertFalse(WebAppUtilities.isWebApp(helper.getProject()));
        helper.getLaunchConfig().setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, GwtLaunchConfigurationProcessorUtilities.GWT_SHELL_MAIN_TYPE);
        assertTrue(args.indexOf("-remoteUI") >= 0);
        new RemoteUiArgumentProcessor().update(helper.getLaunchConfig(), JavaCore.create(helper.getProject()), args, null);
        assertTrue(args.indexOf("-remoteUI") >= 0);
    }

    @Override
    protected void tearDown() throws Exception {
        helper.tearDown();
    }
}
