package org.fest.swing.driver;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.testing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.testing.TestGroups.*;
import static org.fest.util.Arrays.array;

/**
 * Tests for <code>{@link JComboBoxEditorQuery}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = { GUI, EDT_ACTION })
public class JComboBoxEditorQueryTest {

    private Robot robot;

    private JComboBox comboBox;

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        MyWindow window = MyWindow.createNew();
        comboBox = window.comboBox;
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnEditorComponentFromJComboBox() {
        assertThat(JComboBoxEditorQuery.editorOf(comboBox)).isInstanceOf(JTextComponent.class);
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        final JComboBox comboBox = new JComboBox(array("first", "second", "third"));

        static MyWindow createNew() {
            return new MyWindow();
        }

        private MyWindow() {
            super(JComboBoxEditorQueryTest.class);
            add(comboBox);
        }
    }
}
