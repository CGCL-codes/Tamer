package org.dag.dmj;

import java.awt.Component;
import javax.swing.*;
import org.dag.dmj.data.MonsterData;

public class MonCellRenderer extends DefaultListCellRenderer implements ListCellRenderer {

    private JLabel listlabel;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        listlabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof String) listlabel.setIcon(MonsterData.MonsterIcon[index]); else listlabel.setIcon(((MonsterData) value).pic);
        return listlabel;
    }
}
