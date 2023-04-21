package com.lonelytaste.narafms.gui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import com.lonelytaste.narafms.framework.FrameworkMain;
import com.lonelytaste.narafms.service.api.HelloService;

public class Activator implements BundleActivator {

    static {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("swt-win32-3536");
    }

    private FrameworkMain frameworkMain = null;

    public void start(BundleContext context) throws Exception {
        ServiceReference sref = context.getServiceReference(HelloService.class.getName());
        if (sref != null) {
            HelloService hello = (HelloService) context.getService(sref);
            System.out.println(hello.hello("【gui】"));
        } else {
            System.out.println("HelloService is null");
        }
    }

    public void stop(BundleContext context) throws Exception {
        frameworkMain.closeShell();
    }

    public FrameworkMain getFrameworkMain() {
        return frameworkMain;
    }

    public void setFrameworkMain(FrameworkMain frameworkMain) {
        this.frameworkMain = frameworkMain;
    }
}
