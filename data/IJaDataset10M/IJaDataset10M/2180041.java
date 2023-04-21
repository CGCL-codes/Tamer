package dnd4e;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import com.jidesoft.grid.SortableTableModel;
import net.rptools.chartool.model.property.PropertyMap;
import net.rptools.chartool.ui.charsheet.component.CharSheetTable;
import net.rptools.chartool.ui.charsheet.component.CharSheetTable.CharSheetTableModel;
import net.rptools.chartool.ui.component.BlankIcon;

/**
 * A table cell renderer that modifies the renderers for powers on the combat page based on their data.
 * 
 * @author Jay
 */
public class CombatPowerTableCellRenderer extends CharacterPowerTableCellRenderer {

    /**
   * @see dnd4e.CharacterPowerTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
    @Override
    public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
        Component c = super.getTableCellRendererComponent(aTable, aValue, aIsSelected, aHasFocus, aRow, aColumn);
        int row = ((CharSheetTable) aTable).getActualRowAt(aRow);
        PropertyMap power = ((CharSheetTableModel) ((SortableTableModel) ((CharSheetTable) aTable).getModel()).getActualModel()).getRow(row);
        if (power == null) return c;
        String type = (String) power.get("type");
        if (((CharSheetTable) aTable).convertColumnIndexToModel(aColumn) == 5) {
            JCheckBox cb = (JCheckBox) c;
            if (type.equalsIgnoreCase("at-will")) {
                cb.setDisabledIcon(BlankIcon.BLANK_ICON);
                cb.setIcon(BlankIcon.BLANK_ICON);
                cb.setEnabled(false);
            } else {
                cb.setIcon(null);
                cb.setEnabled(true);
            }
        }
        return c;
    }

    /**
   * @see dnd4e.CharacterPowerTableCellRenderer#setHorizontalAlignment(java.awt.Component, net.rptools.chartool.model.property.PropertyMap, javax.swing.JTable, int)
   */
    @Override
    protected void setHorizontalAlignment(Component c, PropertyMap power, JTable aTable, int aColumn) {
        if (c instanceof JCheckBox) {
            ((JCheckBox) c).setHorizontalAlignment(JCheckBox.CENTER);
            return;
        }
        int alignment = JLabel.CENTER;
        if (aTable.convertColumnIndexToModel(aColumn) == 0) alignment = ((Boolean) power.get("available")).booleanValue() ? JLabel.LEFT : JLabel.RIGHT;
        ((JLabel) c).setHorizontalAlignment(alignment);
    }
}
