package org.fao.geonet.notifier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import jeeves.server.context.ServiceContext;
import jeeves.server.resources.ResourceManager;
import jeeves.utils.Log;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;

public class MetadataNotifierControl {

    private ServiceContext srvContext;

    private GeonetContext gc;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MetadataNotifierControl(ServiceContext srvContext, GeonetContext gc) {
        this.srvContext = srvContext;
        this.gc = gc;
    }

    public void runOnce() throws Exception {
        Log.debug(Geonet.DATA_MANAGER, "MetadataNotifierControl runOnce start");
        ResourceManager rm = srvContext.getResourceManager();
        Log.debug(Geonet.DATA_MANAGER, "getUnregisteredMetadata after dbms");
        final MetadataNotifierTask updateTask = new MetadataNotifierTask(rm, gc);
        @SuppressWarnings("unused") final ScheduledFuture<?> updateTaskHandle = scheduler.schedule(updateTask, 20, TimeUnit.SECONDS);
        Log.debug(Geonet.DATA_MANAGER, "MetadataNotifierControl runOnce finish");
    }

    public void shutDown() throws Exception {
        scheduler.shutdown();
    }
}
