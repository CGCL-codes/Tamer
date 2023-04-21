package agentcrawler;

import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

/**
 * @author Andrey
 *
 */
public class MonitorCrawlConfirmed extends Actuator {

    @Override
    public boolean act(FOS arg0) {
        System.out.println("MonitorCrawlConfirmed Actuator triggered for Agent: " + agent.getName());
        AgentCrawlerService acs = (AgentCrawlerService) this.getService(AgentCrawlerService.NAME);
        if (acs == null) {
            return false;
        }
        FOS fosAgent = arg0.argAt(0);
        FOS fosUrl = arg0.argAt(1);
        if (fosAgent != null && fosUrl != null) {
            acs.crawlCompleted(fosAgent.toString(), fosUrl.toString(), agent.getIterations(), agent.getInbox().size());
        }
        return true;
    }
}
