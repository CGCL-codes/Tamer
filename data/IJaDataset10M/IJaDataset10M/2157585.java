package org.apache.jetspeed.services.psmlmanager.db;

import java.sql.Connection;
import org.apache.jetspeed.services.psmlmanager.PsmlManagerService;
import org.apache.jetspeed.services.JetspeedSecurity;
import org.apache.jetspeed.om.profile.Profile;
import org.apache.jetspeed.om.profile.ProfileLocator;
import org.apache.jetspeed.om.profile.QueryLocator;
import org.apache.jetspeed.services.Profiler;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.om.profile.Portlets;
import org.apache.jetspeed.om.profile.PSMLDocument;
import org.apache.jetspeed.om.profile.BasePSMLDocument;
import org.apache.turbine.services.TurbineBaseService;
import org.apache.turbine.services.InitializationException;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.services.resources.ResourceService;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.services.servlet.ServletService;
import org.apache.torque.Torque;
import org.apache.jetspeed.om.security.JetspeedUser;
import org.apache.jetspeed.om.security.JetspeedUserFactory;
import org.apache.jetspeed.om.security.Role;
import org.apache.jetspeed.om.security.JetspeedRoleFactory;
import org.apache.jetspeed.om.security.Group;
import org.apache.jetspeed.om.security.JetspeedGroupFactory;
import org.apache.jetspeed.services.security.JetspeedSecurityException;
import javax.servlet.ServletConfig;
import org.apache.jetspeed.om.dbpsml.JetspeedUserProfile;
import org.apache.jetspeed.om.dbpsml.JetspeedUserProfilePeer;
import org.apache.jetspeed.om.dbpsml.JetspeedRoleProfile;
import org.apache.jetspeed.om.dbpsml.JetspeedRoleProfilePeer;
import org.apache.jetspeed.om.dbpsml.JetspeedGroupProfile;
import org.apache.jetspeed.om.dbpsml.JetspeedGroupProfilePeer;
import java.lang.Thread;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.File;
import org.exolab.castor.mapping.Mapping;
import org.xml.sax.InputSource;

/**
 * This service is responsible for loading and saving PSML documents. It uses
 * database to persist the PSML documents.
 *
 * @author <a href="mailto:adambalk@cisco.com">Atul Dambalkar</a>
 * @author <a href="mailto:mvaidya@cisco.com">Medha Vaidya</a>
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: DatabasePsmlManagerService.java,v 1.35 2004/02/23 03:32:19 jford Exp $
 */
public class DatabasePsmlManagerService extends TurbineBaseService implements DatabasePsmlManager {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(DatabasePsmlManagerService.class.getName());

    private Map psmlCache = new HashMap();

    /** The watcher for the document locations */
    private CacheRefresher refresher = null;

    /** the base refresh rate for documents */
    private long refreshRate;

    private static final String REFRESH_RATE = "refresh-rate";

    private static final long DEFAULT_REFRESH_RATE = 60 * 60 * 8 * 1000;

    /** whether caching is allowed */
    private boolean cachingOn;

    private static final String CACHING_ON = "caching-on";

    private static final boolean DEFAULT_CACHING_ON = false;

    private static final String POOL_NAME = "database";

    /** the import/export consumer service **/
    private PsmlManagerService consumer = null;

    public static final String DEFAULT_MAPPING = "${webappRoot}/WEB-INF/conf/psml-mapping.xml";

    String mapFile = null;

    /** the Castor mapping file name */
    private Mapping mapping = null;

    /** The pool name to use for database requests. */
    private String poolName = null;

    /**
     * This is the early initialization method called by the
     * Turbine <code>Service</code> framework
     */
    public void init(ServletConfig conf) throws InitializationException {
        if (getInit()) {
            return;
        }
        logger.info("Initializing DatabasePsmlManagerService...");
        initConfiguration(conf);
        logger.info("Done initializing DatabasePsmlManagerService.");
    }

    /**
     * Loads the configuration parameters for this service from the
     * JetspeedResources.properties file.
     *
     * @exception throws a <code>InitializationException</code> if the service
     * fails to initialize
     */
    private void initConfiguration(ServletConfig conf) throws InitializationException {
        TurbineServices.getInstance().initService(ServletService.SERVICE_NAME, conf);
        ResourceService serviceConf = ((TurbineServices) TurbineServices.getInstance()).getResources(PsmlManagerService.SERVICE_NAME);
        try {
            String value = serviceConf.getString(REFRESH_RATE);
            refreshRate = DEFAULT_REFRESH_RATE;
            try {
                refreshRate = Long.parseLong(value);
            } catch (Exception e) {
                logger.warn("DatabasePsmlManagerService: error in refresh-rate configuration: using default");
            }
            poolName = serviceConf.getString(POOL_NAME);
            value = serviceConf.getString(CACHING_ON);
            cachingOn = DEFAULT_CACHING_ON;
            try {
                cachingOn = value.equals("true");
            } catch (Exception e) {
                logger.warn("DatabasePsmlManagerService: error in caching-on configuration: using default");
            }
            mapFile = serviceConf.getString("mapping", DEFAULT_MAPPING);
            mapFile = TurbineServlet.getRealPath(mapFile);
            loadMapping();
        } catch (Throwable t) {
            logger.error(this + ".init:", t);
            throw new InitializationException("Exception initializing DatabasePsmlManagerService" + t);
        }
        if (cachingOn) {
            this.refresher = new CacheRefresher();
            refresher.start();
        }
    }

    /** Late init method from Turbine Service model */
    public void init() throws InitializationException {
        setInit(true);
    }

    protected void loadMapping() throws InitializationException {
        if (mapFile != null) {
            File map = new File(mapFile);
            if (logger.isDebugEnabled()) logger.debug("Loading psml mapping file " + mapFile);
            if (map.exists() && map.isFile() && map.canRead()) {
                try {
                    mapping = new Mapping();
                    InputSource is = new InputSource(new FileReader(map));
                    is.setSystemId(mapFile);
                    mapping.loadMapping(is);
                } catch (Exception e) {
                    logger.error("Error in psml mapping creation", e);
                    throw new InitializationException("Error in mapping", e);
                }
            } else {
                throw new InitializationException("PSML Mapping not found or not a file or unreadable: " + mapFile);
            }
        }
    }

    /**
     * This is the shutdown method called by the
     * Turbine <code>Service</code> framework
     */
    public void shutdown() {
        if (this.refresher != null) {
            this.refresher.setDone(true);
        }
    }

    /**
     * A thread implementation of cache refreshing mechanism for database
     * persisted PSMLs. We have to refresh the cache after specific intervals
     * if someone manually updates the PSML database.
     *
     * @author <a href="mailto:adambalk@cisco.com">Atul Dambalkar</a>
     */
    class CacheRefresher extends Thread {

        private boolean done = false;

        /**
         * Constructor to to set the priority.
         */
        CacheRefresher() {
            setDaemon(true);
            setPriority(Thread.MIN_PRIORITY + 1);
        }

        /**
         * We are all done, system is shutting down.
         */
        void setDone(boolean done) {
            this.done = done;
        }

        /**
         * Method as needed for a Thread to run
         */
        public void run() {
            try {
                while (!done) {
                    if (logger.isDebugEnabled()) logger.debug("Cache Refresher thread sleeping now!");
                    sleep(refreshRate);
                    if (logger.isDebugEnabled()) logger.debug("Cache Refresher thread working now!");
                    try {
                        synchronized (this) {
                            Iterator i = psmlCache.keySet().iterator();
                            while (i.hasNext()) {
                                String locator = (String) i.next();
                                PSMLDocument doc = refresh(stringToLocator(locator));
                                psmlCache.put(locator, doc);
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("DatabasePsmlManagerService.CacheRefresher: Error in cache refresher...", e);
                    }
                }
            } catch (InterruptedException e) {
                if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService.CacheRefresher: recieved interruption, aborting.");
            }
        }
    }

    /**
     * Return a unique string identifying this object.
     */
    private String locatorToString(ProfileLocator locator) {
        StringBuffer keybuf = new StringBuffer();
        JetspeedUser user = locator.getUser();
        Role role = locator.getRole();
        Group group = locator.getGroup();
        String name = locator.getName();
        String mediaType = locator.getMediaType();
        String country = locator.getCountry();
        String language = locator.getLanguage();
        synchronized (this) {
            if (user != null) {
                keybuf.append("User:").append(user.getUserName());
            } else if (group != null) {
                keybuf.append("Group:").append(group.getName());
            } else if (role != null) {
                keybuf.append("Role:").append(role.getName());
            }
            if (name != null) {
                keybuf.append('$').append("Page:").append(name);
            }
            if (mediaType != null) {
                keybuf.append('$').append("MediaType:").append(mediaType);
            }
            if (country != null && (!country.equals("-1"))) {
                keybuf.append('$').append("Country:").append(country);
            }
            if (language != null && (!language.equals("-1"))) {
                keybuf.append('$').append("Language:").append(language);
            }
        }
        if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService: Returning locator string: " + keybuf.toString());
        return keybuf.toString();
    }

    private ProfileLocator stringToLocator(String locstr) throws Exception {
        ProfileLocator locator = Profiler.createLocator();
        String entity = null;
        if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService: Creating locator for string: " + locstr);
        StringTokenizer dollarTokens = new StringTokenizer(locstr, "$");
        while (dollarTokens.hasMoreTokens()) {
            String dollarToken = dollarTokens.nextToken().trim();
            StringTokenizer colonTokens = new StringTokenizer(dollarToken, ":");
            String colonToken = colonTokens.nextToken();
            if (colonToken.equals("User")) {
                entity = colonTokens.nextToken().trim();
                locator.setUser(JetspeedSecurity.getUser(entity));
            } else if (colonToken.equals("Group")) {
                entity = colonTokens.nextToken().trim();
                locator.setGroup(JetspeedSecurity.getGroup(entity));
            } else if (colonToken.equals("Role")) {
                entity = colonTokens.nextToken().trim();
                locator.setRole(JetspeedSecurity.getRole(entity));
            } else if (colonToken.equals("Page")) {
                entity = colonTokens.nextToken().trim();
                locator.setName(entity);
            } else if (colonToken.equals("MediaType")) {
                entity = colonTokens.nextToken().trim();
                locator.setMediaType(entity);
            } else if (colonToken.equals("Country")) {
                entity = colonTokens.nextToken().trim();
                locator.setCountry(entity);
            } else if (colonToken.equals("Language")) {
                entity = colonTokens.nextToken().trim();
                locator.setLanguage(entity);
            }
        }
        if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService: Returning locator for string: " + locatorToString(locator));
        return locator;
    }

    public PSMLDocument getDocument(String name) {
        logger.warn("*** NOT SUPPORTED: GETDOC FROM DATABASE PSML MANAGER!!!");
        return null;
    }

    public boolean saveDocument(String fileOrUrl, PSMLDocument doc) {
        logger.warn("*** NOT SUPPORTED: SAVING DOC FROM DATABASE PSML MANAGER!!!");
        return false;
    }

    public boolean saveDocument(PSMLDocument doc) {
        logger.warn("*** NOT SUPPORTED: SAVING DOC FROM DATABASE PSML MANAGER!!!");
        return false;
    }

    /**
     * Returns a PSML document for the given locator
     *
     * @param locator The locator descriptor(ProfileLocator object) of the
     * document to be retrieved.
     * @return psmldoc The PSMLDocument object
     */
    public PSMLDocument getDocument(ProfileLocator locator) {
        if (locator == null) {
            String message = "PSMLManager: Must specify a locator";
            logger.warn("DatabasePsmlManagerService.getDocument: " + message);
            throw new IllegalArgumentException(message);
        }
        PSMLDocument psmldoc = null;
        String locStr = locatorToString(locator);
        boolean inCache = false;
        if (cachingOn) {
            synchronized (psmlCache) {
                inCache = psmlCache.containsKey(locStr);
                if (inCache) {
                    psmldoc = (PSMLDocument) psmlCache.get(locStr);
                }
            }
            if (inCache) {
                return psmldoc;
            }
        }
        try {
            return refresh(locator);
        } catch (Exception e) {
            logger.warn("DatabasePSMLManagerService.getDocument: exception:", e);
            throw new RuntimeException("Could not get profile from DB");
        }
    }

    /**
     * Stores the PSML document in DB for the given profile
     *
     * @param profile The profile that holds the PSMLDocument.
     * @return PSMLDocument The PSMLDocument that got created in DB.
     */
    public PSMLDocument createDocument(Profile profile) {
        return createOrSaveDocument(profile, INSERT);
    }

    /**
     * Update the PSML document in DB for the given profile
     *
     * @param profile The profile that holds the PSMLDocument.
     * @return PSMLDocument The PSMLDocument that got created in DB.
     */
    public boolean store(Profile profile) {
        return createOrSaveDocument(profile, UPDATE) != null;
    }

    private PSMLDocument createOrSaveDocument(Profile profile, int operation) {
        if (profile == null) {
            String message = "PSMLManager: Must specify a profile";
            logger.warn("DatabasePsmlManagerService.createOrSaveDocument: " + message);
            throw new IllegalArgumentException(message);
        }
        JetspeedUser user = profile.getUser();
        Role role = profile.getRole();
        Group group = profile.getGroup();
        String tableName = null;
        Connection dbCon = getDbConnection();
        try {
            if (user != null) {
                tableName = "JETSPEED_USER_PROFILE";
                if (operation == INSERT) {
                    new JetspeedUserProfilePeer().insert(profile, dbCon);
                } else if (operation == UPDATE) {
                    new JetspeedUserProfilePeer().update(profile, dbCon);
                }
            } else if (role != null) {
                tableName = "JETSPEED_ROLE_PROFILE";
                if (operation == INSERT) {
                    new JetspeedRoleProfilePeer().insert(profile, dbCon);
                } else if (operation == UPDATE) {
                    new JetspeedRoleProfilePeer().update(profile, dbCon);
                }
            } else if (group != null) {
                tableName = "JETSPEED_GROUP_PROFILE";
                if (operation == INSERT) {
                    new JetspeedGroupProfilePeer().insert(profile, dbCon);
                } else if (operation == UPDATE) {
                    new JetspeedGroupProfilePeer().update(profile, dbCon);
                }
            }
            if (cachingOn) {
                synchronized (psmlCache) {
                    if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService.createOrSaveDocument: caching document: profile: " + locatorToString(profile));
                    psmlCache.put(locatorToString(profile), profile.getDocument());
                }
            }
            return profile.getDocument();
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.createOrSaveDocument: profile: " + profile + " tableName: " + tableName, e);
            throw new RuntimeException("Could not create new profile in DB");
        } finally {
            releaseDbConnection(dbCon);
        }
    }

    /**
     * Remove the PSMLDocument/profile for given locator object.
     *
     * @param locator The profile locator criteria for profile to be removed.
     */
    public void removeDocument(ProfileLocator locator) {
        if (locator == null) {
            String message = "PSMLManager: Must specify a locator";
            logger.warn("DatabasePsmlManagerService.removeDocument: " + message);
            throw new IllegalArgumentException(message);
        }
        JetspeedUser user = locator.getUser();
        Role role = locator.getRole();
        Group group = locator.getGroup();
        String tableName = null;
        Connection dbCon = getDbConnection();
        try {
            if (user != null) {
                new JetspeedUserProfilePeer().delete(locator, dbCon);
                tableName = "JETSPEED_USER_PROFILE";
            } else if (role != null) {
                new JetspeedRoleProfilePeer().delete(locator, dbCon);
                tableName = "JETSPEED_ROLE_PROFILE";
            } else if (group != null) {
                new JetspeedGroupProfilePeer().delete(locator, dbCon);
                tableName = "JETSPEED_GROUP_PROFILE";
            }
            if (cachingOn) {
                synchronized (psmlCache) {
                    psmlCache.remove(locatorToString(locator));
                }
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.removeDocument: profile: " + locatorToString(locator) + " tableName: " + tableName, e);
            throw new RuntimeException("Could not delete profile for given locator from DB");
        } finally {
            releaseDbConnection(dbCon);
        }
    }

    /**
     * Query for a collection of profiles given a profile locator criteria.
     * Use SQL engine to get the required profiles.
     *
     * @param locator The profile locator criteria.
     * @return Iterator object with the PSMLDocuments satisfying query
     */
    public Iterator query(QueryLocator locator) {
        if (locator == null) {
            String message = "PSMLManager: Must specify a locator";
            logger.warn("DatabasePsmlManagerService.query: " + message);
            throw new IllegalArgumentException(message);
        }
        Connection dbCon = getDbConnection();
        try {
            List userData = null;
            List groupData = null;
            List roleData = null;
            int queryMode = locator.getQueryMode();
            List list = new ArrayList();
            switch(queryMode) {
                case QueryLocator.QUERY_USER:
                    userData = new JetspeedUserProfilePeer().selectOrdered(locator, dbCon);
                    if (userData != null) {
                        list = getProfiles(userData);
                    }
                    break;
                case QueryLocator.QUERY_GROUP:
                    groupData = new JetspeedGroupProfilePeer().selectOrdered(locator, dbCon);
                    if (groupData != null) {
                        list = getProfiles(groupData);
                    }
                    break;
                case QueryLocator.QUERY_ROLE:
                    roleData = new JetspeedRoleProfilePeer().selectOrdered(locator, dbCon);
                    if (roleData != null) {
                        list = getProfiles(roleData);
                    }
                    break;
                default:
                    userData = new JetspeedUserProfilePeer().selectOrdered(locator, dbCon);
                    if (userData != null) {
                        list.addAll(getProfiles(userData));
                    }
                    groupData = new JetspeedGroupProfilePeer().selectOrdered(locator, dbCon);
                    if (groupData != null) {
                        list.addAll(getProfiles(groupData));
                    }
                    roleData = new JetspeedRoleProfilePeer().selectOrdered(locator, dbCon);
                    if (roleData != null) {
                        list.addAll(getProfiles(roleData));
                    }
                    break;
            }
            return list.iterator();
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.query: exception", e);
        } finally {
            releaseDbConnection(dbCon);
        }
        return new ArrayList().iterator();
    }

    /**
     * Get profile iterator from given list of objects.
     *
     * @param data List of JetspeedUserProfile, JetspeedGroupProfile,
     * JetspeedRoleProfile, objects
     * @return List of profiles
     */
    private List getProfiles(List data) {
        List list = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            Object obj = data.get(i);
            Portlets portlets = null;
            if (obj instanceof JetspeedUserProfile) {
                portlets = DBUtils.bytesToPortlets(((JetspeedUserProfile) obj).getProfile(), this.mapping);
                list.add(createUserProfile((JetspeedUserProfile) obj, portlets));
            } else if (obj instanceof JetspeedGroupProfile) {
                portlets = DBUtils.bytesToPortlets(((JetspeedGroupProfile) obj).getProfile(), this.mapping);
                list.add(createGroupProfile((JetspeedGroupProfile) obj, portlets));
            } else if (obj instanceof JetspeedRoleProfile) {
                portlets = DBUtils.bytesToPortlets(((JetspeedRoleProfile) obj).getProfile(), this.mapping);
                list.add(createRoleProfile((JetspeedRoleProfile) obj, portlets));
            }
        }
        return list;
    }

    /**
     * Get PSMLDocument object for given pagename and portlets.
     *
     * @param portlets Portlets for the given page name
     * @param page page name for this resource
     * @return PSMLDocument object for given page and portlets
     */
    private PSMLDocument getPSMLDocument(String page, Portlets portlets) {
        PSMLDocument psmldoc = new BasePSMLDocument();
        psmldoc.setName(page);
        psmldoc.setPortlets(portlets);
        return psmldoc;
    }

    /**
     * Given ordered list of locators, find the first document matching
     * a profile locator, starting from the beginning of the list and working
     * to the end.
     *
     * @param locator The ordered list of profile locators.
     * @return PSMLDocument object for the first document matching a locator
     */
    public PSMLDocument getDocument(List locators) {
        if (locators == null) {
            String message = "PSMLManager: Must specify a list of locators";
            logger.warn("DatabasePsmlManagerService.getDocument: " + message);
            throw new IllegalArgumentException(message);
        }
        for (int i = 0; i < locators.size(); i++) {
            PSMLDocument psmldoc = getDocument((ProfileLocator) locators.get(i));
            if (psmldoc != null) {
                return psmldoc;
            }
        }
        return null;
    }

    /**
     * Returns a PSML document for the given locator, it is called by the cache
     * refresher
     *
     * @param locator The locator descriptor(ProfileLocator object) of the
     * document to be retrieved.
     * @return psmldoc The PSMLDocument object
     */
    public PSMLDocument refresh(ProfileLocator locator) {
        if (locator == null) {
            String message = "PSMLManager: Must specify a locator";
            logger.warn("DatabasePsmlManagerService.refresh: " + message);
            throw new IllegalArgumentException(message);
        }
        JetspeedUser user = locator.getUser();
        Role role = locator.getRole();
        Group group = locator.getGroup();
        String tableName = null;
        List records = null;
        Portlets portlets = null;
        PSMLDocument psmldoc = null;
        String page = null;
        Connection dbCon = getDbConnection();
        try {
            if (user != null) {
                tableName = "JETSPEED_USER_PROFILE";
                records = new JetspeedUserProfilePeer().select(locator, dbCon);
                Iterator iterator = records.iterator();
                while (iterator.hasNext()) {
                    JetspeedUserProfile uprofile = (JetspeedUserProfile) iterator.next();
                    page = uprofile.getPage();
                    portlets = DBUtils.bytesToPortlets(uprofile.getProfile(), this.mapping);
                }
            } else if (role != null) {
                tableName = "JETSPEED_ROLE_PROFILE";
                records = new JetspeedRoleProfilePeer().select(locator, dbCon);
                Iterator iterator = records.iterator();
                while (iterator.hasNext()) {
                    JetspeedRoleProfile rprofile = (JetspeedRoleProfile) iterator.next();
                    page = rprofile.getPage();
                    portlets = DBUtils.bytesToPortlets(rprofile.getProfile(), this.mapping);
                }
            } else if (group != null) {
                tableName = "JETSPEED_GROUP_PROFILE";
                records = new JetspeedGroupProfilePeer().select(locator, dbCon);
                Iterator iterator = records.iterator();
                while (iterator.hasNext()) {
                    JetspeedGroupProfile gprofile = (JetspeedGroupProfile) iterator.next();
                    page = gprofile.getPage();
                    portlets = DBUtils.bytesToPortlets(gprofile.getProfile(), this.mapping);
                }
            }
            if (page != null && portlets != null) {
                psmldoc = getPSMLDocument(page, portlets);
                if (cachingOn) {
                    synchronized (psmlCache) {
                        if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService.refresh: caching document: profile: " + locatorToString(locator));
                        psmlCache.put(locatorToString(locator), psmldoc);
                    }
                }
                return psmldoc;
            } else {
                if (cachingOn) {
                    psmlCache.put(locatorToString(locator), null);
                    if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService.refresh: caching 'document not found': profile: " + locatorToString(locator));
                }
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.refresh: profile: " + locatorToString(locator) + " tableName: " + tableName, e);
            throw new RuntimeException("Could not refresh profile from DB");
        } finally {
            releaseDbConnection(dbCon);
        }
        if (logger.isDebugEnabled()) logger.debug("DatabasePsmlManagerService.refresh: no document found: profile: " + locatorToString(locator));
        return null;
    }

    /** Removes all documents for a given user.
     *
     * @param user The user object.
     */
    public void removeUserDocuments(JetspeedUser user) {
        Connection dbCon = getDbConnection();
        try {
            if (user != null) {
                new JetspeedUserProfilePeer().delete(user, dbCon);
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.removeUserDocuments: exception:", e);
            throw new RuntimeException("Could not delete documents for given user from DB");
        } finally {
            releaseDbConnection(dbCon);
        }
    }

    /** Removes all documents for a given role.
     *
     * @param role The role object.
     */
    public void removeRoleDocuments(Role role) {
        Connection dbCon = getDbConnection();
        try {
            if (role != null) {
                new JetspeedRoleProfilePeer().delete(role, dbCon);
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.removeRoleDocuments: exception:", e);
            throw new RuntimeException("Could not delete documents for given role from DB");
        } finally {
            releaseDbConnection(dbCon);
        }
    }

    /** Removes all documents for a given group.
     *
     * @param group The group object.
     */
    public void removeGroupDocuments(Group group) {
        Connection dbCon = getDbConnection();
        try {
            if (group != null) {
                new JetspeedGroupProfilePeer().delete(group, dbCon);
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.removeGroupDocuments: exception:", e);
            throw new RuntimeException("Could not delete documents for given group from DB");
        } finally {
            releaseDbConnection(dbCon);
        }
    }

    /** Query for a collection of profiles given a profile locator criteria.
     *  This method should be used when importing or exporting profiles between services.
     *
     * @param locator The profile locator criteria.
     * @return The count of profiles exported.
     */
    public int export(PsmlManagerService consumer, QueryLocator locator) {
        Iterator profiles = null;
        int count = 0;
        try {
            this.consumer = consumer;
            profiles = query(locator);
            while (profiles.hasNext()) {
                Profile profile = (Profile) profiles.next();
                try {
                    consumer.createDocument(profile);
                    count++;
                } catch (Exception ex) {
                    try {
                        consumer.store(profile);
                        count++;
                    } catch (Exception e) {
                        logger.warn("DatabasePsmlManagerService.export: profile: " + profile, ex);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.export: exception:", e);
        } finally {
        }
        return count;
    }

    public Mapping getMapping() {
        return this.mapping;
    }

    /**
     * Creates a user profile from a JetspeedUserProfile database object.
     *
     * @param entity The user profile entity in the database.
     * @param portlets The PSML blob.
     * @return A new profile object representing the locator and PSML blob.
     */
    public Profile createUserProfile(JetspeedUserProfile entity, Portlets portlets) {
        Profile profile = Profiler.createProfile();
        try {
            JetspeedUser user = JetspeedSecurity.getUser(entity.getUserName());
            if (null == user) {
                user = JetspeedUserFactory.getInstance();
                user.setUserName(entity.getUserName());
            }
            profile.setUser(user);
            profile.setMediaType(entity.getMediaType());
            profile.setLanguage(entity.getLanguage());
            profile.setCountry(entity.getCountry());
            profile.setName(entity.getPage());
            profile.setDocument(getPSMLDocument(entity.getPage(), portlets));
        } catch (JetspeedSecurityException e) {
        }
        return profile;
    }

    /**
     * Creates a group profile from a JetspeedGroupProfile database object.
     *
     * @param entity The group profile entity in the database.
     * @param portlets The PSML blob.
     * @return A new profile object representing the locator and PSML blob.
     */
    public Profile createGroupProfile(JetspeedGroupProfile entity, Portlets portlets) {
        Profile profile = Profiler.createProfile();
        try {
            Group group = JetspeedSecurity.getGroup(entity.getGroupName());
            if (null == group) {
                group = JetspeedGroupFactory.getInstance();
                group.setName(entity.getGroupName());
            }
            profile.setGroup(group);
            profile.setMediaType(entity.getMediaType());
            profile.setLanguage(entity.getLanguage());
            profile.setCountry(entity.getCountry());
            profile.setName(entity.getPage());
            profile.setDocument(getPSMLDocument(entity.getPage(), portlets));
        } catch (JetspeedSecurityException e) {
        }
        return profile;
    }

    /**
     * Creates a role profile from a JetspeedRoleProfile database object.
     *
     * @param entity The group profile entity in the database.
     * @param portlets The PSML blob.
     * @return A new profile object representing the locator and PSML blob.
     */
    public Profile createRoleProfile(JetspeedRoleProfile entity, Portlets portlets) {
        Profile profile = Profiler.createProfile();
        try {
            Role role = JetspeedSecurity.getRole(entity.getRoleName());
            if (null == role) {
                role = JetspeedRoleFactory.getInstance();
                role.setName(entity.getRoleName());
            }
            profile.setRole(role);
            profile.setMediaType(entity.getMediaType());
            profile.setLanguage(entity.getLanguage());
            profile.setCountry(entity.getCountry());
            profile.setName(entity.getPage());
            profile.setDocument(getPSMLDocument(entity.getPage(), portlets));
        } catch (JetspeedSecurityException e) {
        }
        return profile;
    }

    /**
    * Get a database connection to the default or specifed torque database pool
    */
    private Connection getDbConnection() {
        try {
            if (poolName == null) {
                return Torque.getConnection();
            } else {
                return Torque.getConnection(poolName);
            }
        } catch (Exception e) {
            logger.warn("DatabasePsmlManagerService.getDbConnection: exception: " + e);
            return null;
        }
    }

    /**
    * Release a previously gotten database connection back to the torque pool
    */
    private void releaseDbConnection(Connection connection) {
        Torque.closeConnection(connection);
    }
}
