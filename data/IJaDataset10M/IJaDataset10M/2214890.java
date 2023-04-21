package org.slasoi.studio.plugin.support;

import java.util.List;
import java.util.Set;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.slasoi.seval.prediction.service.IEvaluationResult;
import org.slasoi.seval.prediction.service.ISingleResult;

public class TableDisplayDialog extends Dialog implements ITableLabelProvider {

    protected Shell shell;

    protected Table table;

    protected Button button;

    protected Label label;

    protected Layout layout;

    protected TableLayout tableLayout;

    public TableDisplayDialog(Shell parent, Set<IEvaluationResult> results) {
        super(parent);
        shell = new Shell(getParent(), SWT.TITLE | SWT.RESIZE | SWT.CLOSE);
        shell.setSize(800, 400);
        layout = new GridLayout(1, false);
        shell.setLayout(layout);
        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                shell.close();
            }
        };
        label = new Label(shell, SWT.NONE);
        table = new Table(shell, SWT.NONE);
        button = new Button(shell, SWT.NONE);
        button.setText("Ok");
        button.addListener(SWT.Selection, listener);
        TableViewer tableViewer = new TableViewer(table);
        tableLayout = new TableLayout();
        table.setLayout(tableLayout);
        table.setHeaderVisible(true);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false));
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        button.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false));
        if (results != null && results.size() != 0) {
            IEvaluationResult result = null;
            for (IEvaluationResult item : results) {
                result = item;
                break;
            }
            tableLayout.addColumnData(new ColumnWeightData(1));
            TableColumn nameColumn = new TableColumn(table, SWT.CENTER);
            nameColumn.setText("Implementation");
            List<ISingleResult> a = result.getResults();
            for (ISingleResult isr : a) {
                tableLayout.addColumnData(new ColumnWeightData(1));
                TableColumn column = new TableColumn(table, SWT.CENTER);
                column.setText(isr.getAggregationType().toString());
            }
            label.setText(a.get(0).getServiceID());
        }
        int i = 0;
        Object[] data = new Object[results.size()];
        for (IEvaluationResult item : results) {
            data[i] = item;
            i++;
        }
        tableViewer.setLabelProvider(this);
        tableViewer.add(data);
    }

    public void open(String title) {
        shell.setText(title);
        shell.open();
    }

    @Override
    public void dispose() {
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        IEvaluationResult row = (IEvaluationResult) element;
        if (columnIndex == 0) {
            return row.getBuilder().getUuid();
        } else {
            List<ISingleResult> item = row.getResults();
            Double value = item.get(columnIndex - 1).getValue();
            return Double.toString(value);
        }
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }
}
