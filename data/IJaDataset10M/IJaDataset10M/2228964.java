package org.plazmaforge.bsolution.finance.client.swing.forms;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.plazmaforge.bsolution.base.EnterpriseEnvironment;
import org.plazmaforge.bsolution.document.client.swing.forms.AbstractDocumentList;
import org.plazmaforge.bsolution.finance.client.swing.GUIFinanceEnvironment;
import org.plazmaforge.bsolution.finance.common.beans.Contract;
import org.plazmaforge.bsolution.finance.common.services.ContractService;
import org.plazmaforge.framework.client.swing.gui.GUIEnvironment;
import org.plazmaforge.framework.client.swing.gui.table.ColumnProperty;
import org.plazmaforge.framework.core.exception.ApplicationException;
import java.awt.Window;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Oleh Hapon
 * Date: 26.12.2004
 * Time: 12:05:56
 * $Id: ContractList.java,v 1.3 2010/12/05 07:56:43 ohapon Exp $
 */
public class ContractList extends AbstractDocumentList {

    public ContractList() throws ApplicationException {
        super(GUIFinanceEnvironment.getResources());
        initialize();
    }

    public ContractList(Window window) throws ApplicationException {
        super(window, GUIFinanceEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(Contract.class);
        this.setEntityServiceClass(ContractService.class);
        this.setEntityEditFormClass(ContractEdit.class);
    }

    protected void initComponents() throws ApplicationException {
        super.initComponents();
        setTitle(getString("title"));
    }

    protected void initShell() throws ApplicationException {
        super.initShell();
        getShell().setSize(750, DEFAULT_HEIGHT);
    }

    protected List<ColumnProperty> createTableColumnProperties() throws ApplicationException {
        List<ColumnProperty> columns = new ArrayList<ColumnProperty>();
        ColumnProperty d = new ColumnProperty();
        d.setName(getString("table.column-code.name"));
        d.setFieldName("code");
        d.setColumnClass(String.class);
        d.setSize(DEFAULT_CODE_COLUMN_WIDTH);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-date.name"));
        d.setFieldName("date");
        d.setColumnClass(Date.class);
        d.setSize(DEFAULT_DATE_COLUMN_WIDTH);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-partner.name"));
        d.setFieldName("partnerName");
        d.setColumnClass(String.class);
        d.setSize(DEFAULT_LONG_NAME_COLUMN_WIDTH);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-currency-amount.name"));
        d.setFieldName("currencyEnterTotal");
        d.setColumnClass(Double.class);
        d.setSize(DEFAULT_AMOUNT_COLUMN_WIDTH);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(EnterpriseEnvironment.getCurrencyLabel());
        d.setFieldName("currencyName");
        d.setColumnClass(String.class);
        d.setSize(DEFAULT_CURRENCY_CHAR_COLUMN_WIDTH);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-amount.name"));
        d.setFieldName("enterTotal");
        d.setColumnClass(Double.class);
        d.setSize(DEFAULT_AMOUNT_COLUMN_WIDTH);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-user-name.name"));
        d.setFieldName("userName");
        d.setColumnClass(String.class);
        d.setSize(DEFAULT_USER_NAME_COLUMN_WIDTH);
        columns.add(d);
        return columns;
    }

    protected JTable createTable(TableModel model) throws ApplicationException {
        JTable table = super.createTable(model);
        table.getColumn(getString("table.column-currency-amount.name")).setCellRenderer(GUIEnvironment.createCurrencyTableCellRenderer());
        table.getColumn(getString("table.column-amount.name")).setCellRenderer(GUIEnvironment.createCurrencyTableCellRenderer());
        return table;
    }
}
