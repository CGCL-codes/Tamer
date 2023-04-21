package org.openxava.test.tests;

import org.openxava.jpa.XPersistence;
import org.openxava.test.model.TreeItemTwo;
import org.openxava.util.Is;

/**
 * @author Federico Alcantara
 */
public class TreeViewTestNoDefaultPathTest extends TreeViewTestBase {

    public TreeViewTestNoDefaultPathTest(String testName) {
        super(testName, "TreeViewNoDefaultPath");
    }

    public void testIfActionsPresent() throws Exception {
        execute("Mode.detailAndFirst");
        assertNoErrors();
        assertAction("TreeView.new");
        assertAction("TreeView.removeSelected");
        assertAction("Print.generatePdf");
        assertAction("Print.generateExcel");
        assertAction("TreeView.up");
        assertAction("TreeView.down");
        assertAction("TreeView.left");
        assertAction("TreeView.right");
        execute("Mode.list");
    }

    public void testTreeStructure() throws Exception {
        execute("Mode.detailAndFirst");
        assertTreeViewRowCount("treeItemTwos", 7);
        assertValueInTreeViewIgnoreCase("treeItemTwos", 0, "ROOT ITEM 1");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 1, "CHILD ITEM 1");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 2, "SUBITEM 1 OF 1");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 3, "SUBITEM 2 OF 1");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 4, "CHILD ITEM 2");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 5, "CHILD ITEM 3");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 6, "SUBITEM 1 OF 3");
    }

    public void testTreeCRUD() throws Exception {
        execute("Mode.detailAndFirst");
        execute("TreeView.new", "viewObject=xava_view_treeItemTwos");
        setValue("description", "ROOT ITEM 2");
        execute("TreeView.save");
        assertNoErrors();
        assertValueInTreeViewIgnoreCase("treeItemTwos", 7, "ROOT ITEM 2");
        executeOnTreeViewItem("treeItemTwos", "Collection.edit", 7);
        assertValue("description", "ROOT ITEM 2");
        execute("TreeView.remove");
        assertNoErrors();
        executeOnTreeViewItem("treeItemTwos", "Collection.edit", 4);
        assertValue("description", "CHILD ITEM 2");
        setValue("description", "THIS CHILD 2");
        execute("TreeView.save");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 4, "THIS CHILD 2");
        executeOnTreeViewItem("treeItemTwos", "Collection.edit", 4);
        setValue("description", "CHILD ITEM 2");
        execute("TreeView.save");
        checkRowTreeView("treeItemTwos", 6);
        execute("TreeView.removeSelected", "viewObject=xava_view_treeItemTwos");
        assertNoErrors();
        assertTreeViewRowCount("treeItemTwos", 6);
        checkRowTreeView("treeItemTwos", 5);
        execute("TreeView.new", "viewObject=xava_view_treeItemTwos");
        setValue("description", "SUBITEM 1 OF CHILD ITEM 3");
        execute("TreeView.save");
        assertNoErrors();
        assertTreeViewRowCount("treeItemTwos", 7);
        TreeItemTwo parentItem = getTreeItemTwoByDescription("CHILD ITEM 3");
        TreeItemTwo item = getTreeItemTwoByDescription("SUBITEM 1 OF CHILD ITEM 3");
        String childPath = parentItem.getFolder() + "/" + parentItem.getId();
        assertEquals(childPath, item.getFolder());
        checkRowTreeView("treeItemTwos", 1);
        execute("TreeView.removeSelected", "viewObject=xava_view_treeItemTwos");
        assertNoErrors();
        assertTreeViewRowCount("treeItemTwos", 4);
    }

    public void testItemMove() throws Exception {
        execute("Mode.detailAndFirst");
        TreeItemTwo item = getTreeItemTwoByDescription("CHILD ITEM 2");
        String originalPath = item.getFolder();
        assertNotEquals("path should not be empty", "[" + originalPath + "]", "[]");
        checkRowTreeView("treeItemTwos", 4);
        execute("TreeView.left", "viewObject=xava_view_treeItemTwos");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 6, "CHILD ITEM 2");
        TreeItemTwo itemMoved = getTreeItemTwoByDescription("CHILD ITEM 2");
        String path = itemMoved.getFolder();
        if (Is.emptyString(path)) {
            path = "";
        } else {
            System.out.println(path);
            fail("path should be empty");
        }
        assertNotEquals("path should be different than before moving it", "[" + path + "]", "[" + originalPath + "]");
        checkRowTreeView("treeItemTwos", 6);
        execute("TreeView.right", "viewObject=xava_view_treeItemTwos");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 6, "CHILD ITEM 2");
        itemMoved = getTreeItemTwoByDescription("CHILD ITEM 2");
        assertNotEquals("path should not be empty", "[" + itemMoved.getFolder() + "]", "[]");
        checkRowTreeView("treeItemTwos", 6);
        execute("TreeView.up", "viewObject=xava_view_treeItemTwos");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 4, "CHILD ITEM 2");
        checkRowTreeView("treeItemTwos", 4);
        execute("TreeView.down", "viewObject=xava_view_treeItemTwos");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 6, "CHILD ITEM 2");
        checkRowTreeView("treeItemTwos", 1);
        execute("TreeView.down", "viewObject=xava_view_treeItemTwos");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 3, "CHILD ITEM 1");
        assertValueInTreeViewIgnoreCase("treeItemTwos", 5, "SUBITEM 2 OF 1");
        checkRowTreeView("treeItemTwos", 3);
        execute("TreeView.left", "viewObject=xava_view_treeItemTwos");
        item = getTreeItemTwoByDescription("CHILD ITEM 1");
        assertEquals("path should be empty", "[" + item.getFolder() + "]", "[]");
        itemMoved = getTreeItemTwoByDescription("SUBITEM 1 OF 1");
        assertEquals("It is not a child of Item 1", "[" + itemMoved.getFolder() + "]", "[/" + item.getId() + "]");
        itemMoved = getTreeItemTwoByDescription("SUBITEM 2 OF 1");
        assertEquals("It is not a child of Item 1", "[" + itemMoved.getFolder() + "]", "[/" + item.getId() + "]");
    }

    private TreeItemTwo getTreeItemTwoByDescription(String description) throws Exception {
        TreeItemTwo returnValue = (TreeItemTwo) XPersistence.getManager().createQuery("select t from TreeItemTwo t where " + "t.description=:description").setParameter("description", description).getSingleResult();
        XPersistence.getManager().refresh(returnValue);
        return returnValue;
    }
}
