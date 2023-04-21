package org.vastenhouw.util;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import org.vastenhouw.util.Debug;

public class Splash extends JWindow implements KeyListener, MouseListener, ActionListener {

    protected JPanel mainPan = null;

    protected JProgressBar progress = null;

    protected BoundedRangeModel progressModel = null;

    public Splash(String title, String imageName, BoundedRangeModel progressModel) {
        super();
        this.progressModel = progressModel;
        Image img = null;
        try {
            img = ImageToolkit.getImageResource(imageName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Dimension sz = new Dimension(480, 360);
        JLabel lbl = new JLabel(title);
        if (img != null) {
            this.prepareImage(img, this);
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            try {
                tracker.waitForAll();
            } catch (InterruptedException ex) {
                if (Debug.INFO) Debug.out.println("Splash: media tracker interrupted!\n" + "   " + ex.getMessage());
            }
            sz.width = img.getWidth(this);
            sz.height = img.getHeight(this);
            Icon icon = new ImageIcon(img);
            lbl = new JLabel(icon);
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - sz.width) / 2;
        int y = (screen.height - sz.height) / 3;
        this.setBounds(x, y, sz.width, sz.height);
        Container content = this.getContentPane();
        this.mainPan = new JPanel();
        this.mainPan.setLayout(new BorderLayout());
        content.setLayout(new BorderLayout());
        content.add(BorderLayout.CENTER, this.mainPan);
        this.mainPan.add(BorderLayout.CENTER, lbl);
        lbl.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(5, 5, 5, 5))));
        lbl.setLayout(new BorderLayout());
        if (progressModel != null) {
            this.progress = new JProgressBar(progressModel);
            Dimension d = this.progress.getPreferredSize();
            d.setSize(d.width, 8);
            this.progress.setPreferredSize(d);
            this.progress.setBorderPainted(false);
            lbl.add(BorderLayout.NORTH, this.progress);
        }
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
            }
        });
    }

    public synchronized void enableDismissEvents() {
    }

    public void keyTyped(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
    }

    public void keyPressed(KeyEvent event) {
        this.dispose();
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseClicked(MouseEvent event) {
        this.dispose();
    }

    public void actionPerformed(ActionEvent event) {
        if (this.progressModel != null) {
            this.progressModel.setValue(this.progressModel.getMaximum());
            this.progress.repaint();
        }
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ex) {
        }
        this.dispose();
    }

    public static void main(String[] args) {
        DefaultBoundedRangeModel model = new DefaultBoundedRangeModel(0, 0, 0, 100);
        Splash splash = new Splash("TestSplash", "/org/vastenhouw/jphotar/resources/images/splash.jpg", model);
        splash.setVisible(true);
        splash.requestFocus();
        splash.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        (splash.new Progressor(splash, model)).start();
    }

    private class Progressor extends Thread {

        Splash splash;

        BoundedRangeModel model;

        public Progressor(Splash s, BoundedRangeModel m) {
            super("Model");
            this.splash = s;
            this.model = m;
        }

        public void run() {
            try {
                this.sleep(100);
            } catch (InterruptedException ex) {
            }
            for (; this.model.getValue() < this.model.getMaximum(); ) {
                this.model.setValue(this.model.getValue() + 10);
                try {
                    this.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            this.model.setValue(this.model.getMaximum());
            this.splash.enableDismissEvents();
            try {
                this.sleep(8000);
            } catch (InterruptedException ex) {
            }
            this.splash.dispose();
        }
    }
}
