package org.monet.kernel.model;

import java.util.HashMap;
import org.monet.kernel.agents.AgentSession;
import org.monet.kernel.constants.Database;
import org.monet.kernel.constants.Strings;

public class Context extends BaseObject {

    private static Context oInstance;

    private String sFrameworkName;

    private String sFrameworkDir;

    private AgentSession oAgentSession;

    private HashMap<Long, String> hmDBConnectionTypes;

    private HashMap<Long, String> hmSessions;

    private HashMap<Long, String> hmRemoteHosts;

    private HashMap<Long, String> hmApplications;

    private HashMap<Long, String> hmInterfaces;

    private HashMap<Long, String> hmDomains;

    private HashMap<Long, String> hmPaths;

    private HashMap<Long, Integer> hmPorts;

    private Context() {
        super();
        this.sFrameworkName = "";
        this.sFrameworkDir = "";
        this.oAgentSession = AgentSession.getInstance();
        this.hmDBConnectionTypes = new HashMap<Long, String>();
        this.hmSessions = new HashMap<Long, String>();
        this.hmRemoteHosts = new HashMap<Long, String>();
        this.hmApplications = new HashMap<Long, String>();
        this.hmInterfaces = new HashMap<Long, String>();
        this.hmDomains = new HashMap<Long, String>();
        this.hmPaths = new HashMap<Long, String>();
        this.hmPorts = new HashMap<Long, Integer>();
    }

    public static synchronized Context getInstance() {
        if (oInstance == null) {
            oInstance = new Context();
        }
        return oInstance;
    }

    public Boolean setFrameworkName(String sPath) {
        this.sFrameworkName = sPath;
        return true;
    }

    public String getFrameworkName() {
        return this.sFrameworkName;
    }

    public Boolean setFrameworkDir(String sDirectory) {
        this.sFrameworkDir = sDirectory;
        return true;
    }

    public String getFrameworkDir() {
        return this.sFrameworkDir;
    }

    public String getDatabaseConnectionType(Long idThread) {
        if (!this.hmDBConnectionTypes.containsKey(idThread)) return Database.ConnectionTypes.AUTO_COMMIT;
        return this.hmDBConnectionTypes.get(idThread);
    }

    public Boolean setDatabaseConnectionType(Long idThread, String sDBConnectionType) {
        this.hmDBConnectionTypes.put(idThread, sDBConnectionType);
        return true;
    }

    public Boolean autoCommit() {
        String sDBConnectionType = this.getDatabaseConnectionType(Thread.currentThread().getId());
        return sDBConnectionType.equals(Database.ConnectionTypes.AUTO_COMMIT);
    }

    public String getIdSession(Long idThread) {
        if (!this.hmSessions.containsKey(idThread)) return null;
        return this.hmSessions.get(idThread);
    }

    public Session getCurrentSession() {
        return this.getSession(Thread.currentThread().getId());
    }

    public Session getSession(Long idThread) {
        if (!this.hmSessions.containsKey(idThread)) return null;
        return this.oAgentSession.get(this.hmSessions.get(idThread));
    }

    public Boolean setSessionId(Long idThread, String idSession) {
        this.hmSessions.put(idThread, idSession);
        return true;
    }

    public String getHost(Long idThread) {
        if (!this.hmRemoteHosts.containsKey(idThread)) return null;
        return this.hmRemoteHosts.get(idThread);
    }

    public String getApplication(Long idThread) {
        if (!this.hmApplications.containsKey(idThread)) return Strings.EMPTY;
        return this.hmApplications.get(idThread);
    }

    public String getApplicationInterface(Long idThread) {
        if (!this.hmInterfaces.containsKey(idThread)) return Strings.EMPTY;
        return this.hmInterfaces.get(idThread);
    }

    public Boolean setApplication(Long idThread, String sHost, String sApplication, String sInterface) {
        this.hmRemoteHosts.put(idThread, sHost);
        this.hmApplications.put(idThread, sApplication);
        this.hmInterfaces.put(idThread, sInterface);
        return true;
    }

    public String getUrl(Long idThread, Boolean useSSL, Boolean bExcludePath) {
        String sUrl = "";
        if (!this.hmDomains.containsKey(idThread)) return null;
        if (useSSL) sUrl = "https://" + this.hmDomains.get(idThread); else sUrl = "http://" + this.hmDomains.get(idThread) + ":" + this.hmPorts.get(idThread);
        if (!bExcludePath) sUrl += this.hmPaths.get(idThread);
        return sUrl;
    }

    public String getDomain(Long idThread) {
        if (!this.hmDomains.containsKey(idThread)) return null;
        return this.hmDomains.get(idThread);
    }

    public String getPath(Long idThread) {
        if (!this.hmPaths.containsKey(idThread)) return null;
        return this.hmPaths.get(idThread);
    }

    public Integer getPort(Long idThread) {
        if (!this.hmPorts.containsKey(idThread)) return null;
        return this.hmPorts.get(idThread);
    }

    public Boolean setUserServerConfig(Long idThread, String sDomain, String sPath, Integer iPort) {
        this.hmDomains.put(idThread, sDomain);
        this.hmPaths.put(idThread, sPath);
        this.hmPorts.put(idThread, iPort);
        return true;
    }

    public Boolean clear(Long idThread) {
        this.hmDBConnectionTypes.remove(idThread);
        this.hmSessions.remove(idThread);
        this.hmRemoteHosts.remove(idThread);
        this.hmApplications.remove(idThread);
        this.hmInterfaces.remove(idThread);
        this.hmDomains.remove(idThread);
        this.hmPaths.remove(idThread);
        this.hmPorts.remove(idThread);
        return true;
    }
}
