package org.plazmaforge.bsolution.personality.client.swt.forms;

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
 * $Id: MilitarySpecialityListForm.java,v 1.2 2010/04/28 06:31:02 ohapon Exp $
 */
public class MilitarySpecialityListForm extends AbstractListForm {

    private Table table;

    public MilitarySpecialityListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("MilitarySpecialityListForm.title"));
        TableColumn tableColumn;
        TableColumn codeColumn;
        TableColumn nameColumn;
        setLayout(new FillLayout(SWT.VERTICAL));
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        codeColumn = new TableColumn(table, SWT.NONE);
        codeColumn.setMoveable(true);
        codeColumn.setWidth(100);
        codeColumn.setText(Messages.getString("MilitarySpecialityListForm.codeColumn.text"));
        nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setMoveable(true);
        nameColumn.setWidth(300);
        nameColumn.setText(Messages.getString("MilitarySpecialityListForm.nameColumn.text"));
        this.setSize(new Point(600, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "code");
        bindColumn(2, "name");
    }
}
