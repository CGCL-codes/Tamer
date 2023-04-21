package com.antilia.demo.picviewer.osgi;

import org.osgi.framework.BundleContext;
import com.antilia.common.osgi.Aggregator;
import com.antilia.common.osgi.IServiceActivator;
import com.antilia.common.osgi.TypedServiceTracker;

/**
 * 
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class PicturesServiceTracker extends Aggregator<IPicturesSource> implements IServiceActivator {

    private TypedServiceTracker<IPicturesService> tracker;

    public PicturesServiceTracker() {
        super();
    }

    public boolean isMandatory() throws Exception {
        return true;
    }

    public void start(BundleContext context) throws Exception {
        tracker = new TypedServiceTracker<IPicturesService>(context, IPicturesService.class) {

            @Override
            public void onAddingService(IPicturesService service) {
                for (IPicturesSource source : elements()) {
                    service.addPicturesSource(source);
                }
            }

            @Override
            public void onServiceRemoved(IPicturesService service) {
                for (IPicturesSource source : elements()) {
                    service.removePicturesSource(source);
                }
            }
        };
        tracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        tracker.close();
        tracker = null;
    }
}
