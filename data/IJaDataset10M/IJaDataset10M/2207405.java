package org.metadon.info;

import javax.microedition.lcdui.Display;
import org.metadon.control.Controller;

/**
 *
 * @author Hannes
 */
public class LibrariesInfoCollector extends InfoCollector {

    /**
     * Creates a new collector.
     */
    public LibrariesInfoCollector() {
        super();
    }

    public void collectInfos(Controller controller, Display display) {
        try {
            Class.forName("javax.microedition.media.control.VideoControl");
            addInfo("MMAPI: ", "yes");
            addInfo("MMAPI-Version: ", System.getProperty("microedition.media.version"));
        } catch (ClassNotFoundException e) {
            addInfo("MMAPI: ", "no");
        }
        try {
            Class.forName("javax.wireless.messaging.Message");
            addInfo("WMAPI 1.1: ", "yes");
            try {
                Class.forName("javax.wireless.messaging.MultipartMessage");
                addInfo("WMAPI 2.0: ", "yes");
            } catch (ClassNotFoundException e) {
                addInfo("WMAPI 2.0: ", "no");
            }
        } catch (ClassNotFoundException e) {
            addInfo("WMAPI 1.1: ", "no");
        }
        try {
            Class.forName("javax.microedition.m3g.Graphics3D");
            addInfo("M3G-API: ", "yes");
        } catch (ClassNotFoundException e) {
            addInfo("M3G-API: ", "no");
        }
        try {
            Class.forName("javax.microedition.pim.PIM");
            addInfo("PIM-API: ", "yes");
        } catch (ClassNotFoundException e) {
            addInfo("PIM-API: ", "no");
        }
        if (fileconnectionAPIAvailable()) {
            addInfo("FileConnection-API: ", "yes");
        } else {
            addInfo("FileConnection-API: ", "no");
        }
        if (bluetoothAPIAvailable()) {
            addInfo("Bluetooth-API: ", "yes");
            try {
                Class.forName("javax.obex.ClientSession");
                addInfo("Bluetooth-Obex-API: ", "yes");
            } catch (ClassNotFoundException e) {
                addInfo("Bluetooth-Obex-API: ", "no");
            }
        } else {
            addInfo("Bluetooth-API: ", "no");
        }
        try {
            Class.forName("javax.microedition.location.Location");
            addInfo("Location-API: ", "yes");
        } catch (ClassNotFoundException e) {
            addInfo("Location-API: ", "no");
        }
        try {
            Class.forName("javax.microedition.xml.rpc.Operation");
            addInfo("WebServices-API: ", "yes");
        } catch (ClassNotFoundException e) {
            addInfo("WebServices-API: ", "no");
        }
        try {
            Class.forName("javax.microedition.sip.SipConnection");
            addInfo("SIP-API: ", "yes");
        } catch (ClassNotFoundException e) {
            addInfo("SIP-API: ", "no");
        }
        try {
            Class.forName("com.nokia.mid.ui.FullCanvas");
            addInfo("Nokia-UI-API: ", "yes");
        } catch (ClassNotFoundException e) {
            addInfo("Nokia-UI-API: ", "no");
        }
        try {
            Class.forName("com.siemens.mp.MIDlet");
            addInfo("Siemens-Extension-API: ", "yes");
            try {
                Class.forName("com.siemens.mp.color_game.GameCanvas");
                addInfo("Siemens-ColorGame-API: ", "yes");
            } catch (ClassNotFoundException e) {
                addInfo("Siemens-ColorGame-API: ", "no");
            }
        } catch (ClassNotFoundException e) {
            addInfo("Siemens-Extension-API: ", "no");
        }
    }

    public boolean bluetoothAPIAvailable() {
        try {
            Class.forName("javax.bluetooth.DiscoveryAgent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean fileconnectionAPIAvailable() {
        try {
            Class.forName("javax.microedition.io.file.FileSystemRegistry");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
