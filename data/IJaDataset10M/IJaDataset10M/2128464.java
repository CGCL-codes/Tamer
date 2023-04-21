package barrywei.igosyncdocs.gui.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * 
 *
 *
 * @author BarryWei
 * @version 1.0, Jul 16, 2010
 * @since JDK1.6
 */
public class IGoSyncDocsRemoteViewTableHaderRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = -2063543272610033501L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 3 || column == 0 || column == 1) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        setText((String) value);
        setBackground(table.getTableHeader().getBackground());
        setForeground(table.getTableHeader().getForeground());
        setEnabled(table.isEnabled());
        setFont(table.getFont());
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return this;
    }
}
