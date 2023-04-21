package se.l4.crayon.services;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.l4.crayon.CrayonModule;
import se.l4.crayon.annotation.Contribution;
import se.l4.crayon.annotation.Order;
import se.l4.crayon.services.internal.ServiceManagerImpl;

/**
 * Module configuration for services. Binds {@link ServiceManager} to its
 * default implementation.
 * 
 * @author Andreas Holstenson
 *
 */
public class ServicesModule extends CrayonModule {

    @Override
    public void configure() {
        bind(ServiceManager.class).to(ServiceManagerImpl.class);
    }

    @Contribution(name = "services")
    @Order("last")
    public void startServices(ServiceManager manager) {
        manager.startAll();
        Collection<ServiceInfo> info = manager.getInfo();
        if (info.isEmpty()) {
            return;
        }
        Logger logger = LoggerFactory.getLogger(ServiceManager.class);
        logger.info("Service status:");
        for (ServiceInfo i : info) {
            logger.info(i.toString());
        }
    }
}
