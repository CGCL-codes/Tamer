package riafswing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import org.apache.log4j.Logger;
import riaf.controller.RiafMgr;
import riaf.facade.IComponent;
import riaf.facade.IContainer;
import riaf.facade.IFrame;
import riaf.facade.IInterceptor;
import riaf.facade.ILayoutManager;
import riaf.facade.IMenuBar;
import riaf.facade.IStyle;
import riaf.facade.IToolBar;
import riaf.facade.ITopLevelWindow;
import riaf.models.ModelEvent;
import riafswing.Factory.RGlassPane;
import riafswing.helper.KeyMgr;
import com.javadocking.DockingManager;
import com.javadocking.dock.FloatDock;
import com.javadocking.dock.Priority;
import com.javadocking.dock.factory.TabDockFactory;
import com.javadocking.model.DockModel;
import com.javadocking.visualizer.FloatExternalizer;
import core.utils.Message;
import core.utils.MessageException;
import core.utils.Performance;

/**
 * The Class RTopLevelWindow is a helper class built over RContainer to add
 * ability of setting modal status of the container and disposing it. Base class
 * for RDialog and RFrame currently.
 */
public abstract class RTopLevelWindow extends RContainer implements ITopLevelWindow {

    /**
	 * static logger-object
	 */
    private static final Logger logger = Logger.getLogger(RTopLevelWindow.class);

    /**
	 * The "decorated" attribute name. Used in read/write user style.
	 */
    protected static final String DECORATED_ATTRIBUTE = "decorated";

    /** The content pane. */
    public JPanel contentPane = null;

    /** The id with which the window is registered in the DockingManager. */
    protected String mDockOwnerId = null;

    /**
	 * The window's float externalizer.
	 */
    protected FloatExternalizer mFloatExternalizer;

    /**
	 * A listener used to notify when the window is about to close.
	 */
    protected WindowClosingListener mCloseListener;

    /**
	 * A custom glass pane used for the accelerators and for the "unpacked"
	 * state.
	 */
    protected RGlassPane glassPane;

    /** The status bar. */
    protected IContainer statusBar = null;

    /**
	 * The west tool bar. This reference is used for convenient access and
	 * control over the frame's west tool bar.
	 */
    public IToolBar leftToolBar = null;

    /**
	 * The east tool bar. This reference is used for convenient access and
	 * control over the frame's east tool bar.
	 */
    public IToolBar rightToolBar = null;

    protected boolean locationChanged = false;

    protected boolean sizeChanged = false;

    protected UserInteractionListener frameSettingsListener;

    /**
	 * Instantiates a new r top level window.
	 * 
	 * @param id
	 *            the id
	 * @param parent
	 *            the parent
	 */
    public RTopLevelWindow(String id, IContainer parent) {
        super(id, parent);
        contentPane = new JPanel(new BorderLayout());
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        mCloseListener = new WindowClosingListener(this);
        frameSettingsListener = new UserInteractionListener(this);
        maybeRegisterWithDockingManager();
    }

    /**
	 * Empty implementation different layouts not supported.
	 */
    @Override
    public void setLayout(String layout) {
    }

    @Override
    public void setTooltip(String tooltip) {
        contentPane.setToolTipText(tooltip);
    }

    @Override
    public void add(IComponent component) {
        if (isDuplicateId(component.getID())) {
            return;
        }
        childs.add(component);
        childIDs.put(component.getID(), component);
        if (component instanceof RComponent) {
            ((RComponent) component).setDirty(Boolean.TRUE);
        }
        component.setParent(this);
        if (component instanceof IMenuBar) {
            if (getImpl() instanceof JDialog) {
                ((JDialog) getImpl()).setJMenuBar((JMenuBar) component.getImpl());
            } else if (getImpl() instanceof JFrame) {
                ((JFrame) getImpl()).setJMenuBar((JMenuBar) component.getImpl());
            } else {
                contentPane.add(((RComponent) component).getImpl());
            }
        } else {
            contentPane.add(((RComponent) component).getImpl());
        }
    }

    @Override
    public Window getImpl() {
        return (Window) super.getImpl();
    }

    @Override
    public void dispose() {
        notifyDispose();
        getImpl().dispose();
    }

    /**
	 * Prepares the frame, applies the styles if any to its components and when
	 * done makes it visible.
	 */
    @Override
    public boolean packComponent() {
        Performance perf = new Performance(true);
        maybeRegisterWithDockingManager();
        boolean styleChanged = super.packComponent();
        if (!getImpl().isShowing()) {
            getImpl().pack();
        }
        Dimension finalSize = null;
        IStyle current = getStyles().getCurrent();
        Integer minWidth = current.getInteger(IStyle.Properties.MIN_SIZE_WIDTH);
        Integer minHeight = current.getInteger(IStyle.Properties.MIN_SIZE_HEIGHT);
        if (minWidth != null || minHeight != null) {
            Dimension size = getImpl().getMinimumSize();
            if (minWidth != null && minWidth != ILayoutManager.NOT_SET) size.width = minWidth;
            if (minHeight != null && minHeight != ILayoutManager.NOT_SET) size.height = minHeight;
            getImpl().setMinimumSize(size);
        }
        Integer prefWidth;
        Integer userSize = getUserPrefWidth();
        if (userSize != null) {
            prefWidth = userSize;
        } else {
            prefWidth = current.getInteger(IStyle.Properties.PREF_SIZE_WIDTH);
        }
        Integer prefHeight;
        userSize = getUserPrefHeight();
        if (userSize != null) {
            prefHeight = userSize;
        } else {
            prefHeight = current.getInteger(IStyle.Properties.PREF_SIZE_HEIGHT);
        }
        if (prefWidth != null || prefHeight != null) {
            Dimension size = getImpl().getPreferredSize();
            if (prefWidth != null && prefWidth != ILayoutManager.NOT_SET) size.width = prefWidth;
            if (prefHeight != null && prefHeight != ILayoutManager.NOT_SET) size.height = prefHeight;
            finalSize = size;
        }
        Dimension windowSize;
        if (finalSize != null) windowSize = finalSize; else windowSize = getImpl().getSize();
        Point loc = getUserLocation();
        if (loc == null) {
            loc = (Point) current.getProperty(IStyle.Properties.LOCATION);
        }
        if (loc == null) {
            loc = new Point(0, 0);
            String alignx = getAlignX();
            Dimension scr = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getSize();
            if (alignx.equals(IStyle.CENTER)) loc.x = Math.max(0, (scr.width - windowSize.width) / 2); else if (alignx.equals(IStyle.RIGHT)) loc.x = Math.max(0, scr.width - windowSize.width);
            String aligny = getAlignY();
            if (aligny.equals(IStyle.CENTER)) loc.y = Math.max(0, (scr.height - windowSize.height) / 2); else if (aligny.equals(IStyle.BOTTOM)) loc.y = Math.max(0, scr.height - windowSize.height);
        }
        getImpl().setLocation(loc);
        glassPane.setParentUnpacked(false);
        boolean windowStateSet = false;
        if (this instanceof IFrame && !((IFrame) this).getWindowState().equals(IFrame.NORMAL)) windowStateSet = true;
        if (finalSize != null && !windowStateSet) {
            getImpl().setSize(finalSize);
        }
        perf.stopAndPrint("packTopLevelWindow(" + getID() + "):" + Boolean.toString(styleChanged), performanceThreshold, logger);
        return styleChanged;
    }

    @Override
    protected ProcessResult styleComponent() {
        ProcessResult result = super.styleComponent();
        if (getImpl() instanceof RootPaneContainer) {
            applyToComponent(getStyles().getCurrent(), ((RootPaneContainer) getImpl()).getContentPane());
            contentPane.setOpaque(false);
        } else {
            applyToComponent(getStyles().getCurrent(), contentPane);
        }
        return result;
    }

    @Override
    public void show() {
        if (!getImpl().isShowing()) {
            getImpl().setVisible(true);
        } else {
            getImpl().toFront();
        }
    }

    @Override
    protected void setPadding(Insets padding) {
        if (getImpl() instanceof RootPaneContainer && ((RootPaneContainer) getImpl()).getContentPane() instanceof JComponent) {
            setPadding(padding, (JComponent) ((RootPaneContainer) getImpl()).getContentPane());
            contentPane.setOpaque(false);
        } else {
            applyToComponent(getStyles().getCurrent(), contentPane);
        }
        super.setPadding(padding);
    }

    protected void createAndSetGlassPane() {
        if (getImpl() instanceof RootPaneContainer) {
            RootPaneContainer impl = (RootPaneContainer) getImpl();
            glassPane = new RGlassPane(impl);
            glassPane.setParentUnpacked(true);
            impl.setGlassPane(glassPane);
        }
    }

    @Override
    public void setStatusBar(IContainer status_bar) {
        if (statusBar != null) {
            childs.remove(statusBar);
            childIDs.remove(statusBar.getID());
            getImpl().remove((Component) statusBar.getImpl());
            statusBar.setParent(null);
        }
        if (status_bar == null || isDuplicateId(status_bar.getID())) {
            return;
        }
        statusBar = status_bar;
        childs.add(statusBar);
        childIDs.put(statusBar.getID(), status_bar);
        if (statusBar instanceof RComponent) {
            ((RComponent) statusBar).setDirty(Boolean.TRUE);
        }
        statusBar.setParent(this);
        getImpl().add((Component) statusBar.getImpl(), BorderLayout.SOUTH);
    }

    @Override
    public IContainer getStatusBar() {
        return statusBar;
    }

    @Override
    public void remove(IComponent child) {
        if (leftToolBar != null && leftToolBar == child) removeToolBar(leftToolBar); else if (rightToolBar != null && rightToolBar == child) removeToolBar(rightToolBar); else super.remove(child);
    }

    @Override
    protected void removeFromSwing(Component c) {
        contentPane.remove(c);
    }

    public boolean addToolBar(IToolBar toolBar, String position) {
        String pos = getToolBarLayoutConstraint(position);
        if (pos == null) {
            Message msg = new Message("IncorrectToolBarPosition", logger);
            msg.addParam("ToolBarCanNotBeAdded");
            msg.addParam(position);
            msg.log();
            return false;
        }
        BorderLayout borderLayout = (BorderLayout) contentPane.getLayout();
        Component componentAtPosition = borderLayout.getLayoutComponent(pos);
        if (componentAtPosition != null) {
            Message msg = new Message("PositionIsNotEmpty", logger);
            msg.addParam("ToolBarCanNotBeAdded");
            msg.addParam(pos);
            msg.log();
            return false;
        }
        childs.add(toolBar);
        childIDs.put(toolBar.getID(), toolBar);
        contentPane.add((Component) toolBar.getImpl(), pos);
        toolBar.setParent(this);
        if (pos == RIGHT) rightToolBar = toolBar; else if (pos == LEFT) leftToolBar = toolBar;
        return true;
    }

    /**
	 * Removes the tool bar at the specified position. If at there is no tool
	 * bar at the given position this method does nothing.
	 * 
	 * @param position
	 *            the position of the tool bar to remove.
	 * @see #removeToolBar(IToolBar)
	 */
    public void removeToolBarAt(String position) {
        String pos = getToolBarLayoutConstraint(position);
        if (pos == RIGHT) {
            removeToolBar(rightToolBar);
        } else if (pos == LEFT) {
            removeToolBar(leftToolBar);
        }
    }

    /**
	 * Removes the given tool bar from the window. If the given tool bar is not
	 * added to this window this method does nothing.
	 * 
	 * @param toolBar
	 *            the tool bar to remove.
	 */
    public void removeToolBar(IToolBar toolBar) {
        if (toolBar == null) return;
        if (toolBar == rightToolBar) rightToolBar = null; else if (toolBar == leftToolBar) leftToolBar = null;
        childs.remove(toolBar);
        childIDs.remove(toolBar.getID());
        contentPane.remove((Component) toolBar.getImpl());
        toolBar.setParent(null);
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);
        if (icon == null) return;
        try {
            getImpl().setIconImage(RiafMgr.global().loadImage(icon));
        } catch (Exception e) {
            if (e instanceof MessageException) {
                ((MessageException) e).getMsg().log();
            } else {
                Message msg = new Message("CannotSetIcon", logger);
                msg.addParam(getID());
                msg.addParam(e.getMessage());
                msg.log();
            }
            getImpl().setIconImage(null);
        }
    }

    public String getToolBarLayoutConstraint(String position) {
        if (LEFT.equalsIgnoreCase(position)) return BorderLayout.WEST; else if (RIGHT.equalsIgnoreCase(position)) return BorderLayout.EAST; else if (TOP.equalsIgnoreCase(position)) {
            return BorderLayout.NORTH;
        } else if (BOTTOM.equalsIgnoreCase(position)) {
            return BorderLayout.SOUTH;
        } else return null;
    }

    @Override
    public void setDecorated(boolean decorated) {
        if (getImpl() instanceof JFrame) {
            ((JFrame) getImpl()).setUndecorated(!decorated);
        } else if (getImpl() instanceof JDialog) {
            ((JDialog) getImpl()).setUndecorated(!decorated);
        } else {
            Message msg = new Message("DecorationNotSupported", logger);
            msg.addParam(getImpl().getClass().getName());
            msg.log();
        }
    }

    /**
	 * Called before the window is disposed to unregister the listeners and
	 * unregister the window from the docking manager.
	 */
    protected void notifyDispose() {
        getImpl().removeWindowListener(mCloseListener);
        unregisterWithDockingManager();
    }

    protected void maybeRegisterWithDockingManager() {
        DockModel dm = DockingManager.getDockModel();
        if (dm.getOwnerID(getImpl()) != null) return;
        if (mDockOwnerId == null) mDockOwnerId = getID();
        int idIndex = 0;
        for (int i = 0; i < dm.getOwnerCount(); i++) {
            if (dm.getOwnerID(dm.getOwner(i)).equals(mDockOwnerId)) {
                mDockOwnerId = getID() + idIndex;
                i = 0;
                idIndex++;
            }
        }
        dm.addOwner(mDockOwnerId, getImpl());
        FloatDock floatDock = dm.getFloatDock(getImpl());
        floatDock.setChildDockFactory(new TabDockFactory());
        floatDock.setDockPriority(Priority.CAN_DOCK_WITH_PRIORITY);
        if (dm.getVisualizer(mDockOwnerId + "_floatExternalizer") == null) {
            mFloatExternalizer = new FloatExternalizer(getImpl());
            dm.addVisualizer(mDockOwnerId + "_floatExternalizer", mFloatExternalizer, getImpl());
        }
        getImpl().removeWindowListener(mCloseListener);
        getImpl().addWindowListener(mCloseListener);
    }

    protected void unregisterWithDockingManager() {
        if (mDockOwnerId == null) return;
        getImpl().removeWindowListener(mCloseListener);
        DockModel dm = DockingManager.getDockModel();
        if (dm.getVisualizer(mDockOwnerId + "_floatExternalizer") != null) {
            dm.removeVisualizer(mFloatExternalizer);
        }
        if (dm.getOwnerID(getImpl()) != null) {
            dm.removeOwner(getImpl());
        }
    }

    protected final Integer getUserPrefWidth() {
        String wStr = RiafMgr.global().getUserSetting(this, IStyle.Properties.PREF_SIZE_WIDTH.toString());
        if (wStr != null) {
            try {
                return new Integer(wStr);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    protected final Integer getUserPrefHeight() {
        String hStr = RiafMgr.global().getUserSetting(this, IStyle.Properties.PREF_SIZE_HEIGHT.toString());
        if (hStr != null) {
            try {
                return new Integer(hStr);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    protected final Point getUserLocation() {
        String locStr = RiafMgr.global().getUserSetting(this, IStyle.Properties.LOCATION.toString());
        if (locStr != null) {
            String[] values = locStr.split("\\,");
            try {
                return new Point(Integer.valueOf(values[0]), Integer.valueOf(values[1]));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected static class WindowClosingListener extends WindowAdapter {

        /**
		 * The listener's owner window.
		 */
        private RTopLevelWindow mOwner = null;

        public WindowClosingListener(RTopLevelWindow owner) {
            mOwner = owner;
        }

        /**
		 * Fires a "Close" event to notify the window's listeners.
		 */
        @Override
        public void windowClosing(WindowEvent e) {
            ModelEvent close = new ModelEvent(mOwner.getModel(), IInterceptor.CLOSE);
            close.setParameter(mOwner.getID());
            mOwner.fireEvent(close);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            KeyMgr.global().hideAccelerators();
        }
    }

    protected static class UserInteractionListener extends ComponentAdapter implements WindowStateListener {

        private RTopLevelWindow window;

        boolean shown = false;

        /**
         * Flag raised when the window is in fullscreen mode. In this mode
         * location and size preferences and changes are ignored/removed.
         */
        private boolean fFullscreen = false;

        /**
         * Flag raised when the window is in maximixed mode. In this mode
         * location and size preferences and changes are ignored/removed.
         */
        private boolean fMaximized = false;

        public UserInteractionListener(RTopLevelWindow window) {
            this.window = window;
            window.getImpl().addComponentListener(this);
            if (window.getImpl() instanceof JFrame) window.getImpl().addWindowStateListener(this);
        }

        @Override
        public void componentShown(ComponentEvent e) {
            shown = true;
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            shown = false;
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            if (shown) {
                if (e.getComponent().isShowing()) {
                    window.locationChanged = true;
                    if (!(fFullscreen || fMaximized)) {
                        Point loc = e.getComponent().getLocationOnScreen();
                        RiafMgr.global().saveUserSetting(window, IStyle.Properties.LOCATION.toString(), loc.x + "," + loc.y);
                    }
                } else {
                    shown = false;
                }
            }
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (shown) {
                window.sizeChanged = true;
                if (!(fFullscreen || fMaximized)) {
                    Component c = e.getComponent();
                    RiafMgr.global().saveUserSetting(window, IStyle.Properties.PREF_SIZE_WIDTH.toString(), Integer.toString(c.getWidth()));
                    RiafMgr.global().saveUserSetting(window, IStyle.Properties.PREF_SIZE_HEIGHT.toString(), Integer.toString(c.getHeight()));
                }
            }
        }

        /**
         * Updates the state flags and the component custom settings when the
         * state has changed
         * 
         * @param newState
         *            the new state. One of IFrame's state constants
         */
        public void updateState(String newState) {
            fFullscreen = IFrame.FULLSCREEN.equals(newState);
            fMaximized = IFrame.MAXIMIZED.equals(newState);
            if (newState != null) {
                RiafMgr.global().saveUserSetting(window, IFrame.STATE_PROPERTY, newState);
                if (fFullscreen || fMaximized) {
                    RiafMgr.global().removeUserSetting(window, IStyle.Properties.LOCATION.toString());
                    RiafMgr.global().removeUserSetting(window, IStyle.Properties.PREF_SIZE_WIDTH.toString());
                    RiafMgr.global().removeUserSetting(window, IStyle.Properties.PREF_SIZE_HEIGHT.toString());
                }
            } else {
                RiafMgr.global().removeUserSetting(window, IFrame.STATE_PROPERTY);
            }
        }

        @Override
        public void windowStateChanged(WindowEvent e) {
            window.sizeChanged = true;
            window.locationChanged = true;
            String newState = null;
            switch(e.getNewState()) {
                case Frame.ICONIFIED:
                    newState = IFrame.MINIMIZED;
                    break;
                case Frame.MAXIMIZED_BOTH:
                    newState = IFrame.MAXIMIZED;
                    break;
                case Frame.NORMAL:
                case Frame.MAXIMIZED_HORIZ:
                case Frame.MAXIMIZED_VERT:
                    newState = IFrame.NORMAL;
                    break;
                default:
                    GraphicsDevice[] gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
                    for (int i = 0; i < gds.length; i++) {
                        if (gds[i].getFullScreenWindow() == window.getImpl()) newState = IFrame.FULLSCREEN;
                    }
            }
            updateState(newState);
        }
    }
}
