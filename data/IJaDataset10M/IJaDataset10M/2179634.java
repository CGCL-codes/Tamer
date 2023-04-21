package hr.drezga.diplomski.hostbundle;

import java.util.Enumeration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        System.out.println("Got resource : ");
        System.out.println(bundleContext.getBundle().getResource("/config/test.txt"));
        System.out.println("Found bundle files : ");
        Enumeration e = bundleContext.getBundle().findEntries("/config", "*.txt", true);
        while (e != null && e.hasMoreElements()) System.out.println(e.nextElement());
        IGreeter greeter = (IGreeter) bundleContext.getBundle().loadClass("hr.drezga.diplomski.fragmentbundle.TestClass").newInstance();
        greeter.sayHello();
    }

    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }
}
