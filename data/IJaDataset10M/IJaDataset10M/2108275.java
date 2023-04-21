package org.personalsmartspace.pm.prefmodel.api.pss3p;

import java.util.List;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

/**
 * @author Elizabeth
 *
 */
public interface IActionConsumer {

    /**
	 * This method is used by the Proactivity subsystem to send actions to be implemented by the services. 
	 * @param dpi	The Digital Identity of the user currently using the service
	 * @param obj	The IAction object to be implemented
	 * @return		The service should return true if the action was implemented successfully or false if not. 
	 */
    public boolean setIAction(IDigitalPersonalIdentifier dpi, IAction obj);

    /**
	 * This method is used by the Proactivity Subsystem to locate the right instance of a IActionConsumer by filtering them based on their service identifier
	 * @return 		The service has to return its own service identifier
	 */
    public IServiceIdentifier getServiceIdentifier();

    /**
	 * If the service has registered itself with a service type, it should return this using this method.
	 * @return		The service's type 
	 * 
	 */
    public String getServiceType();

    /**
	 * If the service fits under more than one service type category, it can return a list of service types using this method. 
	 * 
	 * @return 		A list of service types in String format
	 */
    public List<String> getServiceTypes();
}
