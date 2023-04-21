package modelo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class EquipoCellRender extends JLabel implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean CellHasFocus) {
        Equipo equipo = (Equipo) value;
        setText(equipo.getNombre());
        if (isSelected) {
            setBackground(Color.red);
            setForeground(Color.BLUE);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
