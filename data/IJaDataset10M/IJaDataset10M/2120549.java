package edu.washington.assist.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Viewer<T extends Component> extends JPanel implements ChangeListener, JDialogPopoutAblePanel<Viewer.Dialog<T>>, ComponentListener {

    private static final long serialVersionUID = 1L;

    private static AssistGUI gui = null;

    private ArrayList<Tab> tabs;

    private JTabbedPane pane;

    private TabDragListener dragListener = null;

    private CloseTabIcon closeIcon;

    private ArrayList<ViewerListener> listeners = new ArrayList<ViewerListener>();

    private ArrayList<ViewerChangeListener> changeListeners = new ArrayList<ViewerChangeListener>();

    @SuppressWarnings("unchecked")
    public Viewer(AssistGUI gui, TabDragListener dragListener) {
        Viewer.gui = gui;
        this.tabs = new ArrayList<Tab>();
        this.pane = new JTabbedPane();
        this.setLayout(new BorderLayout());
        super.add(pane, BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(500, 210));
        this.setPreferredSize(new Dimension(700, 210));
        this.setMaximumSize(new Dimension(10000, 230));
        this.dragListener = dragListener;
        this.addMouseMotionListener(dragListener);
        this.addMouseListener(dragListener);
        pane.addChangeListener(this);
        this.closeIcon = new CloseTabIcon(this);
    }

    /**
	 * Construct a Viewer with non-dragable tabs.
	 * 
	 * @param gui
	 */
    public Viewer() {
        this.tabs = new ArrayList<Tab>();
        this.pane = new JTabbedPane();
        this.setLayout(new BorderLayout());
        super.add(pane, BorderLayout.CENTER);
        pane.addChangeListener(this);
        this.closeIcon = new CloseTabIcon(this);
    }

    public void addViewerListener(ViewerListener listener) {
        this.listeners.add(listener);
    }

    public void removeViewerListener(ViewerListener listener) {
        this.listeners.remove(listener);
    }

    public void removeAllViewerListeners() {
        this.listeners = new ArrayList<ViewerListener>();
    }

    public void addViewerChangeListener(ViewerChangeListener listener) {
        this.changeListeners.add(listener);
    }

    public void removeViewerChangeListener(ViewerChangeListener listener) {
        this.changeListeners.remove(listener);
    }

    public void removeAllViewerChangeListeners() {
        this.changeListeners = new ArrayList<ViewerChangeListener>();
    }

    /**
	 * Add a tab to the viewer.
	 * 
	 * @param tab The tab to be added.
	 */
    public void addTab(Tab<T> tab) {
        if (dragListener != null) {
            propagateListener(tab.component);
        }
        tab.visible = true;
        if (tab.menuItem != null) {
            tab.menuItem.setSelected(true);
        }
        tab.container = this;
        tabs.add(tab);
        pane.addTab(tab.title, this.closeIcon, tab.component, tab.toolTip);
        for (ViewerListener listener : listeners) {
            listener.tabAdded(new ViewerEvent("Tab Added", this, tab));
        }
    }

    /**
	 * Remove a tab from the viewer.  This does not destroy the tab.
	 * 
	 * @param tab
	 */
    public void removeTab(Tab<T> tab) {
        if (tab == null) {
            return;
        }
        tab.visible = false;
        if (tab.menuItem != null) {
            tab.menuItem.setSelected(false);
        }
        tab.container = null;
        tabs.remove(tab);
        pane.remove(tab.component);
        for (ViewerListener listener : listeners) {
            listener.tabRemoved(new ViewerEvent("Tab Removed", this, tab));
        }
        if (getTabCount() <= 0) {
            for (ViewerListener listener : listeners) {
                listener.viewerEmpty(new ViewerEvent("Empty Viewer", this, null));
            }
        }
    }

    public void remove(Tab<T> tab) {
        removeTab(tab);
    }

    /**
	 * Returns the selected Viewer.Tab component of this Viewer.  Or returns null if that
	 * no tab is selected (Viewer is empty).
	 */
    @SuppressWarnings("unchecked")
    public Tab<T> getSelectedTab() {
        if (pane.getSelectedIndex() < 0) {
            return null;
        }
        return tabs.get(pane.getSelectedIndex());
    }

    public void setSelectedTab(Tab<T> tab) {
        pane.setSelectedComponent(tab.component);
    }

    public int getTabCount() {
        return pane.getTabCount();
    }

    public void stateChanged(ChangeEvent e) {
        for (ViewerChangeListener listener : changeListeners) {
            listener.selectedTabChange(new ViewerEvent("Tab Changed", this, getSelectedTab()));
        }
    }

    protected JTabbedPane getTabbedPane() {
        return pane;
    }

    @SuppressWarnings("unchecked")
    public void popin(Dialog<T> d) {
        d.setVisible(false);
        if (d.viewer != null) {
            d.viewer.addTab(d.tab);
        } else {
            d.tab.visible = false;
            gui.showTab(d.tab);
        }
        d.tab.dialog = null;
        d.tab.menuItem.setSelected(true);
    }

    @SuppressWarnings("unchecked")
    public Dialog<T> popout() {
        Tab t = getSelectedTab();
        this.removeTab(t);
        t.visible = true;
        t.menuItem.setSelected(true);
        Dialog d = new Dialog(this, t, (PopoutMenuPanel) t.component);
        d.addComponentListener(this);
        d.setMinimumSize(this.getMinimumSize());
        d.setPreferredSize(this.getPreferredSize());
        d.pack();
        d.setLocationRelativeTo(gui);
        d.setVisible(true);
        t.dialog = d;
        return d;
    }

    private void propagateListener(Component component) {
        component.addMouseListener(dragListener);
        if (Container.class.isInstance(component)) {
            for (Component c : ((Container) component).getComponents()) {
                c.addMouseListener(dragListener);
            }
        }
    }

    @SuppressWarnings("hiding")
    public static class Tab<T extends Component> {

        protected boolean visible;

        protected String title;

        protected String toolTip;

        protected JCheckBoxMenuItem menuItem;

        protected Viewer container;

        protected Dialog dialog = null;

        protected T component;

        public Tab(String title, T component) {
            this.title = title;
            this.toolTip = null;
            this.visible = false;
            this.menuItem = null;
            this.component = component;
        }

        public Tab(String title, JCheckBoxMenuItem menuItem, T component) {
            this.title = title;
            this.toolTip = null;
            this.visible = false;
            this.menuItem = menuItem;
            menuItem.setState(true);
            this.component = component;
        }

        public Tab(String title, String toolTip, JCheckBoxMenuItem menuItem, T component) {
            this.title = title;
            this.toolTip = toolTip;
            this.visible = false;
            this.menuItem = menuItem;
            menuItem.setState(true);
            this.component = component;
        }

        public boolean isVisible() {
            return visible;
        }

        public String getTitle() {
            return title;
        }

        public String getToolTip() {
            return toolTip;
        }

        public T getComponent() {
            return component;
        }
    }

    @SuppressWarnings({ "serial", "hiding" })
    public static class Dialog<T extends Component> extends JDialog {

        protected Tab tab;

        protected Viewer viewer;

        protected PopoutMenuPanel component;

        public Dialog(Viewer viewer, Tab tab, PopoutMenuPanel component) {
            super(gui, "kittynet: " + tab.title, false);
            this.viewer = viewer;
            this.tab = tab;
            this.component = component;
            JPanel p = new JPanel(new BorderLayout());
            p.add(tab.component, BorderLayout.CENTER);
            this.setContentPane(p);
            this.setAlwaysOnTop(true);
        }
    }

    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        pane.setPreferredSize(d);
    }

    public void setMinimumSize(Dimension d) {
        super.setMinimumSize(d);
        pane.setMinimumSize(d);
    }

    public void setMaxmimumSize(Dimension d) {
        super.setMaximumSize(d);
        pane.setMaximumSize(d);
    }

    public void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        pane.addMouseListener(l);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        pane.addMouseMotionListener(l);
    }

    @SuppressWarnings("unchecked")
    public void componentHidden(ComponentEvent arg0) {
        Dialog d = (Dialog) arg0.getComponent();
        d.tab.dialog = null;
        d.tab.container = null;
        d.tab.visible = false;
        d.tab.menuItem.setSelected(false);
        d.component.updatePopoutMenu(false);
    }

    public void componentMoved(ComponentEvent arg0) {
    }

    public void componentResized(ComponentEvent arg0) {
    }

    public void componentShown(ComponentEvent arg0) {
    }
}
