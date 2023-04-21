package edu.uiuc.itg.virtuallab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;

/**
 * Splash Screen that shows up when the program starts up
 * 
 * @author mflider
 *
 */
class SplashScreen extends JWindow implements Runnable {

    private JFrame myFrame;

    private JProgressBar pb;

    private JPanel startupPanel;

    private Image bg;

    private StartDialog dialog;

    private StartPanel sp;

    private Thread startupThread;

    private boolean shouldRun = true;

    /**
     * 
     */
    public SplashScreen(JFrame aFrame) {
        myFrame = aFrame;
        setSize(455, 435);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(new Point(screenSize.width / 2 - 227, screenSize.height / 2 - 227));
        getContentPane().setLayout(new BorderLayout());
        JPanel jPanel = new JPanel(new BorderLayout());
        getContentPane().add(jPanel);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel startupPanel = new JPanel(gridbag);
        startupPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jPanel.add(startupPanel);
        JLabel ij = new JLabel(new ImageIcon("resources/vsem/startuppage.jpg"));
        c.anchor = GridBagConstraints.NORTH;
        gridbag.setConstraints(ij, c);
        startupPanel.add(ij);
        pb = new JProgressBar();
        c.gridy = 441;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridbag.setConstraints(pb, c);
        startupPanel.add(pb);
    }

    public void start() {
        shouldRun = true;
        if (startupThread == null) {
            startupThread = new Thread(this);
            startupThread.start();
            startupThread.setPriority(startupThread.getPriority() - 3);
        }
    }

    public void run() {
        setVisible(true);
        pb.setIndeterminate(true);
        while (shouldRun) {
            try {
                Thread.sleep(300);
            } catch (Exception eee) {
            }
        }
        bg = Toolkit.getDefaultToolkit().getImage("resources/vsem/bg.jpg");
        dialog = new StartDialog(myFrame, "Virtual SEM Control");
        sp = new StartPanel(myFrame, "Virtual SEM Control");
        setVisible(true);
        dispose();
        sp.setVisible(false);
    }

    public void stop() {
        shouldRun = false;
    }

    private JPanel getInfoPanel() {
        JLabel myLabel;
        JPanel startupMain = new JPanel();
        startupMain.setLayout(new BoxLayout(startupMain, BoxLayout.PAGE_AXIS));
        startupMain.add(new JLabel("<html><center><h1>Virtual Light Microscope (VLM)</h1>" + "<h3>Version 1.0</h3>" + "Developed by:<br>"));
        startupMain.add(new JLabel(new ImageIcon("resources/vsem/itg-logo-t.gif"), JLabel.CENTER));
        startupMain.add(new JLabel("<html><center><h2>Imaging Technology Group</h2>" + "Beckman Institute for Advanced Science and Technology<br>" + "<i>University of Illinois at Urbana-Champaign</i>"));
        return startupMain;
    }
}
