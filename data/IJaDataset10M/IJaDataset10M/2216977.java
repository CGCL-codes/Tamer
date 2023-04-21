package net.sf.wwusmart.gui;

import net.sf.wwusmart.database.DataManager;
import net.sf.wwusmart.helper.Couple;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * To render algorithm's descriptors count in database in a table as progress bar.
 * See {@linkplain javax.swing.table.TableCellRenderer}.
 *
 * @author Armin
 * @author Thilo
 * @version $Rev: 777 $
 */
class DescriptorsCountTableCellRenderer extends JProgressBar implements TableCellRenderer {

    private int numberOfShapes;

    public DescriptorsCountTableCellRenderer() throws SQLException {
        super(0, Integer.MAX_VALUE);
        numberOfShapes = DataManager.getInstance().getTotalNumberOfShapes();
        this.setMaximum(numberOfShapes);
        this.setStringPainted(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Couple) {
            setMaximum(((Couple<Integer, Integer>) value).getB());
            setValue(((Couple<Integer, Integer>) value).getA());
        } else if (value instanceof Integer) {
            setValue((Integer) value);
        }
        setString(String.format("%d / %d", getValue(), getMaximum()));
        return this;
    }
}
