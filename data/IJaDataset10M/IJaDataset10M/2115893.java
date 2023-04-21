package ants.p2p.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import kerjodando.casper.tools.Tray;
import ants.p2p.security.*;
import ants.p2p.utils.addresses.*;
import ants.p2p.utils.indexer.*;
import ants.p2p.filesharing.*;
import org.apache.log4j.*;

public class FrameAnt extends JFrame {

    static FrameAnt instance;

    GuiAnt ga;

    public static int WIDTH = 600;

    public static int HEIGHT = 600;

    static boolean isReady = false;

    public static String logFileName = WarriorAnt.workingPath + "Log.properties";

    static {
        File log = new File(FrameAnt.logFileName);
        if (log.exists()) {
            PropertyConfigurator.configure(FrameAnt.logFileName);
        } else {
            BasicConfigurator.configure();
        }
        Logger.getRootLogger().setLevel(Level.DEBUG);
        FrameAnt.checkWorkingPath();
    }

    static Logger _logger = Logger.getLogger(FrameAnt.class.getName());

    FrameAnt(String title) {
        super(title);
        instance = this;
        try {
            jbInit();
            isReady = true;
        } catch (Exception e) {
            _logger.error("", e);
        }
    }

    public GuiAnt getGuiAnt() {
        return ga;
    }

    public static FrameAnt getInstance(String title) {
        if (instance == null) {
            return instance = new FrameAnt(title);
        } else {
            return instance;
        }
    }

    public static boolean isReady() {
        return isReady;
    }

    public static void checkWorkingPath() {
        File cpf = new File(WarriorAnt.workingPath);
        if (cpf.exists() && !cpf.isDirectory()) {
            cpf.delete();
            cpf.mkdir();
        } else if (!cpf.exists()) {
            cpf.mkdir();
        }
    }

    public static void main(String args[]) {
        _logger.info("Running VM: " + System.getProperty("java.version"));
        _logger.info("Running DIR: " + System.getProperty("java.home"));
        Logger.getRootLogger().setLevel(Level.INFO);
        try {
            _logger.info("Checking cipher..............");
            Cipher cipher = Cipher.getInstance(EndpointSecurityManager.cipher);
            _logger.info("Cipher OK");
            _logger.info("Checking key size............");
            byte[] key = new byte[EndpointSecurityManager.cipherKeySize];
            SecretKeySpec secKey = new SecretKeySpec(key, EndpointSecurityManager.cipher);
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            _logger.info("Key size OK");
        } catch (Exception e) {
            _logger.error("Some features needed by ANts are not supported by current VM", e);
        }
        _logger.info("Loading static settings......");
        SettingsAntPanel.loadStaticSettings();
        ji.JI.setCurrentLanguage(SettingsAntPanel.currentLanguage);
        FrameAnt fa = FrameAnt.getInstance("Kerjodando - version " + WarriorAnt.getVersion() + " - Protocol: " + WarriorAnt.getProtocolVersion());
        _logger.info("Showing window...");
        Tray.setupTray(fa);
        Tray.hiddenFrame();
        if (!Tray.isSupported) fa.setVisible(true);
        fa.validateTree();
    }

    public FrameAnt() {
        try {
            jbInit();
            isReady = true;
        } catch (Exception e) {
            _logger.error("", e);
        }
    }

    private void jbInit() throws Exception {
        this.setResizable(true);
        this.addComponentListener(new FrameAnt_this_componentAdapter(this));
        this.addWindowListener(new FrameAnt_this_windowAdapter(this));
        ga = new GuiAnt();
        this.getContentPane().add(ga);
        Image ico = null;
        if (this.ga.animationPanel != null) {
            ico = this.ga.animationPanel.getIcon();
        }
        if (ico != null) {
            this.setIcon(ico);
        }
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowIconified(java.awt.event.WindowEvent e) {
                Tray.hiddenFrame();
            }
        });
        setBounds(100, 100, 650, 550);
        if (this.ga.sap.autoStartANts()) {
            _logger.info("Autostart enabled: running ANts");
            final SwingWorker worker = new SwingWorker() {

                public Object construct() {
                    ga.cap.startANts();
                    return null;
                }
            };
            worker.start();
        }
    }

    public void setIcon(Image ico) {
        if (ico != null) {
            try {
                this.setIconImage(ico);
            } catch (Exception e) {
            }
        }
    }

    public void onClose(boolean bCalledByTray) {
        JFrame waitingFrame = null;
        if (!bCalledByTray) {
            int confirm = JOptionPane.showConfirmDialog(this, ji.JI.i("Do you really want to exit?"), ji.JI.i("Really?"), JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
                return;
            }
            waitingFrame = this.showWorkingFrame();
        }
        if (this.ga.cap.warriorAnt != null) {
            this.ga.cap.warriorAnt.disconnectWarrior();
        }
        if (this.ga.cap.connectionManager != null) {
            this.ga.cap.connectionManager.stop();
            this.ga.cap.connectionManager = null;
        }
        if (BackgroundEngine.getInstance() != null) {
            BackgroundEngine.getInstance().setPriority(Thread.NORM_PRIORITY);
            BackgroundEngine.getInstance().terminate(true);
        }
        System.exit(0);
    }

    void this_componentResized(ComponentEvent e) {
        if (this.ga != null && this.getContentPane() != null) {
            this.ga.setPreferredSize(this.getContentPane().getSize());
            this.ga.setSize(this.getContentPane().getSize());
        }
    }

    private JFrame showWorkingFrame() {
        final JFrame connectionDialog = new JFrame("ANts");
        connectionDialog.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        connectionDialog.getContentPane().add(new JLabel("ANts is cleaning up. Please wait..."));
        JButton confirmConnection = new JButton("Force Exit");
        confirmConnection.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        connectionDialog.getContentPane().add(confirmConnection);
        connectionDialog.pack();
        connectionDialog.setLocation(300, 300);
        connectionDialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        connectionDialog.setVisible(true);
        return connectionDialog;
    }
}

class FrameAnt_this_windowAdapter extends java.awt.event.WindowAdapter {

    FrameAnt adaptee;

    FrameAnt_this_windowAdapter(FrameAnt adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosing(WindowEvent e) {
        final WindowEvent event = e;
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                adaptee.onClose(false);
                return null;
            }
        };
        worker.start();
    }
}

class FrameAnt_this_componentAdapter extends java.awt.event.ComponentAdapter {

    FrameAnt adaptee;

    FrameAnt_this_componentAdapter(FrameAnt adaptee) {
        this.adaptee = adaptee;
    }

    public void componentResized(ComponentEvent e) {
        adaptee.this_componentResized(e);
    }
}
