package net.sourceforge.processdash.ui.lib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class MultiWindowCheckboxIcon implements Icon {

    private Color checkColor = Color.black;

    private Color boxColor = Color.gray;

    private Color frameColor = Color.black;

    private Color titleBarColor = Color.blue.darker();

    private Color windowColor = Color.white;

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getIconHeight() {
        return 11;
    }

    public int getIconWidth() {
        return 25;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(boxColor);
        g.drawRect(x + 0, y + 1, 8, 8);
        paintFrame(g, x + 12, y);
        paintFrame(g, x + 16, y + 3);
        if (isChecked()) {
            g.setColor(checkColor);
            for (int i = -1; i < 4; i++) g.drawLine(x + 3 + i, y + 6 - Math.abs(i), x + 3 + i, y + 7 - Math.abs(i));
        }
    }

    private void paintFrame(Graphics g, int x, int y) {
        g.setColor(frameColor);
        g.drawRect(x, y, 8, 7);
        g.setColor(windowColor);
        g.fillRect(x + 1, y + 1, 7, 6);
        g.setColor(titleBarColor);
        g.drawLine(x + 1, y + 1, x + 7, y + 1);
    }
}
