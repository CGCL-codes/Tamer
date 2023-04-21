package org.plazmaforge.bsolution.personality.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.plazmaforge.framework.client.swt.SWTToolkit;

/** 
 * @author Oleh Hapon
 * $Id: PersonJobListForm.java,v 1.2 2010/04/28 06:31:02 ohapon Exp $
 */
public class PersonJobListForm extends AbstractPersonableList {

    private Table table;

    public PersonJobListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("PersonJobListForm.title"));
        TableColumn tableColumn;
        TableColumn startDateColumn;
        TableColumn endDateColumn;
        TableColumn organizationColumn;
        TableColumn positionColumn;
        setLayout(new FillLayout());
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        startDateColumn = new TableColumn(table, SWT.NONE);
        startDateColumn.setWidth(100);
        startDateColumn.setText(Messages.getString("PersonJobListForm.startDateColumn.text"));
        endDateColumn = new TableColumn(table, SWT.NONE);
        endDateColumn.setWidth(100);
        endDateColumn.setText(Messages.getString("PersonJobListForm.endDateColumn.text"));
        organizationColumn = new TableColumn(table, SWT.NONE);
        organizationColumn.setWidth(300);
        organizationColumn.setText(Messages.getString("PersonJobListForm.organizationColumn.text"));
        positionColumn = new TableColumn(table, SWT.NONE);
        positionColumn.setWidth(200);
        positionColumn.setText(Messages.getString("PersonJobListForm.positionColumn.text"));
        this.setSize(new Point(700, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "startDate", getDateFormat());
        bindColumn(2, "endDate", getDateFormat());
        bindColumn(3, "organization");
        bindColumn(4, "position");
    }
}
