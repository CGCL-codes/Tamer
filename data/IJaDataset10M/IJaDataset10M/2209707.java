package hotair.support.perceptor;

import com.agentfactory.logic.agent.Perceptor;
import hotair.support.module.ProviderInformation;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author  Administrator
 */
public class ProviderStatus extends Perceptor {

    public void perceive() {
        ProviderInformation information = (ProviderInformation) getModuleByName("providerDetails");
        Map<String, Integer> providers = information.getProviders();
        synchronized (providers) {
            String agentID = null;
            int num = -1;
            Iterator it = providers.keySet().iterator();
            while (it.hasNext()) {
                agentID = (String) it.next();
                num = providers.get(agentID);
                adoptBelief("BELIEF(provider(" + agentID + "," + num + "))");
            }
        }
    }
}
