package org.epics.apps.pvimage;

import java.awt.*;
import javax.swing.JFrame;
import gov.aps.jca.CAException;
import gov.aps.jca.TimeoutException;
import gov.aps.jca.CAStatus;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.DBR;
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.dbr.STRING;
import gov.aps.jca.event.GetEvent;
import gov.aps.jca.event.GetListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;

public class Main extends JFrame {

    DrawingCanvas m_drawingPanel;

    /** JCA context. */
    Context m_context = null;

    public Main(String title) {
        super(title);
        m_drawingPanel = new DrawingCanvas();
    }

    /**
     * Initialize JCA context.
     * @throws CAException	throws on any failure.
     */
    void createContext() throws CAException, TimeoutException {
        JCALibrary jca = JCALibrary.getInstance();
        m_context = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
        System.out.println(m_context.getVersion().getVersionString());
        m_context.printInfo();
        System.out.println();
    }

    void createChannel(String pvName) throws CAException, TimeoutException {
        m_drawingPanel.createChannel(m_context, pvName);
    }

    void createImageBuffer() throws CAException, TimeoutException {
        m_drawingPanel.createImageBuffer();
    }

    void createMonitor() throws CAException, TimeoutException {
        m_drawingPanel.createMonitor();
    }

    void setPanel(int width, int height) {
        m_drawingPanel.setPreferredSize(new Dimension(width, height));
        getContentPane().add(m_drawingPanel, BorderLayout.CENTER);
        pack();
    }

    /** Destroy JCA context. */
    private void destroy() {
        try {
            if (m_context != null) m_context.destroy();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int wPanel = 1024;
        int hPanel = 768;
        String pvName = "13SIM1:image1:PvImage";
        Main app = new Main("PvImage Application");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            app.createContext();
        } catch (Throwable th) {
            th.printStackTrace();
            app.destroy();
            System.exit(0);
        }
        app.setPanel(wPanel, hPanel);
        try {
            app.createChannel(pvName);
        } catch (Throwable th) {
            th.printStackTrace();
            app.destroy();
            System.exit(0);
        }
        try {
            app.createImageBuffer();
        } catch (Throwable th) {
            th.printStackTrace();
            app.destroy();
            System.exit(0);
        }
        try {
            app.createMonitor();
        } catch (Throwable th) {
            th.printStackTrace();
            app.destroy();
            System.exit(0);
        }
        app.setVisible(true);
    }
}
