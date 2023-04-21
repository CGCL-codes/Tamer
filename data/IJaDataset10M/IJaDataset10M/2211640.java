package com.ek.mitapp.ui.table;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import com.ek.mitapp.data.BidItem;
import com.ek.mitapp.data.BidItemRegistry;
import com.ek.mitapp.event.BidItemRegistryEvent;
import com.ek.mitapp.event.BidItemRegistryListener;

/**
 * A table model that contains available bid items.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public class AvailableBidItemsTableModel extends AbstractTableModel implements BidItemRegistryListener {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(AvailableBidItemsTableModel.class.getName());

    /**
	 * List of rows.
	 */
    private final List<BidItem> rows = new ArrayList<BidItem>();

    /**
	 * The <code>Vector</code> of column headers.
	 */
    protected Vector<String> columnHeaders = new Vector<String>();

    /**
	 * Class parameters.
	 */
    public static enum Columns {

        /**
		 * The bid item
		 */
        BidItem(0), /**
		 * The bid item type
		 */
        Type(1), /**
		 * The bid item unit
		 */
        Unit(2), /**
		 * The bid item unit cost
		 */
        UnitCost(3);

        /**
		 * Column index
		 */
        private int columnIndex;

        /**
		 * Constructor.
		 * 
		 * @param columnIndex
		 */
        private Columns(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        /**
		 * @return The column index
		 */
        public int getColumnIndex() {
            return columnIndex;
        }
    }

    /**
	 * Default constructor.
	 * 
	 * @param bidItemRegistry The bid item registry
	 */
    public AvailableBidItemsTableModel(BidItemRegistry bidItemRegistry) {
        super();
        if (bidItemRegistry == null) throw new IllegalArgumentException("Bid item registry cannot be null");
        columnHeaders.add("Item description");
        columnHeaders.add("Type");
        columnHeaders.add("Unit");
        columnHeaders.add("Unit cost");
        buildModel(bidItemRegistry);
        bidItemRegistry.addListener(this);
    }

    /**
	 * Build the table model.
	 * 
	 * @param registry Bid item registry
	 */
    private void buildModel(BidItemRegistry registry) {
        for (BidItem bidItem : registry.getBidItems()) {
            rows.add(bidItem);
        }
    }

    /**
	 * Add a row.
	 * 
	 * @param row Row to add
	 */
    private void addRow(BidItem row) {
        rows.add(row);
        fireTableDataChanged();
    }

    /**
	 * Remove a row.
	 * 
	 * @param row Row to remove
	 */
    private void removeRow(BidItem row) {
        rows.remove(row);
        fireTableDataChanged();
    }

    /**
	 * Remove a row.
	 * 
	 * @param rowIndex The row index
	 */
    public void removeRow(int rowIndex) {
        rows.remove(rowIndex);
        fireTableDataChanged();
    }

    /**
	 * Attempt to retrieve a row. This method will return null if no row is found.
	 * 
	 * @param rowIndex The row index
	 * @return Reference to the retrieved row.
	 */
    public BidItem getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    public int getColumnCount() {
        return columnHeaders.size();
    }

    /**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    public int getRowCount() {
        return rows.size();
    }

    /**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
    public Object getValueAt(int rowIndex, int colIndex) {
        BidItem row = rows.get(rowIndex);
        if (colIndex == Columns.BidItem.getColumnIndex()) {
            return row.getName();
        } else if (colIndex == Columns.Type.getColumnIndex()) {
            return row.getType();
        } else if (colIndex == Columns.Unit.getColumnIndex()) {
            return row.getUnit();
        } else if (colIndex == Columns.UnitCost.getColumnIndex()) {
            return row.getUnitCost();
        } else {
            return "";
        }
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
    @Override
    public String getColumnName(int col) {
        return columnHeaders.get(col);
    }

    /**
	 * @see com.ek.mitapp.event.BidItemRegistryListener#bidItemAdded(com.ek.mitapp.event.BidItemRegistryEvent)
	 */
    public void bidItemAdded(BidItemRegistryEvent eventObj) {
        logger.debug("Received bid item registry added event");
        addRow((BidItem) eventObj.getSource());
    }

    /**
	 * @see com.ek.mitapp.event.BidItemRegistryListener#bidItemRemoved(com.ek.mitapp.event.BidItemRegistryEvent)
	 */
    public void bidItemRemoved(BidItemRegistryEvent eventObj) {
        logger.debug("Received bid item registry removed event");
        BidItem bidItem = (BidItem) eventObj.getSource();
        removeRow(bidItem);
    }

    /**
	 * @see com.ek.mitapp.event.BidItemRegistryListener#bidItemUpdated(com.ek.mitapp.event.BidItemRegistryEvent)
	 */
    public void bidItemUpdated(BidItemRegistryEvent eventObj) {
        logger.debug("Received bid item registry update event");
        BidItem bidItem = (BidItem) eventObj.getSource();
        removeRow(bidItem);
        addRow(bidItem);
    }
}
