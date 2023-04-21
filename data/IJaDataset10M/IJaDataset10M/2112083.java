package cw.customermanagementmodul.gui.renderer;

import cw.boardingschoolmanagement.app.CWUtils;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ManuelG
 */
public class GenderTableCellRenderer extends DefaultTableCellRenderer {

    private JLabel cell;

    private Icon maleIcon;

    private Icon femaleIcon;

    public GenderTableCellRenderer() {
        maleIcon = CWUtils.loadIcon("cw/customermanagementmodul/images/male.png");
        femaleIcon = CWUtils.loadIcon("cw/customermanagementmodul/images/female.png");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        boolean male = (Boolean) value;
        if (male) {
            cell.setIcon(maleIcon);
            cell.setText("Herr");
        } else {
            cell.setIcon(femaleIcon);
            cell.setText("Frau");
        }
        return cell;
    }
}
