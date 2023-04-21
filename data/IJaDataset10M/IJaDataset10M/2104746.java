package org.ln.millesimus.gui.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.ln.millesimus.vo.InvoiceFamily;

public class InvoiceFamilyTableModel extends BaseTableModel<InvoiceFamily> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public InvoiceFamilyTableModel() {
        this(new ArrayList<InvoiceFamily>());
    }

    /**
     * @param dataList
     */
    public InvoiceFamilyTableModel(List<InvoiceFamily> dataList) {
        this(getColunmNames(), dataList);
    }

    /**
     * @param columnNames
     * @param dataList
     */
    public InvoiceFamilyTableModel(List<String> columnNames, List<InvoiceFamily> dataList) {
        super(columnNames, dataList);
    }

    @Override
    public Object getValueAt(int row, int col) {
        InvoiceFamily item = dataList.get(row);
        switch(col) {
            case 0:
                return item.getCode();
            case 1:
                return item.getFamilyCode();
            case 2:
                return item.getQuantity();
            default:
                return item.getCode();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return BigDecimal.class;
            default:
                return String.class;
        }
    }

    /**
	 * @return
	 */
    private static List<String> getColunmNames() {
        List<String> columnNames = new ArrayList<String>();
        columnNames.add("Codice");
        columnNames.add("Codice famiglia");
        columnNames.add("Quantità");
        return columnNames;
    }
}
