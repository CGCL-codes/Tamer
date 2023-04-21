package net.sf.ooweb.sessions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ooweb.util.Base64;
import net.sf.ooweb.util.StringUtils;

/**
 * A single class that handles a replicated/distributed
 * cache. Uses UDP multicast to announce its
 * presence to other nodes and send updated values.
 * 
 * Deliberately wrote this functionality rather than
 * using swarmcache/similar products because:
 * 
 * 		1. It's small
 * 		2. You can shove it in your app without adding
 *    	   another jar.
 *      3. I can
 *    
 * Can be a bit confusing, basically, the static 
 * members of this class once started run forever
 * (listening for input) and handle comms, however the 
 * instance versions of this class are for manipulating 
 * tables keys and values only.
 * 
 * Just means from an end user perspective, you create
 * one with a table name and put/get values. The
 * rest is taken care of.
 * 
 * @author Robin Rawson-Tetley
 * @author Darren Davison
 * @since 0.5
 */
class ReplicatedHashtable implements java.util.Map {

    protected static final Logger logger = Logger.getLogger("net.sf.ooweb.sessions.ReplicatedHashtable");

    /** All the hashtables */
    private static Hashtable<String, Hashtable<Object, Object>> tables = new Hashtable<String, Hashtable<Object, Object>>();

    /** The communication handler */
    private static UDPHandler udp = null;

    /** The table this hash is hooked up to */
    private Hashtable<Object, Object> table = null;

    /** The name of this table */
    private String tableName = "";

    /** The UUID representing the current node */
    private static String node = StringUtils.generateUUID();

    /** The multicast group used for sending/receiving packets */
    private static String MULTICAST_GROUP = "230.0.0.1";

    /** Separator used in protocol */
    private static String SEPARATOR = "||||";

    /** Terminator of messages in datagram packets */
    private static String TERMINATOR = "****";

    /** Whether or not we have announced our presence */
    protected static boolean announced = false;

    /** The default port to use for UDP communication */
    public static final int DEFAULT_PORT = 32473;

    /** The size of the incoming buffer in bytes */
    public static final int INCOMING_BUFFER = 65535;

    /** Thread sleep time in ms to use on receive
	 *  thread. */
    public static final int SLEEP_LENGTH = 50;

    /**
	 * Allows access to a replicated hashtable with the
	 * name given. If no hashtable exists for the given
	 * tableName, it is automatically created and announced
	 * to other nodes.
	 * 
	 * @param cacheName
	 */
    public ReplicatedHashtable(String tableName) {
        create(tableName);
    }

    /**
	 * Performs the creation of the replicated table.
	 * @param tableName
	 */
    protected void create(String tableName) {
        this.tableName = tableName;
        table = tables.get(tableName);
        if (table == null) {
            table = new Hashtable<Object, Object>();
            tables.put(tableName, table);
            if (logger.isLoggable(Level.INFO)) logger.info("Created new replicated hashtable [" + tableName + "]");
            announceNewTable(tableName);
        }
    }

    public Object get(Object key) {
        return table.get(key);
    }

    public Object put(Object key, Object value) {
        table.put(key, value);
        announceNewValue(tableName, key, value);
        return value;
    }

    public Object remove(Object key) {
        table.remove(key);
        Object o = table.get(key);
        announceDeletedValue(tableName, key);
        return o;
    }

    /**
	 * Destroys the current table and informs all other
	 * nodes to do so.
	 */
    public void delete() {
        announceDeletedTable(tableName);
    }

    protected static void announceNewTable(String tableName) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Announcing new table [" + tableName + "]");
        udp.send("1" + tableName + TERMINATOR);
    }

    /**
	 * Sends a message to other nodes announcing
	 * a new/updated value in a table.
	 * 
	 * @param tableName
	 * @param key
	 * @param value
	 */
    protected static void announceNewValue(String tableName, Object key, Object value) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Announcing new/updated value in table: " + tableName);
        udp.send("2" + tableName + SEPARATOR + serialize(key) + SEPARATOR + serialize(value) + TERMINATOR);
    }

    /**
	 * Send a message to other nodes announcing a
	 * deleted value in a table
	 * @param tableName
	 * @param key
	 */
    protected static void announceDeletedValue(String tableName, Object key) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Announcing deletion from table: " + tableName);
        udp.send("3" + tableName + SEPARATOR + serialize(key) + TERMINATOR);
    }

    /**
	 * Sends a message to other nodes announcing a clear()
	 * operation on a table
	 */
    protected static void announceClearedTable(String tableName) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Announcing table clear: " + tableName);
        udp.send("4" + tableName + TERMINATOR);
    }

    /**
	 * Sends a message to other nodes announcing the deletion
	 * of a table
	 */
    protected static void announceDeletedTable(String tableName) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Announcing table deletion: " + tableName);
        udp.send("5" + tableName + TERMINATOR);
    }

    /**
	 * Gets the serialized version of an object
	 * as a string
	 * @param o The object to serialize
	 * @return The object's byte stream as a string
	 */
    private static String serialize(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            return new String(Base64.encode(baos.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**					
	 * Takes a string of serialized object data
	 * and turns it back into an object.
	 * 
	 * @param o
	 * @return The deserialized object, or null if
	 * deserialization failed.
	 */
    private static Object deserialize(String o) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(o.toCharArray()));
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Sends a message to other nodes announcing
	 * this node. Also generates a UUID to represent
	 * this node and sets up the listening/sending
	 * sockets.
	 */
    protected static void announceMe() {
        if (!announced) {
            if (logger.isLoggable(Level.FINE)) logger.fine("Broadcasting announcement I exist to *:" + DEFAULT_PORT);
            udp = new UDPHandler();
            udp.setName("RHUDP");
            udp.start();
            udp.send("0" + node + TERMINATOR);
            announced = true;
        }
    }

    /**
	 * Handles all UDP communication
	 *
	 * To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Generation - Code and Comments
	 */
    private static class UDPHandler extends Thread {

        public void run() {
            try {
                byte[] buf = new byte[INCOMING_BUFFER];
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                MulticastSocket s = new MulticastSocket(DEFAULT_PORT);
                InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
                s.joinGroup(group);
                while (true) {
                    try {
                        s.receive(p);
                        gotData(p);
                    } catch (Exception e) {
                    }
                    try {
                        Thread.sleep(SLEEP_LENGTH);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
		 * Called when a datagram packet is received
		 * to be parsed and the new data injected.
		 * 
		 * @param p
		 */
        private void gotData(DatagramPacket p) {
            byte[] b = p.getData();
            String d = new String(b);
            d = d.substring(0, d.indexOf(TERMINATOR));
            switch(Integer.parseInt(d.substring(0, 1))) {
                case 0:
                    if (logger.isLoggable(Level.FINE)) logger.fine("Received announcement from new node: " + d.substring(1));
                    break;
                case 1:
                    String table = d.substring(1);
                    if (logger.isLoggable(Level.FINE)) logger.fine("Received announcement of new table: " + table);
                    tables.put(table, new Hashtable<Object, Object>());
                    break;
                case 2:
                    table = d.substring(1, d.indexOf(SEPARATOR));
                    int firstsep = d.indexOf(SEPARATOR);
                    int secondsep = d.indexOf(SEPARATOR, firstsep + SEPARATOR.length() + 1);
                    String keyData = d.substring(firstsep + SEPARATOR.length(), secondsep);
                    Object key = deserialize(keyData);
                    if (logger.isLoggable(Level.FINE)) logger.fine("Received announcement of new value: " + key + " in table: " + table);
                    Object value = deserialize(d.substring(secondsep + SEPARATOR.length()));
                    Hashtable<Object, Object> t = tables.get(table);
                    if (t == null) {
                        if (logger.isLoggable(Level.FINE)) logger.fine("(Creating non-existent table: " + table + ")");
                        t = new Hashtable<Object, Object>();
                        tables.put(table, t);
                    }
                    t.put(key, value);
                    break;
                case 3:
                    table = d.substring(1, d.indexOf(SEPARATOR));
                    firstsep = d.indexOf(SEPARATOR);
                    key = deserialize(d.substring(firstsep + SEPARATOR.length()));
                    if (logger.isLoggable(Level.FINE)) logger.fine("Deleting value: " + key.toString() + " in table: " + table);
                    t = tables.get(table);
                    t.remove(key);
                    break;
                case 4:
                    table = d.substring(1);
                    if (logger.isLoggable(Level.FINE)) logger.fine("Clearing table: " + table);
                    t = tables.get(table);
                    if (t != null) t.clear();
                    break;
                case 5:
                    table = d.substring(1);
                    if (logger.isLoggable(Level.FINE)) logger.fine("Removing table: " + table);
                    t = tables.get(table);
                    if (t != null) t.clear();
                    tables.remove(table);
                    t = null;
                    break;
                default:
                    if (logger.isLoggable(Level.FINE)) logger.fine("WARNING: Unable to handle message: " + d);
            }
        }

        /**
		 * Sends a string of data to the other nodes
		 * @param data
		 */
        public void send(String data) {
            try {
                DatagramPacket p = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(MULTICAST_GROUP), DEFAULT_PORT);
                DatagramSocket sock = new DatagramSocket();
                sock.send(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * @see java.util.Map#clear()
	 */
    public void clear() {
        announceClearedTable(tableName);
        table.clear();
    }

    /**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
    public boolean containsKey(Object key) {
        return table.containsKey(key);
    }

    /**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
    public boolean containsValue(Object value) {
        return table.containsValue(value);
    }

    /**
	 * @see java.util.Map#entrySet()
	 */
    public Set entrySet() {
        return table.entrySet();
    }

    /**
	 * @see java.util.Map#isEmpty()
	 */
    public boolean isEmpty() {
        return table.isEmpty();
    }

    /**
	 * @see java.util.Map#keySet()
	 */
    public Set keySet() {
        return table.keySet();
    }

    /**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
    public void putAll(Map t) {
        throw new UnsupportedOperationException("Not currently valid for replication.");
    }

    /**
	 * @see java.util.Map#size()
	 */
    public int size() {
        return table.size();
    }

    /**
	 * @see java.util.Map#values()
	 */
    public Collection values() {
        return table.values();
    }
}
