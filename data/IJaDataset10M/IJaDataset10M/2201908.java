package de.outofbounds.kinderactive.gui.learn.time;

import de.outofbounds.kinderactive.gui.Palette;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JPanel;

/**
 *
 * @author root
 */
public class ClockPanel extends JPanel {

    static final Stroke hourHandStroke = new BasicStroke(24, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    static final Stroke minuteHandStroke = new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    static final Stroke secondHandStroke = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    static final Color clockFaceColor = Color.WHITE;

    static final Color hourColor = new Color(192, 64, 64);

    static final Color hourTraceColor = Palette.RED;

    static final Color minutesColor = new Color(64, 192, 64);

    static final Color minutesTraceColor = Palette.GREEN;

    static final Color secondsColor = new Color(255, 255, 0);

    static final Color secondsTraceColor = Palette.YELLOW;

    Thread clockWorker;

    int hour;

    int minutes;

    int seconds;

    int speed;

    /** Creates a new instance of ClockPanel */
    public ClockPanel() {
        speed = 1;
        clockWorker = new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        seconds++;
                        if (seconds == 60) {
                            seconds = 0;
                            minutes++;
                            if (minutes == 60) {
                                minutes = 0;
                                hour = ++hour % 24;
                            }
                        }
                        repaint();
                        Thread.sleep(1000 / speed);
                    }
                } catch (InterruptedException ex) {
                }
            }
        });
        clockWorker.start();
        resetToCurrentTime();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight() - 80;
        int xbase = w / 2;
        int ybase = h / 2 + 80;
        int fontSize = w / 25;
        Font clockFaceFont = new Font("Arial", Font.BOLD, fontSize);
        int r = Math.min(w, h) / 2 - 2 * fontSize;
        double hourAngle = 2 * Math.PI * (double) hour / 12;
        double minuteAngle = 2 * Math.PI * (double) minutes / 60;
        double secondAngle = 2 * Math.PI * (double) seconds / 60;
        double timeAngle = 2 * Math.PI * (double) (hour + (double) minutes / 60) / 12;
        int solx = xbase + (int) ((h / 2 + 40) * Math.sin(timeAngle / 2 - Math.PI) * 1.3);
        int soly = ybase - (int) ((h / 2 + 40) * Math.cos(timeAngle / 2 - Math.PI));
        int lunx = xbase + (int) ((h / 2 + 40) * Math.sin(timeAngle / 2) * 1.4);
        int luny = ybase - (int) ((h / 2 + 40) * Math.cos(timeAngle / 2));
        double f = Math.max(0.0, Math.min(1.0, (0.25 + Math.cos(timeAngle / 2 - Math.PI)) * 1.2));
        Color skyColor = new Color(0, 0, (int) (255 * f));
        g2.setColor(skyColor);
        g2.fillRect(0, 0, w, h / 2 + 80);
        if (5 <= hour && hour <= 18) {
            g2.setColor(Color.YELLOW);
            g2.fillOval(solx - 32, soly - 32, 64, 64);
        }
        if (hour <= 6 || 17 <= hour) {
            g2.setColor(Color.YELLOW);
            g2.fillOval(lunx - 32, luny - 32, 64, 64);
            g2.setColor(skyColor);
            g2.fillOval(lunx - 18, luny - 45, 90, 90);
        }
        g2.setColor(Color.GRAY);
        g2.drawLine(0, ybase, w, ybase);
        g2.fillRect(0, ybase, w, h / 2);
        g2.setColor(clockFaceColor);
        int d = r + fontSize * 12 / 8;
        g.fillOval(xbase - d, ybase - d, 2 * d, 2 * d);
        double stf = 1.06;
        double mtf = 0.94;
        double htf = 0.78;
        g2.setColor(secondsTraceColor);
        g2.fillArc((int) (xbase - r * stf), (int) (ybase - r * stf), (int) (2 * r * stf), (int) (2 * r * stf), 90, -6 * seconds);
        g2.setColor(clockFaceColor);
        g2.fillArc((int) (xbase - r * mtf), (int) (ybase - r * mtf), (int) (2 * r * mtf), (int) (2 * r * mtf), 90, -6 * seconds);
        g2.setColor(minutesTraceColor);
        g2.fillArc((int) (xbase - r * mtf), (int) (ybase - r * mtf), (int) (2 * r * mtf), (int) (2 * r * mtf), 90, -6 * minutes);
        g2.setColor(hourTraceColor);
        g2.fillArc((int) (xbase - r * htf), (int) (ybase - r * htf), (int) (2 * r * htf), (int) (2 * r * htf), 60 - (hour % 12) * 30, 30);
        {
            int dotSize = fontSize / 8;
            g2.setColor(Color.BLACK);
            for (int i = 0; i < 12; ++i) {
                int x = xbase + (int) (r * Math.sin(2 * Math.PI * (i + 1) / 12));
                int y = ybase - (int) (r * Math.cos(2 * Math.PI * (i + 1) / 12));
                g2.fillOval(x - dotSize, y - dotSize, 2 * dotSize, 2 * dotSize);
            }
        }
        {
            int dotSize = fontSize / 16;
            g2.setColor(Color.BLACK);
            for (int i = 0; i < 60; ++i) {
                int x = xbase + (int) (r * Math.sin(2 * Math.PI * (i + 1) / 60));
                int y = ybase - (int) (r * Math.cos(2 * Math.PI * (i + 1) / 60));
                g2.fillOval(x - dotSize, y - dotSize, 2 * dotSize, 2 * dotSize);
            }
        }
        g2.setFont(clockFaceFont);
        for (int i = 0; i < 12; ++i) {
            int x = xbase + (int) ((r + 0.85 * fontSize) * Math.sin(2 * Math.PI * (i + 1) / 12));
            int y = ybase - (int) ((r + 0.85 * fontSize) * Math.cos(2 * Math.PI * (i + 1) / 12));
            g2.drawString(Integer.toString(i + 1), x - (i < 9 ? fontSize * 19 / 64 : fontSize * 4 / 7), y + fontSize * 5 / 12);
        }
        g2.fillOval(xbase - 20, ybase - 20, 40, 40);
        g2.setStroke(hourHandStroke);
        int hx = xbase + (int) ((r * 0.7) * Math.sin(timeAngle));
        int hy = ybase - (int) ((r * 0.7) * Math.cos(timeAngle));
        g2.setColor(hourColor);
        g2.drawLine(xbase, ybase, hx, hy);
        g2.setStroke(minuteHandStroke);
        int mx = xbase + (int) ((r * 1.1) * Math.sin(minuteAngle));
        int my = ybase - (int) ((r * 1.1) * Math.cos(minuteAngle));
        g2.setColor(minutesColor);
        g2.drawLine(xbase, ybase, mx, my);
        g2.setStroke(secondHandStroke);
        int sx = xbase + (int) ((r * 1.1) * Math.sin(secondAngle));
        int sy = ybase - (int) ((r * 1.1) * Math.cos(secondAngle));
        g2.setColor(secondsColor);
        g2.drawLine(xbase, ybase, sx, sy);
    }

    protected int normalizedHour(int hour) {
        return (hour - 1) % 24 + 1;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void adjustHour(int offset) {
        hour = normalizedHour(hour + 24 + offset);
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void adjustMinutes(int offset) {
        minutes += offset;
        if (minutes < 0) {
            minutes = minutes + 60;
            adjustHour(-1);
        }
        if (minutes >= 60) {
            minutes = minutes % 60;
            adjustHour(1);
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void adjustSeconds(int offset) {
    }

    public void resetToCurrentTime() {
        Calendar cal = new GregorianCalendar();
        hour = normalizedHour(cal.get(Calendar.HOUR_OF_DAY));
        minutes = cal.get(Calendar.MINUTE);
        seconds = cal.get(Calendar.SECOND);
        repaint();
    }
}
