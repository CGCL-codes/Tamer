package ntorrent.torrenttable.view.renderers;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import ntorrent.torrenttable.model.Eta;

/**
 * 
 * @author Hans Hasert
 *
 */
public class EtaRenderer extends JPanel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private final JLabel eta = new JLabel();

    private final GridLayout layout = new GridLayout(0, 1);

    boolean firstrun = true;

    public EtaRenderer() {
        setLayout(layout);
        add(eta);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (firstrun) {
            firstrun = false;
            eta.setFont(table.getFont());
            eta.setHorizontalAlignment(JLabel.RIGHT);
        }
        if (value == null) return null;
        Eta e = (Eta) value;
        eta.setText(e.toString() + " ");
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
            eta.setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
            eta.setForeground(table.getForeground());
        }
        return this;
    }
}
