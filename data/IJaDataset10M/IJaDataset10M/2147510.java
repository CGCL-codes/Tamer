package es.java.otro.view;

import javax.swing.table.TableStringConverter;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import es.java.otro.common.OtroException;
import es.java.otro.model.BrowserEditorInput;
import es.java.otro.model.Entry;

public class FeedView extends ViewPart {

    public static final String ID = "otro.entries.view";

    private Composite top = null;

    private Table feedTable = null;

    private TableViewer tableViewer = null;

    public FeedView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new GridLayout());
        feedTable = new Table(top, SWT.FULL_SELECTION);
        EntryTableSorter sorter = new EntryTableSorter();
        tableViewer = new TableViewer(feedTable);
        tableViewer.setSorter(sorter);
        feedTable.setHeaderVisible(true);
        feedTable.setLayoutData(gridData);
        feedTable.setLinesVisible(true);
        feedTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                Entry entry = (Entry) ((StructuredSelection) getSite().getSelectionProvider().getSelection()).getFirstElement();
                BrowserEditorInput input = new BrowserEditorInput();
                input.setEntry(entry);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    page.openEditor(input, Content.ID);
                } catch (PartInitException e1) {
                    throw new OtroException(e1);
                }
            }
        });
        addColumns();
        tableViewer.setContentProvider(new EntryTableContentProvider());
        tableViewer.setLabelProvider(new EntryTableLableProvider());
        getSite().setSelectionProvider(tableViewer);
    }

    private void addColumns() {
        TableColumn readColumn = new TableColumn(feedTable, SWT.NONE);
        readColumn.setWidth(15);
        addSortingSupport(readColumn, 0);
        TableColumn titleColumn = new TableColumn(feedTable, SWT.NONE);
        titleColumn.setWidth(350);
        titleColumn.setText("Title");
        addSortingSupport(titleColumn, 1);
        TableColumn publishedColumn = new TableColumn(feedTable, SWT.NONE);
        publishedColumn.setWidth(60);
        publishedColumn.setText("Published");
        addSortingSupport(publishedColumn, 2);
        TableColumn authorColumn = new TableColumn(feedTable, SWT.NONE);
        authorColumn.setWidth(60);
        authorColumn.setText("Author");
        addSortingSupport(authorColumn, 3);
    }

    private void addSortingSupport(final TableColumn column, final int index) {
        column.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                EntryTableSorter sorter = (EntryTableSorter) tableViewer.getSorter();
                sorter.setColumnIndex(index);
                int dir = tableViewer.getTable().getSortDirection();
                if (tableViewer.getTable().getSortColumn() == null) {
                    tableViewer.getTable().setSortDirection(SWT.UP);
                }
                if (tableViewer.getTable().getSortColumn() != null && tableViewer.getTable().getSortColumn() != column) {
                    tableViewer.getTable().setSortDirection(SWT.UP);
                } else {
                    tableViewer.getTable().setSortDirection(-tableViewer.getTable().getSortDirection());
                }
                tableViewer.getTable().setSortColumn(column);
                tableViewer.refresh();
            }
        });
    }

    @Override
    public void setFocus() {
    }

    public void setInput(Object object) {
        tableViewer.setInput(object);
    }
}
