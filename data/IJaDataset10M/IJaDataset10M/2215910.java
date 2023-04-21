package eu.ict.persist.RFID.client.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import eu.ict.persist.RFID.client.api.RFIDClientAPI;

public class Activator implements BundleActivator {

    RFIDGUI gui;

    public void start(BundleContext bc) throws Exception {
        gui = new RFIDGUI(bc);
        String[] interfaces = { RFIDGUI.class.getName(), RFIDClientAPI.class.getName() };
        bc.registerService(interfaces, gui, null);
    }

    public void stop(BundleContext arg0) throws Exception {
        gui.close();
        gui = null;
    }

    public int setState(String x) {
        return 0;
    }

    public String getState() {
        return "a string";
    }
}
