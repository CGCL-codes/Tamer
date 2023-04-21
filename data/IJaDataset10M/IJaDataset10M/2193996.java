package org.gaea.ui.utilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * Useful repetitive methods for handling UI.
 * 
 * @author jsgoupil
 */
public class UtilityUI {

    /**
	 * Closes the frame when escape is pressed.
	 * 
	 * @param frame
	 */
    public static void closeOnEscape(final JFrame frame) {
        Action escapeAction = new AbstractAction() {

            private static final long serialVersionUID = 2668530465156356891L;

            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        };
        doOnEscape(frame, escapeAction);
    }

    /**
	 * Closes the dialog when escape is pressed.
	 * 
	 * @param dialog
	 */
    public static void closeOnEscape(final JDialog dialog) {
        Action escapeAction = new AbstractAction() {

            private static final long serialVersionUID = 2668530465156356891L;

            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };
        doOnEscape(dialog, escapeAction);
    }

    /**
	 * Executes an action when the escape is pressed in the frame.
	 * 
	 * @param frame
	 * @param action
	 */
    public static void doOnEscape(final JFrame frame, Action action) {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        frame.getRootPane().getActionMap().put("ESCAPE", action);
    }

    /**
	 * Executes an action when the escape is pressed in the dialog.
	 * 
	 * @param dialog
	 * @param action
	 */
    public static void doOnEscape(final JDialog dialog, Action action) {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        dialog.getRootPane().getActionMap().put("ESCAPE", action);
    }

    /**
	 * Add an empty border around the component.
	 * 
	 * @param component
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 * @return new panel with empty border
	 */
    public static JPanel setComponentBorder(JComponent component, int top, int left, int bottom, int right) {
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        panel.add(component, constraints);
        panel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return panel;
    }

    /**
	 * Searches for the top parent JFrame.
	 * 
	 * @param obj
	 * @return frame if found, otherwise null
	 */
    public static JFrame getTopParent(Container obj) {
        if (obj instanceof JFrame) {
            return (JFrame) obj;
        }
        if (obj != null) {
            return getTopParent(obj.getParent());
        }
        return null;
    }

    /**
	 * Creates and return the default border used in the UI.
	 * 
	 * @return Border created.
	 */
    public static Border getDefaultBorder() {
        return new CompoundBorder(BorderFactory.createLineBorder(new Color(33, 150, 33)), BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}
