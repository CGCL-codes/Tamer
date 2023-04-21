package org.osmorc.run.managingbundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * OSGi Bundle which is used by the plugin to list information about the container and control the deployment of
 * Bundles.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id$
 */
public class ManagingBundleActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = ManagingBundle.class.getName();
            ManagingBundle implementation = new ManagingBundleImpl(context);
            ManagingBundle stub = (ManagingBundle) UnicastRemoteObject.exportObject(implementation, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
        } catch (Exception e) {
            System.err.println("Problem when trying to register the ManagingBundle:");
            e.printStackTrace();
        }
    }

    public void stop(BundleContext context) throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry();
            registry.unbind(ManagingBundle.class.getName());
        } catch (Exception e) {
            System.err.println("Problem when trying to unregister the ManagingBundle:");
            e.printStackTrace();
        }
    }
}
