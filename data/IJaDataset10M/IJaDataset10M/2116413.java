package lokahi.agent.interfaces;

/**
 * @author Stephen Toback
 * @version $Id: AgentTomcat.java,v 1.1 2006/03/02 19:19:40 drtobes Exp $
 */
public interface AgentTomcat extends MonitoredProcess {

    boolean startContext(String context);

    boolean stopContext(String context);

    boolean reloadContext(String context);

    boolean addContext(String path, String docBase);

    String[] listContexts();

    boolean removeContext(String context);

    String getSessions(String context);

    void setName(String name);

    void setJavaOptions(String options);

    void setEnvironment(String env);

    void setJavaHome(String javaHome);

    void setStartClass(String startClass);

    void setStandardOutLog(String logLocation);

    String getStartCommand();
}
