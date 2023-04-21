package org.fest.swing.driver;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JList;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.testing.TestList;
import org.fest.swing.testing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.testing.TestGroups.GUI;

/**
 * Tests for <code>{@link JListLocation}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
@Test(groups = GUI)
public class JListLocationTest {

    private Robot robot;

    private JList list;

    private JListLocation location;

    @BeforeMethod
    public void setUp() {
        location = new JListLocation();
        robot = robotWithNewAwtHierarchy();
        MyWindow window = MyWindow.createNew();
        list = window.list;
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnLocationOfIndex() {
        final Point p = location.pointAt(list, 2);
        int index = locationToIndex(list, p);
        assertThat(index).isEqualTo(2);
    }

    private static int locationToIndex(final JList list, final Point location) {
        return execute(new GuiQuery<Integer>() {

            protected Integer executeInEDT() {
                return list.locationToIndex(location);
            }
        });
    }

    public void shouldThrowErrorIfIndexOutOfBoundsWhenLookingForLocation() {
        try {
            location.pointAt(list, 8);
            fail();
        } catch (IndexOutOfBoundsException expected) {
            assertThat(expected).message().isEqualTo("Item index (8) should be between [0] and [2] (inclusive)");
        }
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        final TestList list = new TestList("one", "two", "three");

        static MyWindow createNew() {
            return new MyWindow();
        }

        private MyWindow() {
            super(JListLocationTest.class);
            add(list);
            list.setPreferredSize(new Dimension(60, 80));
            setPreferredSize(new Dimension(100, 120));
        }
    }
}
