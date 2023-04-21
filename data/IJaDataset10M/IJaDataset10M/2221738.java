package infrastructureAPI;

import java.util.List;

/**
 * Description: 
 * A ServiceHandler is able to maintain and store a
 * set of services provided by the cloud. 
 * Service(s) can be registered or deRegistered from the ServiceHandler.
 * After the starting of a ServiceHandler it's still 
 * possible to add or remove services from the running handler.
 * Each system may provide it's own particular procedure to
 * register and deregister a service. 
 * The developer has to investigate how that procedures works 
 * in each system.
 *
 */
public interface ServiceHandler {

    /**
	 * Register the input Service 's' in the current 
	 * ServiceHandler.
	 * Returns true if none error occurs.
	 * @param s : Service
	 * @return done : boolean
	 */
    public boolean registerService(Service s);

    /**
	 * DeRegiset (removes) the input Service 's'
	 * from the current ServiceHandler.
	 * Returns true if none error occurs.
	 * @param s
	 * @return done : boolean
	 */
    public boolean deRegisterService(Service s);

    /**
	 * Returns a List<Service> list of the actual registered
	 * Service(s) in the current ServiceHandler.
	 * @return
	 */
    public List<Service> getRegisteredServices();
}
