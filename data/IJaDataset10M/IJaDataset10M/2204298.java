package risk.ui.gui.initialGUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import risk.resources.Resources;

public class SplashPanel extends javax.swing.JPanel {

    private Image loadScreen;

    private javax.swing.JLabel statusLabel;

    public SplashPanel() {
        initComponents();
        initOwn();
    }

    private void initOwn() {
        MediaTracker mTracker = new MediaTracker(this);
        loadScreen = Toolkit.getDefaultToolkit().getImage(Resources.class.getResource("graphic/load_small.jpg"));
        mTracker.addImage(loadScreen, 0);
        try {
            mTracker.waitForID(0);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        mTracker = null;
    }

    private void initComponents() {
        javax.swing.JLabel statusLabel;
        statusLabel = new javax.swing.JLabel();
        setLayout(null);
        statusLabel.setForeground(java.awt.Color.white);
        statusLabel.setText(Resources.getMessage("status.load"));
        add(statusLabel);
        statusLabel.setBounds(50, 250, 54, 16);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loadScreen, 0, 0, this);
    }

    public javax.swing.JLabel getStatusLabel() {
        return statusLabel;
    }
}
