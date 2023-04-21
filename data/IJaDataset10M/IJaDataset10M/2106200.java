package furbelow;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.Timer;

public class SpinningDialWaitIndicator extends WaitIndicator implements ActionListener {

    private static final int MAX_SIZE = 64;

    private static final int FADE_INTERVAL = 1000 / 24;

    private static final int FADE_THRESHOLD = 192;

    private static final int MARGIN = 8;

    private static final int MARGIN_FRACTION = 8;

    private Timer timer;

    private int fade;

    private int verticalOffset;

    private SpinningDial dial;

    private String text;

    public SpinningDialWaitIndicator(JFrame frame) {
        this(frame.getLayeredPane());
        JMenuBar mb = frame.getJMenuBar();
        if (mb != null) {
            verticalOffset = mb.getHeight();
        }
    }

    public SpinningDialWaitIndicator(JComponent target) {
        this(target, null);
    }

    public SpinningDialWaitIndicator(final JComponent target, String text) {
        super(target);
        this.text = text;
        dial = new SpinningDial() {

            public int getIconWidth() {
                Rectangle r = getComponent().getVisibleRect();
                int margin = Math.min(MARGIN, r.width / MARGIN_FRACTION);
                return Math.min(MAX_SIZE, r.width - margin * 2);
            }

            public int getIconHeight() {
                Rectangle r = getComponent().getVisibleRect();
                int margin = Math.min(MARGIN, r.height / MARGIN_FRACTION);
                return Math.min(MAX_SIZE, r.height - verticalOffset - margin * 2);
            }
        };
        dial.setFrameInterval(0);
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    /** Fade the affected component to background, then apply a spinning
     * wait indicator.
     */
    public void paint(Graphics graphics) {
        if (timer == null) {
            timer = new Timer(FADE_INTERVAL, this);
            timer.start();
        }
        Graphics2D g = (Graphics2D) graphics.create();
        Rectangle r = getComponent().getVisibleRect();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Color bg = getComponent().getBackground();
        g.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), fade));
        g.fillRect(r.x, r.y, r.width, r.height);
        if (fade < FADE_THRESHOLD) return;
        int x = r.x;
        if (text == null) x += (r.width - dial.getIconWidth()) / 2; else x += dial.getIconWidth() / 4;
        int y = r.y + verticalOffset + (r.height - verticalOffset - dial.getIconHeight()) / 2;
        dial.paintIcon(getPainter(), g, x, y);
        if (text != null) {
            Font font = g.getFont();
            g.setFont(font.deriveFont(Font.BOLD, dial.getIconHeight() / 2));
            FontMetrics m = g.getFontMetrics();
            x += dial.getIconWidth() * 5 / 4;
            y += dial.getIconHeight() - (dial.getIconHeight() - m.getAscent()) / 2;
            g.setColor(getComponent().getForeground());
            g.drawString(text, x, y);
        }
        g.dispose();
    }

    /** Remove the wait decoration. */
    public void dispose() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        super.dispose();
    }

    /** First fade the background, then spin the dial. */
    public void actionPerformed(ActionEvent e) {
        if (fade < FADE_THRESHOLD) {
            fade += 32;
            if (fade >= FADE_THRESHOLD) timer.setDelay(SpinningDial.SPIN_INTERVAL);
        } else {
            dial.nextFrame();
        }
        getPainter().repaint();
    }
}
