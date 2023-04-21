package com.platonov.colorizer.gui.mycontrols.itemrenderers;

import com.platonov.colorizer.proxy.NetworkObj;
import javax.swing.*;
import java.awt.*;

/**
 * User: Platonov
 * Date: 31.08.11
 */
public class MyListItemRenderer extends JPanel implements ListCellRenderer {

    private ListItem item;

    private JLabel lbl;

    public void setNetworkObject(NetworkObj networkObj) {
        item.networkObj = networkObj;
        lbl.setText(networkObj.toString());
    }

    public NetworkObj getNetworkObject() {
        return item.networkObj;
    }

    public MyListItemRenderer() {
        lbl = new JLabel();
        add(lbl);
        lbl.setText("���� �� �������");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        item = (ListItem) value;
        lbl.setText(item.networkObj == null ? "-" : item.networkObj.toString());
        setBackground(item.color);
        setForeground(item.color);
        if (isSelected) {
            lbl.setFont(new Font("Courier New", Font.BOLD, 15));
        } else {
            lbl.setFont(new Font("Courier New", Font.ITALIC, 13));
        }
        return this;
    }
}
