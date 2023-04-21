package org.columba.mail.gui.tree;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.menu.ExtendablePopupMenu;
import org.columba.core.gui.menu.MenuXMLDecoder;
import org.columba.core.io.DiskIO;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.gui.tree.comparator.FolderComparator;
import org.columba.mail.gui.tree.comparator.UnreadFolderComparator;
import org.columba.mail.gui.tree.util.FolderTreeCellRenderer;

/**
 * this class shows the the folder hierarchy
 */
public class TreeController implements TreeSelectionListener, TreeWillExpandListener, ITreeController {

    private IMailFolder selectedFolder;

    private TreeView view;

    private IFrameMediator frameController;

    private ExtendablePopupMenu menu;

    private FolderComparator folderComparator;

    protected EventListenerList listenerList = new EventListenerList();

    /**
	 * Constructor for tree controller.
	 * 
	 * @param controller
	 *            the parent controller.
	 * @param model
	 *            the tree model to display.
	 */
    public TreeController(IFrameMediator controller, FolderTreeModel model) {
        frameController = controller;
        view = new TreeView(model);
        view.setSortingEnabled(false);
        view.addTreeWillExpandListener(this);
        FolderTreeCellRenderer renderer = new FolderTreeCellRenderer();
        view.setCellRenderer(renderer);
        getView().setTransferHandler(new TreeViewTransferHandler(controller));
        getView().setDragEnabled(true);
        getView().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        getView().addTreeSelectionListener(this);
    }

    /**
	 * @see org.columba.mail.gui.tree.ITreeController#sortAscending(boolean)
	 */
    public void sortAscending(boolean ascending) {
        folderComparator.setAscending(ascending);
        view.setSortingComparator(folderComparator);
    }

    /**
	 * Returns the tree view.
	 * 
	 * @return the tree view.
	 */
    public TreeView getView() {
        return view;
    }

    /**
	 * Set the specified folder as seleceted.
	 * 
	 * @param folder
	 *            the new selected folder.
	 */
    public void setSelected(IMailFolder folder) {
        view.clearSelection();
        TreePath path = folder.getSelectionTreePath();
        view.requestFocus();
        view.setSelectionPath(path);
        view.expandPath(path);
        this.selectedFolder = folder;
    }

    /**
	 * Creates a Popup menu.
	 */
    public void createPopupMenu() {
        try {
            InputStream is = DiskIO.getResourceStream("org/columba/mail/action/tree_contextmenu.xml");
            menu = new MenuXMLDecoder(frameController).createPopupMenu(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Returns the pop up menu for the controller.
	 * 
	 * @return the pop up menu.
	 */
    public JPopupMenu getPopupMenu() {
        return menu;
    }

    /**
	 * @see org.columba.mail.gui.tree.ITreeController#getSelected()
	 */
    public IMailFolder getSelected() {
        return selectedFolder;
    }

    /**
	 * Returns the mailFrameController.
	 * 
	 * @return MailFrameController
	 */
    public IFrameMediator getFrameController() {
        return frameController;
    }

    /** {@inheritDoc} */
    public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
        IMailFolder treeNode = (IMailFolder) e.getPath().getLastPathComponent();
        if (treeNode == null) {
            return;
        }
        saveExpandedState(treeNode, e.getPath());
    }

    /** {@inheritDoc} */
    public void treeWillCollapse(TreeExpansionEvent e) {
        IMailFolder treeNode = (IMailFolder) e.getPath().getLastPathComponent();
        if (treeNode == null) {
            return;
        }
        saveExpandedState(treeNode, e.getPath());
    }

    /**
	 * Saves the tree expanded state.
	 * 
	 * @param folder
	 *            the folder to get the configuration for.
	 * @param path
	 *            the tree path in the tree view.
	 */
    private void saveExpandedState(IMailFolder folder, TreePath path) {
        IFolderItem item = folder.getConfiguration();
        XmlElement property = item.getElement("property");
        if (!getView().isExpanded(path)) {
            property.addAttribute("expanded", "true");
        } else {
            property.addAttribute("expanded", "false");
        }
    }

    /**
	 * @see org.columba.mail.gui.tree.ITreeController#getModel()
	 */
    public TreeModel getModel() {
        return getView().getModel();
    }

    /**
	 * @see org.columba.mail.gui.tree.ITreeController#setSortingEnabled(boolean)
	 */
    public void setSortingEnabled(boolean enabled) {
        view.setSortingEnabled(enabled);
    }

    public void setSortingMode(SORTING_MODE_ENUM sortingMode, boolean ascending) {
        if (sortingMode == SORTING_MODE_ENUM.ALPHABETICAL) {
            setFolderComparator(new FolderComparator(ascending));
        } else if (sortingMode == SORTING_MODE_ENUM.UNREAD_COUNT) {
            setFolderComparator(new UnreadFolderComparator(ascending));
        } else {
        }
    }

    /**
	 * Set a new folder comparator for sorting the folders.
	 * 
	 * @param comparator
	 *            the folder comparator to use.
	 */
    private void setFolderComparator(FolderComparator comparator) {
        folderComparator = comparator;
        view.setSortingComparator(folderComparator);
    }

    public void addFolderSelectionListener(IFolderSelectionListener l) {
        listenerList.add(IFolderSelectionListener.class, l);
    }

    public void removeFolderSelectionListener(IFolderSelectionListener l) {
        listenerList.remove(IFolderSelectionListener.class, l);
    }

    protected void fireFolderSelectionChangedEvent(IMailFolder folder) {
        IFolderSelectionEvent e = new FolderSelectionEvent(this, folder);
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IFolderSelectionListener.class) {
                ((IFolderSelectionListener) listeners[i + 1]).selectionChanged(e);
            }
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getView().getLastSelectedPathComponent();
        if (node == null) return;
        fireFolderSelectionChangedEvent((IMailFolder) node);
    }
}
