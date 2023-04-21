package net.rptools.chartool.ui.component;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Special table cell render for icons that contain images downloaded in the background.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class RPIconTableCellRenderer extends DefaultTableCellRenderer {

    /**
   * Center the icon in the columns.
   */
    public RPIconTableCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
   * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
    @Override
    public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
        assert aValue == null || aValue instanceof RPIcon : "The value needs to be an RPIcon but was a " + aValue.getClass().getName();
        super.getTableCellRendererComponent(aTable, aValue, aIsSelected, aHasFocus, aRow, aColumn);
        RPIcon icon = (RPIcon) aValue;
        setText(null);
        setIcon(icon);
        if (icon != null) icon.repaintOnLoad(aTable);
        return this;
    }
}
