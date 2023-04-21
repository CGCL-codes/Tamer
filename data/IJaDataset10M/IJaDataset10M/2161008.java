package org.sgodden.ui.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Provides various ui utility methods.
 * @author goddens
 */
public class UiUtils {

    public static final Insets DEFAULT_GRID_INSETS = new Insets(2, 2, 2, 2);

    public static JFrame getContainingFrame(JComponent c) {
        return (JFrame) getContainingObject(c, JFrame.class);
    }

    public static Object getContainingObject(JComponent c, Class targetClass) {
        Object ret = null;
        Container ct = c.getParent();
        while (ret == null && ct != null) {
            if (targetClass.isAssignableFrom(ct.getClass())) {
                ret = ct;
            } else {
                ct = ct.getParent();
            }
        }
        return ret;
    }

    /**
	 * Uses {@link SwingUtilities#invokeLater(Runnable)} to request
	 * focus on the specified component.
	 * @param c the component for which focus is requested.
	 */
    public static void requestFocus(final Component c) {
        if (c == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                c.requestFocusInWindow();
            }
        });
    }

    public static Insets getDefaultInsets() {
        return new Insets(5, 5, 5, 5);
    }

    public static void addToGridBag(Container container, Component comp, int x, int y, int width, int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        container.add(comp, gbc);
    }

    public static void addToGridBag(Container container, Component comp, int x, int y, int width, int height, int anchor, int weightx, int weighty, int fill, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        gbc.insets = insets;
        gbc.anchor = anchor;
        container.add(comp, gbc);
    }

    public static void addToGridBag(Container container, Component comp, int x, int y, int width, int height, int anchor, int weightx, int weighty, int fill) {
        addToGridBag(container, comp, x, y, width, height, anchor, weightx, weighty, fill, DEFAULT_GRID_INSETS);
    }

    /**
	 * Adds a component acting as a field label to a grid bag, anchoring it to
	 * the east, with a weight of 1 in both directions, and with a fill of NONE.
	 * 
	 * @param container
	 * @param comp
	 * @param x
	 * @param y
	 */
    public static void addFieldLabelToGridBag(Container container, Component comp, int x, int y) {
        addToGridBag(container, comp, x, y, 1, 1, GridBagConstraints.EAST, 1, 1, GridBagConstraints.NONE);
    }

    /**
	 * Adds a text field to a grid bag, anchoring it to
	 * the west, with a weight of 1 in both directions, and with a fill of NONE.
	 */
    public static void addFieldToGridBag(Container container, Component comp, int x, int y, int width, int height) {
        addToGridBag(container, comp, x, y, width, height, GridBagConstraints.WEST, 1, 1, GridBagConstraints.NONE);
    }

    /**
	 * Recurses down all components in a container and sets editable or not
	 * according to the passed value.
	 * @param editable
	 */
    public static void setEditable(Container container, boolean editable) {
        for (Component c : container.getComponents()) {
            try {
                Method m = c.getClass().getMethod("setEditable", new Class[] { boolean.class });
                m.invoke(c, new Object[] { editable });
                m = c.getClass().getMethod("setEnabled", new Class[] { boolean.class });
                m.invoke(c, new Object[] { editable });
            } catch (Exception e) {
                if (c instanceof Container) {
                    setEditable((Container) c, editable);
                }
            }
        }
    }
}
