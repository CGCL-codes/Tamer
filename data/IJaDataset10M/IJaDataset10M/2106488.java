package de.cinek.rssview.ui.table;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import de.cinek.rssview.RssSettings;

/**
 * Wraps a TableModel with a SortableTableModel to provide sorting capabilities.
 * As the row index in the sorted Tablemodel is not necessarily the same as in
 * the TableModel you <strong>must </strong> use translateRowToModel method to
 * translate row indices of JSortableTable to the underlying TableModel.
 * 
 * @author saintedlama
 */
public class JSortableTable extends JTable {

    private SortableTableModel sorter;

    /**
	 * Creates a new initially unsorted JSortableTable.
	 * 
	 * @param model The TableModel that should be wrapped
	 */
    public JSortableTable(TableModel model) {
        super();
        loadFontSettings();
        setModel(model);
        getTableHeader().setDefaultRenderer(new SortableTableHeaderRenderer(sorter));
        sorter.addMouseListenerToHeaderInTable(this);
    }

    /**
	 * Creates a new initlialy sorted JSortableTable
	 * 
	 * @param model The TableModel that should be wrapped
	 * @param column The Column that should be sorted initially
	 * @param ascending The sort direction
	 */
    public JSortableTable(TableModel model, int column, boolean ascending) {
        this(model);
        this.setDefaultSortColumn(column, ascending);
    }

    public void loadFontSettings() {
        java.awt.Font TableFont = RssSettings.getInstance().getTableFont();
        setFont(TableFont);
        getTableHeader().setFont(TableFont);
        if (TableFont.getSize() >= 14) {
            setRowHeight(TableFont.getSize() + 4);
        }
    }

    /**
	 * Sets the model attribute of the JSortableTable object
	 * 
	 * @param model The new model value
	 */
    public void setModel(TableModel model) {
        sorter = new SortableTableModel(model);
        super.setModel(sorter);
    }

    /**
	 * Sets the defaultSortColumn attribute of the JSortableTable object
	 * 
	 * @param column The new defaultSortColumn value
	 * @param ascending The new defaultSortColumn value
	 */
    public void setDefaultSortColumn(int column, boolean ascending) {
        sorter.addSortCriterion(column, ascending);
        sorter.sort();
    }

    /**
	 * Description of the Method
	 * 
	 * @param row Description of Parameter
	 * 
	 * @return Description of the Returned Value
	 */
    public int translateRowToModel(int row) {
        return sorter.translateRowToModel(row);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param column DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public int rowIndex(int column) {
        return sorter.rowIndex(column);
    }

    /**
	 * Getter for property sorter.
	 * 
	 * @return Value of property sorter.
	 */
    public SortableTableHeaderRenderer createHeaderRenderer() {
        return new SortableTableHeaderRenderer(sorter);
    }
}
