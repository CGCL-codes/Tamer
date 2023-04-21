package dsb.bar.tks.client.admin.sales;

import javax.swing.JTable;

public class CustomerTable extends JTable {

    private static final long serialVersionUID = 1L;

    public CustomerTable() {
        this(new CustomerTableModel());
    }

    public CustomerTable(CustomerTableModel model) {
        super(model);
        this.getColumnModel().getColumn(0).setMinWidth(200);
        this.getColumnModel().getColumn(0).setPreferredWidth(200);
        this.getColumnModel().getColumn(0).setMaxWidth(200);
        this.getColumnModel().getColumn(1).setMinWidth(200);
        this.getColumnModel().getColumn(1).setPreferredWidth(200);
        this.getColumnModel().getColumn(1).setMaxWidth(200);
    }
}
