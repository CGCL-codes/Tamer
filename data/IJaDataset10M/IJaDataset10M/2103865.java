package whiteboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.agentfactory.platform.core.IAgent;
import com.agentfactory.platform.impl.AbstractPlatformService;

public class WhiteboardService extends AbstractPlatformService {

    public static final String NAME = "af.std.wb";

    private Map<String, String> information;

    public WhiteboardService() {
        information = new HashMap<String, String>();
    }

    public synchronized void putInformation(IAgent agent, String info) {
        information.put(agent.getName(), info);
        setChanged();
        notifyObservers();
    }

    public synchronized String removeInformation(IAgent agent) {
        String s = (String) information.remove(agent.getName());
        setChanged();
        notifyObservers();
        return s;
    }

    public synchronized List<String> getInformation() {
        List<String> list = new ArrayList<String>();
        for (String key : information.keySet()) {
            list.add("information(" + key + "," + information.get(key) + ")");
        }
        return list;
    }
}
