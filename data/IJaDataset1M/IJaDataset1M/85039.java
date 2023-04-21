package net.sourceforge.x360mediaserve.database.hibernate;

import java.util.Properties;
import net.sourceforge.x360mediaserve.api.database.MediaDatabase;
import net.sourceforge.x360mediaserve.api.services.ConfigService;
import net.sourceforge.x360mediaserve.database.backends.hibernate.HibernateDB;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigServiceTrackerCustomiser implements ServiceTrackerCustomizer {

    Logger logger = LoggerFactory.getLogger(ConfigServiceTrackerCustomiser.class);

    BundleContext context;

    private HibernateDB db;

    boolean started;

    public ConfigServiceTrackerCustomiser(BundleContext context) {
        super();
        started = false;
        this.context = context;
    }

    public void startEverything(ConfigService configService) {
        if (started) {
            logger.error("Already started data manager!");
        } else {
            logger.info("Starting dataManager");
            configService.getStorageDir();
            db = new HibernateDB(configService.getStorageDir());
            context.registerService(MediaDatabase.class.getName(), db, new Properties());
        }
    }

    public Object addingService(ServiceReference reference) {
        ConfigService configService = (ConfigService) context.getService(reference);
        startEverything(configService);
        return configService;
    }

    public void modifiedService(ServiceReference reference, Object service) {
    }

    public void removedService(ServiceReference reference, Object service) {
    }

    public void shutDown() {
        if (db != null) {
            db.shutdown();
        }
    }
}
