package org.columba.addressbook.gui.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.columba.addressbook.facade.IContactItem;
import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.folder.AddressbookTreeNode;
import org.columba.addressbook.folder.FolderListener;
import org.columba.addressbook.folder.IContactStorage;
import org.columba.addressbook.folder.IFolderEvent;
import org.columba.addressbook.gui.focus.FocusManager;
import org.columba.addressbook.gui.focus.FocusOwner;
import org.columba.addressbook.gui.frame.AddressbookFrameMediator;
import org.columba.addressbook.gui.table.model.AddressbookTableModel;
import org.columba.addressbook.gui.table.model.FilterDecorator;
import org.columba.addressbook.gui.table.model.SortDecorator;
import org.columba.addressbook.model.IContactModelPartial;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.logging.Logging;

/**
 * @author fdietz
 */
public class TableController implements TreeSelectionListener, FolderListener, FocusOwner {

    private TableView view;

    private AddressbookFrameMediator mediator;

    private AddressbookTableModel addressbookModel;

    private SortDecorator sortDecorator;

    private FilterDecorator filterDecorator;

    private AddressbookTreeNode selectedFolder;

    /**
	 * 
	 */
    public TableController(AddressbookFrameMediator mediator) {
        super();
        this.mediator = mediator;
        addressbookModel = new AddressbookTableModel();
        sortDecorator = new SortDecorator(addressbookModel);
        filterDecorator = new FilterDecorator(sortDecorator);
        view = new TableView(this, filterDecorator);
        addMouseListenerToHeaderInTable();
        view.addMouseListener(new TableMouseListener(this));
        FocusManager.getInstance().registerComponent(this);
    }

    /**
	 * Add MouseListener to JTableHeader to sort table based on clicked column
	 * header.
	 * 
	 */
    protected void addMouseListenerToHeaderInTable() {
        final JTable tableView = getView();
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if ((e.getClickCount() == 1) && (column != -1)) {
                    if (sortDecorator.getSelectedColumn() == column) sortDecorator.setSortOrder(!sortDecorator.isSortOrder());
                    sortDecorator.sort(column);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }

    /**
	 * @return AddressbookFrameController
	 */
    public AddressbookFrameMediator getMediator() {
        return mediator;
    }

    /**
	 * @return TableView
	 */
    public TableView getView() {
        return view;
    }

    /**
	 * Update table on tree selection changes.
	 */
    public void valueChanged(TreeSelectionEvent e) {
        AddressbookTreeNode node = (AddressbookTreeNode) e.getPath().getLastPathComponent();
        if (node == null) {
            return;
        }
        if (node instanceof IContactStorage) {
            try {
                ((AbstractFolder) node).removeFolderListener(this);
                selectedFolder = node;
                ((AbstractFolder) node).addFolderListener(this);
                filterDecorator.setContactItemMap(((AbstractFolder) selectedFolder).getContactItemMap());
            } catch (Exception e1) {
                if (Logging.DEBUG) e1.printStackTrace();
                ErrorDialog.createDialog(e1.getMessage(), e1);
            }
        } else {
            filterDecorator.setContactItemMap(null);
        }
    }

    /**
	 * Get selected uids.
	 * 
	 * @return
	 */
    public String[] getUids() {
        int[] rows = getView().getSelectedRows();
        String[] uids = new String[rows.length];
        IContactModelPartial item;
        for (int i = 0; i < rows.length; i++) {
            item = (IContactModelPartial) filterDecorator.getContactItem(rows[i]);
            uids[i] = item.getId();
        }
        return uids;
    }

    /**
	 * @return Returns the addressbookModel.
	 */
    public AddressbookTableModel getAddressbookModel() {
        return addressbookModel;
    }

    public IContactItem getSelectedItem() {
        int row = getView().getSelectedRow();
        IContactItem item = (IContactItem) sortDecorator.getContactItem(row);
        return item;
    }

    /**
	 * @see org.columba.addressbook.folder.FolderListener#itemAdded(org.columba.addressbook.folder.FolderEvent)
	 */
    public void itemAdded(IFolderEvent e) {
        getAddressbookModel().update();
    }

    /**
	 * @see org.columba.addressbook.folder.FolderListener#itemChanged(org.columba.addressbook.folder.FolderEvent)
	 */
    public void itemChanged(IFolderEvent e) {
        getAddressbookModel().update();
    }

    /**
	 * @see org.columba.addressbook.folder.FolderListener#itemRemoved(org.columba.addressbook.folder.FolderEvent)
	 */
    public void itemRemoved(IFolderEvent e) {
        getAddressbookModel().update();
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#copy()
	 */
    public void copy() {
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#cut()
	 */
    public void cut() {
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#delete()
	 */
    public void delete() {
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#getComponent()
	 */
    public JComponent getComponent() {
        return getView();
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isCopyActionEnabled()
	 */
    public boolean isCopyActionEnabled() {
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isCutActionEnabled()
	 */
    public boolean isCutActionEnabled() {
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isDeleteActionEnabled()
	 */
    public boolean isDeleteActionEnabled() {
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isPasteActionEnabled()
	 */
    public boolean isPasteActionEnabled() {
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isRedoActionEnabled()
	 */
    public boolean isRedoActionEnabled() {
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isSelectAllActionEnabled()
	 */
    public boolean isSelectAllActionEnabled() {
        if (getView().getRowCount() > 0) {
            return true;
        }
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isUndoActionEnabled()
	 */
    public boolean isUndoActionEnabled() {
        return false;
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#paste()
	 */
    public void paste() {
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#redo()
	 */
    public void redo() {
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#selectAll()
	 */
    public void selectAll() {
        getView().selectAll();
    }

    /**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#undo()
	 */
    public void undo() {
    }

    /**
	 * @return Returns the sortDecorator.
	 */
    public SortDecorator getSortDecorator() {
        return sortDecorator;
    }

    /**
	 * @return Returns the filterDecorator.
	 */
    public FilterDecorator getFilterDecorator() {
        return filterDecorator;
    }
}
