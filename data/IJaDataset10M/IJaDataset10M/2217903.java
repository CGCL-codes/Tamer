package net.sf.yari.ui.util;

import java.util.List;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

/**
 * This class shows an about box, based on TitleAreaDialog
 */
public class SearchResultShell {

    private List<PropertyBean> resultList;

    private TableViewer resultTable;

    private Shell shell;

    public SearchResultShell(String criteria, List<PropertyBean> result) {
        shell = new Shell();
        this.resultList = result;
        shell.setSize(500, 500);
        shell.setText("Search results for \"" + criteria + "\"");
        createTree();
    }

    protected void createTree() {
        shell.setLayout(new FillLayout());
        resultTable = new TableViewer(shell);
        resultTable.getTable().setHeaderVisible(true);
        resultTable.getTable().setLinesVisible(true);
        shell.addControlListener(new ColumnResizeListener(resultTable.getTable()));
        TableColumn column = new TableColumn(resultTable.getTable(), SWT.NONE);
        column.setText("Node");
        column.setWidth(150);
        column = new TableColumn(resultTable.getTable(), SWT.NONE);
        column.setText("Property");
        column.setWidth(150);
        column = new TableColumn(resultTable.getTable(), SWT.NONE);
        column.setText("Value");
        column.setWidth(150);
        resultTable.setLabelProvider(new ResultLabelProvider());
        resultTable.setContentProvider(new ArrayContentProvider());
        resultTable.setInput(resultList);
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        resultTable.addSelectionChangedListener(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        resultTable.removeSelectionChangedListener(listener);
    }

    public void open() {
        shell.open();
    }

    private class ResultLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            PropertyBean pb = (PropertyBean) element;
            String result = null;
            switch(columnIndex) {
                case 0:
                    result = pb.getObject() == null ? "" : pb.getObject().toString();
                    break;
                case 1:
                    result = pb.getProperty();
                    break;
                case 2:
                    result = pb.getValue() == null ? "" : pb.getValue().toString();
                    break;
            }
            return result;
        }
    }
}
