package mx4j.examples.services.loading;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.ServiceNotFoundException;
import javax.management.loading.MLet;

/**
 * The starter class for loading MBeans via an MLET file. <br>
 * Modify at your wish.
 *
 * @version $Revision: 1.3 $
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        MLet mlet = new MLet();
        ObjectName mletName = new ObjectName("system:mbean=loader");
        server.registerMBean(mlet, mletName);
        Thread.currentThread().setContextClassLoader(mlet);
        URL mbeansURL = null;
        if (args.length == 1) {
            String file = args[0];
            mbeansURL = new File(file).toURL();
        } else {
            mbeansURL = mlet.getResource("examples/services/loading/mbeans.mlet");
        }
        if (mbeansURL == null) throw new ServiceNotFoundException("Could not find MBeans to load");
        Set mbeans = mlet.getMBeansFromURL(mbeansURL);
        System.out.println("MLet has now the following classpath: " + Arrays.asList(mlet.getURLs()));
        checkMBeansLoadedSuccessfully(mbeans);
        initializeMBeans(server, mbeans);
        startMBeans(server, mbeans);
        System.out.println("System up and running !");
    }

    private static void checkMBeansLoadedSuccessfully(Set mbeans) throws ServiceNotFoundException {
        boolean allLoaded = true;
        for (Iterator i = mbeans.iterator(); i.hasNext(); ) {
            Object mbean = i.next();
            if (mbean instanceof Throwable) {
                ((Throwable) mbean).printStackTrace();
                allLoaded = false;
            } else {
                System.out.println("Registered MBean: " + mbean);
            }
        }
        if (!allLoaded) throw new ServiceNotFoundException("Some MBean could not be loaded");
    }

    private static void initializeMBeans(MBeanServer server, Set mbeans) {
        for (Iterator i = mbeans.iterator(); i.hasNext(); ) {
            try {
                ObjectInstance instance = (ObjectInstance) i.next();
                if (server.isInstanceOf(instance.getObjectName(), "org.apache.avalon.framework.activity.Initializable")) {
                    try {
                        server.invoke(instance.getObjectName(), "initialize", null, null);
                    } catch (ReflectionException ignored) {
                    }
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    private static void startMBeans(MBeanServer server, Set mbeans) {
        for (Iterator i = mbeans.iterator(); i.hasNext(); ) {
            try {
                ObjectInstance instance = (ObjectInstance) i.next();
                if (server.isInstanceOf(instance.getObjectName(), "org.apache.avalon.framework.activity.Startable")) {
                    try {
                        server.invoke(instance.getObjectName(), "start", null, null);
                    } catch (ReflectionException ignored) {
                    }
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }
}
