package org.yarl;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;

public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    static Logger log4j = Logger.getLogger(TextAreaRenderer.class);

    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(new Font("Default", Font.PLAIN, 9));
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        log4j.debug("TextAreaRenderer: " + (String) obj);
        setText((String) obj);
        return this;
    }
}
