package es.usc.citius.servando.android.logging;

import java.util.HashMap;
import org.apache.log4j.Logger;
import es.usc.citius.servando.android.models.services.IPlatformService;

/**
 * This class allows any component to obtain a {@link Logger} configured for writting to the platform logs file.
 * 
 * @author Ángel Piñeiro
 */
public class ServandoLogger {

    /**
	 * Map of loggers, one for each service
	 */
    private static HashMap<String, Logger> serviceLoggers = new HashMap<String, Logger>();

    /**
	 * Retrieve a logger for an especific service. The idea is that each service can get its logger at initialization
	 * 
	 * @param service The service who requests a logger
	 * @return An instance of {@link Logger}
	 */
    public static Logger getServiceLogger(IPlatformService service) {
        if (!serviceLoggers.containsKey(service.getId())) {
            serviceLoggers.put(service.getId(), Logger.getLogger(service.getClass()));
        }
        return serviceLoggers.get(service.getId());
    }

    /**
	 * Retrieve a logger for an especific class.
	 * 
	 * @return An instance of {@link Logger}
	 */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz);
    }
}
