package com.google.gwt.eclipse.core.util;

import com.google.gwt.eclipse.core.test.AbstractGWTPluginTestCase;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import java.text.MessageFormat;

/**
 * Test cases for the {@link Util} class.
 */
public class UtilTest extends AbstractGWTPluginTestCase {

    public void testGetProject() {
        String entryPointPath = MessageFormat.format("/{0}/src/{1}/{2}.java", AbstractGWTPluginTestCase.TEST_PROJECT_NAME, AbstractGWTPluginTestCase.TEST_PROJECT_SRC_PACKAGE.replace('.', '/'), AbstractGWTPluginTestCase.TEST_PROJECT_ENTRY_POINT);
        IProject project = Util.getProject(new Path(entryPointPath));
        assertEquals(AbstractGWTPluginTestCase.TEST_PROJECT_NAME, project.getName());
        assertNull(Util.getProject(new Path(AbstractGWTPluginTestCase.TEST_PROJECT_NAME + "/src/com/hello/Hello.java")));
        assertNull(Util.getProject(new Path("/NonExistentProject/src/com/hello/Hello.java")));
    }

    public void testGetWorkspaceRoot() {
        assertNotNull(Util.getWorkspaceRoot());
    }

    @Override
    protected boolean requiresTestProject() {
        return true;
    }
}
