package net.sf.epfe.core.ui.editors.i18n.components;

import net.sf.epfe.core.ui.resources.ResManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class PropertyLocaleTableManager {

    TreeViewer fTableView;

    String[] columnNames;

    ITableLabelProvider fLabelProvider;

    IStructuredContentProvider fContentProvider;

    public PropertyLocaleTableManager() {
        columnNames = new String[2];
        columnNames[0] = "!";
        columnNames[1] = ResManager.getString("key");
    }

    public void addColumn(String aLocale, String aDescription) {
    }

    public void createTreeTable(Composite parent) {
        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
        final int NUMBER_COLUMNS = this.columnNames.length;
        fTableView = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        Tree lTable = fTableView.getTree();
        lTable.setLinesVisible(true);
        lTable.setHeaderVisible(true);
    }

    public TreeViewer getTableView() {
        return fTableView;
    }

    public void setLayoutData(Object aLayoutData) {
        fTableView.getTree().setLayoutData(aLayoutData);
    }
}
