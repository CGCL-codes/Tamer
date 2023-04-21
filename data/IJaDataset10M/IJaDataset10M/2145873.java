package org.eclipse.help.ui.internal.preferences;

import java.util.Arrays;
import org.eclipse.help.internal.base.remote.RemoteIC;
import org.eclipse.help.ui.internal.Messages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class RemoteICViewer {

    /**
	 * @param parent
	 */
    public RemoteICViewer(Composite parent) {
        createTable(parent);
        createTableViewer();
        tableViewer.setContentProvider(new RemoteICContentProvider());
        tableViewer.setLabelProvider(new RemoteICLabelProvider());
        remoteICList = new RemoteICList();
        tableViewer.setInput(remoteICList);
    }

    private Table table;

    private TableViewer tableViewer;

    private RemoteICList remoteICList = new RemoteICList();

    private final String NAME_COLUMN = Messages.RemoteICViewer_Name;

    private final String LOCATION_COLUMN = Messages.RemoteICViewer_URL;

    private final String STATUS_COLUMN = Messages.RemoteICViewer_Enabled;

    private String[] columnNames = new String[] { NAME_COLUMN, LOCATION_COLUMN, STATUS_COLUMN };

    /**
	 * Release resources
	 */
    public void dispose() {
        tableViewer.getLabelProvider().dispose();
    }

    /**
	 * Create the Table
	 */
    private void createTable(Composite parent) {
        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
        table = new Table(parent, style);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
        gridData.heightHint = table.getItemHeight();
        table.setLayoutData(gridData);
        table.setFont(parent.getFont());
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableColumn column;
        column = new TableColumn(table, SWT.LEFT);
        column.setText(NAME_COLUMN);
        column.setWidth(85);
        column = new TableColumn(table, SWT.LEFT);
        column.setText(LOCATION_COLUMN);
        column.setWidth(165);
        column = new TableColumn(table, SWT.CENTER);
        column.setText(STATUS_COLUMN);
        column.setWidth(60);
    }

    /**
	 * Create the TableViewer
	 */
    private void createTableViewer() {
        tableViewer = new TableViewer(table);
        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(columnNames);
    }

    /**
	 * Proxy for the the RemoteICList which provides content
	 * for the Table. This class implements IRemoteHelpListViewer interface an 
	 * registers itself with RemoteICList
	 */
    class RemoteICContentProvider implements IStructuredContentProvider, IRemoteHelpListViewer {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            if (newInput != null) ((RemoteICList) newInput).addChangeListener(this);
            if (oldInput != null) ((RemoteICList) oldInput).removeChangeListener(this);
        }

        public void dispose() {
            remoteICList.removeChangeListener(this);
        }

        public Object[] getElements(Object parent) {
            return remoteICList.getRemoteICs().toArray();
        }

        public void addRemoteIC(RemoteIC remoteic) {
            tableViewer.add(remoteic);
        }

        public void removeRemoteIC(RemoteIC remoteic) {
            tableViewer.remove(remoteic);
        }

        public void updateRemoteIC(RemoteIC remoteic) {
            tableViewer.update(remoteic, null);
        }

        public void refreshRemoteIC(RemoteIC remoteic, int selectedIndex) {
            tableViewer.replace(remoteic, selectedIndex);
        }

        public void removeAllRemoteICs(Object[] remoteICs) {
            tableViewer.remove(remoteICs);
        }
    }

    /**
	 * Return the column names in a collection
	 * 
	 * @return List containing column names
	 */
    public java.util.List getColumnNames() {
        return Arrays.asList(columnNames);
    }

    /**
	 * @return currently selected item
	 */
    public ISelection getSelection() {
        return tableViewer.getSelection();
    }

    /**
	 * Return the RemoteICList
	 */
    public RemoteICList getRemoteICList() {
        return remoteICList;
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    /**
	 * Return the parent composite
	 */
    public Control getControl() {
        return table.getParent();
    }

    public Table getTable() {
        return table;
    }
}
