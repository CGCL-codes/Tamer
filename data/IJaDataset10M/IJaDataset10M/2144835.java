package agentcrawler;

import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

public class MonitorSaveIndexedInfo extends Actuator {

    @Override
    public boolean act(FOS arg0) {
        System.out.println("CrawlerFinished Actuator triggered for Agent: " + agent.getName());
        AgentCrawlerService acs = (AgentCrawlerService) this.getService(AgentCrawlerService.NAME);
        if (acs == null) {
            return false;
        }
        String agentName = arg0.argAt(0).toString();
        if (agentName == null) {
            return false;
        }
        acs.removeWorkingCrawler(agentName);
        return true;
    }
}
