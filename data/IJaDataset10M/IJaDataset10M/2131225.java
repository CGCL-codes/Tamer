package org.plazmaforge.bsolution.payroll.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.plazmaforge.bsolution.document.client.swt.forms.AbstractDocumentListForm;
import org.plazmaforge.framework.client.swt.SWTToolkit;

/** 
 * @author Oleh Hapon
 * $Id: EmployeeVacationListForm.java,v 1.3 2010/04/28 06:31:06 ohapon Exp $
 */
public class EmployeeVacationListForm extends AbstractDocumentListForm {

    private Table table;

    public EmployeeVacationListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("EmployeeVacationListForm.title"));
        TableColumn tableColumn;
        TableColumn documentNoColumn;
        TableColumn documentDateColumn;
        TableColumn employeeColumn;
        TableColumn startDateColumn;
        TableColumn endDateColumn;
        TableColumn responsibleColumn;
        setLayout(new FillLayout());
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setSortDirection(SWT.DOWN);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(10);
        documentNoColumn = new TableColumn(table, SWT.NONE);
        documentNoColumn.setWidth(50);
        documentNoColumn.setText(Messages.getString("EmployeeVacationListForm.documentNoColumn.text"));
        documentDateColumn = new TableColumn(table, SWT.NONE);
        documentDateColumn.setWidth(100);
        documentDateColumn.setText(Messages.getString("EmployeeVacationListForm.documentDateColumn.text"));
        employeeColumn = new TableColumn(table, SWT.NONE);
        employeeColumn.setWidth(300);
        employeeColumn.setText(Messages.getString("EmployeeVacationListForm.employeeColumn.text"));
        startDateColumn = new TableColumn(table, SWT.RIGHT);
        startDateColumn.setWidth(100);
        startDateColumn.setText(Messages.getString("EmployeeVacationListForm.startDateColumn.text"));
        endDateColumn = new TableColumn(table, SWT.RIGHT);
        endDateColumn.setWidth(100);
        endDateColumn.setText(Messages.getString("EmployeeVacationListForm.endDateColumn.text"));
        responsibleColumn = new TableColumn(table, SWT.NONE);
        responsibleColumn.setMoveable(true);
        responsibleColumn.setWidth(200);
        responsibleColumn.setText(Messages.getString("EmployeeVacationListForm.responsibleColumn.text"));
        this.setSize(new Point(700, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "documentNo");
        bindColumn("documentDate", getDateFormat());
        bindColumn("employeeName");
        bindColumn("startDate", getDateFormat());
        bindColumn("endDate", getDateFormat());
        bindColumn("responsibleName");
    }
}
