package org.fest.swing.driver;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.testing.TestFrame;
import static java.awt.event.InputEvent.*;
import static java.awt.event.KeyEvent.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.testing.TestGroups.GUI;

/**
 * Tests for <code>{@link ComponentDriver}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = GUI)
public class ComponentDriverGuiTest {

    private Robot robot;

    private ComponentDriver driver;

    private JTextField textField;

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        driver = new ComponentDriver(robot);
        MyFrame frame = new MyFrame();
        textField = frame.textField;
        robot.showWindow(frame);
    }

    public void shouldPressKeyAndModifiers() {
        final List<KeyAction> actions = new ArrayList<KeyAction>();
        textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                actions.add(new KeyAction(KeyActionType.PRESSED, e.getKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                actions.add(new KeyAction(KeyActionType.RELEASED, e.getKeyCode()));
            }
        });
        driver.pressAndReleaseKey(textField, VK_C, new int[] { CTRL_MASK, SHIFT_MASK });
        assertThat(actions).hasSize(6);
        assertThat(actions.get(0)).isEqualTo(new KeyAction(KeyActionType.PRESSED, VK_SHIFT));
        assertThat(actions.get(1)).isEqualTo(new KeyAction(KeyActionType.PRESSED, VK_CONTROL));
        assertThat(actions.get(2)).isEqualTo(new KeyAction(KeyActionType.PRESSED, VK_C));
        assertThat(actions.get(3)).isEqualTo(new KeyAction(KeyActionType.RELEASED, VK_C));
        assertThat(actions.get(4)).isEqualTo(new KeyAction(KeyActionType.RELEASED, VK_CONTROL));
        assertThat(actions.get(5)).isEqualTo(new KeyAction(KeyActionType.RELEASED, VK_SHIFT));
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    private static class KeyAction {

        final KeyActionType type;

        final int keyCode;

        KeyAction(KeyActionType type, int keyCode) {
            this.type = type;
            this.keyCode = keyCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final KeyAction other = (KeyAction) obj;
            if (keyCode != other.keyCode) return false;
            if (type == null && other.type != null) return false;
            if (!type.equals(other.type)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + keyCode;
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("type=").append(type);
            b.append("keyCode=").append(keyCode);
            return b.toString();
        }
    }

    private static enum KeyActionType {

        PRESSED, RELEASED
    }

    private static class MyFrame extends TestFrame {

        private static final long serialVersionUID = 1L;

        final JTextField textField = new JTextField(20);

        MyFrame() {
            super(ComponentDriverGuiTest.class);
            add(textField);
        }
    }
}
