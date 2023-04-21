package org.personalsmartspace.sre.agi.impl;

import java.util.Properties;
import java.lang.Thread;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.personalsmartspace.sre.agi.api.platform.IAdminGUIFrame;

public class Activator implements BundleActivator {

    ServiceRegistration sre;

    public void start(BundleContext bc) throws Exception {
        Properties props = new Properties();
        props.put("componentName", "Admin GUI Mainframe");
        props.put("providedBy", "SETCCE");
        props.put("category", "GUI");
        final AdminFrame agi = new AdminFrame();
        sre = bc.registerService(IAdminGUIFrame.class.getName(), agi, props);
        System.out.println("The Service " + agi + " should now be registered!");
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                agi.setVisible(true);
            }
        });
    }

    public void stop(BundleContext arg0) throws Exception {
        sre.unregister();
    }
}
