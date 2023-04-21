package AGO.Vista.ASwing.Renderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import AGO.Vista.ASwing.Componentes.ATable;
import java.awt.*;

public class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    protected ATable table;

    protected Border selectedBorder;

    protected Border normalBorder;

    protected Font selectedFont;

    protected Font normalFont;

    final JTableHeader header;

    public RowHeaderRenderer(ATable table) {
        this.table = table;
        normalBorder = UIManager.getBorder("TableHeader.cellBorder");
        selectedBorder = BorderFactory.createRaisedBevelBorder();
        header = table.getTableHeader();
        normalFont = header.getFont();
        selectedFont = normalFont.deriveFont(normalFont.getStyle() | Font.BOLD);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setOpaque(true);
        setHorizontalAlignment(LEFT);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Border border = null;
        if (cellHasFocus) {
            border = UIManager.getBorder("TableHeader.focusCellBorder");
        }
        if (border == null) {
            border = UIManager.getBorder("TableHeader.cellBorder");
        }
        setBorder(border);
        setText(table.getRowName(index));
        if (header != null) {
            Color fgColor = null;
            Color bgColor = null;
            if (cellHasFocus) {
                fgColor = UIManager.getColor("TableHeader.focusCellForeground");
                bgColor = UIManager.getColor("TableHeader.focusCellBackground");
            }
            if (fgColor == null) {
                fgColor = header.getForeground();
            }
            if (bgColor == null) {
                bgColor = header.getBackground();
            }
            setForeground(fgColor);
            setBackground(bgColor);
            setFont(header.getFont());
        }
        return this;
    }
}
