package de.iritgo.openmetix.interfacing.gagingsystemdriver;

import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.interfacing.InterfacingManager;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.link.InterfaceDriver;

/**
 * Gaging system drivers are executed by gaging system driver threads.
 *
 * @version $Id: GagingSystemDriverThread.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class GagingSystemDriverThread extends Thread {

    /** The driver we are running. */
    GagingSystemDriver driver;

    /** The communication interface driver. */
    InterfaceDriver iface;

    /**
	 * Create a new driver thread.
	 *
	 * @param system Gaging system for which to start a driver thread.
	 */
    public GagingSystemDriverThread(GagingSystem system) {
        GagingSystemDriverDescriptor descriptor = GagingSystemDriverDescriptor.get(system.getDriverId());
        InterfacingManager interfaceManager = (InterfacingManager) Engine.getManager("InterfacingManager");
        iface = interfaceManager.getInterfaceDriver(system.getInterfaceId());
        try {
            driver = (GagingSystemDriver) descriptor.getClassLoader().loadClass(descriptor.getClassName()).newInstance();
            driver.setGagingSystem(system);
            driver.setInterfaceDriver(iface);
        } catch (ClassNotFoundException x) {
            Log.logError("server", "GagingSystemDriverThread", "Unable to start driver thread for driver '" + descriptor.getId() + "' (" + x + ")");
        } catch (InstantiationException x) {
            Log.logError("server", "GagingSystemDriverThread", "Unable to start driver thread for driver '" + descriptor.getId() + "' (" + x + ")");
        } catch (IllegalAccessException x) {
            Log.logError("server", "GagingSystemDriverThread", "Unable to start driver thread for driver '" + descriptor.getId() + "' (" + x + ")");
        }
    }

    /**
	 * Terminate the drivers processing loop.
	 */
    public void terminate() {
        driver.terminate();
        interrupt();
        for (int timeout = 50; isAlive() && timeout > 0; --timeout) {
            try {
                sleep(100);
            } catch (InterruptedException x) {
            }
        }
        if (iface != null) {
            iface.close();
        }
    }

    /**
	 * Start the driver processing loop.
	 */
    public void run() {
        if (driver != null) {
            if (iface != null) {
                iface.open();
            }
            driver.run();
        }
    }
}
