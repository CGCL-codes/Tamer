package utilidad.componentes;

import java.awt.Component;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import utilidad.componentes.MiTable.CheckBoxHeader;

public class MiCheckBoxHeader extends JCheckBox implements TableCellRenderer, MouseListener {

    public MiCheckBoxHeader rendererComponent;

    protected int column;

    protected boolean mousePressed = false;

    public MiCheckBoxHeader() {
        rendererComponent = this;
    }

    public MiCheckBoxHeader(ItemListener itemListener) {
        rendererComponent = this;
        rendererComponent.addItemListener(itemListener);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                rendererComponent.setForeground(header.getForeground());
                rendererComponent.setBackground(header.getBackground());
                rendererComponent.setFont(header.getFont());
                header.addMouseListener(rendererComponent);
            }
        }
        setColumn(col);
        rendererComponent.setText((String) value);
        rendererComponent.setHorizontalAlignment(SwingConstants.CENTER);
        rendererComponent.setOpaque(true);
        rendererComponent.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        super.setBorderPainted(true);
        return rendererComponent;
    }

    protected void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    protected void handleClickEvent(MouseEvent e) {
        if (mousePressed) {
            mousePressed = false;
            JTableHeader header = (JTableHeader) (e.getSource());
            JTable tableView = header.getTable();
            TableColumnModel columnModel = tableView.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = tableView.convertColumnIndexToModel(viewColumn);
            if (viewColumn == this.column && e.getClickCount() == 1 && column != -1) {
                doClick();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        handleClickEvent(e);
        ((JTableHeader) e.getSource()).repaint();
    }

    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
