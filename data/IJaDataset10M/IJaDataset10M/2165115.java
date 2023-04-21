package org.plazmaforge.bsolution.security.client.swt.forms;

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
 * $Id: RoleListForm.java,v 1.2 2010/04/28 06:31:03 ohapon Exp $
 */
public class RoleListForm extends AbstractListForm {

    private Table table;

    public RoleListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("RoleListForm.title"));
        TableColumn tableColumn;
        TableColumn nameColumn;
        setLayout(new FillLayout());
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setSortDirection(SWT.DOWN);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        nameColumn = new TableColumn(table, SWT.NONE);
        table.setSortColumn(nameColumn);
        nameColumn.setWidth(300);
        nameColumn.setText(Messages.getString("RoleListForm.nameColumn.text"));
        this.setSize(new Point(400, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "name");
    }
}
