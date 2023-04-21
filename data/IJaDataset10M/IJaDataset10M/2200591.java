package lokahi.agent.tomcat;

import org.apache.log4j.Logger;
import lokahi.agent.dao.AgentJob;
import lokahi.agent.interfaces.AgentTomcat;
import lokahi.agent.util.TMCAgentBrowser;
import lokahi.core.api.state.State;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Stephen Toback
 * @version $Id: HttpTomcat.java,v 1.2 2006/03/06 22:31:44 drtobes Exp $
 */
public class HttpTomcat implements AgentTomcat {

    static final Logger logger = Logger.getLogger(HttpTomcat.class);

    private int pk;

    private int pid;

    private String name;

    private String javaOptions;

    private int httpPort;

    private String javaHome;

    private boolean shouldRestart;

    private String standardOutLog;

    private State state;

    private String startClass;

    private HashMap<String, String> env = new HashMap<String, String>();

    public void setEnvironment(String s) {
        while (!"".equals(s.trim())) {
            env.put(s.substring(0, s.indexOf('=') - 1), s.substring(s.indexOf('='), s.indexOf(';') - 1));
            s = s.substring(s.indexOf(';'));
        }
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getStartClass() {
        return startClass;
    }

    public void setStartClass(String startClass) {
        this.startClass = startClass;
    }

    public String getStandardOutLog() {
        return standardOutLog;
    }

    public void setStandardOutLog(String standardOutLog) {
        this.standardOutLog = standardOutLog;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaOptions() {
        return javaOptions;
    }

    public void setJavaOptions(String javaOptions) {
        this.javaOptions = javaOptions;
    }

    public boolean startContext(String context) {
        return httpRequest("start&path=" + checkContextName(context)).startsWith("OK");
    }

    public boolean stopContext(String context) {
        return httpRequest("stop&path=" + checkContextName(context)).startsWith("OK");
    }

    public boolean reloadContext(String context) {
        return httpRequest("reload&path=" + checkContextName(context)).startsWith("OK");
    }

    public boolean addContext(String path, String docBase) {
        docBase = "file:" + docBase;
        if (docBase.endsWith(".war")) {
            docBase = "jar:" + docBase + "!/";
        }
        return httpRequest("install&path=" + checkContextName(path) + "&war=" + docBase).startsWith("OK");
    }

    public String[] listContexts() {
        return httpRequest("list").split("\n");
    }

    public boolean removeContext(String context) {
        return httpRequest("remove&path=" + checkContextName(context)).startsWith("OK");
    }

    public String getSessions(String context) {
        return httpRequest("sessions&path=" + checkContextName(context));
    }

    private String httpRequest(String url) {
        String ret = "Error, malformed URL";
        try {
            ret = TMCAgentBrowser.get(new URL("http://127.0.0.1:" + httpPort + "/TMAgent/TMAgentManagerServlet?command=" + url));
        } catch (MalformedURLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
        }
        return ret;
    }

    public String getStartCommand() {
        return javaHome + "/bin/java " + this.javaOptions + ' ' + this.getStartClass() + " start >>" + standardOutLog;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public boolean shouldRestart() {
        return shouldRestart;
    }

    public AgentJob localRestart() {
        return null;
    }

    public State getState() {
        return state;
    }

    public void setState(State s) {
        this.state = s;
    }

    public void update() throws SQLException {
    }

    protected static String checkContextName(String name) {
        if (!name.trim().startsWith("/")) name = new StringBuilder(name.trim()).insert(0, '/').toString();
        return name.trim();
    }

    public void setShouldRestart(boolean shouldRestart) {
        this.shouldRestart = shouldRestart;
    }
}
