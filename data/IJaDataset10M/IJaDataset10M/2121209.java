package org.fest.swing.driver;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.testing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.driver.JTreeSelectRowsTask.selectRows;
import static org.fest.swing.task.JTreeSelectRowTask.selectRow;
import static org.fest.swing.testing.TestGroups.*;
import static org.fest.util.Arrays.array;

/**
 * Tests for <code>{@link JTreeSingleRowSelectedQuery}</code>.
 *
 * @author Yvonne Wang
 */
@Test(groups = { GUI, EDT_ACTION })
public class JTreeSingleRowSelectedQueryTest {

    private Robot robot;

    private JTree tree;

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        MyWindow window = MyWindow.createNew();
        tree = window.tree;
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnTrueIfSingleRowSelected() {
        int row = 0;
        selectRow(tree, row);
        robot.waitForIdle();
        assertThat(JTreeSingleRowSelectedQuery.isSingleRowSelected(tree, row)).isTrue();
    }

    public void shouldReturnFalseIfMultipleRowSelected() {
        int row = 0;
        int[] rows = { row, 1 };
        selectRows(tree, rows);
        robot.waitForIdle();
        assertThat(JTreeSingleRowSelectedQuery.isSingleRowSelected(tree, row)).isFalse();
    }

    public void shouldReturnFalseIfThereIsNoSelection() {
        assertThat(JTreeSingleRowSelectedQuery.isSingleRowSelected(tree, 0)).isFalse();
    }

    public void shouldReturnFalseIfOtherRowIsSelected() {
        selectRow(tree, 0);
        robot.waitForIdle();
        assertThat(JTreeSingleRowSelectedQuery.isSingleRowSelected(tree, 1)).isFalse();
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        static MyWindow createNew() {
            return new MyWindow();
        }

        final JTree tree = new JTree(array("One", "Two"));

        private MyWindow() {
            super(JTreeSingleRowSelectedQueryTest.class);
            tree.setSelectionModel(new DefaultTreeSelectionModel());
            JScrollPane scrollPane = new JScrollPane(tree);
            scrollPane.setPreferredSize(new Dimension(80, 60));
            addComponents(scrollPane);
        }
    }
}
