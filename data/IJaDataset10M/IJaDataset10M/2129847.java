package org.pvs.superpalitos.sp_lite.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/**
 * @author Angel Luis Calvo Ortega
 */
public class ColorBox extends JComboBox {

    private static final long serialVersionUID = 3546083536269357619L;

    private static final Color[] DEFAULT_COLORS = { Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE, Color.YELLOW };

    private Color[] colors;

    public ColorBox(Color[] colors) {
        super(colors);
        this.colors = colors;
        setRenderer(new ColorBoxRenderer());
    }

    public ColorBox() {
        this(DEFAULT_COLORS);
    }

    public void setColors(Color[] colors) {
        super.setModel(new DefaultComboBoxModel(colors));
        this.colors = colors;
    }

    public int getIndex(Color color) {
        for (int i = 0; i < colors.length; i++) {
            if (color.equals(colors[i])) {
                return i;
            }
        }
        return 0;
    }

    public void setSelectedColor(Color color) {
        setSelectedIndex(getIndex(color));
    }

    public Color getSelectedColor() {
        return colors[getSelectedIndex()];
    }

    static class ColorBoxRenderer extends JPanel implements ListCellRenderer {

        private static final long serialVersionUID = 3904964131575575092L;

        private Color color = Color.black;

        private Color focusColor = (Color) UIManager.get("List.selectionBackground");

        private Color nonFocusColor = Color.white;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (cellHasFocus || isSelected) {
                setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10, focusColor), new LineBorder(Color.black)));
            } else {
                setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10, nonFocusColor), new LineBorder(Color.black)));
            }
            if (value instanceof Color) {
                color = (Color) value;
            }
            return this;
        }

        @Override
        public void paintComponent(Graphics g) {
            setBackground(color);
            super.paintComponent(g);
        }
    }
}
