package es.aeat.eett.rubik.tableRubik;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import swingUtil.Tamemasa.table.AttributiveCellTableModel;
import swingUtil.Tamemasa.table.CellAttribute;
import swingUtil.Tamemasa.table.CellFont;
import swingUtil.Tamemasa.table.CellImage;
import swingUtil.Tamemasa.table.CellImage2;
import swingUtil.Tamemasa.table.CellToolTipText;
import swingUtil.Tamemasa.table.ColoredCell;

/**
 * @author f00992
 */
public class RowHeaderRendererBiIcon extends JPanel implements TableCellRenderer {

    private static final long serialVersionUID = 5709249762575290797L;

    private JLabel l_main = new JLabel();

    private JLabel l_aux = new JLabel();

    RowHeaderRendererBiIcon() {
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setLayout(new java.awt.BorderLayout());
        add(l_main, java.awt.BorderLayout.CENTER);
        add(l_aux, java.awt.BorderLayout.EAST);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color foreground = null;
        Color background = null;
        Font font = null;
        ImageIcon imageIcon = null;
        ImageIcon imageIcon2 = null;
        String toolTipText = null;
        TableModel model = table.getModel();
        if (model instanceof AttributiveCellTableModel) {
            CellAttribute cellAtt = ((AttributiveCellTableModel) model).getCellAttribute();
            if (cellAtt instanceof ColoredCell) {
                foreground = ((ColoredCell) cellAtt).getForeground(row, column);
                background = ((ColoredCell) cellAtt).getBackground(row, column);
            }
            if (cellAtt instanceof CellFont) {
                font = ((CellFont) cellAtt).getFont(row, column);
            }
            if (cellAtt instanceof CellImage) {
                imageIcon = ((CellImage) cellAtt).getImageIcon(row, column);
            }
            if (cellAtt instanceof CellImage2) {
                imageIcon2 = ((CellImage2) cellAtt).getImageIcon2(row, column);
            }
            if (cellAtt instanceof CellToolTipText) {
                toolTipText = ((CellToolTipText) cellAtt).getToolTipText(row, column);
            }
        }
        setForeground((foreground != null) ? foreground : UIManager.getColor("TableHeader.foreground"));
        setBackground((background != null) ? background : UIManager.getColor("TableHeader.background"));
        font = (font != null) ? font : table.getTableHeader().getFont();
        setFont(font);
        l_main.setFont(font);
        l_main.setIcon(imageIcon);
        l_aux.setIcon(imageIcon2);
        l_main.setForeground(foreground);
        setToolTipText(toolTipText);
        setValue(value);
        return this;
    }

    protected void setValue(Object value) {
        l_main.setText((value == null) ? "" : value.toString());
    }
}
