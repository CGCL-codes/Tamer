package jppl.core.agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jppl.JpplException;
import jppl.console.ConsoleResourceMan;
import jppl.core.CoreFactory;
import jppl.core.impl.store.BasicStoreImpl;
import jppl.core.store.BasicStore;

/**
 *
 * @author rolf
 */
public class ResourceAgent extends Agent {

    private static Map<Class, List<Agent>> agentMap;

    private static BasicStore centralStore;

    private static CoreFactory coreFactory;

    private static ConsoleResourceMan consoleFactory;

    static {
        agentMap = new HashMap<Class, List<Agent>>();
        centralStore = new BasicStoreImpl();
    }

    public static Agent requestAgent(Class requestedAgent) throws JpplException {
        Agent compatibleAgent = getCompatibleAgent(requestedAgent);
        if (compatibleAgent == null) return CoreFactory.newAgent(requestedAgent);
        throw new JpplException("Was not able to get a compatible agent");
    }

    public static BasicStore requestStore() {
        return centralStore;
    }

    public static CoreFactory requestCoreFactory() {
        if (ResourceAgent.coreFactory == null) {
            ResourceAgent.coreFactory = new CoreFactory();
        }
        return ResourceAgent.coreFactory;
    }

    private static Agent getCompatibleAgent(Class requestedAgent) {
        if (agentMap.containsKey(requestedAgent)) if (agentMap.get(requestedAgent).size() != 0) return agentMap.get(requestedAgent).get(0);
        return null;
    }
}
