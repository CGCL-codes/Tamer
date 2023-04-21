package com.testonica.kickelhahn.core.ui.device;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JOptionPane;
import com.testonica.kickelhahn.core.Constant;
import com.testonica.kickelhahn.core.KickelhahnInfo;
import com.testonica.kickelhahn.core.formats.svf.SVFActionVector;
import com.testonica.kickelhahn.core.formats.svf.SVFCommand;
import com.testonica.kickelhahn.core.formats.svf.SVFVector;
import com.testonica.kickelhahn.core.hardware.JTAGLink;
import com.testonica.kickelhahn.core.hardware.JTAGLinkException;
import com.testonica.kickelhahn.core.manager.tap.TAPEvent;
import com.testonica.kickelhahn.core.manager.tap.TAPDeviceLink;
import com.testonica.kickelhahn.core.manager.tap.TapHighLevelEvent;
import com.testonica.kickelhahn.core.manager.tap.TapHighLevelEventException;

/**
 * Class that holds currently
 * connected (disconnected) device
 * its state and parameters
 * 
 * @author Sergei Devadze
 *  */
public class DeviceManager implements TAPDeviceLink {

    /** Current link to the device */
    private JTAGLink link = null;

    private KickelhahnInfo info;

    public DeviceManager(KickelhahnInfo info) {
        this.info = info;
        info.getFrame().addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                closingEvent();
            }
        });
    }

    public boolean isDeviceConnected() {
        return (link != null);
    }

    public boolean isHighLevelSupported() {
        if (link == null) return false;
        return link.isSvfSupported();
    }

    public void showConnectDialog() {
        DeviceConnectDialog dlgConnect = new DeviceConnectDialog(info.getFrame(), info.getPath() + File.separator + Constant.MWS_SERVERS_FILE);
        dlgConnect.showDialog();
        if (!dlgConnect.isOKPressed()) return;
        try {
            link = dlgConnect.createLink();
        } catch (JTAGLinkException e) {
            info.getErrorHandler().error(e);
        }
        info.updateDeviceStatus();
    }

    public boolean reset() {
        if (link != null) return link.reset();
        return true;
    }

    public void disconnect() {
        if (JOptionPane.showConfirmDialog(info.getFrame(), "Disconnect device?", "Confirmation", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        disconnectInternal();
    }

    /**
	 * Sends data from TAPEvent to device
	 * (if it is connected) */
    public boolean[] sendData(TAPEvent e) {
        if (link == null) return null;
        try {
            return sendDataInternal(e);
        } catch (JTAGLinkException x) {
            info.getErrorHandler().error(x);
            return null;
        }
    }

    public boolean[] sendData(TapHighLevelEvent e) throws TapHighLevelEventException {
        if (link == null) return null;
        if (!isHighLevelSupported()) throw new TapHighLevelEventException("High-level commands not supported by device!");
        try {
            return sendDataInternal(e.getCommand());
        } catch (JTAGLinkException x) {
            info.getErrorHandler().error(x);
            throw new TapHighLevelEventException("Cannot send data to JTAG link", x);
        }
    }

    public String getLinkTextInfo() {
        if (link == null) return null; else return link.toString();
    }

    /** Catch the event of closing main window of Trainer */
    private void closingEvent() {
        if (link != null) disconnectInternal();
    }

    private void disconnectInternal() {
        link.close();
        link = null;
        info.updateDeviceStatus();
    }

    private boolean[] sendDataInternal(TAPEvent e) throws JTAGLinkException {
        boolean[] tdo = null;
        if (e.isClocked()) tdo = link.pulse(e.getTMSValues(), e.getTDIValues()); else {
            tdo = new boolean[1];
            tdo[0] = link.set(e.isRising(), e.getTMS(), e.getTDI());
            System.out.println("" + System.currentTimeMillis() + " TDO: " + tdo[0] + " ( " + e + ")");
        }
        return tdo;
    }

    private boolean[] sendDataInternal(SVFCommand c) throws JTAGLinkException {
        SVFVector response = link.svfCommand(c);
        if (!(c instanceof SVFActionVector)) return null;
        if (response == null) return null;
        return response.getBits();
    }
}
