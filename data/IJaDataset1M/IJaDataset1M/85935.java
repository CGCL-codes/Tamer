package org.plazmaforge.bsolution.employee.client.swing.forms;

import java.awt.Window;
import java.util.List;
import java.util.ArrayList;
import org.plazmaforge.bsolution.employee.client.swing.GUIEmployeeEnvironment;
import org.plazmaforge.bsolution.employee.client.swing.forms.EmployeeRankEdit;
import org.plazmaforge.bsolution.employee.common.beans.EmployeeRank;
import org.plazmaforge.bsolution.employee.common.services.EmployeeRankService;
import org.plazmaforge.framework.client.swing.forms.EXTListForm;
import org.plazmaforge.framework.client.swing.gui.table.ColumnProperty;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon
 * Date: 04.10.2004
 * Time: 21:56:28
 * $Id: EmployeeRankList.java,v 1.3 2010/12/05 07:56:45 ohapon Exp $
 */
public class EmployeeRankList extends EXTListForm {

    public EmployeeRankList() throws ApplicationException {
        super(GUIEmployeeEnvironment.getResources());
        initialize();
    }

    public EmployeeRankList(Window window) throws ApplicationException {
        super(window, GUIEmployeeEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(EmployeeRank.class);
        this.setEntityServiceClass(EmployeeRankService.class);
        this.setEntityEditFormClass(EmployeeRankEdit.class);
    }

    protected void initComponents() throws ApplicationException {
        super.initComponents();
        setTitle(getString("title"));
    }

    protected List<ColumnProperty> createTableColumnProperties() throws ApplicationException {
        List<ColumnProperty> columns = new ArrayList<ColumnProperty>();
        ColumnProperty d = new ColumnProperty();
        d.setName(getString("table.column-name.name"));
        d.setFieldName("name");
        d.setColumnClass(String.class);
        d.setSize(30);
        columns.add(d);
        return columns;
    }
}
