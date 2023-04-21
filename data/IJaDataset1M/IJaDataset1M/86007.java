package grocerylister.ui;

import java.util.*;
import javax.swing.table.DefaultTableModel;
import grocerylister.*;

/**
 * This is the table model for the grocery list.
 * 
 * @author STAN SAKL
 */
public class GroceryTableModel extends DefaultTableModel {

    private static final String[] m_headings = new String[] { "Qty", "Item", "Description", "Brand", "Size", "Discount Type" };

    public static final int QTY_INDEX = 0;

    public static final int ITEM_INDEX = 1;

    public static final int DESCRIPTION_INDEX = 2;

    public static final int BRAND_INDEX = 3;

    public static final int SIZE_INDEX = 4;

    public static final int DISCOUNT_TYPE_INDEX = 5;

    protected static NavigableSet<IGroceryItem> itemSet = new TreeSet<IGroceryItem>();

    /** Creates a new instance of GroceryTableModel */
    public GroceryTableModel() {
        super(null, m_headings);
    }

    /**
     * Returns the class of object for the column
     * 
     * @param column the column index
     * @return <code>Class</code> corresponding to the column type
     */
    @Override
    public Class getColumnClass(final int column) {
        Class retClass = null;
        if (column == QTY_INDEX) retClass = Integer.class; else retClass = Object.class;
        return retClass;
    }

    @Override
    public String getColumnName(final int column) {
        return m_headings[column];
    }

    public boolean addItem(IGroceryItem item) {
        return itemSet.add(item);
    }

    public void clearSet() {
        itemSet.clear();
    }

    /**
     * Removes a row from the model. It is overridden so that we
     * can remove the backing Set's IGroceryItem as well.
     *
     * @param row the index of the row to remove.
     */
    @Override
    public void removeRow(int row) {
        String item = this.getValueAt(row, ITEM_INDEX).toString();
        String brand = this.getValueAt(row, BRAND_INDEX).toString();
        String size = this.getValueAt(row, SIZE_INDEX).toString();
        String desc = this.getValueAt(row, DESCRIPTION_INDEX).toString();
        IGroceryItem grocery = new GroceryItem(item, desc, brand, size);
        if (itemSet.contains(grocery)) {
            itemSet.remove(grocery);
        }
        super.removeRow(row);
    }
}
