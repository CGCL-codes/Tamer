package org.mitre.rt.client.ui.profile.selectgroup;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.rtclient.GroupSelectorType;

/**
 *
 * @author JWINSTON
 */
public class SelectGroupCheckboxRenderer extends JCheckBox implements TableCellRenderer {

    private static final Logger logger = Logger.getLogger(SelectGroupCheckboxRenderer.class.getPackage().getName());

    public SelectGroupCheckboxRenderer(boolean canEdit) {
        logger.debug("Constructor Called");
        super.setEnabled(canEdit);
        super.setHorizontalAlignment(JCheckBox.CENTER);
        super.setVisible(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        GroupSelectorType selectGroup = (GroupSelectorType) value;
        GlobalUITools.setupTableRendererUI(this, table, row, column, isSelected);
        if (selectGroup != null) {
            setSelected(selectGroup.getSelected());
        }
        return this;
    }
}
