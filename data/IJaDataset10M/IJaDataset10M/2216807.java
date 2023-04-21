package components;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;

public class ComponentUtil {

    private ComponentUtil() {
    }

    public static void addEnterKeyDelegator(Component aSource, JButton aTarget) {
        aSource.addKeyListener(new ComponentEnterKeyDelegator(aTarget));
    }

    public static void addDefaultButtonBehaviour(JButton aTarget) {
        aTarget.addKeyListener(new ButtonEnterKeyHandler(aTarget));
    }

    private static class ComponentEnterKeyDelegator implements KeyListener {

        public final JButton delegatingButton;

        public ComponentEnterKeyDelegator(JButton aDelegatingButton) {
            delegatingButton = aDelegatingButton;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                delegatingButton.doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private static class ButtonEnterKeyHandler implements KeyListener {

        public final JButton button;

        public ButtonEnterKeyHandler(JButton aButton) {
            button = aButton;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                button.doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
