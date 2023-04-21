package com.limegroup.gnutella.gui.statistics;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import com.limegroup.gnutella.gui.BoxPanel;
import com.limegroup.gnutella.gui.statistics.panes.PaneItem;

final class StatisticsPaneParent implements StatisticsPane {

    /**
	 * Constant for the <tt>Container</tt> that elements are added to. This
	 * is implemented as a <tt>BoxPanel</tt>.
	 */
    private final JComponent CONTAINER = new BoxPanel();

    /**
	 * Constant for the <tt>List</tt> that contains all of the 
	 * <tt>PaneItem</tt> instances associated with this panel.
	 */
    private final List PANE_ITEMS_LIST = new ArrayList();

    /**
	 * <tt>String</tt> for the name of this panel.  This name is used as the
	 * key for identifying this panel in the <tt>CardLayout</tt>.
	 */
    private String _name;

    /**
	 * This sole constructor overrides the the public accessibility of the 
	 * default constructor and is usually called implicitly by subclasses.
	 * 
	 * @param name the unique identifying name of this
	 *             <tt>AbstractStatisticsPane</tt>
	 */
    StatisticsPaneParent(final String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public JComponent getComponent() {
        return CONTAINER;
    }

    /**
	 * Add the <tt>Container</tt>s of the <tt>PaneItem</tt> object to the 
	 * <tt>StatisticsPane</tt> and also <i>registers</i> that pane 
	 * with this class, which means that it is added to the <tt>ArrayList</tt>
	 * of contained <tt>PaneItem</tt> instances.
	 *
	 * @param item the <tt>PaneItem</tt> instance to add
	 */
    public void add(PaneItem item) {
        PANE_ITEMS_LIST.add(item);
        CONTAINER.add(item.getContainer());
    }

    public PaneItem getFirstPaneItem() {
        return (PaneItem) PANE_ITEMS_LIST.get(0);
    }

    public void refresh() {
    }

    public boolean display() {
        return false;
    }

    public void componentResized(ComponentEvent e, Component comp) {
        for (int i = 0, size = PANE_ITEMS_LIST.size(); i < size; i++) {
            ((PaneItem) PANE_ITEMS_LIST.get(i)).componentResized(e, comp);
        }
    }
}
