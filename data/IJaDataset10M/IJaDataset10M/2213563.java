package org.fest.swing.driver;

import javax.swing.JComboBox;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.test.swing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.*;
import static org.fest.util.Arrays.array;

/**
 * Tests for <code>{@link JComboBoxSetSelectedIndexTask}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = { GUI, ACTION })
public class JComboBoxSelectItemAtIndexTaskTest {

    private Robot robot;

    private JComboBox comboBox;

    private int index;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        MyWindow window = MyWindow.createNew();
        comboBox = window.comboBox;
        index = 1;
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldSetSelectedIndex() {
        assertThat(selectedIndexOf(comboBox)).isNotEqualTo(index);
        JComboBoxSetSelectedIndexTask.setSelectedIndex(comboBox, index);
        robot.waitForIdle();
        assertThat(selectedIndexOf(comboBox)).isEqualTo(index);
    }

    @RunsInEDT
    private static int selectedIndexOf(final JComboBox comboBox) {
        return execute(new GuiQuery<Integer>() {

            protected Integer executeInEDT() {
                return comboBox.getSelectedIndex();
            }
        });
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        final JComboBox comboBox = new JComboBox(array("first", "second", "third"));

        @RunsInEDT
        static MyWindow createNew() {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return new MyWindow();
                }
            });
        }

        private MyWindow() {
            super(JComboBoxSelectItemAtIndexTaskTest.class);
            addComponents(comboBox);
        }
    }
}
