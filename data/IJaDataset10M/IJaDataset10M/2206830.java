package com.sun.spot.spotworld.gui;

import com.sun.spot.spotworld.SpotWorld;
import com.sun.spot.spotworld.participants.SquawkHost;
import com.sun.spot.spotworld.virtualobjects.IVirtualObject;
import com.sun.spot.util.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author randy
 */
public class StatusBar extends JMenuBar {

    private RadioIndicator radio;

    private SpotWorld world;

    public StatusBar() {
        init();
    }

    public void init() {
        radio = new RadioIndicator();
        add(Box.createHorizontalGlue());
        add(createSPOTCounter());
        add(Box.createHorizontalStrut(20));
        add(radio);
        radio.setLabel();
        add(Box.createHorizontalStrut(20));
    }

    public JLabel createSPOTCounter() {
        final JLabel lbl = new JLabel(" SPOT Count: 0");
        Runnable r = new Runnable() {

            public void run() {
                while (true) {
                    int count = 0;
                    if (world != null) {
                        for (IVirtualObject vo : world.getVirtualObjectsCopy()) {
                            if (vo instanceof SquawkHost) count++;
                        }
                    }
                    lbl.setText(" SPOT Count: " + count);
                    lbl.repaint();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        (new Thread(r)).start();
        lbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return lbl;
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 85);
    }

    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, 85);
    }

    public SpotWorld getWorld() {
        return world;
    }

    public void setWorld(SpotWorld world) {
        this.world = world;
    }

    public RadioIndicator getRadioIndicator() {
        return radio;
    }

    public class RadioIndicator extends JPanel implements IRadioIndicator {

        private int radius = 12;

        private JLabel label;

        private Color[] colors = new Color[5];

        private Color color1B = Color.YELLOW;

        private Color color2B = Color.ORANGE;

        private Color color1N = Color.LIGHT_GRAY;

        private Color color2N = Color.GRAY;

        private int busyCount = 0;

        public RadioIndicator() {
            init();
        }

        public void init() {
            colors[0] = color1N;
            colors[1] = color2N;
            colors[2] = color1N;
            colors[3] = color2N;
            colors[4] = color1N;
            setAlignmentX(Component.RIGHT_ALIGNMENT);
        }

        public void setLabel() {
            label = new JLabel("basestation");
            int lblW = (int) label.getUI().getPreferredSize(label).getWidth();
            int lblH = (int) label.getUI().getPreferredSize(label).getHeight();
            label.setBounds((getPreferredSize().width - lblW) / 2, (getPreferredSize().height - lblH) / 2, lblW, lblH);
        }

        public void showAsBusy() {
            busyCount++;
            if (busyCount > 1) return;
            final Runnable r = new Runnable() {

                public void run() {
                    while (busyCount > 0) {
                        synchronized (this) {
                            colors[0] = color1B;
                            colors[1] = color2B;
                            colors[2] = color1B;
                            colors[3] = color2B;
                            colors[4] = color1B;
                        }
                        paintImmediately(0, 0, getWidth(), getHeight());
                        repaint();
                        Utils.sleep(200);
                        synchronized (this) {
                            colors[0] = color2B;
                            colors[1] = color1B;
                            colors[2] = color2B;
                            colors[3] = color1B;
                            colors[4] = color2B;
                        }
                        paintImmediately(0, 0, getWidth(), getHeight());
                        repaint();
                        Utils.sleep(200);
                    }
                    colors[0] = color1N;
                    colors[1] = color2N;
                    colors[2] = color1N;
                    colors[3] = color2N;
                    colors[4] = color1N;
                    paintImmediately(0, 0, getWidth(), getHeight());
                    repaint();
                }
            };
            (new Thread(r)).start();
        }

        public void showAsNoLongerBusy() {
            busyCount--;
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            return new Dimension(radius * (2 * colors.length + 1) / 2 + 14, radius * 2 + 14);
        }

        public void paintComponent(Graphics g) {
            synchronized (this) {
                int xEdge = 0;
                int yC = getHeight() / 2;
                for (int i = 0; i < colors.length; i++) {
                    g.setColor(colors[i]);
                    g.fillOval(xEdge, yC - radius, 2 * radius, 2 * radius);
                    g.fillOval(getWidth() - xEdge - 2 * radius, yC - radius, 2 * radius, 2 * radius);
                    xEdge += radius / 2;
                }
                label.setForeground(busyCount > 0 ? Color.BLACK : Color.WHITE);
                g.translate(label.getX(), label.getY());
                label.paint(g);
            }
        }
    }
}
