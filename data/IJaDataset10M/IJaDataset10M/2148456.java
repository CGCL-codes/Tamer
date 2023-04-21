package net.sf.jftp.gui.hostchooser;

import net.sf.jftp.*;
import net.sf.jftp.config.*;
import net.sf.jftp.gui.framework.*;
import net.sf.jftp.gui.tasks.ExternalDisplayer;
import net.sf.jftp.net.*;
import net.sf.jftp.system.logging.Log;
import net.sf.jftp.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class NfsHostChooser extends HFrame implements ActionListener, WindowListener {

    public static HTextField host = new HTextField("URL:", "nfs://localhost:v2m/tmp", 20);

    public static HTextField user = new HTextField("Username:", "<anonymous>", 15);

    public static HPasswordField pass = new HPasswordField("Password:", "nopasswd");

    public static HButton info = new HButton("Read me!");

    private HPanel okP = new HPanel();

    private HButton ok = new HButton("Connect");

    private ComponentListener listener = null;

    private boolean useLocal = false;

    public NfsHostChooser(ComponentListener l, boolean local) {
        listener = l;
        useLocal = local;
        init();
    }

    public NfsHostChooser(ComponentListener l) {
        listener = l;
        init();
    }

    public NfsHostChooser() {
        init();
    }

    public void init() {
        setLocation(100, 150);
        setTitle("NFS Connection...");
        setBackground(okP.getBackground());
        getContentPane().setLayout(new GridLayout(4, 2, 5, 3));
        JPanel p = new JPanel();
        p.add(info);
        try {
            File f = new File(Settings.appHomeDir);
            f.mkdir();
            File f1 = new File(Settings.login);
            f1.createNewFile();
            File f2 = new File(Settings.login_def_nfs);
            f2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        LoadSet l = new LoadSet();
        String[] login = l.loadSet(Settings.login_def_nfs);
        if ((login[0] != null) && (login.length > 1)) {
            host.setText(login[0]);
            user.setText(login[1]);
        }
        if (Settings.getStorePasswords()) {
            if ((login[0] != null) && (login.length > 2) && (login[2] != null)) {
                pass.setText(login[2]);
            }
        } else {
            pass.setText("");
        }
        getContentPane().add(host);
        getContentPane().add(p);
        getContentPane().add(user);
        getContentPane().add(pass);
        getContentPane().add(new JLabel(""));
        getContentPane().add(okP);
        okP.add(ok);
        ok.addActionListener(this);
        info.addActionListener(this);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        pass.text.addActionListener(this);
        pack();
        setModal(false);
        setVisible(false);
        addWindowListener(this);
    }

    public void update() {
        fixLocation();
        setVisible(true);
        toFront();
        host.requestFocus();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == info) {
            java.net.URL url = ClassLoader.getSystemResource(Settings.nfsinfo);
            if (url == null) {
                url = HImage.class.getResource("/" + Settings.nfsinfo);
            }
            ExternalDisplayer d = new ExternalDisplayer(url);
        } else if ((e.getSource() == ok) || (e.getSource() == pass.text)) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            NfsConnection con = null;
            String htmp = host.getText().trim();
            String utmp = user.getText().trim();
            String ptmp = pass.getText();
            int potmp = 0;
            String userName = user.text.getText();
            try {
                boolean status;
                status = StartConnection.startCon("NFS", htmp, userName, ptmp, potmp, "", useLocal);
            } catch (Exception ex) {
                Log.debug("Could not create NfsConnection!");
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.dispose();
            JFtp.mainFrame.setVisible(true);
            JFtp.mainFrame.toFront();
            if (listener != null) {
                listener.componentResized(new ComponentEvent(this, 0));
            }
        }
    }

    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public Insets getInsets() {
        Insets std = super.getInsets();
        return new Insets(std.top + 10, std.left + 10, std.bottom + 10, std.right + 10);
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
        }
    }
}
