package modelcrawler;

import java.util.HashMap;
import com.agentfactory.platform.core.IAgent;

/**
 * @author Niall
 *
 */
public class AgentList extends HashMap<String, AgentProperties> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6387434334997201253L;

    public AgentList() {
    }

    public int hashCode() {
        return super.hashCode();
    }

    public synchronized AgentProperties getByName(String name) {
        return this.get(name);
    }

    public AgentProperties addNew(AgentProperties ap) {
        super.put(ap.getName(), ap);
        return ap;
    }

    public int getCrawlerCount() {
        int cnt = 0;
        for (AgentProperties ap : super.values()) {
            if (ap.getName().startsWith("crawler")) {
                cnt++;
            }
        }
        return cnt;
    }

    public int getIndexerCount() {
        int cnt = 0;
        for (AgentProperties ap : super.values()) {
            if (ap.getName().startsWith("indexer")) {
                cnt++;
            }
        }
        return cnt;
    }

    public int getKilledCount() {
        int cnt = 0;
        for (AgentProperties ap : super.values()) {
            if (ap.isTerminated()) {
                cnt++;
            }
        }
        return cnt;
    }

    public int getActiveCount() {
        int cnt = 0;
        for (AgentProperties ap : super.values()) {
            if (!ap.isTerminated()) {
                cnt++;
            }
        }
        return cnt;
    }

    public void setTerminated(IAgent a) {
        AgentProperties ap = this.get(a.getName());
        if (ap != null) {
            ap.setTerminated();
        }
    }
}
