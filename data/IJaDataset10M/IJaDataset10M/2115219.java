package aurora.events;

import aurora.service.IService;

/**
 * fires before load service config
 * Parameter: IService (ServiceInstance)
 */
public interface E_PrepareServiceConfig {

    public static final String EVENT_NAME = "PrepareServiceConfig";

    public int onPrepareServiceConfig(IService service) throws Exception;
}
