package tests.com.ivis.xprocess.abbot.vcs;

import java.io.IOException;
import org.eclipse.swt.SWT;
import tests.com.ivis.xprocess.ui.util.DatasourceHelper;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.ProjectHelper;
import tests.com.ivis.xprocess.ui.util.SpecificHierarchy;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import tests.com.ivis.xprocess.ui.util.VCSHelper;
import abbot.finder.matchers.swt.TextMatcher;
import abbot.finder.swt.BasicFinder;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import abbot.tester.swt.Robot;
import abbot.tester.swt.WidgetTester;
import com.ivis.xprocess.ui.UIConstants;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.properties.VCSMessages;
import com.ivis.xprocess.ui.util.DatasourceUtil;

public class TestArtifactOpenDetection extends TestVCSInteraction {

    private static final String filePath1 = "C:\\temp\\AbbotFile1.rtf";

    private UITestUtil uitu;

    public TestArtifactOpenDetection(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uitu = UITestUtil.getInstance();
        uitu.ensureXProcessIsStarted();
    }

    protected void tearDown() throws Exception {
        VCSHelper.enableAutoCommit(true);
        UIPlugin.closeDataSource(null);
        VCSHelper.nukeTopLevelFolder(repositoryURL, USERNAME, PASSWORD, "testArtifactOpenDetection");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testArtifactOpenDetection");
        super.tearDown();
    }

    public void testArtifactCommitWithOpenArtifact() {
        try {
            Helper.abbotSystemOut("testArtifactCommitWithOpenArtifact");
            VCSHelper.enableAutoCommit(false);
            Helper.createTextFile(filePath1, "Name = $Name$");
            DatasourceHelper.switchToNewDatasource("testArtifactOpenDetection", repositoryURL + "testArtifactOpenDetection", true, false);
            uitu.delay(Helper.SHORT_DELAY);
            String projectName = Helper.getNewProjectName();
            ProjectHelper.createProject(projectName);
            String referenceName = Helper.getNewArtifactReferenceName();
            Helper.createArtifactReference(referenceName, Helper.getRootElementName() + "/" + projectName, null);
            VCSHelper.commitAll(true);
            ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName, referenceName);
            String artifactName = Helper.getNewArtifactName();
            Helper.addManagedFileToArtifactReference(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName, filePath1);
            uitu.delay(Helper.SHORT_DELAY);
            ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName);
            EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName);
            uitu.delay(5000);
            VCSHelper.commitAll(true);
        } catch (WidgetNotFoundException e) {
            e.printStackTrace();
        } catch (MultipleWidgetsFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            fail("Unable to create artifact file - " + e);
        }
    }

    public void testArtifactUpdateWithOpenArtifact() {
        try {
            Helper.abbotSystemOut("testArtifactUpdateWithOpenArtifact");
            VCSHelper.enableAutoCommit(false);
            UIPlugin.getDefault().getPreferenceStore().setValue(UIConstants.autoupdate, false);
            Helper.createTextFile(filePath1, "Name = $Name$");
            DatasourceHelper.switchToNewDatasource("testArtifactOpenDetection", repositoryURL + "testArtifactOpenDetection", true, false);
            String ds1 = UIPlugin.getDataSource().getName();
            String projectName = Helper.getNewProjectName();
            ProjectHelper.createProject(projectName);
            String referenceName = Helper.getNewArtifactReferenceName();
            Helper.createArtifactReference(referenceName, Helper.getRootElementName() + "/" + projectName, null);
            ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName, referenceName);
            String artifactName = Helper.getNewArtifactName();
            Helper.addManagedFileToArtifactReference(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName, filePath1);
            ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName);
            uitu.delay(Helper.MEDIUM_DELAY);
            VCSHelper.commitAll(true);
            DatasourceHelper.switchToNewDatasource("testArtifactOpenDetection2", repositoryURL + "testArtifactOpenDetection", true, true);
            String ds2 = UIPlugin.getDataSource().getName();
            DatasourceHelper.switchToExistingDatasource(ds1);
            uitu.delay(Helper.SHORT_DELAY);
            ProjectHelper.delayUntilSchedulingCompleted();
            EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName);
            uitu.delay(2000);
            uitu.getWidgetTester().actionKey('c', uitu.getDisplay());
            uitu.delay(Helper.SHORT_DELAY);
            uitu.getWidgetTester().actionKey(SWT.ALT | SWT.F4, uitu.getDisplay());
            uitu.delay(Helper.SHORT_DELAY);
            uitu.getWidgetTester().actionKey(SWT.CR, uitu.getDisplay());
            uitu.delay(Helper.SHORT_DELAY);
            uitu.getWidgetTester().actionKey(SWT.CR, uitu.getDisplay());
            uitu.delay(Helper.SHORT_DELAY);
            VCSHelper.commitAll(true);
            DatasourceHelper.switchToExistingDatasource(ds2);
            uitu.delay(Helper.MEDIUM_DELAY);
            EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + referenceName, artifactName);
            uitu.delay(5000);
            uitu.getWidgetTester().actionKey(SWT.ALT | ' ', uitu.getDisplay());
            uitu.delay(Helper.SHORT_DELAY);
            uitu.getWidgetTester().actionKey('N', uitu.getDisplay());
            uitu.delay(Helper.SHORT_DELAY);
            uitu.delay(Helper.SHORT_DELAY);
            VCSHelper.updateAll(false);
            WidgetTester.waitForFrameShowing(VCSMessages.vcs_error_dialog_title);
            Robot.syncExec(uitu.getDisplay(), null, new Runnable() {

                public void run() {
                    SpecificHierarchy specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedControl().getParent().getParent());
                    BasicFinder specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
                    try {
                        assertNotNull(specificFinder.find(new TextMatcher(VCSMessages.vcs_artifact_update_blocked)));
                    } catch (WidgetNotFoundException e) {
                        fail("Widget not found");
                    } catch (MultipleWidgetsFoundException e) {
                        fail("Multiple Widget not found");
                    }
                }
            });
            uitu.clickOKButton();
            uitu.delay(Helper.MEDIUM_DELAY);
        } catch (WidgetNotFoundException e) {
            e.printStackTrace();
        } catch (MultipleWidgetsFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            fail("Unable to create artifact file - " + e);
        }
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testArtifactOpenDetection2");
    }
}
