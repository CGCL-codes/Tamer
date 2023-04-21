package connex.app.wsUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */
public class ListRenderer extends DefaultListCellRenderer {

    String element;

    ImageIcon ico;

    public ListRenderer(ImageIcon ico) {
        setOpaque(false);
        this.ico = ico;
        this.setFont(new java.awt.Font("SansSerif", 1, 17));
        this.setBackground(Color.white);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setIcon(ico);
        return (label);
    }
}
