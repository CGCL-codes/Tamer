package net.hypotenubel.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.util.*;
import javax.swing.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.*;

/**
 * Contains some swing utility methods.
 * 
 * @author Christoph Daniel Schulze
 * @version $Id: SwingUtils.java 149 2006-10-04 16:16:45Z captainnuss $
 */
public class SwingUtils {

    /**
     * Private, no instantiation required.
     */
    private SwingUtils() {
    }

    /**
     * Equals the button's widths. Each button becomes as wide as the widest
     * button of them.
     * 
     * @param buttons array of {@code AbstractButton}s to operate on.
     */
    public static void equalButtonWidth(AbstractButton[] buttons) {
        int width = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getPreferredSize().getWidth() > width) {
                width = (int) buttons[i].getPreferredSize().getWidth();
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPreferredSize(new Dimension(width, (int) buttons[i].getPreferredSize().getHeight()));
        }
    }

    /**
     * Returns a panel with the given component, with the given indent. The
     * indent is measured in the {@code dlu} unit, since the JGoodies Forms
     * layout is used to layout the panel.
     * 
     * @param c the component to add to the panel.
     * @param indent the indent, measured in dialog units.
     * @return panel with the given component.
     */
    public static JPanel getIndentedFormsComponent(JComponent c, int indent) {
        PanelBuilder builder = new PanelBuilder(new FormLayout(String.valueOf(indent) + "dlu, fill:default:grow", "p"));
        builder.add(c, new CellConstraints(2, 1));
        return builder.getPanel();
    }

    /**
     * Iterates through all the children of the given component and returns
     * those that can be cast to an instance of class {@code c}. If the parent
     * itself can be cast to {@code c}, it too is in the result.
     * 
     * @param parent the parent whose children are to be examined.
     * @param c the class of components to return.
     * @return list of components that can be cast to {@code c}.
     */
    public static List<JComponent> findComponents(JComponent parent, Class c) {
        return findComponents(parent, c, new ArrayList<JComponent>(10));
    }

    /**
     * Does the actual work for {@link #findComponents(JComponent, Class)}.
     * 
     * @param parent the parent whose children are to be examined.
     * @param c the class of components to return.
     * @param list the list of components to add the results to.
     * @return list of components that can be cast to {@code c}.
     */
    private static List<JComponent> findComponents(JComponent parent, Class c, List<JComponent> list) {
        if (c.isInstance(parent)) {
            list.add(parent);
        }
        for (Component child : parent.getComponents()) {
            if (child instanceof JComponent) {
                findComponents((JComponent) child, c, list);
            }
        }
        return list;
    }
}
