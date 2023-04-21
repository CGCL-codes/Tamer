package javax.swing.plaf.multi;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TabbedPaneUI;

/**
 * A UI delegate that that coordinates multiple {@link TabbedPaneUI} 
 * instances, one from the primary look and feel, and one or more from the 
 * auxiliary look and feel(s).
 * 
 * @see UIManager#addAuxiliaryLookAndFeel(LookAndFeel)
 */
public class MultiTabbedPaneUI extends TabbedPaneUI {

    /** A list of references to the actual component UIs. */
    protected Vector uis;

    /**
   * Creates a new <code>MultiTabbedPaneUI</code> instance.
   * 
   * @see #createUI(JComponent)
   */
    public MultiTabbedPaneUI() {
        uis = new Vector();
    }

    /**
   * Creates a delegate object for the specified component.  If any auxiliary 
   * look and feels support this component, a <code>MultiTabbedPaneUI</code> is
   * returned, otherwise the UI from the default look and feel is returned.
   * 
   * @param target  the component.
   * 
   * @see MultiLookAndFeel#createUIs(ComponentUI, Vector, JComponent)
   */
    public static ComponentUI createUI(JComponent target) {
        MultiTabbedPaneUI mui = new MultiTabbedPaneUI();
        return MultiLookAndFeel.createUIs(mui, mui.uis, target);
    }

    /**
   * Calls the {@link ComponentUI#installUI(JComponent)} method for all 
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>.
   * 
   * @param c  the component.
   */
    public void installUI(JComponent c) {
        Iterator iterator = uis.iterator();
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.installUI(c);
        }
    }

    /**
   * Calls the {@link ComponentUI#uninstallUI(JComponent)} method for all 
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>.
   * 
   * @param c  the component.
   */
    public void uninstallUI(JComponent c) {
        Iterator iterator = uis.iterator();
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.uninstallUI(c);
        }
    }

    /**
   * Returns an array containing the UI delegates managed by this
   * <code>MultiTabbedPaneUI</code>.  The first item in the array is always 
   * the UI delegate from the installed default look and feel.
   * 
   * @return An array of UI delegates.
   */
    public ComponentUI[] getUIs() {
        return MultiLookAndFeel.uisToArray(uis);
    }

    /**
   * Calls the {@link ComponentUI#contains(JComponent, int, int)} method for all 
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>, 
   * returning the result for the UI delegate from the primary look and 
   * feel. 
   * 
   * @param c  the component.
   * @param x  the x-coordinate.
   * @param y  the y-coordinate.
   * 
   * @return <code>true</code> if the specified (x, y) coordinate falls within
   *         the bounds of the component as rendered by the UI delegate in the
   *         primary look and feel, and <code>false</code> otherwise. 
   */
    public boolean contains(JComponent c, int x, int y) {
        boolean result = false;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            result = ui.contains(c, x, y);
        }
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.contains(c, x, y);
        }
        return result;
    }

    /**
   * Calls the {@link ComponentUI#update(Graphics, JComponent)} method for all 
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>.
   * 
   * @param g  the graphics device.
   * @param c  the component.
   */
    public void update(Graphics g, JComponent c) {
        Iterator iterator = uis.iterator();
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.update(g, c);
        }
    }

    /**
   * Calls the <code>paint(Graphics, JComponent)</code> method for all the UI 
   * delegates managed by this <code>MultiTabbedPaneUI</code>.
   * 
   * @param g  the graphics device.
   * @param c  the component.
   */
    public void paint(Graphics g, JComponent c) {
        Iterator iterator = uis.iterator();
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.paint(g, c);
        }
    }

    /**
   * Calls the {@link ComponentUI#getPreferredSize(JComponent)} method for all
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>, 
   * returning the preferred size for the UI delegate from the primary look and 
   * feel. 
   * 
   * @param c  the component.
   * 
   * @return The preferred size returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public Dimension getPreferredSize(JComponent c) {
        Dimension result = null;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            result = ui.getPreferredSize(c);
        }
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.getPreferredSize(c);
        }
        return result;
    }

    /**
   * Calls the {@link ComponentUI#getMinimumSize(JComponent)} method for all
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>, 
   * returning the minimum size for the UI delegate from the primary look and 
   * feel. 
   * 
   * @param c  the component.
   * 
   * @return The minimum size returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public Dimension getMinimumSize(JComponent c) {
        Dimension result = null;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            result = ui.getMinimumSize(c);
        }
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.getMinimumSize(c);
        }
        return result;
    }

    /**
   * Calls the {@link ComponentUI#getMaximumSize(JComponent)} method for all
   * the UI delegates managed by this <code>MultiTabbedPaneUI</code>, 
   * returning the maximum size for the UI delegate from the primary look and 
   * feel. 
   * 
   * @param c  the component.
   * 
   * @return The maximum size returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public Dimension getMaximumSize(JComponent c) {
        Dimension result = null;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            result = ui.getMaximumSize(c);
        }
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.getMaximumSize(c);
        }
        return result;
    }

    /**
   * Calls the {@link ComponentUI#getAccessibleChildrenCount(JComponent)} method
   * for all the UI delegates managed by this <code>MultiTabbedPaneUI</code>, 
   * returning the count for the UI delegate from the primary look and 
   * feel. 
   * 
   * @param c  the component.
   * 
   * @return The count returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public int getAccessibleChildrenCount(JComponent c) {
        int result = 0;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            result = ui.getAccessibleChildrenCount(c);
        }
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.getAccessibleChildrenCount(c);
        }
        return result;
    }

    /**
   * Calls the {@link ComponentUI#getAccessibleChild(JComponent, int)} method
   * for all the UI delegates managed by this <code>MultiTabbedPaneUI</code>, 
   * returning the child for the UI delegate from the primary look and 
   * feel. 
   * 
   * @param c  the component
   * @param i  the child index.
   * 
   * @return The child returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public Accessible getAccessibleChild(JComponent c, int i) {
        Accessible result = null;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            result = ui.getAccessibleChild(c, i);
        }
        while (iterator.hasNext()) {
            ComponentUI ui = (ComponentUI) iterator.next();
            ui.getAccessibleChild(c, i);
        }
        return result;
    }

    /**
   * Calls the {@link TabbedPaneUI#tabForCoordinate(JTabbedPane, int, int)} 
   * method for all the UI delegates managed by this 
   * <code>MultiTabbedPaneUI</code>, returning the tab index for the UI 
   * delegate from the primary look and feel. 
   * 
   * @param pane  the tabbed pane.
   * @param x  the x-coordinate.
   * @param y  the y-coordinate.
   * 
   * @return The tab index returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public int tabForCoordinate(JTabbedPane pane, int x, int y) {
        int result = 0;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            TabbedPaneUI ui = (TabbedPaneUI) iterator.next();
            result = ui.tabForCoordinate(pane, x, y);
        }
        while (iterator.hasNext()) {
            TabbedPaneUI ui = (TabbedPaneUI) iterator.next();
            ui.tabForCoordinate(pane, x, y);
        }
        return result;
    }

    /**
   * Calls the {@link TabbedPaneUI#getTabBounds(JTabbedPane, int)} 
   * method for all the UI delegates managed by this 
   * <code>MultiTabbedPaneUI</code>, returning the bounds for the UI 
   * delegate from the primary look and feel. 
   * 
   * @param pane  the tabbed pane.
   * @param index  the index.
   * 
   * @return The bounds returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public Rectangle getTabBounds(JTabbedPane pane, int index) {
        Rectangle result = null;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            TabbedPaneUI ui = (TabbedPaneUI) iterator.next();
            result = ui.getTabBounds(pane, index);
        }
        while (iterator.hasNext()) {
            TabbedPaneUI ui = (TabbedPaneUI) iterator.next();
            ui.getTabRunCount(pane);
        }
        return result;
    }

    /**
   * Calls the {@link TabbedPaneUI#getTabRunCount(JTabbedPane)} 
   * method for all the UI delegates managed by this 
   * <code>MultiTabbedPaneUI</code>, returning the nt for the UI 
   * delegate from the primary look and feel. 
   * 
   * @param pane  the tabbed pane.
   * 
   * @return The count returned by the UI delegate from the primary 
   *         look and feel. 
   */
    public int getTabRunCount(JTabbedPane pane) {
        int result = 0;
        Iterator iterator = uis.iterator();
        if (iterator.hasNext()) {
            TabbedPaneUI ui = (TabbedPaneUI) iterator.next();
            result = ui.getTabRunCount(pane);
        }
        while (iterator.hasNext()) {
            TabbedPaneUI ui = (TabbedPaneUI) iterator.next();
            ui.getTabRunCount(pane);
        }
        return result;
    }
}
