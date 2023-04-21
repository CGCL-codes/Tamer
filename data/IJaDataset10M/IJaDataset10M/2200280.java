package org.personalsmartspace.demoservice.confmgr;

import java.util.Properties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.personalsmartspace.demoservice.confmgr.api.IConfernceManagerService;
import org.personalsmartspace.demoservice.confmgr.impl.ConferanceManagerService;
import com.mysql.jdbc.Driver;

public class Activator implements BundleActivator {

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        Properties props = new Properties();
        props.put("Confservice", "ConfClient");
        context.registerService(IConfernceManagerService.class.getName(), new ConferanceManagerService(), props);
        context.registerService(Driver.class.getName(), new Driver(), props);
        System.out.println("bndlact: " + Driver.class.getName());
    }

    public void stop(BundleContext context) throws Exception {
    }
}
