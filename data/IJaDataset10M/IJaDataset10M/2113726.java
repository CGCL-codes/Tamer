package ho.module.teamAnalyzer.ui.model;

import ho.core.gui.model.BaseTableModel;
import java.util.Vector;

/**
 * Custom RatingTable model
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class UiRosterTableModel extends BaseTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2333079334884026403L;

    /**
     * Creates a new UiRecapTableModel object.
     *
     * @param vector Vector of table data
     * @param vector2 Vector of column names
     */
    public UiRosterTableModel(Vector<Object> vector, Vector<?> vector2) {
        super(vector, vector2);
    }

    /**
     * Creates a new instance of UiFilterTableModel
     */
    public UiRosterTableModel() {
        super();
    }

    /**
     * Method that returns if the cell if editable, false by default
     *
     * @param row the row index of the cell
     * @param column the column index of the cell
     *
     * @return true if editable, false if not
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        if ((column > 4) && (column < 9)) {
            return true;
        }
        return false;
    }
}
