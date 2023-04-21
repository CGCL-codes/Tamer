package org.plazmaforge.bsolution.organization.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.plazmaforge.framework.client.swt.SWTToolkit;
import org.plazmaforge.framework.client.swt.forms.AbstractListForm;

/** 
 * @author Oleh Hapon
 */
public class MtrlResponsibleListForm extends AbstractListForm {

    private Table table;

    public MtrlResponsibleListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("MtrlResponsibleListForm.title"));
        TableColumn tableColumn;
        TableColumn employeeColumn;
        TableColumn warehouseColumn;
        setLayout(new FillLayout());
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        employeeColumn = new TableColumn(table, SWT.NONE);
        employeeColumn.setWidth(300);
        employeeColumn.setText(Messages.getString("MtrlResponsibleListForm.employeeColumn.text"));
        warehouseColumn = new TableColumn(table, SWT.NONE);
        warehouseColumn.setWidth(300);
        warehouseColumn.setText(Messages.getString("MtrlResponsibleListForm.warehouseColumn.text"));
        this.setSize(new Point(600, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "contactName");
        bindColumn(2, "warehouseName");
    }
}
