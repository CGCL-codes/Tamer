package fi.hip.gb.net.discovery;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fi.hip.gb.core.Config;
import fi.hip.gb.net.Discovery;
import fi.hip.gb.utils.ArrayUtils;
import fi.hip.gb.utils.ClassLoaderUtils;
import fi.hip.gb.utils.TextUtils;

/**
 * An default implementation of the {@link Discovery} interface
 * used for discovering GBAgent servers and services. This class
 * is accessible from outside using  {@link fi.hip.gb.net.rpc.DiscoveryService}
 * SOAP stub.
 * <p>
 * The service doesn't know its own address until this service is queried once
 * through AXIS stack. The reason is we cannot discover the URL of the local
 * service to be published for others using Servlet API.
 * 
 * @author Juho Karppinen
 */
public class DiscoveryService implements Discovery {

    /** known servers, key is the service URL and value is corresponding <code>DiscoveryPacket</code> */
    private static Hashtable<String, DiscoveryPacket> knownServices = new Hashtable<String, DiscoveryPacket>();

    /** caches the list of known agents found from JAR files, key is the agent ID
     * and value is corresponding <code>AgentPacket</code> */
    private static Hashtable<String, AgentPacket> knownAgents = new Hashtable<String, AgentPacket>();

    /** modified times of JAR files, file URL is the key and value is timestamp */
    private static Hashtable<URL, Long> modifiedTimes = new Hashtable<URL, Long>();

    /** local context URL is the URL published to others */
    private static String localContextURL = null;

    /** at what time services were validated */
    private static long lastValidation = -1;

    /** static instance of the service */
    private static DiscoveryService instance;

    private static Log log = LogFactory.getLog(DiscoveryService.class);

    /**
     * New discovery service is created, initialize some stuff from
     * Axis messagecontext.
     */
    public DiscoveryService() {
    }

    /**
     * Gets the static instance of the discovery service.
     * @return instance for the service
     */
    public static DiscoveryService getInstance() {
        if (instance == null) {
            instance = new DiscoveryService();
        }
        return instance;
    }

    /**
     * Gets local context URL. It is either automatically discovered or
     * defined in config file.
     * @see fi.hip.gb.net.Discovery#getLocalService() 
     */
    public String getLocalService() throws RemoteException {
        if (localContextURL != null) return localContextURL;
        try {
            String proxyURL = null;
            if (proxyURL == null) {
                String globala = "";
                Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
                while (e1.hasMoreElements()) {
                    NetworkInterface ni = e1.nextElement();
                    Enumeration<InetAddress> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = e2.nextElement();
                        String ias = ia.toString();
                        if (ias.indexOf('.') > -1) {
                            if (ias.indexOf("127.0.0.1") == -1) {
                                globala = ias;
                            }
                        }
                    }
                }
                if ("".equals(globala)) throw new UnknownHostException("No global IP in interfaces");
                proxyURL = "http:/" + globala + ":" + Config.getInstance().getContextPath();
                log.debug("getLocalService returns " + proxyURL + " globala is " + globala);
            }
            return proxyURL;
        } catch (Exception uhe) {
            log.error("Could not find local IP");
        }
        return null;
    }

    public void addService(DiscoveryPacket packet) throws RemoteException {
        try {
            if (packet.getServiceURL().indexOf("localhost") != -1) {
                throw new RemoteException("Localhost is not valid ip-address");
            }
            fi.hip.gb.net.rpc.DiscoveryService discovery = new fi.hip.gb.net.rpc.DiscoveryService(packet.getServiceURL());
            DiscoveryPacket[] packets = discovery.callback(this.listServices());
            packet.resetResponseTime();
            addValidatedService(packet);
            for (int i = 0; i < packets.length; i++) {
                DiscoveryPacket oldPacket = (DiscoveryPacket) knownServices.get(packets[i].getServiceURL());
                if (oldPacket == null || oldPacket.getLastResponse() > packets[i].getLastResponse()) {
                    addValidatedService(packets[i]);
                }
            }
        } catch (RemoteException re) {
            log.error("Could not validate the service " + packet.getServiceURL() + "\n" + re.getMessage());
            throw re;
        } catch (Exception e) {
            log.error("Could not validate the service " + packet.getServiceURL() + "\n" + e.getMessage());
            throw new RemoteException("Could not validate the service because of " + e.getClass().getName() + " : " + e.getMessage());
        }
    }

    /**
     * Adds already validated service to the database. Validation means that
     * the server exists and is reachable.
     * @param packet a validated discovery packet
     */
    public void addValidatedService(DiscoveryPacket packet) {
        if (knownServices.put(packet.getServiceURL(), packet) == null) {
            log.info("Accepting a new service to known hosts " + packet.getServiceURL() + " last response " + packet.getLastResponse() / 1000 + " s ago");
        } else {
            log.debug("Updating an already known host " + packet.getServiceURL() + " last response " + packet.getLastResponse() / 1000 + " s ago");
        }
    }

    public void removeService(String serviceURL) throws RemoteException {
        DiscoveryPacket dp = knownServices.get(serviceURL);
        log.info("Removing service " + serviceURL + " from the list of servers. Last response was  " + dp.getLastResponse() + " ms ago");
        knownServices.remove(serviceURL);
    }

    public DiscoveryPacket[] listServices() throws RemoteException {
        validateServices();
        DiscoveryPacket[] packets = (DiscoveryPacket[]) knownServices.values().toArray(new DiscoveryPacket[knownServices.size()]);
        for (int i = 0; i < packets.length; i++) {
        }
        return packets;
    }

    public AgentPacket[] listAgents() throws RemoteException {
        Hashtable<String, AgentPacket> agents = new Hashtable<String, AgentPacket>();
        DiscoveryPacket[] servers = this.listServices();
        for (int i = 0; i < servers.length; i++) {
            AgentPacket[] localAgents = servers[i].getAgents();
            for (int k = 0; k < localAgents.length; k++) {
                DiscoveryPacket dp = new DiscoveryPacket(servers[i].getServiceURL());
                dp.setJars(new String[] { localAgents[k].getJarURL() });
                AgentPacket packet = (AgentPacket) agents.get(localAgents[k].getServiceClass() + "_" + localAgents[k].getServiceID());
                if (packet != null) {
                    String localURL = getLocalService();
                    if (localURL.equals(dp.getServiceURL())) {
                        packet.setServers((DiscoveryPacket[]) ArrayUtils.insert(packet.getServers(), 0, dp));
                    } else {
                        packet.setServers((DiscoveryPacket[]) ArrayUtils.append(packet.getServers(), dp));
                    }
                    log.debug("duplicate version (" + packet.getServiceID() + ") of the agent " + packet.getServiceClass() + " found from " + dp.getServiceURL());
                } else {
                    localAgents[k].setServers(new DiscoveryPacket[] { dp });
                    agents.put(localAgents[k].getServiceClass() + "_" + localAgents[k].getServiceID(), localAgents[k]);
                }
            }
        }
        return (AgentPacket[]) agents.values().toArray(new AgentPacket[agents.size()]);
    }

    public DiscoveryPacket[] callback(DiscoveryPacket[] packets) throws RemoteException {
        DiscoveryPacket[] ownPackets = this.listServices();
        for (int i = 0; i < packets.length; i++) {
            DiscoveryPacket oldPacket = (DiscoveryPacket) knownServices.get(packets[i].getServiceURL());
            if (oldPacket == null || oldPacket.getLastResponse() > packets[i].getLastResponse()) {
                if (knownServices.put(packets[i].getServiceURL(), packets[i]) == null) {
                    log.info("Received a new service from our discovery server " + packets[i].getServiceURL() + " last response " + packets[i].getLastResponse() / 1000 + " s ago");
                } else {
                }
            }
        }
        return ownPackets;
    }

    /**
     * If last information from server was made discovery rate + 400% ago, 
     * it will be considered as missing and removed from the list of services.
     */
    public void validateServices() {
        if (lastValidation == -1) {
            lastValidation = System.currentTimeMillis();
        }
        for (Enumeration e = knownServices.elements(); e.hasMoreElements(); ) {
            DiscoveryPacket packet = (DiscoveryPacket) e.nextElement();
            if (localContextURL != null && packet.getServiceURL().toString().equals(localContextURL.toString())) {
                packet.resetResponseTime();
            } else {
                packet.setLastResponse(packet.getLastResponse() + (System.currentTimeMillis() - lastValidation));
            }
        }
        lastValidation = System.currentTimeMillis();
    }

    /**
     * Gets a list of deployed agent JAR files on this server.
     * @return an array of URLs for agent jars accessible through http protocol
     */
    public URL[] getDeployedJars() {
        return getDeployedJarTimings().keySet().toArray(new URL[0]);
    }

    /**
     * Gets a list of deployed agent JAR files on this server with last modified tag.
     * Files are found inside plugin directories.
     * @return hashtable of agent jars, keys are URLs for files accessible through http protocol, and
     * the value is last modified timestamp of the file as <code>Long</code>
     */
    public Hashtable<URL, Long> getDeployedJarTimings() {
        Hashtable<URL, Long> jars = new Hashtable<URL, Long>();
        String localService = null;
        try {
            localService = getLocalService();
        } catch (RemoteException re) {
            log.error("Error getting local service URL", re);
        }
        if (localService == null) return jars;
        String[] deployDirs = Config.getInstance().getPluginDirectories();
        for (int i = 0; i < deployDirs.length; i++) {
            File[] deployFiles = null;
            if (deployDirs[i].startsWith("/")) deployFiles = new File(deployDirs[i]).listFiles(); else deployFiles = new File(Config.getInstance().getDataDirectory() + "/" + deployDirs[i]).listFiles();
            for (int k = 0; deployFiles != null && k < deployFiles.length; k++) {
                try {
                    URL url = new URL(localService + "/dl/" + deployFiles[k].getName());
                    jars.put(url, new Long(deployFiles[k].lastModified()));
                } catch (MalformedURLException e) {
                    log.error("Could not create URL for jar " + deployFiles[k]);
                }
            }
        }
        return jars;
    }

    /**
     * Gets the supported agents by the local server. This information is taken
     * from cache if timestamps of files has not changed 
     * 
     * @return an array of unique agents, may contain multiple items for the same
     * agent class but with different versions.
     */
    public AgentPacket[] getSupportedAgents() {
        Hashtable<URL, Long> deployJars = getDeployedJarTimings();
        for (Enumeration<URL> e = modifiedTimes.keys(); e.hasMoreElements(); ) {
            URL cachedFile = e.nextElement();
            Long cachedTime = modifiedTimes.get(cachedFile);
            Long currentTime = deployJars.get(cachedFile);
            if (currentTime == null) {
                log.debug("Agent JAR file " + cachedFile + " has been removed");
                modifiedTimes.remove(cachedFile);
                AgentPacket[] aps = (AgentPacket[]) knownAgents.values().toArray(new AgentPacket[0]);
                for (int i = 0; i < aps.length; i++) {
                    if (aps[i].getJarURL().equals(cachedFile)) {
                        log.debug("Agent " + aps[i].getServiceID() + " is unloaded");
                        knownAgents.remove(aps[i].getServiceID());
                    }
                }
            } else if (cachedTime != null && cachedTime.longValue() == currentTime.longValue()) {
                deployJars.remove(cachedFile);
            }
        }
        for (Enumeration<URL> jarEnum = deployJars.keys(); jarEnum.hasMoreElements(); ) {
            URL deployFile = jarEnum.nextElement();
            try {
                URL[] jars = ClassLoaderUtils.prepareJars(new URL[] { deployFile }, Config.getWorkingDir(null));
                ClassLoaderUtils loader = new ClassLoaderUtils(this, jars);
                String[] classNames = loader.getClassNames(Class.class);
                for (int k = 0; k < classNames.length; k++) {
                    Object agent = (Object) loader.loadInstance(classNames[k]);
                    AgentPacket packet = new AgentPacket(classNames[k]);
                    packet.setJarURL(deployFile.toExternalForm());
                    packet.setServiceDescription(agent.toString());
                    int id = agent.hashCode();
                    packet.setServiceID("" + id);
                    log.debug("found agent " + packet.getServiceClass() + " from " + packet.getJarURL() + " with version " + packet.getServiceID() + " description " + packet.getServiceDescription() + " file lastModified " + TextUtils.getDateFormat(deployJars.get(deployFile)));
                    DiscoveryService.knownAgents.put(packet.getServiceID(), packet);
                }
                DiscoveryService.modifiedTimes.put(deployFile, deployJars.get(deployFile));
            } catch (Exception e) {
                log.error("Could not locate installed agents  in " + localContextURL, e);
            }
        }
        return knownAgents.values().toArray(new AgentPacket[knownAgents.size()]);
    }
}
