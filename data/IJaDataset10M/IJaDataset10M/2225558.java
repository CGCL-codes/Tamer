package common;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import model.CallsListTableModel;
import phonebook.entry.Location;
import javax.swing.JLabel;

public class CustomAlignedCellRenderer extends DefaultTableCellRenderer {

    private CallsListTableModel m_model;

    private boolean m_useIcon = false;

    public CustomAlignedCellRenderer(CallsListTableModel model) {
        super();
        m_model = model;
        m_useIcon = true;
        if (Configuration.getInstance().getComponentOrientation().isLeftToRight()) {
            setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        } else {
            setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        }
    }

    public CustomAlignedCellRenderer() {
        super();
        applyComponentOrientation(Configuration.getInstance().getComponentOrientation());
        m_useIcon = false;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (!m_useIcon) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        } else {
            JLabel ret = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String number = (String) m_model.getValueAt(row, CallsListTableModel.NUMBER_CLM);
            Location loc = null;
            try {
                loc = Configuration.getInstance().getInfoHandler().getLocation(number);
            } catch (NoSuchLocationException ex) {
                ret.setIcon(null);
                return ret;
            } catch (NotFoundException ex) {
                ret.setIcon(null);
                return ret;
            }
            ret.setIcon(loc.getImage());
            ret.setIconTextGap(20);
            return ret;
        }
    }
}
