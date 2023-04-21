package com.thoughtworks.fireworks.core.tree;

import com.thoughtworks.fireworks.stubs.SuccessTestShadow;
import com.thoughtworks.shadow.ComparableTestShadow;
import com.thoughtworks.shadow.ShadowCabinet;
import com.thoughtworks.shadow.TestShadow;
import com.thoughtworks.shadow.TestShadowResult;
import com.thoughtworks.shadow.junit.IgnoredTestCase;
import com.thoughtworks.turtlemock.Mock;
import com.thoughtworks.turtlemock.Turtle;
import com.thoughtworks.turtlemock.constraint.CheckResult;
import com.thoughtworks.turtlemock.constraint.Constraint;
import junit.framework.TestCase;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

public class ShadowTreeModelAsTestListenerTest extends TestCase {

    private ShadowTreeModel model;

    private ComparableTestShadow shadow;

    private ShadowClassTreeNode child;

    private Mock listener;

    private ShadowSummaryTreeNode root;

    private TestShadow grandchildTest;

    private ShadowMethodTreeNode grandchild;

    private ShadowCabinet cabinet;

    private TestShadowResult result;

    protected void setUp() throws Exception {
        root = new ShadowSummaryTreeNode();
        model = new ShadowTreeModel(root);
        grandchildTest = new SuccessTestShadow();
        shadow = new ComparableTestShadow(grandchildTest);
        child = new ShadowClassTreeNode(shadow, root);
        grandchild = new ShadowMethodTreeNode(grandchildTest, child);
        listener = Turtle.mock(TreeModelListener.class);
        cabinet = new ShadowCabinet();
        cabinet.addListener(model);
        result = new TestShadowResult();
        result.addListener(model);
    }

    public void testShouldAddTestIgnoredAsChildOfTestShadow() throws Exception {
        grandchildTest = new IgnoredTestCase("", "");
        shadow = new ComparableTestShadow(grandchildTest);
        child = new ShadowClassTreeNode(shadow, root);
        grandchild = new ShadowMethodTreeNode(grandchildTest, child);
        cabinet.add(shadow);
        cabinet.action(result);
        assertEquals(1, model.getChildCount(child));
    }

    public void testShouldAddTestAsChildOfTestShadowWhichIsAddBeforeByAfterAddTestEvent() throws Exception {
        cabinet.add(shadow);
        cabinet.action(result);
        assertEquals(1, model.getChildCount(child));
        assertEquals(grandchild, model.getChild(child, 0));
        assertEquals(0, model.getIndexOfChild(child, grandchild));
    }

    public void testShouldFireTreeNodesInsertedEventWhenTestStartEventIsFired() throws Exception {
        model.addTreeModelListener((TreeModelListener) listener.mockTarget());
        cabinet.add(shadow);
        listener.assertDid("treeNodesInserted");
        cabinet.action(result);
        listener.assertDid("treeNodesInserted").withFirstArgConstraint(childEventConstraint());
    }

    public void testShouldFireTreeNodesInsertedEventEvenIfTestExistedStart() throws Exception {
        model.addTreeModelListener((TreeModelListener) listener.mockTarget());
        cabinet.add(shadow);
        listener.assertDid("treeNodesInserted");
        cabinet.action(result);
        listener.assertDid("treeNodesInserted");
        cabinet.action(result);
        listener.assertDid("treeNodesInserted").withFirstArgConstraint(childEventConstraint());
    }

    public void testShouldClearTestMethodNodeAndFireTreeNodesRemovedEventWhenStartAction() throws Exception {
        model.addTreeModelListener((TreeModelListener) listener.mockTarget());
        cabinet.add(shadow);
        cabinet.action(result);
        listener.assertNotDid("treeNodesRemoved");
        cabinet.action(result);
        listener.assertDid("treeNodesRemoved").withFirstArgConstraint(childEventConstraint());
    }

    public void testShouldRemoveNodeIfTestExistedButNotStart() throws Exception {
        model.addTreeModelListener((TreeModelListener) listener.mockTarget());
        cabinet.add(shadow);
        cabinet.action(result);
        cabinet.remove(shadow);
        assertEquals(0, model.getChildCount(child));
    }

    private Constraint childEventConstraint() {
        return new Constraint() {

            public CheckResult check(Object param) {
                TreeModelEvent event = (TreeModelEvent) param;
                assertEquals(model, event.getSource());
                assertEquals(2, event.getPath().length);
                assertEquals(new TreePath(new Object[] { root, child }), event.getTreePath());
                assertEquals(1, event.getChildIndices().length);
                assertEquals(0, event.getChildIndices()[0]);
                assertEquals(1, event.getChildren().length);
                assertEquals(grandchild, event.getChildren()[0]);
                return CheckResult.PASS;
            }
        };
    }
}
