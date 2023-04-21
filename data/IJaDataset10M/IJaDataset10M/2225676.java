package de.fhg.igd.semoa.net.farsight;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import codec.x501.BadNameException;
import de.fhg.igd.logging.LogLevel;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.fhg.igd.semoa.net.Util;
import de.fhg.igd.semoa.net.Vicinity;
import de.fhg.igd.semoa.net.ship.ShipConstants;
import de.fhg.igd.semoa.net.ship.ShipException;
import de.fhg.igd.semoa.net.ship.ShipService;
import de.fhg.igd.semoa.server.Environment;
import de.fhg.igd.semoa.service.AbstractService;
import de.fhg.igd.util.NoSuchObjectException;
import de.fhg.igd.util.ObjectExistsException;
import de.fhg.igd.util.ThreadPool;
import de.fhg.igd.util.URL;
import de.fhg.igd.util.VariableSubstitution;
import de.fhg.igd.util.WhatIs;

/**
 * A discovery service for SeMoA hosts that are outside of the LAN.
 * This implementation does not do the discovery itself. This job is left to
 * independent discovery modules that use a certain discovery strategy. All
 * this service does is to manage the list of known contacts. Each newly
 * discovered contact is checked to be reachable via
 * {@linkplain ShipService ship}.
 * If it is, it will be added to the list of known contacts. Every entry of
 * the contact list is periodically contacted by the {@link PollDaemon}. If
 * no contact can be established that host is removed from the contact list.
 * <p>
 * This objects behavior depends strongly on the <i>shared mode</i> option, 
 * which is set via the properties file. If shared mode is off, this service 
 * will ignore the other FarSight services in its vicinity (LAN). It will do 
 * the polling of all known contacts itself and will not broadcast new host 
 * discoveries or dead hosts (it will also not listen to broadcasts). If shared
 * mode is on, a FarSight coordinator is elected via 
 * {@linkplain Vicinity vicinities} voting service. Only this coordinator will 
 * do the polling of known contacts.
 * Listening to and sending of discovery broadcasts will also be enabled so
 * that the contact lists of the different FarSight services stay synchronized.
 * <p>
 * So this service operates in three different states:
 * <ul>
 * <li> shared mode off
 * <li> shared mode on and coordinator
 * <li> shared mode on and not coordinator
 * </ul>
 *
 * @author Jan Haevecker
 * @version "$id: $"
 * @see Vicinity
 * @see ShipService
 */
public class FarSightImpl extends AbstractService implements FarSight {

    /** 
     * The <code>Logger</code> instance for this class 
     */
    private static Logger log_ = LoggerFactory.getLogger("network/farsight");

    /**
     * The dependencies to other objects in the global <code>
     * Environment</code>.
     */
    private static final String[] DEPEND_ = { "WHATIS:VICINITY", "WHATIS:SHIP" };

    /**
     * The default shared mode setting.
     */
    public static final String DEFAULT_SHARED_MODE = "on";

    /**
     * The default poll delay.
     */
    public static String DEFAULT_POLL_DELAY = "5000";

    /**
     * The default thread pool size.
     */
    public static String DEFAULT_THREADPOOL_SIZE = "20";

    /**
     * The default multicast address.
     */
    public static final String DEFAULT_MULTICAST_ADDR = "239.255.28.12";

    /**
     * The default UDP port.
     */
    public static final String DEFAULT_MULTICAST_PORT = "40001";

    /** 
     * The {@link Vicinity} voting topic 
     */
    public static final String VOTE_TOPIC = "FarSightCoordinator";

    /**
     * self reference (Singelton Pattern) 
     */
    private static FarSightImpl instance_;

    /**
     * Stores the contacts that are known to FarSight 
     */
    private Map hosts_;

    /**
     * The DName of this SeMoA server's owner 
     */
    private String hostId_;

    /**
     * Looks for dead contacts and removes them 
     */
    private PollDaemon pollCoord_;

    /**
     * Communicates with the other FarSight services inside the LAN
     * (Vicinity) to synchronize the contact lists.
     */
    private UpdateDaemon multicastCoord_;

    /**
     * Manages the number of threads spawned by this service 
     */
    private ThreadPool threadPool_;

    /**
     * A path that points to the FarSight properties file 
     */
    private String props_;

    /**
     * Signals if this service should share it's discoveries (and found dead
     * contacts) with the rest of the network vicinity (LAN).
     */
    private boolean sharedMode_;

    /**
     * The default time (in ms) between two polls 
     */
    private int pollDelay_;

    /**
     * The multicast address 
     */
    private InetAddress addr_;

    /**
     * The multicast port 
     */
    private int port_;

    /**
     * The size of the {@link ThreadPool} that is used by the PollDaemon.
     */
    private int pollPoolSize_;

    /**
     * The size of the {@link ThreadPool} that is used for discovery
     * processing.
     */
    private int discoveryPoolSize_;

    /**
     * Sole constructor. Protected due to the singelton pattern.
     *
     * @param props A path that points to the FarSight properties file.
     */
    protected FarSightImpl(String props) throws FarSightException {
        super();
        try {
            hosts_ = new HashMap();
            props_ = props;
            hostId_ = Util.getHostId();
            configure();
            threadPool_ = new ThreadPool(discoveryPoolSize_);
            multicastCoord_ = new UpdateDaemon(hosts_, sharedMode_, addr_, port_);
            pollCoord_ = new PollDaemon(multicastCoord_, hosts_, sharedMode_, pollDelay_, pollPoolSize_);
            publish();
        } catch (Exception ex) {
            throw new FarSightException(ex);
        }
        if (sharedMode_) {
            doElection();
        }
    }

    public String author() {
        return "Jan Haevecker";
    }

    public boolean coordinator() {
        SecurityManager sm;
        boolean result;
        String coord;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new FarSightPermission("", "get"));
        } else {
            AccessController.checkPermission(new FarSightPermission("", "get"));
        }
        coord = Util.getVicinity().getCoordinator(VOTE_TOPIC);
        if (coord == null) {
            return true;
        }
        result = hostId_.equals(coord);
        return result;
    }

    public void discovered(Map newHosts) {
        SecurityManager sm;
        Map.Entry entry;
        Iterator it;
        URL value;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new FarSightPermission("", "set"));
        } else {
            AccessController.checkPermission(new FarSightPermission("", "set"));
        }
        if (newHosts == null) {
            throw new IllegalArgumentException("parameter was null");
        }
        it = newHosts.entrySet().iterator();
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
            try {
                value = (URL) entry.getValue();
            } catch (ClassCastException ex) {
                throw new ClassCastException("map value must be a URL");
            }
            if (value == null) {
                log_.debug("skipped a null value from: " + entry.getKey());
                continue;
            }
            if (!(filter(value))) {
                log_.debug("filter rejected host: " + value);
                continue;
            }
            discovered(value);
        }
    }

    /**
     * Notifies FarSight that a new host has been discovered. If the given url
     * is not in vicinity it will be tested if it is reachable via
     * {@linkplain ShipService ship}. If this test is also passed the contact
     * will be added to farsights contact list. Because ship request can take
     * some time, the verfication and adding of the contact are done by
     * dedicated threads. These threads are managed by a
     * {@linkplain ThreadPool}.
     *
     * @param hostURL Must point to the {@linkplain ShipService ship} port of
     *     that SeMoA host.
     */
    public void discovered(final URL hostURL) {
        SecurityManager sm;
        Runnable worker;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new FarSightPermission("", "set"));
        } else {
            AccessController.checkPermission(new FarSightPermission("", "set"));
        }
        if (hostURL == null) {
            throw new IllegalArgumentException("parameter was null");
        }
        log_.debug("trying to add: " + hostURL.toString());
        if (Util.isInVicinity(hostURL)) {
            log_.debug(hostURL + " is in vicinity, not added");
            return;
        }
        worker = new AddRunner(hostURL);
        try {
            threadPool_.run(worker);
        } catch (InterruptedException ex) {
        }
    }

    public Map getAllContacts() {
        SecurityManager sm;
        Map all;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new FarSightPermission("", "get"));
        } else {
            AccessController.checkPermission(new FarSightPermission("", "get"));
        }
        all = new HashMap();
        synchronized (hosts_) {
            all.putAll(hosts_);
        }
        all.putAll(getVicinityContacts());
        return all;
    }

    public Map getFarContacts() {
        SecurityManager sm;
        Map.Entry entry;
        Iterator i;
        Map ct;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new FarSightPermission("", "get"));
        } else {
            AccessController.checkPermission(new FarSightPermission("", "get"));
        }
        synchronized (hosts_) {
            ct = new HashMap(hosts_.size());
            for (i = hosts_.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                ct.put(entry.getKey(), entry.getValue());
            }
        }
        return ct;
    }

    public Map getVicinityContacts() {
        SecurityManager sm;
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new FarSightPermission("", "get"));
        } else {
            AccessController.checkPermission(new FarSightPermission("", "get"));
        }
        return Util.getVicinity().getContactTable();
    }

    /**
     * Returns a reference to this service. Part of the singelton pattern.
     *
     * @param props A path that points to the FarSight properties file. This
     *     is only needed if no instance of farsight exists yet.
     */
    public static FarSight getInstance(String props) throws FarSightException {
        try {
            synchronized (FarSightImpl.class) {
                if (instance_ == null) {
                    instance_ = new FarSightImpl(props);
                }
            }
        } catch (Exception ex) {
            log_.caught(LogLevel.SEVERE, "could not create instance", ex);
            FarSightException fse = new FarSightException("could not create instance", ex);
            log_.throwing(fse);
            throw fse;
        }
        return instance_;
    }

    public String info() {
        return "SeMoA's host discovery module";
    }

    public String revision() {
        return "$Revision: 1913 $/$Date: 2007-08-07 22:41:53 -0400 (Tue, 07 Aug 2007) $";
    }

    public String[] dependencies() {
        return (String[]) DEPEND_.clone();
    }

    /** 
     * Returns whether this service is running in the shared contacts mode 
     */
    public boolean sharedMode() {
        return sharedMode_;
    }

    public String toString() {
        StringBuffer result;
        Iterator it;
        Set keys;
        result = new StringBuffer();
        result.append(super.toString());
        result.append("\n\n----------------[config]----------------\n");
        result.append("\nSharedDataMode: " + sharedMode_);
        result.append("\nPollDelay: " + pollDelay_);
        result.append("\nMultiCastAddress: " + addr_);
        result.append("\nMultiCastPort: " + port_);
        result.append("\n\n----------------[status]----------------\n");
        result.append("\nCoordinator: " + coordinator());
        result.append("\nServerId: " + hostId_);
        result.append("\n\n----------------[known hosts]----------------\n");
        keys = getFarContacts().entrySet();
        it = keys.iterator();
        while (it.hasNext()) {
            result.append(it.next().toString() + "\n");
        }
        return result.toString();
    }

    /**
     * Removes an entry with a given key from the host map.
     * Additionally a broadcast of this event is done if FarSight is running in
     * shared mode.
     * @see Map#remove
     */
    protected void removeHost(String key) throws BadNameException, IOException {
        if (sharedMode_ == true) {
            multicastCoord_.broadcastRemove(key);
        }
        synchronized (hosts_) {
            hosts_.remove(key);
        }
    }

    /**
     * Adds an entry to the host map.
     * Additionally a broadcast of this entry is done if FarSight is running in
     * the shared mode.
     * @see Map#put
     */
    private void addHost(String key, URL value) {
        synchronized (hosts_) {
            hosts_.put(key, value);
        }
        log_.debug("added: " + key);
        if (sharedMode_ == true) {
            try {
                multicastCoord_.broadcastAdd(key, value.toString());
            } catch (Exception ex) {
                log_.warning("broadcast failed: " + ex.getMessage());
            }
        }
    }

    /**
     * Loads the configuration file 
     */
    private void configure() throws FileNotFoundException, IOException {
        Properties prop;
        prop = VariableSubstitution.parseConfigFile(props_, VariableSubstitution.SYSTEM_PROPERTIES | VariableSubstitution.SHELL_VARIABLES);
        sharedMode_ = DEFAULT_SHARED_MODE.equals(prop.getProperty("SharedDataMode", DEFAULT_SHARED_MODE));
        pollDelay_ = Integer.parseInt(prop.getProperty("PollDelay", DEFAULT_POLL_DELAY));
        addr_ = InetAddress.getByName(prop.getProperty("MulticastAddress", DEFAULT_MULTICAST_ADDR));
        port_ = Integer.parseInt(prop.getProperty("MulticastPort", DEFAULT_MULTICAST_PORT));
        pollPoolSize_ = Integer.parseInt(prop.getProperty("PollThreadPoolSize", DEFAULT_THREADPOOL_SIZE));
        discoveryPoolSize_ = Integer.parseInt(prop.getProperty("DiscoveryThreadPoolSize", DEFAULT_THREADPOOL_SIZE));
    }

    /**
     * {@link Vicinity} elects a FarSight coordinator 
     */
    private void doElection() {
        Vicinity vicinity;
        try {
            vicinity = Util.getVicinity();
            vicinity.registerTopic(VOTE_TOPIC, true);
        } catch (IOException ex) {
            log_.caught(LogLevel.SEVERE, "election error", ex);
        }
    }

    /**
     * Checks wether a given url might be added to the contact list 
     */
    private boolean filter(URL host) {
        boolean result;
        result = !(host.equals(Util.getLocalShipURL()));
        return result;
    }

    /**
     * Publishes this service in the {@link Environment} 
     */
    private void publish() throws ObjectExistsException {
        Environment env;
        String location;
        env = Environment.getEnvironment();
        location = WhatIs.stringValue(FarSight.WHATIS);
        env.publish(location, this, Environment.PLAIN_PROXY);
    }

    /**
     * Retracts this service from the {@link Environment} 
     */
    private void retract() throws NoSuchObjectException {
        Environment env;
        String location;
        env = Environment.getEnvironment();
        location = WhatIs.stringValue(FarSight.WHATIS);
        env.retract(location);
    }

    /**
     * Checks wether a given URL is reachable from this host
     * (uses {@link ShipService}).
     *
     * @param newContact The URL that is to be verified.
     * @return null, if the contact is invalid, else the string representation
     *     of the contacts owner X.501 Distinguished name (as of RFC1779).
     */
    private String verifyContact(URL newContact) {
        ShipService ship;
        Runnable scout;
        String result;
        result = null;
        try {
            ship = Util.getSHIP();
            if (!(ship.isAlive(newContact))) {
                log_.debug("host is not alive: " + newContact.toString());
            } else {
                result = ship.getValue(ShipConstants.OWNER_DN, newContact);
            }
        } catch (ShipException ex) {
            log_.debug("SHIP could not reach: " + newContact.toString());
        }
        return result;
    }

    /**
     * The process that verifies and adds a given contact to the contact table.
     *
     * @author Jan Haevecker
     * @version $id: $
     */
    private class AddRunner implements Runnable {

        private URL hostURL_;

        public AddRunner(URL contact) {
            hostURL_ = contact;
        }

        public void run() {
            String owner;
            owner = verifyContact(hostURL_);
            if (owner != null) {
                addHost(owner, hostURL_);
            }
        }
    }
}
