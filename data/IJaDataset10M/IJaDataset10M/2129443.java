package phex.gui.tabs.security;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import phex.gui.common.GUIRegistry;
import phex.gui.common.table.FWTable;
import phex.security.PhexSecurityManager;
import phex.security.SecurityRule;
import phex.servent.Servent;

public class SecurityRuleRowRenderer implements TableCellRenderer {

    private static final Color darkGreen = new Color(0x00, 0x7F, 0x00);

    private PhexSecurityManager securityMgr;

    public SecurityRuleRowRenderer() {
        Servent servent = GUIRegistry.getInstance().getServent();
        securityMgr = servent.getSecurityService();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TableCellRenderer renderer = table.getDefaultRenderer(table.getColumnClass(column));
        Component comp = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        FWTable fwTable = (FWTable) table;
        int modelRow = fwTable.translateRowIndexToModel(row);
        SecurityRule rule = securityMgr.getIPAccessRule(modelRow);
        comp.setForeground(table.getSelectionForeground());
        if (rule == null || isSelected) {
            return comp;
        }
        if (rule.isDisabled()) {
            comp.setForeground(Color.lightGray);
            return comp;
        }
        if (rule.isSystemRule()) {
            comp.setForeground(Color.gray);
            return comp;
        }
        if (rule.isDenyingRule()) {
            comp.setForeground(Color.red);
        } else {
            comp.setForeground(darkGreen);
        }
        return comp;
    }
}
