package com.tensegrity.palobrowser.views;

import java.util.Iterator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.palo.api.Cube;
import com.tensegrity.palobrowser.actions.ActionRegistry;
import com.tensegrity.palobrowser.actions.EditorHelper;
import com.tensegrity.palobrowser.actions.RenameAction;
import com.tensegrity.palobrowser.bookmarks.Bookmark;
import com.tensegrity.palobrowser.bookmarks.BookmarkModel;
import com.tensegrity.palobrowser.bookmarks.BookmarkTransfer;
import com.tensegrity.palobrowser.bookmarks.BookmarkTreeSorter;
import com.tensegrity.palobrowser.bookmarks.BookmarkTreeContentProvider;
import com.tensegrity.palobrowser.bookmarks.BookmarkTreeLabelProvider;
import com.tensegrity.palobrowser.bookmarks.actions.AddBookmarkFolderAction;
import com.tensegrity.palobrowser.bookmarks.actions.DeleteBookmarkAction;
import com.tensegrity.palobrowser.bookmarks.actions.ExplainDragAndDropAction;
import com.tensegrity.palobrowser.bookmarks.actions.MoveDownAction;
import com.tensegrity.palobrowser.bookmarks.actions.MoveUpAction;
import com.tensegrity.palobrowser.bookmarks.listeners.BookmarkDragListener;
import com.tensegrity.palobrowser.bookmarks.listeners.BookmarkDropAdapter;
import com.tensegrity.palobrowser.cube.internal.HorizontalTableHeader;
import com.tensegrity.palobrowser.cubequery.CubeQuery;
import com.tensegrity.palobrowser.cubequery.QueryViewMatcher;
import com.tensegrity.palobrowser.cubeview.CubeView;
import com.tensegrity.palobrowser.editors.charteditor.ChartEditor;
import com.tensegrity.palobrowser.editors.cubeeditor.CubeEditor;
import com.tensegrity.palobrowser.editors.cubeeditor.CubeEditorInput;
import com.tensegrity.palobrowser.folder.Folder;
import com.tensegrity.palobrowser.tree.TreeNode;

/**
 * <code>BookmarkExplorer</code>
 * This Eclipse view is used to present all bookmarked views to the user: if a
 * user has created a view that she would like to access quickly, she can drag
 * the view from the <code>DbExplorer</code> to the
 * <code>BookmarkExplorer</code> where she can access it quickly (without having
 * to navigate through connections and databases). 
 * The bookmarks are stored in the AdvancedSystem database and thus are stored
 * per server.
 * 
 * @author Philipp Bouillon
 * @version $ID$
 */
public class BookmarkExplorer extends ViewPart {

    /**
     * The JFace TreeViewer object that is used to present the data in this
     * view.
     */
    private TreeViewer viewer;

    /**
     * Initializes this view.
     */
    public void init(IViewSite viewSite) throws PartInitException {
        super.init(viewSite);
        viewSite.getKeyBindingService().setScopes(new String[] { "com.tensegrity.palobrowser.bookmarkScope" });
    }

    /**
     * Creates the SWT controls for this view. Clients should not directly
     * call this method.
     */
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        int ddOperations = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { BookmarkTransfer.getInstance() };
        final BookmarkDragListener dragListener = new BookmarkDragListener(viewer, DND.DROP_COPY | DND.DROP_MOVE);
        BookmarkDropAdapter dropAdapter = new BookmarkDropAdapter(viewer, dragListener);
        viewer.addDragSupport(ddOperations, transfers, dragListener);
        viewer.addDropSupport(ddOperations, transfers, dropAdapter);
        viewer.setContentProvider(new BookmarkTreeContentProvider());
        viewer.setLabelProvider(new BookmarkTreeLabelProvider());
        TreeNode root = BookmarkModel.getInstance().getRoot();
        viewer.setSorter(new BookmarkTreeSorter());
        viewer.setInput(root);
        viewer.expandAll();
        getSite().setSelectionProvider(viewer);
        contributeToActionBars();
        hookContextMenu();
        hookDoubleClickAction();
        appendToToolbar();
        BookmarkModel.getInstance().registerView(this);
    }

    /**
     * Returns the <code>TreeViewer</code> object for the bookmark explorer.
     * @return the <code>TreeViewer</code> object for the bookmark explorer.
     */
    public TreeViewer getTreeViewer() {
        return viewer;
    }

    /**
     * Sets the focus for this view (the TreeViewer receives input focus).
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    /**
     * Hooks a context menu. Adds add, delete and remove actions for folders
     * and views.
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                BookmarkExplorer.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    /**
     * Adds actions to the menu of this view.
     */
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
    }

    /**
     * Adds menus to the main pull down menu of the view.
     * 
     * TODO Currently only bogus menu items for testing. Replace with correct
     * items.
     * 
     * @param manager the menu manager corresponding to the view.
     */
    private void fillLocalPullDown(IMenuManager manager) {
    }

    /**
     * Fills the context menu with meaniningful actions (add, delete, and remove
     * in this case).
     * 
     * @param manager the menu manager corresponding to this view.
     */
    private void fillContextMenu(IMenuManager manager) {
        manager.add(ActionRegistry.getInstance(getViewSite().getWorkbenchWindow()).get(AddBookmarkFolderAction.class));
        manager.add(ActionRegistry.getInstance(getViewSite().getWorkbenchWindow()).get(DeleteBookmarkAction.class));
        manager.add(ActionRegistry.getInstance(getViewSite().getWorkbenchWindow()).get(RenameAction.class));
        manager.add(ActionRegistry.getInstance(getViewSite().getWorkbenchWindow()).get(ExplainDragAndDropAction.class));
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /** 
     * On a double click event on a view, the corresponding view editor should 
     * be opened. If a folder receives this event, it should be collapsed or
     * expanded, depending on its current state.
     */
    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                ISelection rawSelection = getViewSite().getSelectionProvider().getSelection();
                if (!(rawSelection instanceof IStructuredSelection)) {
                    return;
                }
                for (Iterator it = ((IStructuredSelection) rawSelection).iterator(); it.hasNext(); ) {
                    Object obj = it.next();
                    if (!(obj instanceof TreeNode)) {
                        continue;
                    }
                    TreeNode node = (TreeNode) obj;
                    if (node.getUserObject() instanceof Bookmark) {
                        CubeQuery query = ((Bookmark) node.getUserObject()).getCubeQuery();
                        if (query == null) {
                            return;
                        }
                        Cube cube = query.getDatabase().getCubeById(query.getSourceCubeId());
                        org.palo.api.CubeView view = QueryViewMatcher.getInstance().match(query);
                        if (view != null) {
                            for (String key : view.getProperties()) {
                                query.addProperty(key, view.getPropertyValue(key));
                            }
                        }
                        CubeEditorInput input = new CubeEditorInput(cube, false);
                        input.setCubeQuery(query);
                        String str = query.getProperty("ViewType");
                        if (str != null && str.equalsIgnoreCase("Chart")) {
                            ChartEditor ce = (ChartEditor) EditorHelper.openEditor(getViewSite().getWorkbenchWindow(), input, "com.tensegrity.palobrowser.editors.charteditor.ChartEditor");
                            ce.loadViewProperties(query);
                        } else {
                            CubeEditor ce = (CubeEditor) EditorHelper.openEditor(getViewSite().getWorkbenchWindow(), input, CubeEditor.class.getName());
                            if (ce != null) {
                                ((HorizontalTableHeader) ce.getDataEditor().getHHeader()).alignItems();
                            }
                        }
                    } else if (node.getUserObject() instanceof Folder) {
                        if (viewer.getExpandedState(node)) {
                            viewer.collapseToLevel(node, 1);
                        } else {
                            viewer.expandToLevel(node, 1);
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds move up and move down icons to the toolbar of this view. The
     * actions represented by those icons can be used to change the order of
     * the elements in the tree viewer.
     */
    private void appendToToolbar() {
        getViewSite().getActionBars().getToolBarManager().add(ActionRegistry.getInstance(getViewSite().getWorkbenchWindow()).get(MoveUpAction.class));
        getViewSite().getActionBars().getToolBarManager().add(ActionRegistry.getInstance(getViewSite().getWorkbenchWindow()).get(MoveDownAction.class));
    }

    /**
     * Removes all listeners and calls the dispose method from the super class.
     */
    public void dispose() {
        BookmarkModel.getInstance().registerView(null);
        super.dispose();
    }
}
