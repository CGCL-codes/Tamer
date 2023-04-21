package org.rubypeople.rdt.internal.debug.ui.tests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.rubypeople.rdt.internal.debug.core.model.RubyStackFrame;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiPlugin;
import org.rubypeople.rdt.internal.debug.ui.RubySourceLocator;

public class TC_RubySourceLocator extends TestCase {

    public TC_RubySourceLocator(String name) {
        super(name);
    }

    protected void setUp() {
    }

    public void testWorkspaceInternalFile() throws Exception {
        Workspace workspace = (Workspace) RdtDebugUiPlugin.getWorkspace();
        Project p = new TestProject("/SourceLocatorTest", workspace);
        p.create(null);
        p.open(null);
        IPath filePath = new Path("/SourceLocatorTest/test.rb");
        IFile file = RdtDebugUiPlugin.getWorkspace().getRoot().getFile(filePath);
        file.create(new ByteArrayInputStream(new byte[0]), true, null);
        String fullPath = workspace.getRoot().getLocation().toOSString() + File.separator + "SourceLocatorTest/test.rb";
        RubySourceLocator sourceLocator = new RubySourceLocator();
        RubyStackFrame rubyStackFrame = new RubyStackFrame(null, fullPath, 5, 1);
        Object sourceElement = sourceLocator.getSourceElement(rubyStackFrame);
        IEditorInput input = sourceLocator.getEditorInput(sourceElement);
        assertNotNull(input);
        assertTrue(input.exists());
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, sourceLocator.getEditorId(input, sourceElement));
    }

    public void testWorkspaceExternalFile() throws Exception {
        File tmpFile = File.createTempFile("rubyfile", null);
        RubyStackFrame rubyStackFrame = new RubyStackFrame(null, tmpFile.getAbsolutePath(), 5, 1);
        RubySourceLocator sourceLocator = new RubySourceLocator();
        Object sourceElement = sourceLocator.getSourceElement(rubyStackFrame);
        IEditorInput input = sourceLocator.getEditorInput(sourceElement);
        assertNotNull(input);
        assertTrue(input.exists());
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, sourceLocator.getEditorId(input, sourceElement));
        Workspace workspace = (Workspace) RdtDebugUiPlugin.getWorkspace();
        Project p = new TestProject("/WorkingDirIsProject", workspace);
        p.create(null);
        p.open(null);
        sourceLocator.initializeDefaults(new LaunchConfiguration(workspace.getRoot().getLocation().toOSString() + File.separator + "WorkingDirIsProject"));
        sourceElement = sourceLocator.getSourceElement(rubyStackFrame);
        input = sourceLocator.getEditorInput(sourceElement);
        assertNotNull(input);
        assertTrue(input.exists());
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, sourceLocator.getEditorId(input, sourceElement));
        String workspacePath = workspace.getRoot().getLocation().toOSString();
        File externalFile = new File(workspacePath + File.separator + "externalRelativeRubyFile.rb");
        assertTrue(externalFile.createNewFile());
        rubyStackFrame = new RubyStackFrame(null, "../externalRelativeRubyFile.rb", 5, 1);
        sourceElement = sourceLocator.getSourceElement(rubyStackFrame);
        input = sourceLocator.getEditorInput(sourceElement);
        assertNotNull(input);
        assertTrue(input.exists());
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, sourceLocator.getEditorId(input, sourceElement));
    }

    public void testNotExistingFile() throws Exception {
        RubyStackFrame rubyStackFrame = new RubyStackFrame(null, "/tmp/nonexistingtestfile", 5, 1);
        RubySourceLocator sourceLocator = new RubySourceLocator();
        Object sourceElement = sourceLocator.getSourceElement(rubyStackFrame);
        IEditorInput input = sourceLocator.getEditorInput(sourceElement);
        assertNull(input);
    }

    public class TestProject extends Project {

        public TestProject(String aName, Workspace aWorkspace) {
            super(new Path(aName), aWorkspace);
        }
    }

    public class LaunchConfiguration implements ILaunchConfiguration {

        String workingDir;

        public LaunchConfiguration(String pDir) {
            workingDir = pDir;
        }

        public boolean contentsEqual(ILaunchConfiguration configuration) {
            return false;
        }

        public ILaunchConfigurationWorkingCopy copy(String name) throws CoreException {
            return null;
        }

        public void delete() throws CoreException {
        }

        public boolean exists() {
            return false;
        }

        public boolean getAttribute(String attributeName, boolean defaultValue) throws CoreException {
            return false;
        }

        public int getAttribute(String attributeName, int defaultValue) throws CoreException {
            return 0;
        }

        public List getAttribute(String attributeName, List defaultValue) throws CoreException {
            return null;
        }

        public Map getAttribute(String attributeName, Map defaultValue) throws CoreException {
            return null;
        }

        public String getAttribute(String attributeName, String defaultValue) throws CoreException {
            return workingDir;
        }

        public Map getAttributes() throws CoreException {
            return null;
        }

        public String getCategory() throws CoreException {
            return null;
        }

        public IFile getFile() {
            return null;
        }

        public IPath getLocation() {
            return null;
        }

        public String getMemento() throws CoreException {
            return null;
        }

        public String getName() {
            return null;
        }

        public ILaunchConfigurationType getType() throws CoreException {
            return null;
        }

        public ILaunchConfigurationWorkingCopy getWorkingCopy() throws CoreException {
            return null;
        }

        public boolean isLocal() {
            return false;
        }

        public boolean isWorkingCopy() {
            return false;
        }

        public ILaunch launch(String mode, IProgressMonitor monitor) throws CoreException {
            return null;
        }

        public boolean supportsMode(String mode) throws CoreException {
            return false;
        }

        public Object getAdapter(Class adapter) {
            return null;
        }

        public ILaunch launch(String mode, IProgressMonitor monitor, boolean build) throws CoreException {
            return null;
        }

        public ILaunch launch(String mode, IProgressMonitor monitor, boolean build, boolean register) throws CoreException {
            return null;
        }
    }
}
