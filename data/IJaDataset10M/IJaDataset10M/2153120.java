package ants.p2p.gui;

import ants.p2p.query.*;
import javax.swing.*;
import java.awt.event.*;
import org.apache.log4j.*;

public class HttpPopupMenu extends JPopupMenu {

    HttpAntPanel callerHttp;

    HttpServerInfo server;

    static Logger _logger = Logger.getLogger(HttpPopupMenu.class.getName());

    JMenuItem jMenuItem1 = new JMenuItem();

    JMenuItem jMenuItem2 = new JMenuItem();

    JMenuItem jMenuItem3 = new JMenuItem();

    public HttpPopupMenu(HttpAntPanel callerHttp, HttpServerInfo server) {
        super();
        this.callerHttp = callerHttp;
        this.server = server;
        try {
            jbInit();
        } catch (Exception e) {
            _logger.error("", e);
        }
    }

    private void jbInit() throws Exception {
        jMenuItem1.setText(ji.JI.i("Activate Proxy"));
        jMenuItem1.addMouseListener(new HttpPopupMenu_jMenuItem1_mouseAdapter(this));
        jMenuItem2.setText(ji.JI.i("Shutdown Proxy"));
        jMenuItem2.addMouseListener(new HttpPopupMenu_jMenuItem2_mouseAdapter(this));
        jMenuItem3.setText(ji.JI.i("Open Browser"));
        jMenuItem3.addMouseListener(new HttpPopupMenu_jMenuItem3_mouseAdapter(this));
        if (this.callerHttp.container.cap.warriorAnt.getCurrentProxy() == null || !this.callerHttp.container.cap.warriorAnt.getCurrentProxy().getNodeID().equals(server.getOwnerId())) this.add(jMenuItem1); else {
            this.add(jMenuItem2);
            this.add(jMenuItem3);
        }
    }

    void jMenuItem1_mousePressed(MouseEvent e) {
        try {
            ants.p2p.security.EndpointSecurityManager esm = this.callerHttp.container.cap.warriorAnt.getOutputSecureConnectionManager(server.getOwnerId());
            if (esm == null) {
                this.callerHttp.container.cap.warriorAnt.scheduleHttpProxy(server);
                this.callerHttp.container.cap.warriorAnt.createSecureConnection(server.getOwnerId(), false);
            } else {
                this.callerHttp.container.cap.warriorAnt.createProxy(esm);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.callerHttp, ji.JI.i("Cannot set") + " " + server.getOwnerId() + " " + ji.JI.i("as active proxy."), ji.JI.i("Error in setting Proxy"), JOptionPane.ERROR_MESSAGE);
            _logger.error("Cannot set " + server.getOwnerId() + " as active proxy. Reason: " + ex.getMessage());
        }
    }

    void jMenuItem2_mousePressed(MouseEvent e) {
        this.callerHttp.container.cap.warriorAnt.resetCurrentProxy();
    }

    void jMenuItem3_mousePressed(MouseEvent e) {
        String[] args = new String[1];
        try {
            args[0] = "http://127.0.0.1:" + this.callerHttp.container.cap.warriorAnt.getCurrentProxy().getPort();
            if (server.getHomePage().charAt(0) == '/') args[0] += server.getHomePage(); else args[0] += "/" + server.getHomePage();
            System.out.println(args[0]);
            Runtime.getRuntime().exec(HttpAntPanel.browserPath + " " + args[0]);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.callerHttp, ji.JI.i("Not found") + ": " + HttpAntPanel.browserPath + "\n" + ji.JI.i("Please set your browser path in settings") + "...", ji.JI.i("Error in starting Browser"), JOptionPane.ERROR_MESSAGE);
            _logger.error("Error in starting: " + HttpAntPanel.browserPath + " " + args[0], ex);
        }
    }
}

class HttpPopupMenu_jMenuItem1_mouseAdapter extends java.awt.event.MouseAdapter {

    HttpPopupMenu adaptee;

    HttpPopupMenu_jMenuItem1_mouseAdapter(HttpPopupMenu adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        final MouseEvent event = e;
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                adaptee.jMenuItem1_mousePressed(event);
                return null;
            }
        };
        worker.start();
    }
}

class HttpPopupMenu_jMenuItem2_mouseAdapter extends java.awt.event.MouseAdapter {

    HttpPopupMenu adaptee;

    HttpPopupMenu_jMenuItem2_mouseAdapter(HttpPopupMenu adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        final MouseEvent event = e;
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                adaptee.jMenuItem2_mousePressed(event);
                return null;
            }
        };
        worker.start();
    }
}

class HttpPopupMenu_jMenuItem3_mouseAdapter extends java.awt.event.MouseAdapter {

    HttpPopupMenu adaptee;

    HttpPopupMenu_jMenuItem3_mouseAdapter(HttpPopupMenu adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        final MouseEvent event = e;
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                adaptee.jMenuItem3_mousePressed(event);
                return null;
            }
        };
        worker.start();
    }
}
