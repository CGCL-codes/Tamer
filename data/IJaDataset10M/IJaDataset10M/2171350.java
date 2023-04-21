package org.jbug.mcsample.examples.lifecycle;

import org.jboss.dependency.spi.ControllerContext;

/**
 * @author gaston.scapusio
 *
 */
public class LifecycleCallback {

    /**
	 * Called when a bean is being installed.
	 * @param ctx
	 */
    public void install(ControllerContext ctx) {
        System.out.println("Bean " + ctx.getName() + " is being installed. State: " + ctx.getState().getStateString());
    }

    /**
	 * Called when a bean is being uninstalled.
	 * @param ctx
	 */
    public void uninstall(ControllerContext ctx) {
        System.out.println("Bean " + ctx.getName() + " is being uninstalled. State: " + ctx.getState().getStateString());
    }
}
