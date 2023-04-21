package javax.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * This class manages current menu selectection. It provides
 * methods to clear and set current selected menu path.
 * It also fires StateChange event to its registered
 * listeners whenever selected path of the current menu hierarchy
 * changes.
 *
 */
public class MenuSelectionManager {

    /** ChangeEvent fired when selected path changes*/
    protected ChangeEvent changeEvent = new ChangeEvent(this);

    /** List of listeners for this MenuSelectionManager */
    protected EventListenerList listenerList = new EventListenerList();

    /** Default manager for the current menu hierarchy*/
    private static final MenuSelectionManager manager = new MenuSelectionManager();

    /** Path to the currently selected menu */
    private Vector selectedPath = new Vector();

    /**
   * Fires StateChange event to registered listeners
   */
    protected void fireStateChanged() {
        ChangeListener[] listeners = getChangeListeners();
        for (int i = 0; i < listeners.length; i++) listeners[i].stateChanged(changeEvent);
    }

    /**
   * Adds ChangeListener to this MenuSelectionManager
   *
   * @param listener ChangeListener to add
   */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    /**
   * Removes ChangeListener from the list of registered listeners
   * for this MenuSelectionManager.
   *
   * @param listener ChangeListner to remove
   */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    /**
   * Returns list of registered listeners with MenuSelectionManager
   *
   * @since 1.4
   */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(ChangeListener.class);
    }

    /**
   * Unselects all the menu elements on the selection path
   */
    public void clearSelectedPath() {
        for (int i = selectedPath.size() - 1; i >= 0; i--) ((MenuElement) selectedPath.get(i)).menuSelectionChanged(false);
        selectedPath.clear();
        fireStateChanged();
    }

    /**
   * This method returns menu element on the selected path that contains
   * given source point. If no menu element on the selected path contains this
   * point, then null is returned.
   *
   * @param source Component relative to which sourcePoint is given
   * @param sourcePoint point for which we want to find menu element that contains it
   *
   * @return Returns menu element that contains given source point and belongs
   * to the currently selected path. Null is return if no such menu element found.
   */
    public Component componentForPoint(Component source, Point sourcePoint) {
        Point sourcePointOnScreen = sourcePoint;
        if (source.isShowing()) SwingUtilities.convertPointToScreen(sourcePointOnScreen, source);
        Point compPointOnScreen;
        Component resultComp = null;
        for (int i = 0; i < selectedPath.size(); i++) {
            Component comp = ((Component) selectedPath.get(i));
            Dimension size = comp.getSize();
            compPointOnScreen = comp.getLocationOnScreen();
            if (compPointOnScreen.x <= sourcePointOnScreen.x && sourcePointOnScreen.x < compPointOnScreen.x + size.width && compPointOnScreen.y <= sourcePointOnScreen.y && sourcePointOnScreen.y < compPointOnScreen.y + size.height) {
                Point p = sourcePointOnScreen;
                if (comp.isShowing()) SwingUtilities.convertPointFromScreen(p, comp);
                resultComp = SwingUtilities.getDeepestComponentAt(comp, p.x, p.y);
                break;
            }
        }
        return resultComp;
    }

    /**
   * Returns shared instance of MenuSelection Manager
   *
   * @return default Manager
   */
    public static MenuSelectionManager defaultManager() {
        return manager;
    }

    /**
   * Returns path representing current menu selection
   *
   * @return Current selection path
   */
    public MenuElement[] getSelectedPath() {
        MenuElement[] path = new MenuElement[selectedPath.size()];
        for (int i = 0; i < path.length; i++) path[i] = (MenuElement) selectedPath.get(i);
        return path;
    }

    /**
   * Returns true if specified component is part of current menu
   * heirarchy and false otherwise
   *
   * @param c Component for which to check
   * @return True if specified component is part of current menu
   */
    public boolean isComponentPartOfCurrentMenu(Component c) {
        MenuElement[] subElements;
        for (int i = 0; i < selectedPath.size(); i++) {
            subElements = ((MenuElement) selectedPath.get(i)).getSubElements();
            for (int j = 0; j < subElements.length; j++) {
                MenuElement me = subElements[j];
                if (me != null && (me.getComponent()).equals(c)) return true;
            }
        }
        return false;
    }

    /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
    public void processKeyEvent(KeyEvent e) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
   * Forwards given mouse event to all of the source subcomponents.
   *
   * @param event Mouse event
   */
    public void processMouseEvent(MouseEvent event) {
        Component source = ((Component) event.getSource());
        Component mouseOverMenuComp;
        if (event.getID() == MouseEvent.MOUSE_DRAGGED || event.getID() == MouseEvent.MOUSE_RELEASED) mouseOverMenuComp = componentForPoint(source, event.getPoint()); else mouseOverMenuComp = source;
        if (mouseOverMenuComp != null && (mouseOverMenuComp instanceof MenuElement)) {
            MenuElement[] path = getPath(mouseOverMenuComp);
            ((MenuElement) mouseOverMenuComp).processMouseEvent(event, path, manager);
        } else {
            if (event.getID() == MouseEvent.MOUSE_RELEASED) clearSelectedPath();
        }
    }

    /**
   * Sets menu selection to the specified path
   *
   * @param path new selection path
   */
    public void setSelectedPath(MenuElement[] path) {
        if (path == null) {
            clearSelectedPath();
            return;
        }
        int i;
        int minSize = path.length;
        if (path.length > selectedPath.size()) {
            minSize = selectedPath.size();
            for (i = selectedPath.size(); i < path.length; i++) {
                selectedPath.add(path[i]);
                path[i].menuSelectionChanged(true);
            }
        } else if (path.length < selectedPath.size()) {
            for (i = selectedPath.size() - 1; i >= path.length; i--) {
                ((MenuElement) selectedPath.get(i)).menuSelectionChanged(false);
                selectedPath.remove(i);
            }
            minSize = path.length;
        }
        MenuElement oldSelectedItem;
        for (i = minSize - 1; i >= 0; i--) {
            oldSelectedItem = (MenuElement) selectedPath.get(i);
            if (path[i].equals(oldSelectedItem)) break;
            oldSelectedItem.menuSelectionChanged(false);
            path[i].menuSelectionChanged(true);
            selectedPath.setElementAt(path[i], i);
        }
        fireStateChanged();
    }

    /**
   * Returns path to the specified component
   *
   * @param c component for which to find path for
   *
   * @return path to the specified component
   */
    private MenuElement[] getPath(Component c) {
        ArrayList path = new ArrayList();
        if (c instanceof JMenu) path.add(((JMenu) c).getPopupMenu());
        while (c instanceof MenuElement) {
            path.add(0, (MenuElement) c);
            if (c instanceof JPopupMenu) c = ((JPopupMenu) c).getInvoker(); else c = c.getParent();
        }
        MenuElement[] pathArray = new MenuElement[path.size()];
        path.toArray(pathArray);
        return pathArray;
    }
}
