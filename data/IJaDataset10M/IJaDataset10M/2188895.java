package org.mariella.glue.ui;

import java.beans.PropertyDescriptor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.mariella.glue.service.QueryResult;
import org.mariella.persistence.runtime.BeanInfo;
import org.mariella.persistence.runtime.Introspector;
import org.mariella.rcp.ControlFactory;

public abstract class AbstractSearchDialog extends Dialog {

    protected Action searchAction = new Action("Search") {

        public void run() {
            QueryResult<?> result = getImplementation().search();
            setSearchResult(result);
        }

        ;
    };

    private String title;

    protected TableViewer tableViewer;

    protected ControlFactory controlFactory;

    protected QueryResult<?> queryResult;

    protected Object selection;

    public AbstractSearchDialog(Shell shell, String title) {
        super(shell);
        this.title = title;
    }

    public abstract SearchImplementation<?, ?> getImplementation();

    protected void setSearchResult(QueryResult<?> queryResult) {
        this.queryResult = queryResult;
        initializeTableViewer();
        tableViewer.setInput(queryResult);
        tableViewer.refresh();
    }

    protected void initializeTableViewer() {
        for (TableColumn tc : tableViewer.getTable().getColumns()) {
            tc.dispose();
        }
        if (queryResult != null && !queryResult.getResult().isEmpty()) {
            getImplementation().addTableColumns(tableViewer);
        }
        tableViewer.getTable().setHeaderVisible(true);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
        newShell.setMinimumSize(500, 500);
    }

    protected void createTableViewer(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        tableViewer.setContentProvider(createContentProvider());
        tableViewer.setLabelProvider(createLabelProvider());
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                tableSelectionChanged();
            }
        });
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                okPressed();
            }
        });
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        tableSelectionChanged();
    }

    protected void tableSelectionChanged() {
        getButton(IDialogConstants.OK_ID).setEnabled(!tableViewer.getSelection().isEmpty());
    }

    protected Object getValue(Object element, String propertyName) {
        BeanInfo bi = Introspector.Singleton.getBeanInfo(element.getClass());
        try {
            PropertyDescriptor pd = bi.getPropertyDescriptor(propertyName);
            return pd.getReadMethod().invoke(element, new Object[] {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void okPressed() {
        selection = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
        super.okPressed();
    }

    public Object getSelection() {
        return selection;
    }

    protected IStructuredContentProvider createContentProvider() {
        return new IStructuredContentProvider() {

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return inputElement == null ? null : ((QueryResult<?>) inputElement).getResult().toArray();
            }
        };
    }

    protected ITableLabelProvider createLabelProvider() {
        return new ITableLabelProvider() {

            @Override
            public void removeListener(ILabelProviderListener listener) {
            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return true;
            }

            @Override
            public void dispose() {
            }

            @Override
            public void addListener(ILabelProviderListener listener) {
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                return getImplementation().getColumnText(element, columnIndex);
            }

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return getImplementation().getColumnImage(element, columnIndex);
            }
        };
    }
}
