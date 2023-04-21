package fireteam.interfaces;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: tolik1
 * Date: 16.08.2005
 * Time: 10:37:43
 * To change this template use File | Settings | File Templates.
 */
final class tableRenderer extends JLabel implements TableCellRenderer {

    private final Color m_SelectBkColor = new Color(150, 150, 255);

    private final Color m_NormalBkColor = Color.white;

    private final Color m_NormalForeColor = Color.black;

    private final Font m_TextFont = new Font("Arial", 0, 12);

    final boolean m_bOrders = false;

    private final int m_SelectedAlpha = 75;

    public tableRenderer() {
        super();
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object str, boolean isSelected, boolean hasFocus, int row, int column) {
        Component ret = this;
        if (str == null) return ret;
        if (str.getClass().isAssignableFrom(tableData.class)) {
            tableData td = (tableData) str;
            if (td.oData instanceof Boolean) {
                System.out.println(td.oData);
            }
            if (isSelected) {
                Color selectColor = (td.bkColor == null) ? m_NormalBkColor : td.bkColor;
                double divCur = 100 / m_SelectedAlpha;
                double divBk = 100 / (100 - m_SelectedAlpha);
                int R = (int) (selectColor.getRed() / divBk) + (int) (m_SelectBkColor.getRed() / divCur);
                int G = (int) (selectColor.getGreen() / divBk) + (int) (m_SelectBkColor.getGreen() / divCur);
                int B = (int) (selectColor.getBlue() / divBk) + (int) (m_SelectBkColor.getBlue() / divCur);
                R = (R > 255) ? 255 : R;
                G = (G > 255) ? 255 : G;
                B = (B > 255) ? 255 : B;
                Color newCol = new Color(R, G, B);
                setBackground(newCol);
            } else if (td.bkColor != null) setBackground(td.bkColor); else setBackground(m_NormalBkColor);
            setHorizontalAlignment(td.iAligment);
            if (td.frColor != null) setForeground(td.frColor); else setForeground(m_NormalForeColor);
            if (td.fFont != null) setFont(td.fFont); else setFont(m_TextFont);
            if (td.oData == null) setText(""); else setText(td.oData.toString());
        } else {
            if (isSelected) setBackground(m_SelectBkColor); else setBackground(m_NormalBkColor);
            setFont(m_TextFont);
            String sShow = (String) str;
            setText(sShow);
        }
        return ret;
    }
}
