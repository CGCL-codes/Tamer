package org.sqlexp.workbench;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.sqlexp.preferences.Preference;
import org.sqlexp.preferences.data.ShellState;
import org.sqlexp.util.language.LanguageEntry;
import org.sqlexp.workbench.util.AbstractMenuFactory;
import org.sqlexp.workbench.util.AbstractToolBarFactory;

/**
 * Abstract SQL Exp UI View part.
 * @author Matthieu Réjou
 */
public abstract class ViewPart {

    private IViewContainer container;

    private Composite composite;

    private ViewLocation location;

    private DisposeListener disposeListener;

    private ControlListener stateListener;

    private Preference statePreference;

    private String stateProperty;

    private AbstractMenuFactory menuFactory;

    private AbstractToolBarFactory toolBarFactory;

    private LanguageEntry titleEntry;

    private String title;

    private Image image;

    private ArrayList<IViewListener> listeners = new ArrayList<IViewListener>(2);

    /**
	 * Constructs a new view on given parent.
	 * @param container containing the receiver
	 * @param location of the view, relative to its container
	 */
    public final void initialize(final IViewContainer container, final ViewLocation location) {
        this.initialize(container, location, container == null);
    }

    /**
	 * Constructs a new view on given parent.
	 * @param container containing the receiver
	 * @param location of the view, relative to its container
	 * @param window true if view may be displayed in an independent window
	 */
    public final void initialize(final IViewContainer container, final ViewLocation location, final boolean window) {
        if (window && container != null) {
            this.container = new WindowContainer(container.getShell());
        } else if (window) {
            this.container = new WindowContainer();
        } else {
            this.container = container;
        }
        this.location = location;
        this.container.add(this, location);
    }

    /**
	 * Sets a composite as main container,
	 * disposing previous one if view has been already initialized,
	 * and invoking its implementation specific content creation.<br>
	 * Invoked by container on <code>add()</code> method.
	 * @param composite to define as container
	 */
    protected final void setComposite(final Composite composite) {
        if (this.composite != null) {
            while (this.composite.getChildren().length > 0) {
                this.composite.getChildren()[0].setParent(composite);
            }
            if (disposeListener != null) {
                this.composite.removeDisposeListener(disposeListener);
            }
            this.composite.dispose();
            this.composite = composite;
        } else {
            this.composite = composite;
            createContents(composite);
        }
        disposeListener = new DisposeListener() {

            @Override
            public void widgetDisposed(final DisposeEvent e) {
                notifyClosed();
            }
        };
        composite.addDisposeListener(disposeListener);
    }

    /**
	 * Gets the view current location.
	 * @return current view location
	 */
    protected final ViewLocation getLocation() {
        return location;
    }

    /**
	 * Gets the container shell.
	 * @return shell SWT widget
	 */
    public final Shell getShell() {
        return composite.getShell();
    }

    /**
	 * Gets the view display.
	 * @return shell SWT widget
	 */
    public final Display getDisplay() {
        return composite.getDisplay();
    }

    /**
	 * Opens the window, should be invoked only if container is a <code>WindowContainer</code>.
	 * @param wait true if events should be dispatched until the window is closed.
	 */
    public final void open(final boolean wait) {
        Shell shell = getShell();
        computeShellSize();
        shell.open();
        if (wait) {
            Display display = shell.getDisplay();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        }
    }

    /**
	 * Computes shell size.<br>
	 * Should be invoked only if container is a <code>WindowContainer</code>.
	 */
    private void computeShellSize() {
        if (statePreference == null || stateProperty == null || stateProperty.isEmpty()) {
            return;
        }
        final Shell shell = getShell();
        ShellState state = (ShellState) statePreference.get(stateProperty);
        shell.setLocation(state.getLocationX(), state.getLocationY());
        shell.setSize(state.getWidth(), state.getHeight());
        shell.setMaximized(state.isMaximized());
        if (stateListener != null) {
            shell.removeControlListener(stateListener);
        } else {
            stateListener = new ControlListener() {

                @Override
                public void controlResized(final ControlEvent e) {
                    Point p = shell.getSize();
                    ShellState state = (ShellState) statePreference.get(stateProperty);
                    state.setWidth(p.x);
                    state.setHeight(p.y);
                    state.setMaximized(shell.getMaximized());
                }

                @Override
                public void controlMoved(final ControlEvent e) {
                    Point p = shell.getLocation();
                    ShellState state = (ShellState) statePreference.get(stateProperty);
                    state.setLocationX(p.x);
                    state.setLocationY(p.y);
                }
            };
        }
        shell.addControlListener(stateListener);
    }

    /**
	 * Defines the menu factory.<br>
	 * Must be invoked after (or during) initialization.<br>
	 * <b>Multiple invocations to this method will be ignored.</b><br>
	 * Note that if the receiver is an extensible part, menu will be
	 * displayed on each tab selector.
	 * @param menuFactory to define
	 */
    public final void setMenuFactory(final AbstractMenuFactory menuFactory) {
        if (this.menuFactory == null) {
            this.menuFactory = menuFactory;
            menuFactory.createMenu(composite);
        }
    }

    /**
	 * Gets the menu factory associated with the receiver.
	 * @return menu factory, may be null
	 */
    AbstractMenuFactory getMenuFactory() {
        return menuFactory;
    }

    /**
	 * Defines the tool bar factory.<br>
	 * Must be invoked after (or during) initialization.<br>
	 * <b>Multiple invocations to this method will be ignored.</b>
	 * @param toolBarFactory to define
	 */
    public final void setToolBarFactory(final AbstractToolBarFactory toolBarFactory) {
        if (this.toolBarFactory == null) {
            this.toolBarFactory = toolBarFactory;
            ToolBar toolBar = toolBarFactory.createToolbar(composite);
            container.setToolBar(this, toolBar);
        }
    }

    /**
	 * Gets the main composite container.
	 * TODO : set the ViewPart.getComposite() method protected
	 * @return SWT composite
	 */
    public final Composite getComposite() {
        return composite;
    }

    /**
	 * Gets the view bounds, relative to display.
	 * @return rectangle bounds in pixels
	 */
    protected final Rectangle getDisplayBounds() {
        if (container instanceof WindowContainer) {
            return getShellBounds();
        }
        Point location = composite.toDisplay(0, 0);
        Point size = composite.getSize();
        return new Rectangle(location.x, location.y, size.x, size.y);
    }

    /**
	 * Gets the view bounds, relative to display.
	 * @return rectangle bounds in pixels
	 */
    private Rectangle getShellBounds() {
        Shell shell = getShell();
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        Point childLoc;
        for (Control control : shell.getChildren()) {
            childLoc = control.toDisplay(0, 0);
            x = Math.min(x, childLoc.x);
            y = Math.min(y, childLoc.y);
        }
        Point location = new Point(x, y);
        Rectangle clientArea = shell.getClientArea();
        Point size = new Point(clientArea.width, clientArea.height);
        return new Rectangle(location.x, location.y, size.x, size.y);
    }

    /**
	 * Determines if view is active.
	 * @return true if active
	 */
    public final boolean isActive() {
        return composite != null && !composite.isDisposed();
    }

    /**
	 * Disposes all the view content, properly removing it from container.
	 */
    public final void close() {
        container.remove(this);
    }

    /**
	 * Sets the focus on the view.
	 */
    public final void focus() {
        container.focus(this);
    }

    /**
	 * 
	 */
    protected void onFocus() {
    }

    ;

    /**
	 * Initializes view content on the given container.<br>
	 * Invoked on initialization.
	 * @param composite to create controls on
	 */
    protected abstract void createContents(Composite composite);

    /**
	 * gets the receiver parent.
	 * @return parent view
	 */
    public final IViewContainer getParent() {
        return container;
    }

    /**
	 * @return the titleEntry
	 */
    public final LanguageEntry getTitleEntry() {
        return titleEntry;
    }

    /**
	 * @param titleEntry the titleEntry to set
	 */
    public final void setTitleEntry(final LanguageEntry titleEntry) {
        this.titleEntry = titleEntry;
        container.setTitle(this);
    }

    /**
	 * @return the title
	 */
    public final String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public final void setTitle(final String title) {
        this.title = title;
        container.setTitle(this);
    }

    /**
	 * Gets the view image.
	 * @return view image
	 */
    public final Image getImage() {
        return image;
    }

    /**
	 * @param image the image to set
	 */
    public final void setImage(final Image image) {
        this.image = image;
        container.setImage(this);
    }

    /**
	 * Gets the receiver size.
	 * @return size in pixels
	 */
    public final Point getSize() {
        return composite.getSize();
    }

    /**
	 * Converts the given coordinate to display relative coordinate.
	 * @param x coordinate in pixels
	 * @param y coordinate in pixels
	 * @return display relative coordinate
	 * @see Composite#toDisplay(Point)
	 */
    public final Point toDisplay(final int x, final int y) {
        return composite.toDisplay(x, y);
    }

    /**
	 * Converts the given coordinate to display relative coordinate.
	 * @param point to convert (in pixels)
	 * @return display relative coordinate
	 * @see Composite#toDisplay(Point)
	 */
    public final Point toDisplay(final Point point) {
        return composite.toDisplay(point);
    }

    public Point getMinimumSize() {
        return new Point(SWT.DEFAULT, SWT.DEFAULT);
    }

    /**
	 * Sets the state property used to define and store shell state.<br>
	 * Should be invoked only if container is a <code>WindowContainer</code>.
	 * @param preference object
	 * @param property on preference object
	 */
    public final void setStateProperty(final Preference preference, final String property) {
        statePreference = preference;
        stateProperty = property;
        if (getShell().isVisible()) {
            computeShellSize();
        }
    }

    /**
	 * Adds a view listener on the receiver.
	 * @param listener to add
	 */
    public final void addViewListener(final IViewListener listener) {
        if (listener != null || !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
	 * Removes a view listener on the receiver.
	 * @param listener to remove
	 */
    public final void removeViewListener(final IViewListener listener) {
        if (listener != null || listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
	 * Gets a copy of available listeners on the receiver.
	 * @return listeners collection
	 */
    protected final Collection<IViewListener> getListeners() {
        return new ArrayList<IViewListener>(listeners);
    }

    /**
	 * Notifies the listeners the view has been closed.
	 */
    private void notifyClosed() {
        for (IViewListener listener : getListeners()) {
            listener.viewClosed(this);
        }
    }

    /**
	 * Notifies the listeners the given view has been focused.<br>
	 * <b>View contrainers are responsible of firing this event.</b>
	 */
    final void notifyFocused() {
        for (IViewListener listener : getListeners()) {
            listener.viewFocused(this);
        }
    }
}
