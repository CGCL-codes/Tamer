package org.knopflerfish.bundle.trayicon;

import org.osgi.framework.*;
import com.jeans.trayicon.*;
import java.awt.event.*;
import org.knopflerfish.service.log.LogRef;
import org.knopflerfish.service.trayicon.*;

public class Activator implements BundleActivator {

    public static BundleContext bc;

    public static LogRef log;

    static TrayIconManager manager;

    public void start(BundleContext bc) {
        this.bc = bc;
        this.log = new LogRef(bc);
        manager = new TrayIconManager();
        manager.open();
    }

    public void stop(BundleContext bc) {
        try {
            manager.close();
            manager = null;
            this.log = null;
            this.bc = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
