package org.plazmaforge.bsolution.base.client.swing.forms;

import java.awt.Window;
import java.util.List;
import java.util.ArrayList;
import org.plazmaforge.bsolution.base.client.swing.GUIBaseEnvironment;
import org.plazmaforge.bsolution.base.common.beans.Country;
import org.plazmaforge.bsolution.base.common.services.CountryService;
import org.plazmaforge.framework.client.swing.forms.EXTListForm;
import org.plazmaforge.framework.client.swing.gui.table.ColumnProperty;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon Date: 6/10/2003 Time: 18:42:35 $Id: CountryList.java,v 1.1
 *         2007/03/02 07:06:41 ohapon Exp $
 */
public class CountryList extends EXTListForm {

    public CountryList() throws ApplicationException {
        super(GUIBaseEnvironment.getResources());
        initialize();
    }

    public CountryList(Window window) throws ApplicationException {
        super(window, GUIBaseEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(Country.class);
        this.setEntityServiceClass(CountryService.class);
        this.setEntityEditFormClass(CountryEdit.class);
    }

    protected void initComponents() throws ApplicationException {
        super.initComponents();
        setTitle(getString("title"));
    }

    protected void initShell() throws ApplicationException {
        super.initShell();
        getShell().setSize(400, 600);
    }

    protected List<ColumnProperty> createTableColumnProperties() throws ApplicationException {
        List<ColumnProperty> columns = new ArrayList<ColumnProperty>();
        ColumnProperty d = new ColumnProperty();
        d.setName(getString("table.column-code.name"));
        d.setFieldName("code");
        d.setColumnClass(String.class);
        d.setSize(10);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-name.name"));
        d.setFieldName("name");
        d.setColumnClass(String.class);
        d.setSize(50);
        columns.add(d);
        return columns;
    }
}
