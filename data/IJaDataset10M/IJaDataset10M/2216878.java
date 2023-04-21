package com.endfocus.projectbuilder.console;

import java.awt.*;
import javax.swing.*;

public class LineRenderer extends DefaultListCellRenderer {

    protected static Icon errorIcon;

    protected static Color back;

    static {
        errorIcon = com.endfocus.utilities.Utilities.getIcon("com/endfocus/projectbuilder/images/caution.gif");
        back = new Color(255, 255, 204);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TaggedString) {
            int tag = ((TaggedString) value).getTag();
            if (tag == TaggedString.ERROR) setIcon(errorIcon); else setIcon(null);
            if (tag == TaggedString.ERROR || tag == TaggedString.ALERT) setForeground(Color.red); else if (tag == TaggedString.MESSAGE) setForeground(Color.blue); else setForeground(Color.black);
        } else setForeground(Color.black);
        setBackground(back);
        setText(value.toString());
        return this;
    }
}
