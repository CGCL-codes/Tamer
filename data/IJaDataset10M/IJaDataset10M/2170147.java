package net.sf.refactorit.test.loader;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.parser.FastJavaLexer;
import net.sf.refactorit.query.AbstractIndexer;
import net.sf.refactorit.test.ProjectMetadata;
import net.sf.refactorit.test.Utils;
import org.apache.log4j.Category;
import java.util.Iterator;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Tests how loading projects works.
 */
public class ProjectLoadTest extends TestCase {

    /** Logger instance. */
    private static final Category cat = Category.getInstance(ProjectLoadTest.class.getName());

    /** Project metadata. */
    private final ProjectMetadata metadata;

    private int oldJvmMode = FastJavaLexer.JVM_14;

    private static final AbstractIndexer indexer = new AbstractIndexer() {
    };

    /** Creates new ProjectTest */
    public ProjectLoadTest(ProjectMetadata metadata) {
        super("Project \"" + metadata.getName() + "\" loading test");
        this.metadata = metadata;
    }

    public static Test suite() throws Exception {
        final TestSuite suite = new TestSuite("Source loading via Project");
        for (final Iterator i = Utils.getTestProjects().getProjects().iterator(); i.hasNext(); ) {
            final ProjectMetadata data = (ProjectMetadata) i.next();
            if (data.isTestForLoad() && !"RefactorIT".equals(data.getId())) {
                suite.addTest(new ProjectLoadTest(data));
            }
        }
        return suite;
    }

    protected void setUp() throws Exception {
        oldJvmMode = Project.getDefaultOptions().getJvmMode();
        Project.getDefaultOptions().setJvmMode(this.metadata.getJvmMode());
    }

    protected void tearDown() throws Exception {
        Project.getDefaultOptions().setJvmMode(oldJvmMode);
    }

    /** Tests whether project loads properly. */
    private void test() throws Exception {
        cat.info("Testing whether project " + metadata + " loads properly");
        cat.debug("Creating project");
        final Project project = Utils.createTestRbProject(metadata);
        processProject(project);
    }

    /**
   * Proceses project build and searches errors in project
   * @param project
   * @throws Exception
   */
    public static void processProject(Project project) throws Exception {
        cat.debug("Source path:" + project.getPaths().getSourcePath());
        cat.debug("Class path:" + project.getPaths().getClassPath());
        final long timeBefore = System.currentTimeMillis();
        project.getProjectLoader().build();
        final long timeAfter = System.currentTimeMillis();
        cat.debug("Project " + project.getName() + " loaded in " + (timeAfter - timeBefore) + " ms");
        final long timeBeforeIndexing = System.currentTimeMillis();
        indexer.visit(project);
        final long timeAfterIndexing = System.currentTimeMillis();
        cat.debug("Project " + project.getName() + " indexed in " + (timeAfterIndexing - timeBeforeIndexing) + " ms");
        final Iterator errors = (project.getProjectLoader().getErrorCollector()).getUserFriendlyErrors();
        if (errors.hasNext()) {
            String message = "Build errors in project [" + project.getPaths().getSourcePath() + "]" + ":\n";
            int errorIndex = 0;
            while (errors.hasNext()) {
                final Object exception = errors.next();
                message += "Error #" + (errorIndex + 1) + ": " + exception + "\n";
                errorIndex++;
            }
            cat.error(message);
            fail(message);
        } else if ((project.getProjectLoader().getErrorCollector()).hasCriticalUserErrors()) {
            cat.error("Project has critical user errors");
            fail("Project has critical user errors");
        }
        cat.info("SUCCESS");
    }

    /***** Test interface implementation *****/
    public int countTestCases() {
        return 1;
    }

    public void run(TestResult result) {
        if (result.shouldStop()) {
            return;
        }
        result.startTest(this);
        try {
            setUp();
            test();
        } catch (AssertionFailedError e) {
            result.addFailure(this, e);
        } catch (Exception e) {
            result.addError(this, e);
        } finally {
            try {
                tearDown();
            } catch (Exception e) {
                result.addError(this, e);
            }
            result.endTest(this);
        }
    }
}
