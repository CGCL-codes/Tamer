package net.sf.mailsomething.gui.standard;

import java.awt.*;
import javax.swing.*;

/**
 * 
 * A simple component showing a header like this:
 * ------ header -------
 * 
 * <b>Note: This isnt fully finished.</b>
 * 
 * @author Stig Tanggaard
 * @created 12-10-2002
 * 
 */
public class TextLineHeader extends JComponent {

    private String title;

    private int titlewidth = -1;

    public TextLineHeader(String title) {
        this.title = title;
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        if (titlewidth == -1) {
            FontMetrics fontMetrics = g.getFontMetrics();
            titlewidth = SwingUtilities.computeStringWidth(fontMetrics, title);
        }
        int linewidth = (d.width - titlewidth - 8) / 2;
        g.setColor(Color.gray);
        g.drawLine(0, 7, linewidth, 7);
        g.drawLine(linewidth + titlewidth + 8, 7, d.width, 7);
        g.drawString(title, linewidth + 4, 12);
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 25);
    }
}
