package org.fest.swing.fixture;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.testing.TestFrame;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.testing.TestGroups.*;

/**
 * Test case for <a href="http://code.google.com/p/fest/issues/detail?id=116">Bug 116</a>.
 *
 * @author Yvonne Wang
 */
@Test(groups = { GUI, BUG })
public class SelectJComboBoxItemTest {

    private Robot robot;

    private JComboBox target;

    private JComboBoxFixture fixture;

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        MyFrame frame = new MyFrame();
        target = frame.comboBox;
        fixture = new JComboBoxFixture(robot, target);
        robot.showWindow(frame);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldScrollToSelectLastItem() {
        int toSelect = 99;
        fixture.selectItem(toSelect);
        assertThat(target.getSelectedIndex()).isEqualTo(toSelect);
    }

    private static class MyFrame extends TestFrame {

        private static final long serialVersionUID = 1L;

        final JComboBox comboBox = new JComboBox();

        public MyFrame() {
            super(SelectJComboBoxItemTest.class);
            add(comboBox);
            int itemCount = 100;
            Object[] content = new Object[itemCount];
            for (int i = 0; i < itemCount; i++) content[i] = String.valueOf(i + 1);
            comboBox.setModel(new DefaultComboBoxModel(content));
        }
    }
}
