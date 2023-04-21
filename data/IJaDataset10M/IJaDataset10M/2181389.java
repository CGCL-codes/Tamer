package org.dbe.servicies.position.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.dbe.servent.NoSuchServiceException;
import org.dbe.servent.ServerServentException;
import org.dbe.servicies.position.PositionAgregator;
import org.sun.dbe.ClientHelper;

public class AggregattionFrame extends JFrame {

    int x0 = 0;

    int x1 = 0;

    int x2 = 0;

    int x3 = 0;

    int x4 = 0;

    int x5 = 0;

    ClientHelper ch;

    public AggregattionFrame() {
        initComponents();
        try {
            ch = new ClientHelper(new URL("http://localhost:2728"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        JPanel panel = new MyPanel(this);
        panel.setBounds(1, 1, 100, 100);
        this.getContentPane().add(panel);
        this.setVisible(true);
        this.repaint();
    }

    public void getPositions() {
        try {
            PositionAgregator agregator;
            agregator = (PositionAgregator) ch.getProxy(PositionAgregator.class, new String[] { "agregator" });
            if (agregator == null) {
                System.out.println(" not found");
                System.exit(0);
            }
            Collection positions = agregator.getPositions();
            if (positions != null) {
                for (Iterator it = positions.iterator(); it.hasNext(); ) {
                    Point p = (Point) it.next();
                    if (p.x == 0) {
                        x0 = p.y;
                    } else if (p.x == 1) {
                        x1 = p.y;
                    } else if (p.x == 2) {
                        x2 = p.y;
                    } else if (p.x == 3) {
                        x3 = p.y;
                    } else if (p.x == 4) {
                        x4 = p.y;
                    } else {
                        x5 = p.y;
                    }
                }
            }
            repaint();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NoSuchServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] s) {
        AggregattionFrame frame = new AggregattionFrame();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.getPositions();
        }
    }

    public class MyPanel extends JPanel {

        AggregattionFrame f;

        public MyPanel(AggregattionFrame f) {
            this.f = f;
        }

        /**
		 * 
		 */
        private static final long serialVersionUID = 4070377097823649033L;

        public void paint(Graphics g) {
            System.out.println("paint");
            g.setColor(Color.RED);
            g.drawLine(0, 20, x0 * 3, 20);
            g.drawLine(0, 21, x0 * 3, 21);
            g.setColor(Color.BLUE);
            g.drawLine(0, 40, x1 * 3, 40);
            g.drawLine(0, 41, x1 * 3, 41);
            g.setColor(Color.GREEN);
            g.drawLine(0, 80, x2 * 3, 80);
            g.drawLine(0, 81, x2 * 3, 81);
            g.setColor(Color.YELLOW);
            g.drawLine(0, 100, x3 * 3, 100);
            g.drawLine(0, 101, x3 * 3, 101);
            g.setColor(Color.WHITE);
            g.drawLine(0, 120, x4 * 3, 120);
            g.drawLine(0, 121, x4 * 3, 121);
            g.setColor(Color.BLACK);
            g.drawLine(0, 140, x5 * 3, 140);
            g.drawLine(0, 141, x5 * 3, 141);
        }
    }
}
