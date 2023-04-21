package net.sf.karatasi.desktop;

import javax.swing.table.AbstractTableModel;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.karatasi.database.Card;
import net.sf.karatasi.database.Category;
import net.sf.karatasi.database.Database;
import net.sf.karatasi.database.IndexIterator;
import org.jetbrains.annotations.NotNull;

/** TableModel for displaying the cards of a database.
 *
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class CardsTableModel extends AbstractTableModel {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** For the localized texts. */
    private final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder(GuiMain.class);

    /** The Database for which this CardsTableModel is created. */
    private final Database database;

    /** The translation table from row number to card index. */
    private int[] cardRow2idx = null;

    /** The translation table from category id to table row. */
    private int[] categoryRow2Idx = null;

    /** The table of the category names. */
    private String[] categoryNames = null;

    /** Creates a CardsTableModel.
     * @param database Database for which to create the CardsTableModel.
     */
    public CardsTableModel(@NotNull final Database database) {
        this.database = database;
        try {
            database.open();
            cardRow2idx = new int[database.getCardCount()];
            categoryRow2Idx = new int[database.countCategories()];
            categoryNames = new String[database.countCategories()];
            final IndexIterator cardIt = database.getAllCardIndices();
            int row = 0;
            while (cardIt.hasNext()) {
                final int idx = cardIt.next().getIdxValue();
                cardRow2idx[row++] = idx;
            }
            final IndexIterator categoryIt = database.getAllCategoryIndices();
            row = 0;
            while (categoryIt.hasNext()) {
                final int idx = categoryIt.next().getIdxValue();
                categoryRow2Idx[row] = idx;
                categoryNames[row++] = database.getCategoryById(idx).getCategoryName();
            }
            database.close();
        } catch (final Exception e) {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    /** {@inheritDoc} */
    public int getRowCount() {
        return database.getCardCount();
    }

    /** {@inheritDoc} */
    public int getColumnCount() {
        return Category.TEXT_FIELD_COUNT + 2;
    }

    /** {@inheritDoc} */
    public String getColumnName(final int column) {
        if (column == 0) {
            return actionBuilder.getString("viewDatabase.tablehead.index");
        }
        if (column == 1) {
            return actionBuilder.getString("viewDatabase.tablehead.category");
        }
        if (column - 2 < Category.TEXT_FIELD_COUNT) {
            return String.format(actionBuilder.getString("viewDatabase.tablehead.data"), column - 1);
        }
        return null;
    }

    /** {@inheritDoc} */
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return Integer.valueOf(cardRow2idx[rowIndex]);
        }
        if (cardRow2idx == null || categoryRow2Idx == null || categoryNames == null) {
            return null;
        }
        Object retVal = null;
        try {
            database.open();
            final Card card = database.getCardById(cardRow2idx[rowIndex]);
            if (columnIndex == 1) {
                final int categoryId = card.getCategoryId();
                for (int n = 0; n < categoryRow2Idx.length; n++) {
                    if (categoryRow2Idx[n] == categoryId) {
                        retVal = categoryNames[n];
                        break;
                    }
                }
            } else if (columnIndex - 2 < Category.TEXT_FIELD_COUNT) {
                retVal = card.getDataField(columnIndex - 2);
            }
            database.close();
        } catch (final Exception e) {
            if (database.isOpen()) {
                database.close();
            }
        }
        return retVal;
    }
}
