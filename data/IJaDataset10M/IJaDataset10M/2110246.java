package org.fest.swing.driver;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.test.swing.CustomCellRenderer;
import org.fest.swing.test.swing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.GUI;
import static org.fest.util.Arrays.array;

/**
 * Tests for <code>{@link BasicJComboBoxCellReader}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = GUI)
public class BasicJComboBoxCellReaderTest {

    private Robot robot;

    private JComboBox comboBox;

    private BasicJComboBoxCellReader reader;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        reader = new BasicJComboBoxCellReader();
        MyWindow window = MyWindow.createNew();
        comboBox = window.comboBox;
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnModelValueToString() {
        setModelValues(comboBox, array(new Jedi("Yoda")));
        robot.waitForIdle();
        String value = firstItemValue(reader, comboBox);
        assertThat(value).isEqualTo("Yoda");
    }

    public void shouldReturnNullIfRendererNotRecognizedAndModelValueIsNull() {
        setModelValues(comboBox, new Object[] { null });
        setNotRecognizedRendererComponent(comboBox);
        robot.waitForIdle();
        String value = firstItemValue(reader, comboBox);
        assertThat(value).isNull();
    }

    public void shouldReturnTextFromCellRendererIfRendererIsJLabelAndToStringFromModelReturnedNull() {
        setModelValues(comboBox, array(new Jedi(null)));
        setJLabelAsRendererComponent(comboBox, "First");
        robot.waitForIdle();
        String value = firstItemValue(reader, comboBox);
        assertThat(value).isEqualTo("First");
    }

    @RunsInEDT
    private static void setModelValues(final JComboBox comboBox, final Object[] values) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                comboBox.setModel(new DefaultComboBoxModel(values));
            }
        });
    }

    @RunsInEDT
    private static void setJLabelAsRendererComponent(final JComboBox comboBox, final String labelText) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                comboBox.setRenderer(new CustomCellRenderer(new JLabel(labelText)));
            }
        });
    }

    @RunsInEDT
    private static void setNotRecognizedRendererComponent(final JComboBox comboBox) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                comboBox.setRenderer(new CustomCellRenderer(new JToolBar()));
            }
        });
    }

    @RunsInEDT
    private static String firstItemValue(final BasicJComboBoxCellReader reader, final JComboBox comboBox) {
        return execute(new GuiQuery<String>() {

            protected String executeInEDT() {
                return reader.valueAt(comboBox, 0);
            }
        });
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        final JComboBox comboBox = new JComboBox(array("First"));

        @RunsInEDT
        static MyWindow createNew() {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return new MyWindow();
                }
            });
        }

        private MyWindow() {
            super(BasicJComboBoxCellReaderTest.class);
            addComponents(comboBox);
        }
    }
}
