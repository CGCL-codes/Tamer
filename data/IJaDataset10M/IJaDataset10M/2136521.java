package net.sf.uibuilder.gadget;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * DigitalClockLabel is a digital clock gadget.
 *
 * @version   1.0 2003-3-4
 * @author    <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public class DigitalClockLabel extends JComponent implements ActionListener {

    private static DigitalClockLabel _clock = null;

    private static final int DELAY = 1000;

    private final Timer _timer = new Timer(DELAY, this);

    private int _width, _height;

    private SimpleDateFormat _formatter;

    private TimeZone _timeZone = TimeZone.getDefault();

    /**
     * Constructor function
     */
    private DigitalClockLabel() {
        _formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        _formatter.setTimeZone(_timeZone);
    }

    /**
     * The the instance of the clock.
     */
    public static DigitalClockLabel getInstance() {
        if (_clock == null) {
            _clock = new DigitalClockLabel();
            _clock.start();
        }
        return _clock;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        _timer.start();
    }

    /**
     * Advances animation to next frame.  Called by timer.
     */
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * Paints the current frame.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_width == 0) {
            _width = getWidth();
            _height = getHeight();
        }
        Graphics2D g2 = (Graphics2D) g;
        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, _width, _height);
            g2.setColor(getForeground());
        }
        Date date = new Date();
        String dateString = _formatter.format(date);
        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics metrics = g2.getFontMetrics();
        int w = metrics.stringWidth(dateString);
        int h = metrics.getHeight();
        g2.drawString(dateString, _width / 2 - w / 2, _height - 7);
    }
}
