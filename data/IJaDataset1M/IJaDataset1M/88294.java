package lokahi.agent.callable.tomcat;

import lokahi.agent.interfaces.AgentTomcat;
import lokahi.agent.util.ProcessFactory;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Stephen Toback
 * @version $Id: StartTomcatCallable.java,v 1.1 2006/03/02 19:19:39 drtobes Exp $
 */
public class StartTomcatCallable implements Callable<Boolean> {

    private AgentTomcat tc;

    public StartTomcatCallable(AgentTomcat tc) {
        this.tc = tc;
    }

    public Boolean call() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(tc.getStartCommand());
        Map<String, String> env = pb.environment();
        env.clear();
        env.putAll(tc.getEnv());
        pb.directory(new File("/"));
        Process p = pb.start();
        ProcessFactory.addProcess(tc, p);
        return true;
    }
}
