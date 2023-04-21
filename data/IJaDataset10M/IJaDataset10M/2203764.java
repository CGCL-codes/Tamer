package org.fudaa.fudaa.crue.common;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 * Renderer de cellules pour les graphes et calques. Permet d avoir des miniatures des widgets.
 * 
 * @author Adrien Hadoux
 */
@SuppressWarnings("serial")
public class GrapheCellRenderer extends JLabel implements ListCellRenderer {

    private final Color HIGHLIGHT_COLOR = UIManager.getColor("Tree.selectionBackground");

    public GrapheCellRenderer() {
        setOpaque(true);
        setIconTextGap(12);
    }

    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final JLabel entry = (JLabel) value;
        setText(entry.getText());
        setIcon(entry.getIcon());
        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        return this;
    }
}
