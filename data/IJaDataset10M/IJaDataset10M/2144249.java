package tests.com.ivis.xprocess.abbot.explorerview;

import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.widgets.TreeItem;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.OrganizationHelper;
import tests.com.ivis.xprocess.ui.util.ProjectHelper;
import tests.com.ivis.xprocess.ui.util.TaskHelper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;

public class TestOrderOfAppearanceInExplorerView extends ForkedPDETestCase {

    private UITestUtil uitu;

    public TestOrderOfAppearanceInExplorerView(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uitu = UITestUtil.getInstance();
        uitu.ensureXProcessIsStarted();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTaskOrder() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testTaskOrder");
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName1 = "a " + Helper.getNewTaskName();
        TaskHelper.createTask(taskName1, Helper.getRootElementName() + "/" + projectName);
        String taskName2 = "c " + Helper.getNewTaskName();
        TaskHelper.createTask(taskName2, Helper.getRootElementName() + "/" + projectName);
        String taskName3 = "b " + Helper.getNewTaskName();
        TaskHelper.createTask(taskName3, Helper.getRootElementName() + "/" + projectName);
        TreeItem itemOne = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName1);
        TreeItem itemTwo = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName2);
        TreeItem itemThree = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName3);
        int itemOneHeight = uitu.getTreeItemTester().getBounds(itemOne).y;
        int itemTwoHeight = uitu.getTreeItemTester().getBounds(itemTwo).y;
        int itemThreeHeight = uitu.getTreeItemTester().getBounds(itemThree).y;
        assertTrue("name " + taskName1 + " at " + itemOneHeight + " is before " + taskName3 + " at " + itemThreeHeight, itemOneHeight < itemThreeHeight);
        assertTrue("name " + taskName3 + " at " + itemThreeHeight + " is before " + taskName2 + " at " + itemTwoHeight, itemThreeHeight < itemTwoHeight);
        ExplorerTreeHelper.editTreeItemInplace(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName1, "zzzzzzz");
        ExplorerTreeHelper.refreshExplorerBar();
        itemOne = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + "zzzzzzz");
        itemTwo = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName2);
        itemThree = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName3);
        itemOneHeight = uitu.getTreeItemTester().getBounds(itemOne).y;
        itemTwoHeight = uitu.getTreeItemTester().getBounds(itemTwo).y;
        itemThreeHeight = uitu.getTreeItemTester().getBounds(itemThree).y;
        assertTrue("name " + taskName1 + " at " + itemOneHeight + " is after " + taskName2 + " at " + itemTwoHeight, itemOneHeight > itemTwoHeight);
        assertTrue("name " + taskName3 + " at " + itemThreeHeight + " is before " + taskName2 + " at " + itemTwoHeight, itemThreeHeight < itemTwoHeight);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName(), projectName));
    }

    public void testProjectOrder() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testProjectOrder");
        String projectName1 = "a " + Helper.getNewProjectName();
        ProjectHelper.createProject(projectName1);
        String projectName2 = "c " + Helper.getNewProjectName();
        ProjectHelper.createProject(projectName2);
        String projectName3 = "b " + Helper.getNewProjectName();
        ProjectHelper.createProject(projectName3);
        TreeItem itemOne = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName1);
        TreeItem itemTwo = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName2);
        TreeItem itemThree = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName3);
        int itemOneHeight = uitu.getTreeItemTester().getBounds(itemOne).y;
        int itemTwoHeight = uitu.getTreeItemTester().getBounds(itemTwo).y;
        int itemThreeHeight = uitu.getTreeItemTester().getBounds(itemThree).y;
        assertTrue("name " + projectName1 + " at " + itemOneHeight + " is before " + projectName3 + " at " + itemThreeHeight, itemOneHeight < itemThreeHeight);
        assertTrue("name " + projectName3 + " at " + itemThreeHeight + " is before " + projectName2 + " at " + itemTwoHeight, itemThreeHeight < itemTwoHeight);
        ExplorerTreeHelper.editTreeItemInplace(Helper.getRootElementName() + "/" + projectName1, "zzzzzzz");
        ExplorerTreeHelper.refreshExplorerBar();
        itemOne = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + "zzzzzzz");
        itemTwo = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName2);
        itemThree = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + projectName3);
        itemOneHeight = uitu.getTreeItemTester().getBounds(itemOne).y;
        itemTwoHeight = uitu.getTreeItemTester().getBounds(itemTwo).y;
        itemThreeHeight = uitu.getTreeItemTester().getBounds(itemThree).y;
        assertTrue("name " + projectName1 + " at " + itemOneHeight + " is after " + projectName2 + " at " + itemTwoHeight, itemOneHeight > itemTwoHeight);
        assertTrue("name " + projectName3 + " at " + itemThreeHeight + " is before " + projectName2 + " at " + itemTwoHeight, itemThreeHeight < itemTwoHeight);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + "zzzzzzz");
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName(), "zzzzzzz"));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName2);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName(), projectName2));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName3);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName(), projectName3));
    }

    public void testPersonOrder() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testPersonOrder");
        String personName1 = "a " + Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName1, OrganizationHelper.getDefaultOrganization());
        String personName2 = "c " + Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName2, OrganizationHelper.getDefaultOrganization());
        String personName3 = "b " + Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName3, OrganizationHelper.getDefaultOrganization());
        TreeItem itemOne = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName1);
        TreeItem itemTwo = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName2);
        TreeItem itemThree = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName3);
        int itemOneHeight = uitu.getTreeItemTester().getBounds(itemOne).y;
        int itemTwoHeight = uitu.getTreeItemTester().getBounds(itemTwo).y;
        int itemThreeHeight = uitu.getTreeItemTester().getBounds(itemThree).y;
        assertTrue("name " + personName1 + " at " + itemOneHeight + " is before " + personName3 + " at " + itemThreeHeight, itemOneHeight < itemThreeHeight);
        assertTrue("name " + personName3 + " at " + itemThreeHeight + " is before " + personName2 + " at " + itemTwoHeight, itemThreeHeight < itemTwoHeight);
        ExplorerTreeHelper.editTreeItemInplace(OrganizationHelper.getDefaultOrganization() + "/" + personName1, "zzzzzzz");
        ExplorerTreeHelper.refreshExplorerBar();
        itemOne = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + "zzzzzzz");
        itemTwo = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName2);
        itemThree = ExplorerTreeHelper.focusOnTreeItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName3);
        itemOneHeight = uitu.getTreeItemTester().getBounds(itemOne).y;
        itemTwoHeight = uitu.getTreeItemTester().getBounds(itemTwo).y;
        itemThreeHeight = uitu.getTreeItemTester().getBounds(itemThree).y;
        assertTrue("name " + personName1 + " at " + itemOneHeight + " is after " + personName2 + " at " + itemTwoHeight, itemOneHeight > itemTwoHeight);
        assertTrue("name " + personName3 + " at " + itemThreeHeight + " is before " + personName2 + " at " + itemTwoHeight, itemThreeHeight < itemTwoHeight);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName2);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName2));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName3);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName3));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + "zzzzzzz");
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), "zzzzzzz"));
    }
}
