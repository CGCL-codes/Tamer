package org.radrails.db.internal.ui;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.radrails.db.core.DatabaseLog;
import org.radrails.db.core.QueryAdministrator;
import org.radrails.db.core.QueryResult;

/**
 * The editor for a query result.
 *
 * @author	mbaumbach
 *
 * @version	0.3.1
 */
public class DataEditor extends EditorPart {

    private TableViewer tableViewer;

    private String queryString;

    private Database database;

    /**
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
    public void doSave(IProgressMonitor monitor) {
    }

    /**
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
    public void doSaveAs() {
    }

    /**
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    /**
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
    public boolean isDirty() {
        return false;
    }

    /**
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout());
        createTableViewer(parent);
        createQueryViewer(parent);
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
    public void setFocus() {
    }

    /**
	 * Helper method to create the table viewer.
	 * 
	 * @param	parent	Creates the tableviewer in the parent.
	 */
    private void createTableViewer(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
        final Table dataTable = tableViewer.getTable();
        dataTable.setHeaderVisible(true);
        dataTable.setLinesVisible(false);
        dataTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        tableViewer.setLabelProvider(new DataTableLabelProvider());
        tableViewer.setContentProvider(new ArrayContentProvider());
        DataEditorInput editorInput = (DataEditorInput) getEditorInput();
        this.queryString = editorInput.getQuery();
        this.database = editorInput.getDatabase();
        this.setPartName(editorInput.getName());
        executeStatement(queryString);
    }

    /**
	 * Creates a query viewer.
	 * 
	 * @param	parent	The parent to add this viewer to.
	 */
    private void createQueryViewer(Composite parent) {
        Label label = new Label(parent, SWT.LEFT);
        label.setText(queryString);
    }

    /**
	 * Executes the specified query string.
	 * 
	 * @param	queryString	The query string to execute.
	 */
    private void executeStatement(String queryString) {
        try {
            Project project = (Project) database.getParent();
            Connection connection = project.getProjectDatabaseManager().getConnection(database.getName());
            QueryAdministrator query = new QueryAdministrator(connection);
            QueryResult queryResult = query.execute(queryString, 1000);
            Collection columns = queryResult.getColumnNames();
            if (columns != null) {
                Iterator i = columns.iterator();
                while (i.hasNext()) {
                    String columnName = (String) i.next();
                    TableColumn tableColumn = new TableColumn(tableViewer.getTable(), SWT.LEFT);
                    tableColumn.setText(columnName);
                    tableColumn.setWidth(100);
                }
                Collection results = queryResult.getRows();
                tableViewer.setInput(results.toArray());
            }
        } catch (Exception e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Database Error", e.getMessage());
            DatabaseLog.logError("Could not execute statement", e);
        }
    }
}
