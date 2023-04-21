package org.plazmaforge.bsolution.base.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.plazmaforge.bsolution.base.common.services.SystemEntityService;
import org.plazmaforge.bsolution.base.common.services.SystemVariableService;
import org.plazmaforge.framework.client.swt.SWTToolkit;
import org.plazmaforge.framework.client.swt.forms.AbstractListForm;
import org.plazmaforge.framework.config.object.EntityConfig;
import org.plazmaforge.framework.core.criteria.IDisableCriteriaImplementator;
import org.plazmaforge.framework.core.data.Variable;

/** 
 * @author Oleh Hapon
 * $Id: SystemVariableListForm.java,v 1.4 2010/12/05 07:57:20 ohapon Exp $
 */
public class SystemVariableListForm extends AbstractListForm implements IDisableCriteriaImplementator {

    private Table table;

    public SystemVariableListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setEntityClass(Variable.class);
        setEntityServiceClass(SystemVariableService.class);
        setTitle(Messages.getString("SystemVariableListForm.title"));
        setDisablePagingMode();
        TableColumn tableColumn;
        TableColumn codeColumn;
        TableColumn nameColumn;
        TableColumn valueColumn;
        setLayout(new FillLayout());
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        codeColumn = new TableColumn(table, SWT.NONE);
        codeColumn.setWidth(100);
        codeColumn.setText(Messages.getString("SystemVariableListForm.codeColumn.text"));
        nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setWidth(230);
        nameColumn.setText(Messages.getString("SystemVariableListForm.nameColumn.text"));
        valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setWidth(230);
        valueColumn.setText(Messages.getString("SystemVariableListForm.valueColumn.text"));
        this.setSize(new Point(600, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "code");
        bindColumn(2, "name");
        bindColumn(3, "stringDisplayValue");
    }
}
