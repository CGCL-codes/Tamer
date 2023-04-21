package org.fest.swing.core;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.testng.annotations.*;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.lock.ScreenLock;
import org.fest.swing.test.recorder.ClickRecorder;
import org.fest.swing.test.swing.TestWindow;
import static java.awt.event.KeyEvent.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.awt.AWT.centerOf;
import static org.fest.swing.core.ComponentRequestFocusTask.giveFocusTo;
import static org.fest.swing.core.MouseButton.*;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.GUI;
import static org.fest.swing.test.task.ComponentHasFocusCondition.untilFocused;
import static org.fest.swing.timing.Pause.pause;

/**
 * Test case for implementations of <code>{@link InputEventGenerator}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public abstract class InputEventGeneratorTestCase {

    private static final int DELAY = 200;

    private MyWindow window;

    private InputEventGenerator eventGenerator;

    protected static final String MOVE_MOUSE_TEST = "Move Mouse Test";

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        ScreenLock.instance().acquire(this);
        window = MyWindow.createNew(getClass());
        onSetUp();
        eventGenerator = eventGenerator();
        window.display();
    }

    void onSetUp() throws Exception {
    }

    abstract InputEventGenerator eventGenerator();

    @AfterMethod
    public void tearDown() {
        try {
            window.destroy();
        } finally {
            ScreenLock.instance().release(this);
        }
    }

    @Test(groups = { GUI, MOVE_MOUSE_TEST }, enabled = false)
    public void shouldMoveMouse() {
        eventGenerator.moveMouse(window, 10, 10);
        pause(DELAY);
        MouseMotionRecorder recorder = MouseMotionRecorder.attachTo(window);
        pause(DELAY);
        Point center = centerOf(window);
        eventGenerator.moveMouse(window, center.x, center.y);
        pause(DELAY);
        assertThat(recorder.point()).isEqualTo(center);
    }

    @Test(groups = GUI, dataProvider = "mouseButtons")
    public void shouldClickMouseButtonOnComponent(MouseButton button) {
        ClickRecorder recorder = ClickRecorder.attachTo(window.textBox);
        Point center = centerOf(window.textBox);
        eventGenerator.pressMouse(window.textBox, center, button.mask);
        eventGenerator.releaseMouse(button.mask);
        pause(DELAY);
        recorder.clicked(button);
        assertThat(recorder.pointClicked()).isEqualTo(center);
    }

    @Test(groups = GUI, dataProvider = "mouseButtons")
    public void shouldClickMouseButton(MouseButton button) {
        Point center = centerOf(window);
        eventGenerator.moveMouse(window, center.x, center.y);
        ClickRecorder recorder = ClickRecorder.attachTo(window);
        eventGenerator.pressMouse(button.mask);
        eventGenerator.releaseMouse(button.mask);
        pause(DELAY);
        assertThat(recorder.clicked(button));
    }

    @DataProvider(name = "mouseButtons")
    public Object[][] mouseButtons() {
        return new Object[][] { { LEFT_BUTTON }, { MIDDLE_BUTTON }, { RIGHT_BUTTON } };
    }

    @Test(dataProvider = "keys")
    public void shouldTypeKey(int keyToPress, String expectedText) {
        giveFocusTo(window.textBox);
        pause(untilFocused(window.textBox));
        eventGenerator.pressKey(keyToPress, CHAR_UNDEFINED);
        eventGenerator.releaseKey(keyToPress);
        pause(DELAY);
        assertThatTextBoxTextIsEqualTo(expectedText);
    }

    @RunsInEDT
    private void assertThatTextBoxTextIsEqualTo(String expectedText) {
        String text = textOf(window.textBox);
        assertThat(text).isEqualTo(expectedText);
    }

    @RunsInEDT
    private static String textOf(final JTextComponent textComponent) {
        return execute(new GuiQuery<String>() {

            protected String executeInEDT() {
                return textComponent.getText();
            }
        });
    }

    @DataProvider(name = "keys")
    public Object[][] keys() {
        return new Object[][] { { VK_A, "a" }, { VK_S, "s" }, { VK_D, "d" } };
    }

    private static class MouseMotionRecorder extends MouseMotionAdapter {

        private Point point;

        static MouseMotionRecorder attachTo(Component c) {
            MouseMotionRecorder recorder = new MouseMotionRecorder();
            c.addMouseMotionListener(recorder);
            return recorder;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            point = e.getPoint();
        }

        Point point() {
            return point;
        }
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        final JTextField textBox = new JTextField(20);

        @RunsInEDT
        static MyWindow createNew(final Class<? extends InputEventGeneratorTestCase> testClass) {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return new MyWindow(testClass);
                }
            });
        }

        private MyWindow(Class<? extends InputEventGeneratorTestCase> testClass) {
            super(testClass);
            addComponents(textBox);
        }
    }
}
