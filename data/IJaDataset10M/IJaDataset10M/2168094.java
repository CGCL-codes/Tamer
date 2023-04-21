package gomule.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Marco & Silospen
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class D2CellStringRenderer extends DefaultTableCellRenderer {

    public D2CellStringRenderer() {
    }

    public Component getTableCellRendererComponent(JTable pTable, Object pValue, boolean pIsSelected, boolean pHasFocus, int pRow, int pColumn) {
        Object lValue;
        Color lForeground = null;
        String lTooltip = null;
        if (pValue instanceof D2CellValue) {
            lValue = ((D2CellValue) pValue).getValue();
            lForeground = ((D2CellValue) pValue).getForeground();
            lTooltip = ((D2CellValue) pValue).getTooltip();
        } else {
            lValue = pValue;
        }
        Component lRenderer = super.getTableCellRendererComponent(pTable, lValue, pIsSelected, pHasFocus, pRow, pColumn);
        lRenderer.setBackground(Color.DARK_GRAY);
        if (pIsSelected) {
            lRenderer.setBackground(Color.black);
        }
        if (lForeground != null) {
            lRenderer.setForeground(lForeground);
        } else {
            lRenderer.setForeground(Color.black);
        }
        if (lRenderer instanceof D2CellStringRenderer) {
            ((D2CellStringRenderer) lRenderer).setToolTipText(lTooltip);
        }
        return lRenderer;
    }
}
