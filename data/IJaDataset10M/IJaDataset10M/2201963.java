package missionevolver.gui.dialog.layout;

import java.awt.*;
import javax.swing.*;

public class DialogSeparator extends JLabel {

    public static final int OFFSET = 15;

    public DialogSeparator() {
    }

    public DialogSeparator(String text) {
        super(text);
    }

    public Dimension getPreferredSize() {
        return new Dimension(getParent().getWidth(), 20);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Dimension d = getSize();
        int y = (d.height - 3) / 2;
        g.setColor(Color.white);
        g.drawLine(1, y, d.width - 1, y);
        y++;
        g.drawLine(0, y, 1, y);
        g.setColor(Color.gray);
        g.drawLine(d.width - 1, y, d.width, y);
        y++;
        g.drawLine(1, y, d.width - 1, y);
        String text = getText();
        if (text.length() == 0) return;
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        y = (d.height + fm.getAscent()) / 2;
        int l = fm.stringWidth(text);
        g.setColor(getBackground());
        g.fillRect(OFFSET - 5, 0, OFFSET + l, d.height);
        g.setColor(getForeground());
        g.drawString(text, OFFSET, y);
    }
}
