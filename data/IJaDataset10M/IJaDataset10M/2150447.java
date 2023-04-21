package net.sourceforge.pmd.runtime.cmd;

import java.io.InputStream;
import java.util.Map;
import junit.framework.TestCase;
import name.herlin.command.CommandException;
import name.herlin.command.UnsetInputPropertiesException;
import net.sourceforge.pmd.eclipse.EclipseUtils;
import net.sourceforge.pmd.runtime.cmd.ReviewCodeCmd;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * This tests the PMD Processor command
 * 
 * @author Philippe Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2006/06/18 22:29:51  phherlin
 * Begin refactoring the unit tests for the plugin
 *
 * Revision 1.3  2006/01/17 21:26:24  phherlin
 * Ignore exceptions occuring inside the teardown operation
 *
 * Revision 1.2  2005/12/30 16:29:15  phherlin
 * Implement a new preferences model and review some tests
 *
 * Revision 1.1  2005/06/15 21:14:57  phherlin
 * Create the project for the Eclipse plugin unit tests
 *
 *  
 */
public class ReviewCmdTest extends TestCase {

    private IProject testProject;

    /**
     * Test case constructor
     * 
     * @param name
     *            of the test case
     */
    public ReviewCmdTest(String name) {
        super(name);
    }

    /**
     * Test the basic usage of the processor command
     *  
     */
    public void testReviewCmdBasic() throws CommandException, CoreException {
        ReviewCodeCmd cmd = new ReviewCodeCmd();
        cmd.addResource(this.testProject);
        cmd.performExecute();
        cmd.join();
        Map markers = cmd.getMarkers();
        assertNotNull(markers);
        assertTrue("Report size = " + markers.size(), markers.size() > 0);
    }

    /**
     * The ReviewCodeCmd must also work on a ResourceDelta 
     * @throws CommandException
     */
    public void testReviewCmdDelta() throws CommandException {
    }

    /**
     * Normally a null resource and a null resource delta is not acceptable.
     * @throws CommandException
     */
    public void testReviewCmdNullResource() throws CommandException {
        try {
            ReviewCodeCmd cmd = new ReviewCodeCmd();
            cmd.addResource(null);
            cmd.setResourceDelta(null);
            cmd.performExecute();
            fail("An Exception must be thrown");
        } catch (UnsetInputPropertiesException e) {
            fail("An IllegalArgumentException must have been thrown before");
        } catch (IllegalArgumentException e) {
            ;
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testProject = EclipseUtils.createJavaProject("PMDTestProject");
        assertTrue("A test project cannot be created; the tests cannot be performed.", (this.testProject != null) && this.testProject.exists() && this.testProject.isAccessible());
        IFile testFile = EclipseUtils.createTestSourceFile(this.testProject);
        InputStream is = EclipseUtils.getResourceStream(this.testProject, "/Test.java");
        assertNotNull("Cannot find the test source file", is);
        is.close();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        try {
            if (this.testProject != null) {
                if (this.testProject.exists() && this.testProject.isAccessible()) {
                    EclipseUtils.removePMDNature(this.testProject);
                }
            }
            super.tearDown();
        } catch (Exception e) {
            System.out.println("Exception " + e.getClass().getName() + " when tearing down. Ignored.");
        }
    }
}
