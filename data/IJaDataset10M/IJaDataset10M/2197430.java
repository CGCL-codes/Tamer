package lokahi.agent.callable.tomcat;

import lokahi.agent.interfaces.AgentTomcat;
import java.util.concurrent.Callable;

/**
 * @author Stephen Toback
 * @version $Id: RestartTomcatCallable.java,v 1.1 2006/03/02 19:19:39 drtobes Exp $
 */
public class RestartTomcatCallable implements Callable<Boolean> {

    private AgentTomcat tc;

    private int flowLevel;

    public RestartTomcatCallable(AgentTomcat tc, int flowLevel) {
        this.tc = tc;
        this.flowLevel = flowLevel;
    }

    public Boolean call() throws Exception {
        boolean ret = false;
        Callable<Boolean> c = new StopTomcatCallable(tc, flowLevel);
        if (c.call()) {
            c = new StartTomcatCallable(tc);
            ret = c.call();
        }
        return ret;
    }
}
