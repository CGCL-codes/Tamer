package org.archive.crawler.datamodel;

import java.util.Map;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.URIException;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.settings.SettingsHandler;

/**
 * Server and Host cache.
 * @author stack
 * @version $Date: 2006-09-26 21:49:01 +0000 (Tue, 26 Sep 2006) $, $Revision: 4668 $
 */
public class ServerCache {

    private static Logger logger = Logger.getLogger(ServerCache.class.getName());

    protected SettingsHandler settingsHandler = null;

    /**
     * hostname[:port] -> CrawlServer.
     * Set in the initialization.
     */
    protected Map<String, CrawlServer> servers = null;

    /**
     * hostname -> CrawlHost.
     * Set in the initialization.
     */
    protected Map<String, CrawlHost> hosts = null;

    /**
     * Constructor.
     * Shutdown access to the default constructor by making it protected.
     */
    protected ServerCache() {
        super();
    }

    /**
     * This constructor creates a ServerCache that is all memory-based using
     * Hashtables.  Used for unit testing only
     * (Use {@link #ServerCache(CrawlController)} when crawling).
     * @param sh
     * @throws Exception
     */
    public ServerCache(final SettingsHandler sh) throws Exception {
        this.settingsHandler = sh;
        this.servers = new Hashtable<String, CrawlServer>();
        this.hosts = new Hashtable<String, CrawlHost>();
    }

    public ServerCache(final CrawlController c) throws Exception {
        this.settingsHandler = c.getSettingsHandler();
        this.servers = c.getBigMap("servers", String.class, CrawlServer.class);
        this.hosts = c.getBigMap("hosts", String.class, CrawlHost.class);
    }

    /**
     * Get the {@link CrawlServer} associated with <code>name</code>.
     * @param serverKey Server name we're to return server for.
     * @return CrawlServer instance that matches the passed server name.
     */
    public synchronized CrawlServer getServerFor(String serverKey) {
        CrawlServer cserver = (CrawlServer) this.servers.get(serverKey);
        return (cserver != null) ? cserver : createServerFor(serverKey);
    }

    protected CrawlServer createServerFor(String s) {
        CrawlServer cserver = (CrawlServer) this.servers.get(s);
        if (cserver != null) {
            return cserver;
        }
        String skey = new String(s);
        cserver = new CrawlServer(skey);
        cserver.setSettingsHandler(settingsHandler);
        servers.put(skey, cserver);
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("Created server " + s);
        }
        return cserver;
    }

    /**
     * Get the {@link CrawlServer} associated with <code>curi</code>.
     * @param cauri CandidateURI we're to get server from.
     * @return CrawlServer instance that matches the passed CandidateURI.
     */
    public CrawlServer getServerFor(CandidateURI cauri) {
        CrawlServer cs = null;
        try {
            String key = CrawlServer.getServerKey(cauri);
            if (key != null) {
                cs = getServerFor(key);
            }
        } catch (URIException e) {
            logger.severe(e.getMessage() + ": " + cauri);
            e.printStackTrace();
        } catch (NullPointerException npe) {
            logger.severe(npe.getMessage() + ": " + cauri);
            npe.printStackTrace();
        }
        return cs;
    }

    /**
     * Get the {@link CrawlHost} associated with <code>name</code>.
     * @param hostname Host name we're to return Host for.
     * @return CrawlHost instance that matches the passed Host name.
     */
    public synchronized CrawlHost getHostFor(String hostname) {
        if (hostname == null || hostname.length() == 0) {
            return null;
        }
        CrawlHost host = (CrawlHost) this.hosts.get(hostname);
        return (host != null) ? host : createHostFor(hostname);
    }

    protected CrawlHost createHostFor(String hostname) {
        if (hostname == null || hostname.length() == 0) {
            return null;
        }
        CrawlHost host = (CrawlHost) this.hosts.get(hostname);
        if (host != null) {
            return host;
        }
        String hkey = new String(hostname);
        host = new CrawlHost(hkey);
        this.hosts.put(hkey, host);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Created host " + hostname);
        }
        return host;
    }

    /**
     * Get the {@link CrawlHost} associated with <code>curi</code>.
     * @param cauri CandidateURI we're to return Host for.
     * @return CandidateURI instance that matches the passed Host name.
     */
    public CrawlHost getHostFor(CandidateURI cauri) {
        CrawlHost h = null;
        try {
            h = getHostFor(cauri.getUURI().getReferencedHost());
        } catch (URIException e) {
            e.printStackTrace();
        }
        return h;
    }

    /**
     * @param serverKey Key to use doing lookup.
     * @return True if a server instance exists.
     */
    public boolean containsServer(String serverKey) {
        return (CrawlServer) servers.get(serverKey) != null;
    }

    /**
     * @param hostKey Key to use doing lookup.
     * @return True if a host instance exists.
     */
    public boolean containsHost(String hostKey) {
        return (CrawlHost) hosts.get(hostKey) != null;
    }

    /**
     * Called when shutting down the cache so we can do clean up.
     */
    public void cleanup() {
        if (this.hosts != null) {
            this.hosts.clear();
            this.hosts = null;
        }
        if (this.servers != null) {
            this.servers.clear();
            this.servers = null;
        }
    }
}
