package org.plazmaforge.bsolution.partner.client.swing.forms;

import java.awt.Window;
import java.util.List;
import java.util.ArrayList;
import org.plazmaforge.bsolution.partner.client.swing.GUIPartnerEnvironment;
import org.plazmaforge.bsolution.partner.common.beans.PartnerCategory;
import org.plazmaforge.bsolution.partner.common.services.PartnerCategoryService;
import org.plazmaforge.framework.client.swing.forms.EXTListForm;
import org.plazmaforge.framework.client.swing.gui.table.ColumnProperty;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon
 * Date: 15.09.2004
 * Time: 7:56:38
 * $Id: PartnerCategoryList.java,v 1.3 2010/12/05 07:56:43 ohapon Exp $
 */
public class PartnerCategoryList extends EXTListForm {

    public PartnerCategoryList() throws ApplicationException {
        super(GUIPartnerEnvironment.getResources());
        initialize();
    }

    public PartnerCategoryList(Window window) throws ApplicationException {
        super(window, GUIPartnerEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(PartnerCategory.class);
        this.setEntityServiceClass(PartnerCategoryService.class);
        this.setEntityEditFormClass(PartnerCategoryEdit.class);
    }

    protected void initShell() throws ApplicationException {
        super.initShell();
        getShell().setSize(310, DEFAULT_HEIGHT);
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
        d.setSize(50);
        columns.add(d);
        return columns;
    }
}
