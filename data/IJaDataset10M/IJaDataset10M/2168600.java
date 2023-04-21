package hotair.support.actuator;

import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;
import hotair.support.module.ProviderInformation;

/**
 * Assign myself to a provider
 *
 * @author  daithi
 * @version $Id$
 */
public class AssignToProvider extends Actuator {

    public boolean act(FOS action) {
        ProviderInformation information = (ProviderInformation) getModuleByName("providerDetails");
        String agentID = information.assignToProvider();
        if (agentID != null) {
            adoptBelief("BELIEF(assignment(" + agentID + "))");
        }
        return true;
    }
}
