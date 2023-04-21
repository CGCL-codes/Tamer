package org.personalsmartspace.psw3p.cui;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.psw3p.servlet.*;
import org.personalsmartspace.psw3p.servlet.ProcessPSWHttpRequest;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IGui;

public class Activator implements BundleActivator {

    private ServiceTracker httpService;

    ClientUIServiceImpl gui;

    ServiceRegistration sreGui;

    public void start(BundleContext bc) throws Exception {
        httpService = new ServiceTracker(bc, HttpService.class.getName(), null);
        httpService.open();
        PSWServletRegistered pswservletreg = new PSWServletRegistered(httpService);
        gui = new ClientUIServiceImpl(bc);
        ProcessPSWHttpRequest.pswMgrService = gui;
        sreGui = bc.registerService(IGui.class.getName(), gui, null);
        pswservletreg.registerPSWServlet();
        System.out.println("PSW HttpServlet Bundle activated");
    }

    public void stop(BundleContext bundleContext) throws Exception {
        httpService.close();
        sreGui.unregister();
        gui.close();
        gui = null;
    }
}
