package geovista.category;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UIManager;

public class ColorItemList extends JList {

    public ColorItemList() {
        this.setCellRenderer(new GreyItemRender());
    }

    /**
         *
         * @param name  the name of itme to be gray
         * @param gray
         */
    public void colorItem(String name, boolean gray) {
        int index = this.getIndex(name);
        colorItem(index, gray);
    }

    public void colorItem(int index, boolean gray) {
        GreyItemRender render = (GreyItemRender) this.getCellRenderer();
        render.colorItem(index, gray);
        this.repaint();
    }

    public boolean isItemColored(int index) {
        GreyItemRender render = (GreyItemRender) this.getCellRenderer();
        return render.isItemColored(index);
    }

    /**
         * assume no duplicate item in the list
         * @param o
         * @return
         */
    public int getIndex(Object o) {
        ListModel model = this.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Object obj = model.getElementAt(i);
            if (obj.equals(o)) {
                return i;
            }
        }
        return -1;
    }
}

class GreyItemRender extends DefaultListCellRenderer {

    private HashSet indexOfGreyItem = new HashSet();

    private Color specialColor = new Color(150, 150, 150);

    public void colorItem(int index, boolean grey) {
        if (grey) this.indexOfGreyItem.add(new Integer(index)); else this.indexOfGreyItem.remove(new Integer(index));
    }

    public boolean isItemColored(int index) {
        return this.indexOfGreyItem.contains(new Integer(index));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            if (this.isItemColored(index)) {
                setForeground(specialColor);
            } else setForeground(list.getForeground());
        } else {
            setBackground(list.getBackground());
            if (this.isItemColored(index)) {
                setForeground(specialColor);
            } else setForeground(list.getForeground());
        }
        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else {
            setIcon(null);
            setText((value == null) ? "" : value.toString());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
        return this;
    }
}
