package org.wtc.eclipse.platform.tests.helpers;

import com.windowtester.runtime.IUIContext;
import org.wtc.eclipse.platform.helpers.EclipseHelperFactory;
import org.wtc.eclipse.platform.helpers.IJavaProjectHelper;
import org.wtc.eclipse.platform.helpers.IWorkbenchHelper;
import org.wtc.eclipse.platform.helpers.IWorkbenchHelper.Perspective;
import org.wtc.eclipse.platform.tests.EclipseUITest;

/**
 * Tests a dialog popping as a result of another dialog closing.
 *
 * <p>EXPECTED: Click "No" and continue; Test should pass</p>
 */
public class WorkbenchHelperSwitchPerspectiveDialogTest extends EclipseUITest {

    /**
     * Create a resource that will ask for a perspective switch.
     */
    public void testSwitchPerspective() {
        IUIContext ui = getUI();
        IWorkbenchHelper workbench = EclipseHelperFactory.getWorkbenchHelper();
        workbench.openPerspective(ui, Perspective.RESOURCE);
        workbench.listenForDialogOpenPerspective(ui);
        IJavaProjectHelper javaProject = EclipseHelperFactory.getJavaProjectHelper();
        javaProject.createProject(ui, "switchPerspectiveTest");
        ui.pause(5000);
    }
}
