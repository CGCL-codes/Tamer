package hoplugins.trainingExperience.ui.renderer;

import hoplugins.trainingExperience.ui.bar.VerticalIndicator;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * DOCUMENT ME!
 *
 * @author Mirtillo To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OutputTableRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7179773036740605371L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if ((column > 2) && (column < 11)) {
            VerticalIndicator vi = (VerticalIndicator) value;
            vi.setBackground(cell.getBackground());
            vi.setOpaque(true);
            return vi;
        }
        return cell;
    }
}
