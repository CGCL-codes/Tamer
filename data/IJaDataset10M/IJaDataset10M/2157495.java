package org.fest.swing.driver;

import java.awt.Rectangle;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.testing.MethodInvocations;
import org.fest.swing.testing.TestWindow;
import org.fest.swing.testing.MethodInvocations.Args;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.testing.MethodInvocations.Args.args;
import static org.fest.swing.testing.TestGroups.*;

/**
 * Tests for <code>{@link JTreePathBoundsQuery}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
@Test(groups = { GUI, EDT_ACTION })
public class JTreePathBoundsQueryTest {

    private Robot robot;

    private MyTree tree;

    private TreePath rootPath;

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        MyWindow window = MyWindow.createNew();
        tree = window.tree;
        rootPath = new TreePath(tree.root);
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnBoundsOfComponent() {
        Rectangle expected = tree.getPathBounds(rootPath);
        tree.startRecording();
        assertThat(JTreePathBoundsQuery.pathBoundsOf(tree, rootPath)).isEqualTo(expected);
        tree.requireInvoked("getPathBounds", args(rootPath));
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        static MyWindow createNew() {
            return new MyWindow();
        }

        final MyTree tree = new MyTree();

        private MyWindow() {
            super(JTreePathBoundsQueryTest.class);
            addComponents(tree);
        }
    }

    private static class MyTree extends JTree {

        private static final long serialVersionUID = 1L;

        private boolean recording;

        private final MethodInvocations methodInvocations = new MethodInvocations();

        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        MyTree() {
            super();
            root.add(new DefaultMutableTreeNode("node"));
            setModel(new DefaultTreeModel(root, false));
        }

        @Override
        public Rectangle getPathBounds(TreePath path) {
            if (recording) methodInvocations.invoked("getPathBounds", args(path));
            return super.getPathBounds(path);
        }

        void startRecording() {
            recording = true;
        }

        MethodInvocations requireInvoked(String methodName, Args args) {
            return methodInvocations.requireInvoked(methodName, args);
        }
    }
}
