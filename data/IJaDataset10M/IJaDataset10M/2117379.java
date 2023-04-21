package com.monad.homerun.svrd.impl;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import com.monad.homerun.config.ConfigService;
import com.monad.homerun.log.LogService;
import com.monad.homerun.svrd.SvrdService;

/**
 * OSGi Activator class for service registration and discovery bundle
 */
public class Activator implements BundleActivator {

    static LogService logSvc;

    static ConfigService cfgSvc;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        ServiceReference svcRef = context.getServiceReference(LogService.class.getName());
        logSvc = (LogService) context.getService(svcRef);
        svcRef = context.getServiceReference(ConfigService.class.getName());
        cfgSvc = (ConfigService) context.getService(svcRef);
        SvrdService svSvc = new ZeroconfManager();
        context.registerService(SvrdService.class.getName(), svSvc, new Hashtable());
    }

    public void stop(BundleContext context) throws Exception {
    }
}
