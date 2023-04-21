package tests.com.ivis.xprocess.abbot.creation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import tests.com.ivis.xprocess.ui.util.DatasourceHelper;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.ProcessHelper;
import tests.com.ivis.xprocess.ui.util.ProjectHelper;
import tests.com.ivis.xprocess.ui.util.TaskHelper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import com.ivis.xprocess.core.Artifact;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.datawrappers.process.ArtifactWrapper;
import com.ivis.xprocess.ui.properties.ElementMessages;
import com.ivis.xprocess.ui.util.TestHarness;

public class TestNewArtifactReference extends ForkedPDETestCase {

    private static final int SHORT_DELAY = 300;

    private UITestUtil uitu;

    public TestNewArtifactReference(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uitu = UITestUtil.getInstance();
        uitu.ensureXProcessIsStarted();
    }

    public void testArtifactReferenceCreation() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testArtifactReferenceCreation");
        String ds1 = UIPlugin.getDataSource().getName();
        String projectName = "Test AR Project";
        ProjectHelper.createProject(projectName);
        String artifactReferenceName = Helper.getNewArtifactReferenceName();
        Helper.createArtifactReference(artifactReferenceName, Helper.getRootElementName() + "/" + projectName, "");
        assertTrue("Has the Artifact Reference been created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName, artifactReferenceName));
        EditorHelper.switchToTab("ARTIFACTSTAB");
        Tree artifactsTree = (Tree) EditorHelper.getWidgetFromEditor(TestHarness.ARTIFACTS_TAB_TREE, projectName);
        assertEquals(1, uitu.getTreeTester().getItemCount(artifactsTree));
        DatasourceHelper.reloadDatasource(ds1);
        assertTrue("Has the Artifact Reference been reloaded", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName, artifactReferenceName));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName + "/" + artifactReferenceName);
        EditorHelper.openEditor(Helper.getRootElementName(), projectName);
        EditorHelper.switchToTab("ARTIFACTSTAB");
        artifactsTree = (Tree) EditorHelper.getWidgetFromEditor(TestHarness.ARTIFACTS_TAB_TREE, projectName);
        assertEquals(0, uitu.getTreeTester().getItemCount(artifactsTree));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }

    public void testManageReferenceDialog() throws IOException, WidgetNotFoundException, MultipleWidgetsFoundException {
        String filePath = "C:\\temp\\AbbotFile.txt";
        Helper.createTextFile(filePath, "Testing that we can open the artifact from inside the Manage Reference dialog.");
        uitu.ensureDatasourceAvailable("testManageReferenceDialog");
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        Text text = (Text) EditorHelper.getWidgetFromEditor("NAME", projectName);
        assertEquals(projectName, uitu.getTextTester().getText(text));
        uitu.delay(SHORT_DELAY);
        String artifactName = Helper.getNewArtifactName();
        ProjectHelper.createArtifact(Helper.getRootElementName() + "/" + projectName, artifactName, ElementMessages.artifact_managed_file, filePath);
        uitu.delay(SHORT_DELAY);
        assertTrue("Has the Artifact been created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName, artifactName));
        String artifactReferenceName = Helper.getNewArtifactReferenceName();
        Helper.createArtifactReference(artifactReferenceName, Helper.getRootElementName() + "/" + projectName, artifactName);
        assertTrue("Has the Artifact Reference been created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName, artifactReferenceName));
        assertTrue("Has the Artifact been linked", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + artifactReferenceName, artifactName));
        EditorHelper.switchToTab("ARTIFACTSTAB");
        Tree artifactsTree = (Tree) EditorHelper.getWidgetFromEditor(TestHarness.ARTIFACTS_TAB_TREE, projectName);
        assertEquals(2, uitu.getTreeTester().getItemCount(artifactsTree));
        TreeItem treeItem1 = uitu.getTreeTester().getItems(artifactsTree)[0];
        assertEquals(artifactName + " - " + projectName, uitu.getTreeItemTester().getText(treeItem1));
        TreeItem treeItem2 = uitu.getTreeTester().getItems(artifactsTree)[1];
        assertEquals(artifactReferenceName, uitu.getTreeItemTester().getText(treeItem2));
        assertEquals(1, uitu.getTreeItemTester().getItemCount(treeItem2));
        uitu.getTreeItemTester().actionClick(treeItem2);
        uitu.getTreeItemTester().actionKey(SWT.ARROW_RIGHT, uitu.getDisplay());
        TreeItem treeItem3 = uitu.getTreeItemTester().getItems(treeItem2)[0];
        assertEquals(artifactName + " - " + projectName, uitu.getTreeItemTester().getText(treeItem3));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }

    public void testStringSubstitutionInTaskPatternInstantiation() throws Exception {
        String filePath = "C:\\temp\\AbbotFile.txt";
        File tempfile = new File(filePath);
        Helper.createTextFile(filePath, "Name = $Name$");
        uitu.ensureDatasourceAvailable("testStringSubstitutionInTaskPatternInstantiation");
        String patternName = Helper.getNewPatternName();
        String processName = Helper.getRootProcess();
        String projectName = Helper.getNewProjectName();
        String taskName2 = Helper.getNewTaskName();
        String taskName = "$Name$";
        String referenceName = Helper.getNewArtifactReferenceName();
        String artifactName = Helper.getNewArtifactName();
        ProcessHelper.createPattern(patternName, processName);
        TaskHelper.createTaskInPattern(taskName, Helper.getRootElementName() + "/" + processName + "/" + patternName);
        Helper.createManagedFileArtifactOwnedByPrototype(processName, patternName, taskName, referenceName, artifactName, filePath);
        uitu.delay(Helper.MEDIUM_DELAY);
        ProjectHelper.createProject(projectName);
        TaskHelper.addPattern(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), patternName);
        TaskHelper.createTaskFromPattern(taskName2, projectName, patternName);
        ProjectHelper.delayUntilSchedulingCompleted();
        uitu.delay(Helper.MEDIUM_DELAY);
        TreeItem fileWidget = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName2 + "/" + referenceName + "/" + artifactName);
        ArtifactWrapper fileObject = (ArtifactWrapper) uitu.getTreeItemTester().getData(fileWidget);
        Artifact artifact = (Artifact) fileObject.getElement();
        String path = artifact.resolveArtifactPath();
        File file = new File(path);
        String expectedString = "Name = " + taskName2;
        String string = null;
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        while ((str = in.readLine()) != null) {
            string = str;
        }
        in.close();
        Helper.deleteDir(tempfile);
        assertEquals(expectedString, string);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + patternName);
    }

    public void testStringSubstitutionInTaskPatternMultipleInstantiation() throws Exception {
        String filePath = "C:\\temp\\AbbotFile.txt";
        File tempfile = new File(filePath);
        Helper.createTextFile(filePath, "Name = $Name$");
        uitu.ensureDatasourceAvailable("testStringSubstitutionInTaskPatternMultipleInstantiation");
        String patternName = Helper.getNewPatternName();
        String processName = Helper.getRootProcess();
        String projectName = Helper.getNewProjectName();
        String taskName1 = Helper.getNewTaskName();
        String taskName2 = Helper.getNewTaskName();
        String taskName3 = Helper.getNewTaskName();
        String taskName = "$Name$";
        String referenceName = Helper.getNewArtifactReferenceName();
        String artifactName = Helper.getNewArtifactName();
        ProcessHelper.createPattern(patternName, processName);
        TaskHelper.createTaskInPattern(taskName, Helper.getRootElementName() + "/" + processName + "/" + patternName);
        Helper.createManagedFileArtifactOwnedByPrototype(processName, patternName, taskName, referenceName, artifactName, filePath);
        ProjectHelper.createProject(projectName);
        TaskHelper.addPattern(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), patternName);
        String taskNames = taskName1 + "," + taskName2 + "," + taskName3;
        TaskHelper.createTaskFromPattern(taskNames, projectName, patternName, true);
        ProjectHelper.delayUntilSchedulingCompleted();
        uitu.delay(Helper.LONG_DELAY);
        TreeItem fileWidget = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName1 + "/" + referenceName + "/" + artifactName);
        ArtifactWrapper fileObject = (ArtifactWrapper) uitu.getTreeItemTester().getData(fileWidget);
        Artifact artifact = (Artifact) fileObject.getElement();
        String path = artifact.resolveArtifactPath();
        File file = new File(path);
        String expectedString = "Name = " + taskName1;
        String string = null;
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        while ((str = in.readLine()) != null) {
            string = str;
        }
        in.close();
        Helper.deleteDir(tempfile);
        assertEquals(expectedString, string);
        fileWidget = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName2 + "/" + referenceName + "/" + artifactName);
        fileObject = (ArtifactWrapper) uitu.getTreeItemTester().getData(fileWidget);
        artifact = (Artifact) fileObject.getElement();
        path = artifact.resolveArtifactPath();
        file = new File(path);
        expectedString = "Name = " + taskName2;
        string = null;
        in = new BufferedReader(new FileReader(file));
        while ((str = in.readLine()) != null) {
            string = str;
        }
        in.close();
        Helper.deleteDir(tempfile);
        assertEquals(expectedString, string);
        fileWidget = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName3 + "/" + referenceName + "/" + artifactName);
        fileObject = (ArtifactWrapper) uitu.getTreeItemTester().getData(fileWidget);
        artifact = (Artifact) fileObject.getElement();
        path = artifact.resolveArtifactPath();
        file = new File(path);
        expectedString = "Name = " + taskName3;
        string = null;
        in = new BufferedReader(new FileReader(file));
        while ((str = in.readLine()) != null) {
            string = str;
        }
        in.close();
        Helper.deleteDir(tempfile);
        assertEquals(expectedString, string);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }
}
