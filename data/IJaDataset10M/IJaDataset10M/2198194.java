package com.panopset.sf;

import static com.panopset.Util.log;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import javax.swing.JMenuBar;
import com.panopset.Alert;
import com.panopset.UpdateListener;
import com.panopset.Zombie;
import com.panopset.centralen.util.MathUtil;
import com.panopset.java.Version;

/**
 * Message Menubar provides drawing capability in the right hand portion of the
 * menubar that would otherwise be wasted space.
 *
 * @author Karl Dinwiddie.
 */
public final class MessageMenuBar extends JMenuBar {

    /**
     * 90.
     */
    private static final int MEMORY_ALERT_AT_90 = 90;

    /**
     * Half second refresh rate should do it.
     */
    private static final int REFRESH_RATE = 500;

    /**
     * Constructor.
     */
    public MessageMenuBar() {
        super();
        new Thread("painter") {

            @Override
            public void run() {
                while (Zombie.isActive()) {
                    if (MessageMenuBar.this.isShowing()) {
                        repaint();
                    }
                    try {
                        Thread.sleep(REFRESH_RATE);
                    } catch (InterruptedException e) {
                        log(e);
                    }
                }
            }
        }.start();
        new Thread("caps") {

            private boolean caps;

            @Override
            public void run() {
                while (Zombie.isActive() && capsLockCheckAvailable) {
                    boolean newCaps = isCapsLocked();
                    if (newCaps != caps) {
                        repaint();
                    }
                    caps = newCaps;
                    try {
                        Thread.sleep(REFRESH_RATE);
                    } catch (InterruptedException e) {
                        log(e);
                    }
                }
            }
        }.start();
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                Alert.clrmsg();
                repaint();
            }
        });
        Alert.addUpdateListener(new UpdateListener() {

            @Override
            public void update() {
                repaint();
            }
        });
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        if (com.panopset.Strings.isPopulated(Alert.getMessage())) {
            paintMessage(g);
        } else {
            paintMemory(g);
        }
    }

    /**
     * Paint available memory.
     *
     * @param g
     *            Graphics.
     */
    private void paintMemory(final Graphics g) {
        long fm = Runtime.getRuntime().freeMemory();
        long tm = Runtime.getRuntime().totalMemory();
        long mm = Runtime.getRuntime().maxMemory();
        long um = tm - fm;
        MathUtil.Percentage percentage = MathUtil.percent(um, mm, 2);
        boolean alert = percentage.getValue().compareTo(new BigDecimal(MEMORY_ALERT_AT_90)) > 0;
        g.setColor(Color.GRAY);
        if (alert) {
            g.setColor(Color.RED);
        }
        int w = (getSize().width - getLocation().x) / 2;
        int x = getLocation().x + w;
        int y = getLocation().y + 1;
        int h = getSize().height - 2;
        g.fillRect(x, y, w, h);
        g.setColor(Color.LIGHT_GRAY);
        if (alert) {
            g.setColor(Color.BLACK);
        }
        g.fillRect(x + 1, y + 1, w - 2, h - 2);
        g.setFont(ComponentFactory.FM.getPlainArial());
        g.setColor(Color.DARK_GRAY);
        if (alert) {
            g.setColor(Color.RED);
        }
        g.drawString("Memory: " + um + " of " + mm + ", " + percentage + "% used.", w + TEXT_OFFSET_X, h + TEXT_OFFSET_Y);
        drawCaps(g, h);
    }

    /**
     *
     * @param g
     *            Graphics.
     * @param h
     *            Height.
     */
    private void drawCaps(final Graphics g, final int h) {
        if (isCapsLocked()) {
            g.setColor(Color.ORANGE);
            g.drawString("Caps", getSize().width - (getLocation().x + CL_OFFSET), h + TEXT_OFFSET_Y);
        }
    }

    /**
     * Caps loc offset.
     */
    private static final int CL_OFFSET = 40;

    /**
     * Caps lock check available flag.
     */
    private static boolean capsLockCheckAvailable = true;

    /**
     * @return false if caps lock state not available.
     */
    static boolean isCapsLocked() {
        try {
            return java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK);
        } catch (Exception ex) {
            capsLockCheckAvailable = false;
        }
        return false;
    }

    /**
     * Paint message.
     *
     * TODO Use flywheel graphics.
     *
     * @param g
     *            Graphics.
     */
    private void paintMessage(final Graphics g) {
        g.setColor(Color.GRAY);
        int w = (getSize().width - getLocation().x) / 2;
        int x = getLocation().x + w;
        int y = getLocation().y + 1;
        int h = getSize().height - 2;
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.fillRect(x + 1, y + 1, w - 2, h - 2);
        g.setFont(ComponentFactory.FM.getPlainArial());
        g.setColor(Alert.getColor());
        g.drawString(Alert.getMessage(), w + MathUtil.THREE, h - MathUtil.FOUR);
        drawCaps(g, h);
    }

    /**
     * X text offset is 3.
     */
    private static final int TEXT_OFFSET_X = 3;

    /**
     * Y text offset -4.
     */
    private static final int TEXT_OFFSET_Y = -4;

    /**
     * Serial Version ID.
     */
    private static final long serialVersionUID = Version.UNI_VERSION;
}
