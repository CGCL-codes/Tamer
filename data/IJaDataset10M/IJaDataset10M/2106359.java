package projectviewer.vpt;

import java.util.Iterator;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JTree;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import javax.swing.SwingUtilities;
import projectviewer.ProjectViewer;
import projectviewer.action.Action;
import projectviewer.action.SearchAction;
import projectviewer.action.ArchiveAction;
import projectviewer.action.ReimportAction;
import projectviewer.action.FileImportAction;
import projectviewer.action.EditProjectAction;
import projectviewer.action.NodeRemoverAction;
import projectviewer.action.NodeRenamerAction;
import projectviewer.action.OpenWithAppAction;
import projectviewer.action.LaunchBrowserAction;
import projectviewer.config.AppLauncher;

/**
 *	A handler for context menu requests on the tree, providing node-sensitive
 *	functionality.
 *
 *	@author		Marcelo Vanzin
 *	@version	$Id: VPTContextMenu.java 6018 2003-01-21 20:32:21Z vanza $
 */
public class VPTContextMenu extends MouseAdapter {

    private static final ArrayList actions = new ArrayList();

    private static final ArrayList intActions = new ArrayList();

    private static long lastMod = System.currentTimeMillis();

    /** Initializes the internal action list. */
    static {
        intActions.add(new EditProjectAction());
        intActions.add(new FileImportAction());
        intActions.add(new NodeRemoverAction(false));
        intActions.add(new NodeRemoverAction(true));
        intActions.add(new NodeRenamerAction());
        intActions.add(new LaunchBrowserAction());
        intActions.add(new OpenWithAppAction());
        intActions.add(new SearchAction());
        intActions.add(new ReimportAction());
        intActions.add(new ArchiveAction());
    }

    /**
	 *	Adds an action to be shown on the context menu. Actions are shown in the
	 *	same order as they are registered.
	 */
    public static void registerAction(Action action) {
        actions.add(action);
        lastMod = System.currentTimeMillis();
    }

    /** Removes an action from the context menu. */
    public static void unregisterAction(Action action) {
        actions.remove(action);
        lastMod = System.currentTimeMillis();
    }

    private final ProjectViewer viewer;

    private final AppLauncher appList;

    private final JPopupMenu popupMenu;

    private final ArrayList internalActions;

    private long pmLastBuilt;

    /**
	 *  Constructs a listener that will ask the provided viewer instance for
	 *  information about the nodes clicked.
	 */
    public VPTContextMenu(ProjectViewer viewer) {
        this.viewer = viewer;
        appList = AppLauncher.getInstance();
        internalActions = new ArrayList();
        popupMenu = new JPopupMenu();
        loadGUI();
    }

    /** Context-menus are shown on the "pressed" event. */
    public void mousePressed(MouseEvent me) {
        JTree tree = (JTree) me.getSource();
        if (SwingUtilities.isRightMouseButton(me)) {
            TreePath tp = tree.getClosestPathForLocation(me.getX(), me.getY());
            if (tp != null && !tree.isPathSelected(tp)) {
                if ((me.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK) {
                    tree.addSelectionPath(tp);
                } else {
                    tree.setSelectionPath(tp);
                }
            }
        }
        if (me.isPopupTrigger()) {
            handleMouseEvent(me);
        }
    }

    /** Context-menus are shown on the "pressed" event. */
    public void mouseReleased(MouseEvent me) {
        if (me.isPopupTrigger()) {
            handleMouseEvent(me);
        }
    }

    /** Handles the mouse event internally. */
    private void handleMouseEvent(MouseEvent me) {
        JTree tree = viewer.getCurrentTree();
        if (tree.getSelectionCount() == 0) {
            return;
        } else {
            prepareMenu(tree.getSelectionCount() > 1 ? null : viewer.getSelectedNode());
            popupMenu.show(me.getComponent(), me.getX(), me.getY());
        }
    }

    /** Constructs the menus' GUI. */
    private void loadGUI() {
        internalActions.clear();
        popupMenu.removeAll();
        Action a;
        for (Iterator it = intActions.iterator(); it.hasNext(); ) {
            a = (Action) it.next();
            a = (Action) a.clone();
            a.setViewer(viewer);
            internalActions.add(a);
            popupMenu.add(a.getMenuItem());
        }
        if (actions.size() > 0) popupMenu.addSeparator();
        for (Iterator it = actions.iterator(); it.hasNext(); ) {
            a = (Action) it.next();
            a = (Action) a.clone();
            a.setViewer(viewer);
            internalActions.add(a);
            popupMenu.add(a.getMenuItem());
        }
        pmLastBuilt = System.currentTimeMillis();
    }

    /**
	 *	Prepares the context menu for the given node. Shows only menu items
	 *	that are allowed for the node (e.g., "Add Project" only applies for
	 *	the root node). If the node is null, the method guesses that multiple
	 *	nodes are selected, and chooses the appropriate entries.
	 */
    private void prepareMenu(VPTNode selectedNode) {
        if (pmLastBuilt < lastMod) {
            loadGUI();
        }
        for (Iterator it = internalActions.iterator(); it.hasNext(); ) {
            ((Action) it.next()).prepareForNode(selectedNode);
        }
    }
}
