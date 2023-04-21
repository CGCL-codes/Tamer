package org.dbe.p2p.registry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dbe.identity.mgmt.IdentityException;
import org.dbe.identity.mgmt.IdentityManager;
import org.dbe.p2p.registry.jobs.UpdateServiceProxyJob;
import org.dbe.p2p.registry.jobs.UpdateServiceProxyListener;
import org.dbe.p2p.service.DhtService;
import org.dbe.servent.ServentContext;
import org.dbe.servent.ServerServentException;
import org.dbe.servent.core.DefaultCoreComponent;
import org.dbe.servent.p2p.P2PDirectory;
import org.dbe.servent.service.ServiceInfo;
import org.dbe.timer.Timer;
import org.dbe.timer.TimerException;
import org.dbe.toolkit.pa.DBEPropertyKeys;
import org.dbe.toolkit.proxyframework.ServiceProxy;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * This class implements the service registry over DHT. It uses the DhtService and the
 * IdentityManager.
 *
 * @author <a href="mailto:Dominik.Dahlem@cs.tcd.ie">Dominik Dahlem</a>
 */
public final class DhtServiceRegistry extends DefaultCoreComponent implements P2PDirectory {

    private static final Logger LOGGER = Logger.getLogger(DhtServiceRegistry.class);

    private DhtService m_dhtService;

    private IdentityManager m_identityManager;

    private Timer m_timer;

    private Hashtable<String, ServiceProxy> m_results;

    private Hashtable<String, JobDetail> m_jobs;

    private String m_rootPath;

    private String m_config;

    private byte[] m_privateKey;

    private int m_updateInterval;

    public DhtServiceRegistry(DhtService p_dhtService, IdentityManager p_identityManager, Timer p_timer) {
        this.m_dhtService = p_dhtService;
        this.m_identityManager = p_identityManager;
        this.m_timer = p_timer;
        this.m_results = new Hashtable();
        this.m_jobs = new Hashtable();
    }

    /**
     * @see org.dbe.servent.core.DefaultCoreComponent#init(org.dbe.servent.ServentContext)
     */
    public void init(ServentContext p_context) {
        super.init(p_context);
        this.m_rootPath = p_context.getConfig().getRootPath();
        this.m_config = m_rootPath + File.separator + "service-registry" + File.separator + DhtServiceRegistryConstants.PROP_FILENAME;
        LOGGER.info("Set the DhtServiceRegistry configuration to " + this.m_config);
    }

    /**
     * @see org.dbe.servent.core.LifeCycle#restart()
     */
    public void restart() {
        stop();
        start();
    }

    /**
     * @see org.dbe.servent.core.LifeCycle#start()
     */
    public void start() {
        Properties conf = new Properties();
        String identity = null;
        String updateInterval = null;
        try {
            conf.load(new FileInputStream(m_config));
            identity = conf.getProperty(DhtServiceRegistryConstants.SERVICE_REGISTRY_IDENTITY);
            updateInterval = conf.getProperty(DhtServiceRegistryConstants.SERVICE_PROXY_LEASE);
            if ((updateInterval != null) && "".equals(updateInterval)) {
                try {
                    m_updateInterval = Integer.valueOf(updateInterval).intValue();
                } catch (NumberFormatException nfe) {
                    LOGGER.warn("The property " + DhtServiceRegistryConstants.SERVICE_PROXY_LEASE + "=" + updateInterval + " does not have the right format.");
                    m_updateInterval = DhtServiceRegistryConstants.DEFAULT_SERVICE_PROXY_LEASE;
                }
            } else {
                m_updateInterval = DhtServiceRegistryConstants.DEFAULT_SERVICE_PROXY_LEASE;
            }
            if ((identity == null) || "".equals(identity)) {
                LOGGER.error("One identity has to be specified in the configuration " + "as the owner of the proxy objects stored in the DHT");
            }
            m_privateKey = m_identityManager.getPrivateKey(identity);
        } catch (FileNotFoundException fne) {
            LOGGER.error("Could not load the configuration " + m_config);
        } catch (IOException ioe) {
            LOGGER.error("Could not load the configuration " + m_config);
        } catch (IdentityException ie) {
            LOGGER.error("Could not retrieve the secret of identity " + identity);
        }
        this.status = STATUS_RUNNING;
    }

    /**
     * @see org.dbe.servent.core.LifeCycle#stop()
     */
    public void stop() {
        this.status = STATUS_STOPPED;
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#register(org.dbe.servent.service.ServiceInfo, java.util.Map)
     */
    public Object register(ServiceInfo p_info, Map p_properties) throws ServerServentException {
        String id = null;
        byte[] proxyAsByte = null;
        p_properties.put(DBEPropertyKeys.SERVICE_ENDPOINT, p_info.getEndpoint());
        p_properties.put(DBEPropertyKeys.CODEBASE, p_info.getEndpoint() + "/CODEBASE");
        String[] entries = p_info.getEntries();
        if (entries == null) {
            LOGGER.error("Error: At least one entry is expected to be the ID.");
        } else if (entries.length == 0) {
            LOGGER.error("Error: At least one entry is expected to be the ID.");
        } else {
            id = entries[0];
        }
        if (id == null) {
            throw new ServerServentException("Error: At least one entry is expected to be the ID.");
        } else {
            LOGGER.debug("Create ServiceProxy...");
            ServiceProxy proxy = new ServiceProxy(p_properties);
            LOGGER.debug("ServiceProxy created.");
            try {
                LOGGER.info("Create the output streams...");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(proxy);
                oos.close();
                baos.close();
                proxyAsByte = baos.toByteArray();
            } catch (IOException ioe) {
                String errorMsg = "Could not write the object into an ByteArrayOutputStream.";
                LOGGER.error(errorMsg);
                throw new ServerServentException(errorMsg, ioe);
            }
            if (proxyAsByte == null) {
                String errorMsg = "The ProxyObject byte[] is null.";
                LOGGER.error(errorMsg);
                throw new ServerServentException(errorMsg);
            }
            JobDetail jobDetail = new JobDetail("Add ServiceProxy Job", "DhtServiceRegistry", UpdateServiceProxyJob.class);
            Trigger trigger = new SimpleTrigger("Add ServiceProxy Trigger", "DhtServiceRegistry", SimpleTrigger.REPEAT_INDEFINITELY, m_updateInterval * 1000);
            JobListener jobListener = new UpdateServiceProxyListener(id, proxyAsByte, m_privateKey, m_updateInterval, m_dhtService, m_timer);
            try {
                LOGGER.debug("Schedule the addition of a ServiceProxy object" + proxy + " with SMID " + id);
                m_timer.schedule(jobDetail, trigger, jobListener, null);
                m_jobs.put(id, jobDetail);
            } catch (TimerException te) {
                String errorMsg = "Scheduling the addition of a ServiceProxy object" + proxy + " with SMID " + id + " failed.";
                LOGGER.error(errorMsg);
                throw new ServerServentException(errorMsg, te);
            }
        }
        return id;
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#deregister(java.lang.Object)
     */
    public void deregister(Object p_id) {
        JobDetail job = m_jobs.get((String) p_id);
        try {
            m_timer.removeJob(job);
            m_jobs.remove((String) p_id);
        } catch (TimerException te) {
            String errorMsg = "Could not remove the job " + job.getName() + " from the timer service";
            LOGGER.error(errorMsg, te);
        }
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#lookup(java.lang.String[], java.lang.ClassLoader)
     */
    public Serializable[] lookup(String[] p_entries, ClassLoader p_classLoader) throws ServerServentException {
        return this.lookup(p_entries, -1, p_classLoader);
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#lookup(java.lang.String[], long, java.lang.ClassLoader)
     */
    public Serializable[] lookup(String[] p_entries, long p_timeToLive, ClassLoader p_classLoader) throws ServerServentException {
        return this.lookup(p_entries, -1, -1, p_classLoader);
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#lookup(java.lang.String[], int, java.lang.ClassLoader)
     */
    public Serializable[] lookup(String[] p_entries, int p_hops, ClassLoader p_classLoader) throws ServerServentException {
        return this.lookup(p_entries, -1, -1, p_classLoader);
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#justLookup(java.lang.String[], int, long)
     */
    public String justLookup(String[] p_entries, int p_hops, long p_timeout) throws ServerServentException {
        return null;
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#lookup(java.lang.String[], long, int, java.lang.ClassLoader)
     */
    public Serializable[] lookup(String[] p_entries, long p_timeToLive, int p_hops, ClassLoader p_classLoader) throws ServerServentException {
        return new Serializable[0];
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#finished(java.lang.String)
     */
    public boolean finished(String p_searchId) throws ServerServentException {
        return true;
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#retrieveResults(java.lang.String, java.lang.ClassLoader)
     */
    public Serializable[] retrieveResults(String p_searchId, ClassLoader p_classLoader) throws ServerServentException {
        ServiceProxy proxy = m_results.get(p_searchId);
        return new Serializable[] { proxy };
    }

    /**
     * @see org.dbe.servent.p2p.P2PDirectory#waitForSearchId(java.lang.String, java.lang.ClassLoader)
     */
    public Serializable[] waitForSearchId(String p_searchId, ClassLoader p_classLoader) throws ServerServentException {
        ServiceProxy proxy = m_results.get(p_searchId);
        return new Serializable[] { proxy };
    }
}
