package jorgan.gui.construct.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jorgan.disposition.Shortcut;
import jorgan.swing.MacAdapter;

/**
 * PropertyEditor for a shortcut property.
 */
public class ShortcutEditor extends CustomEditor implements ActionListener {

    private JPanel panel = new JPanel();

    private JButton button = new JButton("←");

    private ShortcutField shortcutField = new ShortcutField();

    public ShortcutEditor() {
        panel.setLayout(new BorderLayout());
        MacAdapter.typeToolbar(button);
        button.setFocusable(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.addActionListener(this);
        panel.add(button, BorderLayout.EAST);
        shortcutField.setBorder(null);
        panel.add(shortcutField, BorderLayout.CENTER);
    }

    @Override
    public String format(Object value) {
        Shortcut shortcut = (Shortcut) value;
        if (shortcut == null) {
            return "";
        } else {
            return shortcut.toString();
        }
    }

    @Override
    public Component getCustomEditor(Object value) {
        shortcutField.setShortcut((Shortcut) value);
        return panel;
    }

    @Override
    public Object getEditedValue() {
        return shortcutField.getShortcut();
    }

    public void actionPerformed(ActionEvent e) {
        shortcutField.setShortcut(null);
    }

    private class ShortcutField extends JTextField {

        private Shortcut shortcut;

        private KeyEventPostProcessor processor = new KeyEventPostProcessor() {

            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    Shortcut shortcut = Shortcut.createShortCut(e);
                    if (shortcut != null) {
                        setShortcut(shortcut);
                        return true;
                    }
                }
                return false;
            }
        };

        private ShortcutField() {
            setBorder(null);
            setEnabled(false);
        }

        @Override
        public void addNotify() {
            super.addNotify();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(processor);
        }

        @Override
        public void removeNotify() {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(processor);
            super.removeNotify();
        }

        /**
		 * Set the shortcut.
		 * 
		 * @param shortcut
		 *            shortcut
		 */
        public void setShortcut(Shortcut shortcut) {
            this.shortcut = shortcut;
            if (shortcut == null) {
                setText("");
            } else {
                setText(shortcut.toString());
            }
        }

        /**
		 * Get the shortcut.
		 * 
		 * @return shortcut
		 */
        public Shortcut getShortcut() {
            return shortcut;
        }
    }
}
