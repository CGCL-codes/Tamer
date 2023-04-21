package org.argouml.uml.ui;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

/**
 * A scrollable list of items. This makes sure that there is no horizontal
 * scrollbar (which takes up too much screen real estate) and that sideways
 * scrolling can be achieved instead with arrow keys.
 * @author Bob Tarling
 */
public class ScrollList extends JScrollPane implements KeyListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6711776013279497682L;

    /**
     * The Component that this scroll is wrapping.
     */
    private UMLLinkedList list;

    /**
     * Builds a JList from a given list model and wraps
     * in a scrollable view.
     * @param listModel The model from which to build the list
     */
    public ScrollList(ListModel listModel) {
        this(listModel, true, true);
    }

    /**
     * Builds a JList from a given list model and wraps
     * in a scrollable view.
     * @param listModel The model from which to build the list
     * @param visibleRowCount an integer specifying the preferred number of
     * rows to display without requiring scrolling
     */
    public ScrollList(ListModel listModel, int visibleRowCount) {
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        list = new UMLLinkedList(listModel, true, true);
        list.setVisibleRowCount(visibleRowCount);
        setViewportView(list);
    }

    /**
     * Builds a JList from a given list model and wraps
     * in a scrollable view.
     * @param listModel The model from which to build the list
     * @param showIcon show an icon with elements in the list
     * @param showPath show containment path for elements in list
     */
    public ScrollList(ListModel listModel, boolean showIcon, boolean showPath) {
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        list = new UMLLinkedList(listModel, showIcon, showPath);
        setViewportView(list);
    }

    /**
     * Builds a ScrollList from a given list model and wraps
     * in a scrollable view.
     * @param list The JList to wrap in a scroll
     * @deprecated in 0.27.2 use any other ScrollList constructor
     */
    @Deprecated
    public ScrollList(JList alist) {
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.list = (UMLLinkedList) alist;
        setViewportView(list);
    }

    /**
     * Examine key event to scroll left or right depending on key press
     * @param e the key event to examine
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            final Point posn = getViewport().getViewPosition();
            if (posn.x > 0) {
                getViewport().setViewPosition(new Point(posn.x - 1, posn.y));
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            final Point posn = getViewport().getViewPosition();
            if (list.getWidth() - posn.x > getViewport().getWidth()) {
                getViewport().setViewPosition(new Point(posn.x + 1, posn.y));
            }
        }
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public void addNotify() {
        super.addNotify();
        list.addKeyListener(this);
    }

    public void removeNotify() {
        super.removeNotify();
        list.removeKeyListener(this);
    }
}
