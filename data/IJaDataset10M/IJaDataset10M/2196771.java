package org.plazmaforge.bsolution.document.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.plazmaforge.framework.client.swt.SWTToolkit;
import org.plazmaforge.framework.client.swt.forms.AbstractListForm;

public class DocumentStatusListForm extends AbstractListForm {

    private Table table;

    public DocumentStatusListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("DocumentStatusListForm.title"));
        TableColumn tableColumn;
        TableColumn documentTypeColumn;
        TableColumn codeColumn;
        TableColumn nameColumn;
        setLayout(new FillLayout(SWT.VERTICAL));
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        documentTypeColumn = new TableColumn(table, SWT.NONE);
        documentTypeColumn.setMoveable(true);
        documentTypeColumn.setWidth(300);
        documentTypeColumn.setText(Messages.getString("DocumentStatusListForm.documentColumn.text"));
        codeColumn = new TableColumn(table, SWT.NONE);
        codeColumn.setMoveable(true);
        codeColumn.setWidth(150);
        codeColumn.setText(Messages.getString("DocumentStatusListForm.codeColumn.text"));
        nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setMoveable(true);
        nameColumn.setWidth(300);
        nameColumn.setText(Messages.getString("DocumentStatusListForm.nameColumn.text"));
        this.setSize(new Point(600, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "documentType");
        bindColumn("code");
        bindColumn("name");
    }
}
