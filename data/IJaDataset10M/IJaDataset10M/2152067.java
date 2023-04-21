package org.plazmaforge.bsolution.contact.client.swt.forms;

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
 * $Id: CityListForm.java,v 1.2 2010/04/28 06:31:05 ohapon Exp $
 */
public class CityListForm extends AbstractListForm {

    private Table table;

    public CityListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("CityListForm.title"));
        TableColumn tableColumn;
        TableColumn nameColumn;
        TableColumn regionColumn;
        TableColumn countryColumn;
        setLayout(new FillLayout(SWT.VERTICAL));
        this.setSize(new Point(600, 300));
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setMoveable(true);
        nameColumn.setWidth(300);
        nameColumn.setText(Messages.getString("CityListForm.nameColumn.text"));
        regionColumn = new TableColumn(table, SWT.NONE);
        regionColumn.setMoveable(true);
        regionColumn.setWidth(300);
        regionColumn.setText(Messages.getString("CityListForm.regionColumn.text"));
        countryColumn = new TableColumn(table, SWT.NONE);
        countryColumn.setWidth(300);
        countryColumn.setText(Messages.getString("CityListForm.countryColumn.text"));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "name");
        bindColumn(2, "regionName");
        bindColumn(3, "countryName");
    }
}
