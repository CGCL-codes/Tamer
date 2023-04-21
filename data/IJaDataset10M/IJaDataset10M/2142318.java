package hoplugins.transfers.ui.model;

import hoplugins.Commons;
import hoplugins.transfers.vo.Bookmark;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel representing team bookmarks.
 *
 * @author <a href=mailto:nethyperon@users.sourceforge.net>Boy van der Werf</a>
 */
public class TeamBookmarksTableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6409403277621787085L;

    private List<Bookmark> values;

    private String[] colNames = new String[2];

    /**
     * Creates a TeamBookmarksTableModel.
     *
     * @param values List of values to show in table.
     */
    public TeamBookmarksTableModel(List<Bookmark> values) {
        super();
        this.colNames[0] = Commons.getModel().getLanguageString("ID");
        this.colNames[1] = Commons.getModel().getLanguageString("Name");
        this.values = values;
    }

    /** {@inheritDoc} */
    public final int getColumnCount() {
        return colNames.length;
    }

    /** {@inheritDoc} */
    @Override
    public final String getColumnName(int column) {
        return colNames[column];
    }

    /** {@inheritDoc} */
    public final int getRowCount() {
        return values.size();
    }

    /** {@inheritDoc} */
    public final Object getValueAt(int rowIndex, int columnIndex) {
        final Bookmark bookmark = (Bookmark) values.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return Integer.toString(bookmark.getId());
            case 1:
                return bookmark.getName();
            default:
                return "";
        }
    }
}
