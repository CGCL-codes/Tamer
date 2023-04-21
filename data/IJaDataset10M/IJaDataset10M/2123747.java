package eu.ict.persist.Advertisement.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.personalsmartspace.pm.prefmodel.api.pss3p.IActionConsumer;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IService;
import eu.ict.persist.Advertisement.api.AdvertisementAPI;

public class Activator implements BundleActivator {

    AdvertisementAPI advertisement;

    @Override
    public void start(BundleContext bc) throws Exception {
        advertisement = new AdvertisementIMPL(bc);
        String[] interfaces = { AdvertisementAPI.class.getName(), IService.class.getName(), IActionConsumer.class.getName() };
        bc.registerService(interfaces, advertisement, null);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        advertisement = null;
    }
}
