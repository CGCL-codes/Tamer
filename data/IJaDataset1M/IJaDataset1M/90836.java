package org.progeeks.util.swing;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import org.progeeks.util.*;
import org.progeeks.util.log.*;

/**
 *  A tabbed panel where each tab represents a different
 *  Object.  The panel is associated with an ObservableList of Objects
 *  and when the list changes tabs are added and removed as needed.
 *  Optinally the tabs can also have close buttons which will remove
 *  the associated object from the list.
 *
 *  It is up to subclasses to determine what to do about tab additions
 *  by implementing the createTab() method.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public abstract class ListTabbedPane extends JTabbedPane {

    static Log log = Log.getLog(ListTabbedPane.class);

    private boolean hasCloseButtons;

    private ObservableList objects;

    private ListObserver observer = new ListObserver();

    /**
     *  Matches index-for-index with the tab panes and the objects.
     */
    private List<Object> tabs = new ArrayList<Object>();

    /**
     *  Creates an empty ListTabbedPanel.
     */
    public ListTabbedPane() {
        this(false);
    }

    public ListTabbedPane(boolean hasCloseButtons) {
        this.hasCloseButtons = hasCloseButtons;
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        if (hasCloseButtons) setUI(new CloseButtonUI());
    }

    /**
     *  Returns the currently visible object.
     */
    public Object getSelectedMetaObject() {
        int i = getSelectedIndex();
        if (i < 0) return (null);
        return (objects.get(i));
    }

    /**
     *  Sets the currently visible object.
     */
    public void setSelectedMetaObject(Object mObj) {
        int i = objects.indexOf(mObj);
        if (i < 0) return;
        setSelectedIndex(i);
    }

    /**
     *  Sets the current ObservableList which this tabbed panel reflects.
     */
    public void setList(ObservableList list) {
        if (this.objects == list) return;
        if (this.objects != null) {
            this.objects.removePropertyChangeListener(observer);
            removeAll();
            tabs.clear();
        }
        this.objects = list;
        if (this.objects != null) {
            this.objects.addPropertyChangeListener(observer);
            int index = 0;
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Object mObj = (Object) i.next();
                insertObjectTab(index++, mObj);
            }
        }
    }

    /**
     *  Returns the list of objects for which this tabbed panel reflects.
     */
    public ObservableList getList() {
        return (objects);
    }

    /**
     *  Overridden by subclasses to create the appropriate component
     *  for the specified object.
     */
    protected abstract Component createTab(Object obj);

    /**
     *  Overridden by subclasses to provide a tab name for the specified
     *  object.
     */
    protected abstract String getName(Object obj);

    /**
     *  Overridden by subclasses to intercept the removal of a tab.
     *  This is useful for cleaning up resources and such.  Default
     *  implementation does nothing.
     */
    protected void tabRemoved(Component tab, Object obj) {
    }

    /**
     *  Inserts a tab for the specified object into the tab list
     *  at the specified index.  This is called internally when the
     *  list changes.
     */
    protected void insertObjectTab(int index, Object obj) {
        if (obj == null) {
            tabs.add(index, null);
            super.insertTab("Unknown", null, new JPanel(), null, index);
            return;
        }
        Component tab = createTab(obj);
        tabs.add(index, tab);
        super.insertTab(getName(obj), null, tab, null, index);
    }

    /**
     *  Removes the tab at the specified index.  This is called internally when the
     *  list changes.
     */
    protected void removeObjectTab(int index, Object obj) {
        Component comp = (Component) tabs.remove(index);
        tabRemoved(comp, obj);
        super.remove(index);
    }

    private class ListObserver extends ListPropertyChangeListener {

        private int ignoreEvents = 0;

        public void ignore() {
            ignoreEvents++;
        }

        public void watch() {
            ignoreEvents--;
        }

        protected void itemInserted(Object source, int index, List oldList, List newList) {
            if (ignoreEvents > 0) return;
            insertObjectTab(index, newList.get(index));
            setSelectedIndex(index);
        }

        protected void itemUpdated(Object source, int index, List oldList, List newList) {
            if (ignoreEvents > 0) return;
            throw new UnsupportedOperationException("Tab replacement not supported yet.");
        }

        protected void itemDeleted(Object source, int index, List oldList, List newList) {
            if (ignoreEvents > 0) return;
            removeObjectTab(index, oldList.get(index));
        }
    }

    /**
     * UI that adds close buttons to the individual tabs of the tabbed pane.
     * See http://forum.java.sun.com/thread.jsp?thread=453521&forum=57&message=2066322
     * for reference.
     */
    private class CloseButtonUI extends BasicTabbedPaneUI {

        public CloseButtonUI() {
            super();
        }

        protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
            return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 24;
        }

        protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
            return super.getTabLabelShiftX(tabPlacement, tabIndex, isSelected) - 10;
        }

        protected MouseListener createMouseListener() {
            return new TabMouseHandler();
        }

        protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
            Rectangle rect = rects[tabIndex];
            g.drawRect(rect.x + rect.width - 19, rect.y + 4, 13, 12);
            g.drawLine(rect.x + rect.width - 16, rect.y + 7, rect.x + rect.width - 10, rect.y + 13);
            g.drawLine(rect.x + rect.width - 10, rect.y + 7, rect.x + rect.width - 16, rect.y + 13);
            g.drawLine(rect.x + rect.width - 15, rect.y + 7, rect.x + rect.width - 9, rect.y + 13);
            g.drawLine(rect.x + rect.width - 9, rect.y + 7, rect.x + rect.width - 15, rect.y + 13);
        }

        private class TabMouseHandler extends MouseHandler {

            public TabMouseHandler() {
                super();
            }

            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int tabIndex = -1;
                int tabCount = tabPane.getTabCount();
                for (int i = 0; i < tabCount; i++) {
                    if (rects[i].contains(x, y)) {
                        tabIndex = i;
                        break;
                    }
                }
                if (tabIndex >= 0 && !e.isPopupTrigger()) {
                    Rectangle tabRect = rects[tabIndex];
                    y = y - tabRect.y;
                    if ((x >= tabRect.x + tabRect.width - 18) && (x <= tabRect.x + tabRect.width - 8) && (y >= 5) && (y <= 15)) {
                        if (objects != null) {
                            try {
                                observer.ignore();
                                Object obj = objects.remove(tabIndex);
                                removeObjectTab(tabIndex, obj);
                            } finally {
                                observer.watch();
                            }
                        }
                    }
                }
            }
        }
    }
}
