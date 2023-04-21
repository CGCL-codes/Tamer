package abbot.editor;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import abbot.util.AWT;
import abbot.finder.Hierarchy;

public class CompactHierarchy implements Hierarchy {

    private boolean compact = true;

    private Hierarchy hierarchy;

    public CompactHierarchy(Hierarchy original) {
        this.hierarchy = original;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public boolean isCompact() {
        return compact;
    }

    public Collection getRoots() {
        return hierarchy.getRoots();
    }

    public Container getParent(Component c) {
        if (compact && c instanceof JPopupMenu) {
            Component invoker = ((JPopupMenu) c).getInvoker();
            if (invoker instanceof Container) return (Container) invoker;
        }
        if (compact && c instanceof JToolTip) {
            return ((JToolTip) c).getComponent();
        }
        Container parent = hierarchy.getParent(c);
        if (compact) {
            while (parent != null && isElided(parent)) {
                parent = getParent(parent);
            }
        }
        return parent;
    }

    public boolean contains(Component c) {
        return hierarchy.contains(c);
    }

    public void dispose(Window w) {
        hierarchy.dispose(w);
    }

    /** Returns whether the given component is completely ignored (including
        its children) in the hierarchy.
    */
    private boolean isIgnored(Component c) {
        if (AWT.isTransientPopup(c)) return true;
        return c instanceof JScrollBar && c.getParent() instanceof JScrollPane;
    }

    /** Returns whether the given component is omitted from the component
     * hierarchy when compact is turned on (its children may be shown).
     * For example, a JScrollPane's viewport is elided so that the scrolled
     * content shows up directly beneath the scroll pane.
     */
    private boolean isElided(Component c) {
        if (AWT.isTransientPopup(c)) {
            return true;
        }
        if (c instanceof Container) {
            if (c instanceof Window) return false;
            if (c instanceof JPopupMenu && ((JPopupMenu) c).getInvoker() instanceof JMenu) return true;
            if (AWT.isContentPane(c) && AWT.isTransientPopup(SwingUtilities.getWindowAncestor(c))) return true;
        }
        Container parent = c.getParent();
        if (parent instanceof JScrollPane) {
            return c instanceof JScrollBar || c instanceof JViewport;
        }
        return parent instanceof RootPaneContainer || parent instanceof JRootPane;
    }

    public Collection getComponents(Component c) {
        if (c == null || !(c instanceof Container)) return new ArrayList();
        ArrayList list = new ArrayList();
        if (compact) {
            if (c instanceof JMenu) {
                return getComponents(((JMenu) c).getPopupMenu());
            }
        }
        Iterator iter = hierarchy.getComponents(c).iterator();
        while (iter.hasNext()) {
            Component k = (Component) iter.next();
            if (compact && isElided(k)) {
                if (!isIgnored(k)) {
                    list.addAll(getComponents(k));
                }
            } else {
                list.add(k);
            }
        }
        list.addAll(findInvokerPopups(c));
        return list;
    }

    /** Scan for popups which have been invoked by the given invoker. */
    private Collection findInvokerPopups(Component invoker) {
        ArrayList popups = new ArrayList();
        Window root = AWT.getWindow(invoker);
        Collection parents = new ArrayList(Arrays.asList(root.getOwnedWindows()));
        if (root instanceof JWindow || root instanceof JFrame || root instanceof JDialog) {
            JRootPane rp = ((RootPaneContainer) root).getRootPane();
            if (rp != null) {
                JLayeredPane lp = rp.getLayeredPane();
                if (lp != null) {
                    parents.addAll(Arrays.asList(lp.getComponents()));
                }
            }
        }
        Iterator iter = parents.iterator();
        while (iter.hasNext()) {
            Component c = (Component) iter.next();
            JComponent popup = findInvokedPopup(invoker, c);
            if (popup != null) {
                popups.add(popup);
            }
        }
        return popups;
    }

    /** Returns the invoked popup found under the given parent (if any). */
    private JComponent findInvokedPopup(Component invoker, Component parent) {
        if (AWT.isTransientPopup(parent)) {
            JComponent popup = findPopup((Container) parent);
            if (popup != null && (!(popup instanceof JPopupMenu) || !(getParent(popup) instanceof JMenu))) {
                if (getParent(popup) == invoker) {
                    return popup;
                }
            }
        }
        return null;
    }

    /** Return the popup descendent of the given known transient popup
     * container.
     */
    private JComponent findPopup(Container c) {
        JComponent popup = null;
        Component[] kids = c.getComponents();
        for (int i = 0; i < kids.length && popup == null; i++) {
            if (kids[i] instanceof JPopupMenu || kids[i] instanceof JToolTip) {
                popup = (JComponent) kids[i];
            } else if (kids[i] instanceof Container) {
                popup = findPopup((Container) kids[i]);
            }
        }
        return popup;
    }
}
