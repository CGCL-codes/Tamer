package net.sf.doolin.gui.docking;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.doolin.gui.docking.storage.DefaultDockingStorage;
import net.sf.doolin.gui.docking.storage.DockingStorage;
import net.sf.doolin.gui.expression.GUIExpression;
import net.sf.doolin.gui.service.IconSize;
import net.sf.doolin.gui.view.GUIView;
import net.sf.doolin.gui.view.descriptor.GUIViewDescriptor;
import net.sf.doolin.gui.view.support.GUIViewUtils;
import net.sf.doolin.gui.window.GUIWindow;
import net.sf.doolin.gui.window.descriptor.AbstractCompositeGUIWindowDescriptor;
import net.sf.doolin.gui.window.descriptor.ViewReference;
import net.sf.doolin.util.Utils;
import net.sf.doolin.util.factory.DataFactory;
import net.sf.doolin.util.factory.IdentityDataFactory;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javadocking.DockingManager;
import com.javadocking.component.DefaultSwComponentFactory;
import com.javadocking.dock.BorderDock;
import com.javadocking.dock.CompositeDock;
import com.javadocking.dock.Dock;
import com.javadocking.dock.LeafDock;
import com.javadocking.dock.Position;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dock.docker.BorderDocker;
import com.javadocking.dock.factory.ToolBarDockFactory;
import com.javadocking.dockable.ActionDockable;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.StateActionDockable;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.model.DockModel;
import com.javadocking.model.FloatDockModel;
import com.javadocking.util.DockingUtil;
import com.javadocking.util.LookAndFeelUtil;
import com.javadocking.visualizer.DockingMinimizer;
import com.javadocking.visualizer.FloatExternalizer;
import com.javadocking.visualizer.SingleMaximizer;

/**
 * Window descriptor based on <a
 * href="http://www.javadocking.com/">Javadocking</a> API.
 * 
 * @author Damien Coraboeuf
 * @param <B>
 *            Type of bean for the window
 * 
 */
public class DockingWindowDescriptor<B> extends AbstractCompositeGUIWindowDescriptor<B, DockingConstraint> {

    private static final String ROOT_DOCK_KEY = "rootDock";

    /**
	 * View property that stores the Javadocking close action
	 * 
	 * @see net.sf.doolin.gui.util.PropertyContainer#getProperty(Object)
	 * @see net.sf.doolin.gui.util.PropertyContainer#setProperty(Object, Object)
	 */
    protected static final String VIEW_PROPERTY_DOCKING_CLOSE_ACTION = "DockingCloseAction";

    private static final Logger log = LoggerFactory.getLogger(DockingWindowDescriptor.class);

    /**
	 * Client property that stores in the window the {@link DockModel}.
	 */
    protected static final String CLIENT_PROPERTY_DOCK_MODEL = "dockModel";

    /**
	 * Path separator for the
	 * {@linkplain DockingConstraint#setDockingPath(String) docking path}.
	 */
    private static final String PATH_SEPARATOR = ";";

    private boolean saveWorkspace;

    private DockingStorage dockingStorage = new DefaultDockingStorage();

    @Override
    public void activateView(GUIWindow<B> window, GUIView<?> view) {
        Dockable dockable = getDockable(view);
        if (dockable != null) {
            Dock dock = dockable.getDock();
            if (dock instanceof TabDock) {
                ((TabDock) dock).setSelectedDockable(dockable);
            }
        } else {
            log.warn(String.format("Cannot find dockable for view with ID %s", view.getID()));
        }
        super.activateView(window, view);
    }

    @Override
    public void hideView(GUIWindow<B> window, GUIView<?> view) {
        Action action = (Action) view.getProperty(VIEW_PROPERTY_DOCKING_CLOSE_ACTION);
        if (action != null) {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
        super.hideView(window, view);
    }

    /**
	 * General initialisation of the {@link DockingManager}
	 */
    @PostConstruct
    public void init() {
        DockingManager.setComponentFactory(new DefaultSwComponentFactory() {

            @Override
            public JTabbedPane createJTabbedPane() {
                final JTabbedPane tabbedPane = super.createJTabbedPane();
                tabbedPane.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        TabDock tabDock = (TabDock) SwingUtilities.getAncestorOfClass(TabDock.class, tabbedPane);
                        if (tabDock != null) {
                            Dockable dockable = tabDock.getSelectedDockable();
                            if (dockable != null) {
                                Component content = dockable.getContent();
                                if (content instanceof DockingComponent<?>) {
                                    ((DockingComponent<?>) content).activateView();
                                }
                            }
                        }
                    }
                });
                return tabbedPane;
            }
        });
        log.debug("Initialising the general focus manager");
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                String prop = propertyChangeEvent.getPropertyName();
                if (prop.equals("focusOwner")) {
                    Component owner = (Component) propertyChangeEvent.getNewValue();
                    if (owner != null) {
                        DockingComponent<?> ancestor = (DockingComponent<?>) SwingUtilities.getAncestorOfClass(DockingComponent.class, owner);
                        if (ancestor != null) {
                            ancestor.activateView();
                        }
                    }
                }
            }
        });
    }

    /**
	 * Checks if the docking must be saved when the window is closed.
	 * 
	 * @return <code>true</code> if the docking must be saved when the window is
	 *         closed.
	 */
    public boolean isSaveWorkspace() {
        return this.saveWorkspace;
    }

    /**
	 * Saves the docking paths
	 */
    @Override
    public void onClosingWindow(GUIWindow<B> window) {
        if (this.saveWorkspace) {
            String windowId = window.getId();
            try {
                DockModel dockModel = getDockModel(window);
                Dock rootDock = dockModel.getRootDock(ROOT_DOCK_KEY);
                CompositeDock compositeRootDock = (CompositeDock) rootDock;
                DockDescription rootDockDescription = new DockDescription(compositeRootDock.getClass().getName(), false, null, null);
                fillDockDescription(rootDockDescription, compositeRootDock);
                this.dockingStorage.store(window, rootDockDescription);
            } catch (Exception th) {
                log.error(String.format("Cannot save docking configuration for %s", windowId), th);
            }
        }
        super.onCloseWindow(window);
    }

    /**
	 * Sets the storage used to store the docking information.
	 * 
	 * @param dockingStorage
	 *            Storage to use
	 */
    public void setDockingStorage(DockingStorage dockingStorage) {
        this.dockingStorage = dockingStorage;
    }

    /**
	 * Sets if the docking must be saved when the window is closed.
	 * 
	 * @param saveWorkspace
	 *            <code>true</code> if the docking must be saved when the window
	 *            is closed.
	 */
    public void setSaveWorkspace(boolean saveWorkspace) {
        this.saveWorkspace = saveWorkspace;
    }

    @Override
    protected <V> void arrangeViewInWindow(GUIWindow<B> window, final GUIView<V> view, JComponent viewDecoratedComponent, DockingConstraint constraint) {
        if (constraint == null) {
            constraint = new DockingConstraint();
        }
        Dockable dockable = createViewDockable(window, view, viewDecoratedComponent, constraint);
        DockModel dockModel = getDockModel(window);
        Dock rootDock = dockModel.getRootDock(ROOT_DOCK_KEY);
        Dock dock;
        String dockingPath = constraint.getDockingPath();
        if (StringUtils.isNotBlank(dockingPath)) {
            dock = getDock(rootDock, dockingPath);
        } else {
            dock = rootDock;
        }
        DockingManager.getDockingExecutor().changeDocking(dockable, dock);
    }

    @Override
    protected void arrangeViews(GUIWindow<B> window) {
        DockDescription dockDescription = this.dockingStorage.retrieve(window);
        if (this.saveWorkspace && dockDescription != null) {
            restore(window, dockDescription);
        } else {
            super.arrangeViews(window);
        }
    }

    @Override
    protected void arrangeWindow(GUIWindow<B> window) {
        String windowId = window.getId();
        JPanel windowPanel = window.getWindowPanel();
        Window frame = SwingUtilities.getWindowAncestor(windowPanel);
        windowPanel.setLayout(new BorderLayout());
        LookAndFeelUtil.removeAllSplitPaneBorders();
        FloatDockModel dockModel = new FloatDockModel();
        DockingManager.setDockModel(dockModel);
        dockModel.addOwner(windowId, frame);
        windowPanel.putClientProperty(CLIENT_PROPERTY_DOCK_MODEL, dockModel);
        SplitDock rootDock = new SplitDock();
        dockModel.addRootDock(ROOT_DOCK_KEY, rootDock, frame);
        SingleMaximizer maximizer = new SingleMaximizer(rootDock);
        dockModel.addVisualizer("maximizer", maximizer, frame);
        BorderDock borderDock = new BorderDock(new ToolBarDockFactory());
        borderDock.setMode(BorderDock.MODE_MINIMIZE_BAR);
        borderDock.setCenterComponent(maximizer);
        BorderDocker borderDocker = new BorderDocker();
        borderDocker.setBorderDock(borderDock);
        DockingMinimizer minimizer = new DockingMinimizer(borderDocker);
        dockModel.addVisualizer("minimizer", minimizer, frame);
        dockModel.addRootDock("minimizerBorderDock", borderDock, frame);
        FloatExternalizer externalizer = new FloatExternalizer(frame);
        dockModel.addVisualizer("externalizer", externalizer, frame);
        windowPanel.add(borderDock, BorderLayout.CENTER);
    }

    /**
	 * Creates a {@link Dockable} for a view.
	 * 
	 * @param <V>
	 *            Type of bean for the view
	 * @param window
	 *            Hosting docking window
	 * @param view
	 *            View to dock
	 * @param viewDecoratedComponent
	 *            Decorated component for the view
	 * @param constraint
	 *            Docking constraints
	 * @return Dockable
	 */
    protected <V> Dockable createViewDockable(GUIWindow<B> window, final GUIView<V> view, JComponent viewDecoratedComponent, DockingConstraint constraint) {
        String id = view.getID();
        final GUIExpression titleExpression = GUIViewUtils.getTitleExpression(view);
        String title = titleExpression.getValue();
        Icon icon = null;
        String iconId = view.getViewDescriptor().getIconId();
        if (StringUtils.isNotBlank(iconId)) {
            icon = getIconService().getIcon(iconId, IconSize.SMALL);
        }
        DockingComponent<V> dockingComponent = new DockingComponent<V>(view, viewDecoratedComponent);
        final DefaultDockable sourceDockable = new DefaultDockable(id, dockingComponent, title, icon);
        sourceDockable.setWithHeader(constraint.isHeader());
        titleExpression.connect(view.getActionContext().getSubscriberValidator(), sourceDockable, "title");
        Dockable dockable = sourceDockable;
        int[] actions = constraint.getActions();
        DefaultDockableStateActionFactory stateActionFactory = new DefaultDockableStateActionFactory();
        dockable = new StateActionDockable(dockable, stateActionFactory, actions);
        if (constraint.isCloseable()) {
            Action javadockingCloseAction = stateActionFactory.createDockableStateAction(dockable, DockableState.CLOSED);
            view.setProperty(VIEW_PROPERTY_DOCKING_CLOSE_ACTION, javadockingCloseAction);
            Action closeAction = new CloseAction(javadockingCloseAction, view);
            dockable = new ActionDockable(dockable, new Action[][] { { closeAction } });
        }
        return dockable;
    }

    /**
	 * Fills the dock description for a dock.
	 * 
	 * @param dockDescription
	 *            Dock description to fill
	 * @param parentDock
	 *            Parent dock
	 */
    protected void fillDockDescription(DockDescription dockDescription, CompositeDock parentDock) {
        int count = parentDock.getChildDockCount();
        for (int i = 0; i < count; i++) {
            Dock dock = parentDock.getChildDock(i);
            Position position = parentDock.getChildDockPosition(dock);
            String positionString = toString(position);
            Dimension dimension = null;
            if (dock instanceof Component) {
                dimension = ((Component) dock).getSize();
            }
            String dockClassName = dock.getClass().getName();
            if (dock instanceof CompositeDock) {
                DockDescription description = new DockDescription(dockClassName, false, positionString, dimension);
                dockDescription.add(description);
                fillDockDescription(description, (CompositeDock) dock);
            } else {
                DockDescription description = new DockDescription(dockClassName, true, positionString, dimension);
                dockDescription.add(description);
                LeafDock leafDock = (LeafDock) dock;
                int dockableCount = leafDock.getDockableCount();
                for (int v = 0; v < dockableCount; v++) {
                    Dockable dockable = leafDock.getDockable(v);
                    String dockableID = dockable.getID();
                    description.getViewIds().add(dockableID);
                }
            }
        }
    }

    @Override
    protected <V> ViewReference<V, DockingConstraint> findViewReference(GUIWindow<B> parentWindow, GUIViewDescriptor<V> viewDescriptor, V viewModel) {
        ViewReference<V, DockingConstraint> reference = super.findViewReference(parentWindow, viewDescriptor, viewModel);
        if (reference == null) {
            reference = new ViewReference<V, DockingConstraint>();
            DataFactory<V> modelFactory = new IdentityDataFactory<V>();
            reference.setModelFactory(modelFactory);
            reference.setViewConstraint(new DockingConstraint());
            reference.setViewDescriptor(viewDescriptor);
            reference.setVisible(false);
        }
        return reference;
    }

    /**
	 * Gets the dock from a root dock using a
	 * {@linkplain DockingConstraint#setDockingPath(String) docking path}
	 * 
	 * @param dock
	 *            Root dock
	 * @param dockingPath
	 *            Docking path
	 * @return Dock to use (can have been created)
	 */
    protected Dock getDock(Dock dock, String dockingPath) {
        if (StringUtils.isBlank(dockingPath)) {
            return dock;
        }
        String path = StringUtils.substringBefore(dockingPath, PATH_SEPARATOR);
        String trailingPath = StringUtils.substringAfter(dockingPath, PATH_SEPARATOR);
        if (!(dock instanceof CompositeDock)) {
            log.warn("Expected composite dock");
            return dock;
        }
        CompositeDock compositeDock = (CompositeDock) dock;
        DockingPathConstraints pathConstraints = new DockingPathConstraints(path);
        int position = pathConstraints.getPosition();
        Dock subDock = getDockAt(compositeDock, new Position(position));
        if (subDock != null) {
            return getDock(subDock, trailingPath);
        } else {
            Dock childDock = pathConstraints.getType().createDock();
            compositeDock.addChildDock(childDock, new Position(position));
            if (compositeDock instanceof SplitDock) {
                SplitDock parentDock = (SplitDock) compositeDock;
                Dimension size = parentDock.getSize();
                int dividerLocation = 0;
                int dockSize = pathConstraints.getSize();
                switch(position) {
                    case Position.LEFT:
                    case Position.TOP:
                        dividerLocation = dockSize;
                        break;
                    case Position.RIGHT:
                        dividerLocation = size.width - dockSize;
                        break;
                    case Position.BOTTOM:
                        dividerLocation = size.height - dockSize;
                        break;
                }
                parentDock.setDividerLocation(dividerLocation);
            }
            return getDock(childDock, trailingPath);
        }
    }

    /**
	 * Gets the {@link Dockable} for a view.
	 * 
	 * @param view
	 *            View
	 * @return Dockable or <code>null</code> if not found.
	 */
    protected Dockable getDockable(GUIView<?> view) {
        String viewId = view.getID();
        Dockable dockable = DockingUtil.retrieveDockableOfDockModel(viewId);
        return dockable;
    }

    /**
	 * Gets the dock at a given position
	 * 
	 * @param compositeDock
	 *            Parent dock
	 * @param position
	 *            Position
	 * @return Dock at this position or <code>null</code> if none is defined
	 */
    protected Dock getDockAt(CompositeDock compositeDock, Position position) {
        int count = compositeDock.getChildDockCount();
        for (int i = 0; i < count; i++) {
            Dock dock = compositeDock.getChildDock(i);
            Position childPosition = compositeDock.getChildDockPosition(dock);
            if (ObjectUtils.equals(normalizePosition(position), normalizePosition(childPosition))) {
                return dock;
            }
        }
        return null;
    }

    ;

    /**
	 * Gets the docking path for a given dock, starting from the root.
	 * 
	 * @param dock
	 *            Dock to retrieve the path for
	 * @return Docking path
	 */
    protected String getDockingPath(Dock dock) {
        int size;
        Dimension dimension = new Dimension();
        if (dock instanceof Component) {
            dimension = ((Component) dock).getSize();
        }
        CompositeDock parentDock = dock.getParentDock();
        if (parentDock == null) {
            return null;
        }
        DockType type = DockType.getDockType(parentDock);
        Position position = parentDock.getChildDockPosition(dock);
        String positionText;
        int p = position.getPosition(0);
        switch(p) {
            case Position.BOTTOM:
                positionText = "BOTTOM";
                size = dimension.height;
                break;
            case Position.TOP:
                positionText = "TOP";
                size = dimension.height;
                break;
            case Position.RIGHT:
                positionText = "RIGHT";
                size = dimension.width;
                break;
            case Position.LEFT:
                positionText = "LEFT";
                size = dimension.width;
                break;
            default:
                positionText = null;
                size = 0;
                break;
        }
        String dockPath = null;
        if (positionText != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(positionText);
            buffer.append("?type=").append(type);
            buffer.append("&size=").append(size);
            dockPath = buffer.toString();
        }
        if (parentDock != null) {
            String parentPath = getDockingPath(parentDock);
            if (parentPath != null) {
                if (dockPath != null) {
                    return parentPath + ";" + dockPath;
                } else {
                    return parentPath;
                }
            } else {
                return dockPath;
            }
        } else {
            return dockPath;
        }
    }

    /**
	 * Gets the docking path for a {@link Dockable} from the root.
	 * 
	 * @param dockable
	 *            Dockable to retrieve the path for
	 * @return Docking path
	 */
    protected String getDockingPath(Dockable dockable) {
        LeafDock dock = dockable.getDock();
        return getDockingPath(dock);
    }

    /**
	 * Gets the {@link DockModel} that is associated to a window using its
	 * client property {@link #CLIENT_PROPERTY_DOCK_MODEL}.
	 * 
	 * @param window
	 *            Window
	 * @return {@link DockModel} instance
	 */
    protected DockModel getDockModel(GUIWindow<B> window) {
        DockModel dockModel = (DockModel) window.getWindowPanel().getClientProperty(CLIENT_PROPERTY_DOCK_MODEL);
        return dockModel;
    }

    /**
	 * Normalises a {@link Position} by converting to horizontal instead of
	 * vertical positions. <code>null</code> stays <code>null</code>.
	 * 
	 * @param p
	 *            Initial position
	 * @return Converted position
	 */
    protected Position normalizePosition(Position p) {
        if (p == null) {
            return null;
        } else if (p.getPosition(0) == 0) {
            return new Position(p.getPosition(0));
        } else if (Position.BOTTOM == p.getPosition(0)) {
            return new Position(Position.RIGHT);
        } else if (Position.TOP == p.getPosition(0)) {
            return new Position(Position.LEFT);
        } else {
            return p;
        }
    }

    /**
	 * Restores the docking description for a window.
	 * 
	 * @param window
	 *            Docking window
	 * @param parentDock
	 *            Root dock
	 * @param dockDescription
	 *            Description for this dock
	 */
    protected void restore(GUIWindow<B> window, CompositeDock parentDock, DockDescription dockDescription) {
        List<DockDescription> descriptions = dockDescription.getDescriptions();
        for (DockDescription childDescription : descriptions) {
            String dockClassName = childDescription.getClassName();
            String positionString = childDescription.getPosition();
            Dock dock = (Dock) Utils.newInstance(dockClassName);
            int position;
            if (positionString != null) {
                position = (Integer) Utils.getConstant(Position.class, positionString);
            } else {
                position = 0;
            }
            parentDock.addChildDock(dock, new Position(position));
            Dimension size = childDescription.getSize();
            if (size != null && dock instanceof Component) {
                Component component = (Component) dock;
                component.setPreferredSize(size);
                component.setSize(size);
                if (parentDock instanceof SplitDock) {
                    SplitDock parentSplitDock = (SplitDock) parentDock;
                    Dimension parentSize = parentSplitDock.getSize();
                    int dividerLocation = 0;
                    switch(position) {
                        case Position.LEFT:
                            dividerLocation = size.width;
                            break;
                        case Position.TOP:
                            dividerLocation = size.height;
                            break;
                        case Position.RIGHT:
                            dividerLocation = parentSize.width - size.width;
                            break;
                        case Position.BOTTOM:
                            dividerLocation = parentSize.height - size.height;
                            break;
                    }
                    parentSplitDock.setDividerLocation(dividerLocation);
                }
            }
            if (childDescription.isLeaf()) {
                List<String> viewIds = childDescription.getViewIds();
                for (String viewId : viewIds) {
                    List<ViewReference<?, DockingConstraint>> viewReferences = getViewReferences();
                    for (ViewReference<?, DockingConstraint> viewReference : viewReferences) {
                        if (viewReference.getViewDescriptor().getViewId().equals(viewId)) {
                            restoreView(window, viewReference, dock);
                        }
                    }
                }
            } else {
                restore(window, (CompositeDock) dock, childDescription);
            }
        }
    }

    /**
	 * Restores the docking for a window
	 * 
	 * @param window
	 *            Window
	 * @param dockDescription
	 *            Description for the root dock
	 */
    protected void restore(GUIWindow<B> window, DockDescription dockDescription) {
        DockModel dockModel = getDockModel(window);
        Dock rootDock = dockModel.getRootDock(ROOT_DOCK_KEY);
        CompositeDock compositeRootDock = (CompositeDock) rootDock;
        restore(window, compositeRootDock, dockDescription);
    }

    /**
	 * Restores the docking for a view
	 * 
	 * @param <V>
	 *            Type of bean for the view
	 * @param window
	 *            Hosting window
	 * @param viewReference
	 *            View reference for the view
	 * @param dock
	 *            Root dock
	 */
    protected <V> void restoreView(GUIWindow<B> window, ViewReference<V, DockingConstraint> viewReference, Dock dock) {
        V viewData = viewReference.getModelFactory().create(window.getWindowData());
        GUIView<V> view = viewReference.getViewDescriptor().createView(window, viewData);
        JComponent viewComponent = initViewComponent(window, view, getViewDecorator(), viewReference.getDecorationStyle());
        Dockable dockable = createViewDockable(window, view, viewComponent, viewReference.getViewConstraint());
        DockingManager.getDockingExecutor().changeDocking(dockable, dock);
        view.show();
    }

    /**
	 * Converts a position to a string.
	 * 
	 * @param position
	 *            Position
	 * @return String
	 */
    protected String toString(Position position) {
        String positionText;
        int p = position.getPosition(0);
        switch(p) {
            case Position.BOTTOM:
                positionText = "BOTTOM";
                break;
            case Position.TOP:
                positionText = "TOP";
                break;
            case Position.RIGHT:
                positionText = "RIGHT";
                break;
            case Position.LEFT:
                positionText = "LEFT";
                break;
            default:
                positionText = null;
                break;
        }
        return positionText;
    }

    /**
	 * Decorator for the close action that controls if the view can be closed
	 * 
	 * @author Damien Coraboeuf
	 * 
	 */
    protected static class CloseAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private final GUIView<?> view;

        /**
		 * Constructor
		 * 
		 * @param dockingAction
		 *            Original Javadocking close action (used to get icons and
		 *            so on)
		 * @param view
		 *            View
		 */
        public CloseAction(Action dockingAction, GUIView<?> view) {
            this.view = view;
            putValue(SMALL_ICON, dockingAction.getValue(SMALL_ICON));
        }

        /**
		 * @see GUIView#close(boolean)
		 */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.view.close(false);
        }
    }
}
