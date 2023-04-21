package org.cybergarage.tv;

import org.osgi.framework.*;

public class Activator implements BundleActivator {

    private static BundleContext bc = null;

    TvFrame tv;

    public void start(BundleContext bc) throws Exception {
        Activator.bc = bc;
        tv = new TvFrame(bc);
        tv.start();
    }

    public void stop(BundleContext bc) throws Exception {
        Activator.bc = null;
        if (tv != null) {
            tv.windowClosing(null);
            tv.setVisible(false);
            tv = null;
        }
    }
}
