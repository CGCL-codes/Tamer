package com.dbxml.db.admin.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * Splash
 */
public final class Splash extends JWindow implements Runnable {

    private Thread thread = new Thread(this);

    JLabel lblImage = new JLabel();

    ImageIcon imgImage;

    public Splash() {
        this(null);
    }

    public Splash(Frame frame) {
        super(frame);
        try {
            jbInit();
            pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = getSize();
            if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
            setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        imgImage = new ImageIcon(Splash.class.getResource("splash.jpg"));
        this.getContentPane().add(lblImage);
        lblImage.setIcon(imgImage);
    }

    public synchronized void run() {
        this.setVisible(true);
        try {
            wait();
        } catch (InterruptedException e) {
        }
        this.setVisible(false);
    }

    public synchronized void done() {
        notify();
    }
}
