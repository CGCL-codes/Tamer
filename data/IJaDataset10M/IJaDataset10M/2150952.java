package com.ynhenc.gis.ui.comp;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.*;

public class ComboBoxRenderer_Color extends JLabel implements ListCellRenderer {

    protected Object color = null;

    public ComboBoxRenderer_Color() {
        super();
        int m = 1;
        this.setBorder(new CompoundBorder(new MatteBorder(m, m, m, m, Color.white), new LineBorder(Color.black)));
    }

    public Component getListCellRendererComponent(JList list, Object obj, int row, boolean sel, boolean hasFocus) {
        color = obj;
        Component comp = this;
        return comp;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = size.height < 14 ? 14 : size.height;
        return size;
    }

    public void paint(Graphics g) {
        if (color instanceof Color) {
            super.paint(g);
            Color color = (Color) this.color;
            g.setColor(color);
            Dimension size = this.getSize();
            Insets insets = this.getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = size.width - insets.left - insets.right;
            int height = size.height - insets.top - insets.bottom;
            g.fillRect(x, y, width, height);
        } else {
            this.setText("" + color);
            super.paint(g);
        }
    }

    public static void main(String[] a) {
        JComboBox cbColor = new JComboBox();
        int[] values = new int[] { 0, 128, 192, 255 };
        for (int r = 0; r < values.length; r++) for (int g = 0; g < values.length; g++) for (int b = 0; b < values.length; b++) {
            Color c = new Color(values[r], values[g], values[b]);
            cbColor.addItem(c);
        }
        cbColor.setRenderer(new ComboBoxRenderer_Color());
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.getContentPane().add(cbColor);
        f.pack();
        f.setSize(new Dimension(100, 40));
        f.setVisible(true);
    }
}
