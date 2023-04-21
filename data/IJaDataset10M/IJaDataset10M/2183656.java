package net.sourceforge.jruntimedesigner.common;

import java.applet.Applet;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author Santhosh Kumar T
 */
public abstract class FocusOwnerTracker implements PropertyChangeListener {

    private static final String PERMANENT_FOCUS_OWNER = "permanentFocusOwner";

    private KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    private Component comp;

    private boolean inside;

    public FocusOwnerTracker(Component comp) {
        this.comp = comp;
    }

    public boolean isFocusInside() {
        return isFocusInside(false);
    }

    public boolean isFocusInside(boolean find) {
        if (!find) return inside;
        Component c = focusManager.getPermanentFocusOwner();
        System.out.println("Focus owner: " + (c != null ? c.getName() : null));
        while (c != null) {
            if (c == comp) {
                return true;
            } else if ((c instanceof Window) || (c instanceof Applet && c.getParent() == null)) {
                if (c == SwingUtilities.getRoot(comp)) {
                    return false;
                }
                break;
            }
            c = c.getParent();
        }
        return false;
    }

    public void start() {
        focusManager.addPropertyChangeListener(PERMANENT_FOCUS_OWNER, this);
        inside = isFocusInside(true);
    }

    public void stop() {
        focusManager.removePropertyChangeListener(PERMANENT_FOCUS_OWNER, this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        boolean inside = isFocusInside(true);
        if (this.inside != inside) {
            if (inside) focusGained(); else focusLost();
            this.inside = inside;
        }
    }

    public abstract void focusLost();

    public abstract void focusGained();

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        final JLabel titleLabel = new JLabel();
        new FocusOwnerTracker(panel) {

            public void focusGained() {
                titleLabel.setForeground(UIManager.getColor("textHighlightText"));
                titleLabel.setBackground(UIManager.getColor("textHighlight"));
            }

            public void focusLost() {
                titleLabel.setForeground(UIManager.getColor("textText"));
                titleLabel.setBackground(UIManager.getColor("control").darker());
            }
        };
    }
}
