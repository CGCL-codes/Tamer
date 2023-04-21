package org.argouml.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Swing component to monitor Java heap usage.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class HeapMonitor extends JComponent implements ActionListener {

    private static final int ORANGE_THRESHOLD = 70;

    private static final int RED_THRESHOLD = 90;

    private static final Color GREEN = new Color(0, 255, 0);

    private static final Color ORANGE = new Color(255, 190, 125);

    private static final Color RED = new Color(255, 70, 70);

    private static final long M = 1024 * 1024;

    private long free;

    private long total;

    private long max;

    private long used;

    /**
     * Construct a graphical JVM heap monitor component.
     */
    public HeapMonitor() {
        super();
        Dimension size = new Dimension(200, 20);
        setPreferredSize(size);
        updateStats();
        Timer timer = new Timer(1000, this);
        timer.start();
    }

    public void paint(Graphics g) {
        Rectangle bounds = getBounds();
        int usedX = (int) (used * bounds.width / total);
        int warnX = ORANGE_THRESHOLD * bounds.width / 100;
        int dangerX = RED_THRESHOLD * bounds.width / 100;
        Color savedColor = g.getColor();
        g.setColor(getBackground().darker());
        g.fillRect(0, 0, Math.min(usedX, warnX), bounds.height);
        g.setColor(ORANGE);
        g.fillRect(warnX, 0, Math.min(usedX - warnX, dangerX - warnX), bounds.height);
        g.setColor(RED);
        g.fillRect(dangerX, 0, Math.min(usedX - dangerX, bounds.width - dangerX), bounds.height);
        g.setColor(getForeground());
        String s = MessageFormat.format("{0}M used of {1}M total", new Object[] { (long) (used / M), (long) (total / M) });
        int x = (bounds.width - g.getFontMetrics().stringWidth(s)) / 2;
        int y = (bounds.height + g.getFontMetrics().getHeight()) / 2;
        g.drawString(s, x, y);
        g.setColor(savedColor);
    }

    public void actionPerformed(ActionEvent e) {
        updateStats();
        repaint();
    }

    private void updateStats() {
        free = Runtime.getRuntime().freeMemory();
        total = Runtime.getRuntime().totalMemory();
        max = Runtime.getRuntime().maxMemory();
        used = total - free;
        String tip = MessageFormat.format("Heap use: {0}%  {1}M used of {2}M total.  Max: {3}M", new Object[] { used * 100 / total, (long) (used / M), (long) (total / M), (long) (max / M) });
        setToolTipText(tip);
    }
}
