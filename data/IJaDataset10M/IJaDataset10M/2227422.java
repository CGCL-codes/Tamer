package org.personalsmartspace.lm.mining.c45.impl;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.personalsmartspace.lm.c45.api.platform.IC45Learning;

public class Activator implements BundleActivator {

    IC45Learning c45;

    public void start(BundleContext bc) throws Exception {
        c45 = new C45Learning(bc);
        bc.registerService(IC45Learning.class.getName(), c45, new Hashtable<String, Object>());
    }

    public void stop(BundleContext bundleContext) throws Exception {
        c45 = null;
    }
}
