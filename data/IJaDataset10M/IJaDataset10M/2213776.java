package com.imaginaryday.util;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
import org.jini.rio.resources.client.JiniClient;
import java.rmi.RemoteException;

/**
 * @author rbowers
 *         Date: Nov 11, 2006
 *         Time: 10:02:01 PM
 */
public class ServiceFinder extends JiniClient {

    public ServiceFinder() throws Exception {
        super();
    }

    public JavaSpace getSpace() {
        long startTime = System.currentTimeMillis();
        ServiceTemplate template = new ServiceTemplate(null, new Class[] { JavaSpace.class }, null);
        while (System.currentTimeMillis() - startTime < 60000) {
            for (ServiceRegistrar r : this.getRegistrars()) {
                JavaSpace js = null;
                System.err.println("Looking for space");
                try {
                    js = (JavaSpace) r.lookup(template);
                    System.err.println("Found space");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (js != null) return js;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (getRegistrars().length == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public TransactionManager getTransactionManager() {
        long startTime = System.currentTimeMillis();
        ServiceTemplate template = new ServiceTemplate(null, new Class[] { TransactionManager.class }, null);
        while (System.currentTimeMillis() - startTime < 60000) {
            for (ServiceRegistrar r : this.getRegistrars()) {
                System.err.println("Looking for TM");
                TransactionManager tm = null;
                try {
                    tm = (TransactionManager) r.lookup(template);
                    System.err.println("Found TM");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (tm != null) return tm;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (getRegistrars().length == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
