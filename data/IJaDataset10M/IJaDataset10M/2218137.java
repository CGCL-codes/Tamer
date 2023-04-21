package org.tuotoo.tor.ordescription;

import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import org.tuotoo.crypto.MyRandom;
import org.tuotoo.logging.LogHolder;
import org.tuotoo.logging.LogLevel;
import org.tuotoo.logging.LogType;
import org.tuotoo.util.Base64;
import org.tuotoo.util.Base16;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

public final class ORList {

    private Vector m_onionrouters;

    private Vector m_exitnodes;

    private Vector m_middlenodes;

    private Hashtable m_onionroutersWithNames;

    private MyRandom m_rand;

    private ORListFetcher m_orlistFetcher;

    private Date m_datePublished;

    private int m_countHibernate;

    private static final DateFormat ms_DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        ms_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * constructor
     */
    public ORList(ORListFetcher fetcher) {
        m_onionrouters = new Vector();
        m_exitnodes = new Vector();
        m_middlenodes = new Vector();
        m_onionroutersWithNames = new Hashtable();
        m_orlistFetcher = fetcher;
        m_countHibernate = 0;
        m_rand = new MyRandom();
    }

    /**
     * size of the ORList
     * 
     * @return number of routers in the list
     */
    public synchronized int size() {
        return m_onionrouters.size();
    }

    public synchronized int active() {
        return size() - m_countHibernate;
    }

    public synchronized void setFetcher(ORListFetcher fetcher) {
        m_orlistFetcher = fetcher;
    }

    /**
     * Updates the list of available OR Routers.
     * 
     * @return true if it was ok, false otherwise
     */
    public synchronized boolean updateList() {
        try {
            byte[] buff = null;
            if (size() == 0 || (buff = m_orlistFetcher.getRouterStatus()) == null) {
                buff = m_orlistFetcher.getAllDescriptors();
                if (buff == null) return false;
                return parseFirstDocument(buff);
            } else {
                return parseStatus(buff, true);
            }
        } catch (Throwable t) {
            LogHolder.log(LogLevel.DEBUG, LogType.TOR, "There was a problem with fetching the available ORRouters: " + t.getMessage());
        }
        return false;
    }

    /**
     * returns a List of all onionrouters
     * 
     * @return List of ORDescriptions
     */
    public Vector getList() {
        return (Vector) m_onionrouters.clone();
    }

    /**
     * gets the date when the List was pubished
     * 
     * @return date
     */
    public Date getPublished() {
        return m_datePublished;
    }

    /**
     * gets an onion router by it's name
     * 
     * @param name
     *            name of the OR
     * @return ORDescription of the onion router
     */
    public synchronized ORDescriptor getByName(String name) {
        return (ORDescriptor) m_onionroutersWithNames.get(name);
    }

    /**
     * removes an onion router
     * 
     * @param name
     *            name of the OR
     */
    public synchronized void remove(String name) {
        ORDescriptor ord = getByName(name);
        if (ord == null) {
            return;
        }
        m_onionrouters.removeElement(ord);
        if (ord.isExitNode()) {
            m_exitnodes.removeElement(ord);
        } else {
            m_middlenodes.removeElement(ord);
        }
        m_onionroutersWithNames.remove(name);
    }

    /**
     * add an onion rotuer
     * 
     * @param ord
     *            descriptor for router
     */
    public synchronized void add(ORDescriptor ord) {
        if (ord.isExitNode()) {
            m_exitnodes.addElement(ord);
        } else {
            m_middlenodes.addElement(ord);
        }
        m_onionrouters.addElement(ord);
        m_onionroutersWithNames.put(ord.getName(), ord);
        LogHolder.log(LogLevel.DEBUG, LogType.TOR, "Added: " + ord);
    }

    /**
     * selects a OR randomly from a given list of allowed OR names
     * 
     * @param orlist
     *            list of onionrouter names
     * @return
     */
    public synchronized ORDescriptor getByRandom(Vector allowedNames) {
        if (active() == 0) {
            return null;
        }
        ORDescriptor ord;
        while (true) {
            String orName = (String) allowedNames.elementAt((m_rand.nextInt(allowedNames.size())));
            ord = getByName(orName);
            if (ord == null) {
                return null;
            }
            if (ord.getHibernate() == false) {
                break;
            }
        }
        return ord;
    }

    /**
     * selects a OR randomly (it should not hibernate)
     * 
     * @return
     */
    public synchronized ORDescriptor getByRandom() {
        if (active() == 0) {
            return null;
        }
        ORDescriptor ord;
        while (true) {
            ord = (ORDescriptor) this.m_onionrouters.elementAt(m_rand.nextInt(m_onionrouters.size()));
            if (ord.getHibernate() == false) {
                break;
            }
        }
        return ord;
    }

    /**
     * selects a OR randomly tries to blanace the probability of exit and
     * non-exit nodes
     * 
     * @param length
     *            length of the circuit
     * @return
     */
    public synchronized ORDescriptor getByRandom(int length) {
        if (active() == 0) {
            return null;
        }
        int number_of_routers = m_onionrouters.size();
        int numerator = length * m_exitnodes.size() - number_of_routers;
        int denominator = (length - 1) * number_of_routers;
        ORDescriptor ord;
        while (true) {
            if (m_rand.nextInt(denominator) > numerator) {
                ord = (ORDescriptor) this.m_middlenodes.elementAt(m_rand.nextInt(m_middlenodes.size()));
            } else {
                ord = (ORDescriptor) this.m_exitnodes.elementAt(m_rand.nextInt(m_exitnodes.size()));
            }
            if (ord.getHibernate() == false) {
                break;
            }
        }
        return ord;
    }

    /**
     * returns a ORDescription to the given ORName
     * 
     * @param name
     *            ORName
     * @return ORDescription if the OR exist, null else
     */
    public synchronized ORDescriptor getORDescriptor(String name) {
        if (this.m_onionroutersWithNames.containsKey(name)) {
            return (ORDescriptor) this.m_onionroutersWithNames.get(name);
        }
        return null;
    }

    /**
     * parse router status
     */
    private boolean parseStatus(byte[] document, boolean change) throws Exception {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(document)));
        String curLine = reader.readLine();
        StringTokenizer st;
        byte[] b;
        boolean hibernate = false;
        if (curLine == null || !curLine.startsWith("network-status-version")) {
            return false;
        }
        while (true) {
            reader.mark(200);
            curLine = reader.readLine();
            if (curLine == null) {
                break;
            }
            if (curLine.startsWith("published")) {
                st = new StringTokenizer(curLine, " ");
                st.nextToken();
                String strPublished = st.nextToken();
                strPublished += " " + st.nextToken();
            } else if (curLine.startsWith("r ")) {
                st = new StringTokenizer(curLine, " ");
                st.nextToken();
                String nick = st.nextToken();
                String hashKey = st.nextToken() + "=";
                String hashDescriptor = st.nextToken() + "=";
                String strPublished = st.nextToken();
                strPublished += " " + st.nextToken();
                String address = st.nextToken();
                String version;
                Vector options = new Vector();
                int port = Integer.parseInt(st.nextToken());
                reader.mark(200);
                curLine = reader.readLine();
                if (!curLine.startsWith("s ")) {
                    reader.reset();
                } else {
                    st = new StringTokenizer(curLine);
                    st.nextToken();
                    while (st.hasMoreTokens()) {
                        options.addElement(st.nextToken());
                    }
                }
                curLine = reader.readLine();
                if (curLine.startsWith("v ")) {
                    version = curLine.substring(2);
                } else if (curLine.startsWith("opt v ")) {
                    version = curLine.substring(6);
                } else {
                    reader.reset();
                }
                ORDescriptor ord = getORDescriptor(nick);
                String digest = Base16.encode((Base64.decode(hashDescriptor)));
                if ((ord == null) || ((ord.getHash() == null) || (!digest.equals(ord.getHash())))) {
                    b = m_orlistFetcher.getDescriptor(digest);
                    if (b != null) {
                        if (ord != null && ord.getHibernate()) {
                            hibernate = true;
                        }
                        remove(nick);
                        LineNumberReader l = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(b)));
                        ord = ORDescriptor.parse(l);
                        ord.setHash(digest);
                        if (hibernate && !ord.getHibernate()) {
                            m_countHibernate--;
                        }
                        add(ord);
                    }
                }
            }
        }
        return true;
    }

    /**
     * parses the document and creates a list with all ORDescriptions
     * 
     * @param strDocument
     * @throws Exception
     * @return false if document is not a valid directory, true otherwise
     */
    private synchronized boolean parseFirstDocument(byte[] document) throws Exception {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(document)));
        Date published = new Date();
        reader.mark(200);
        String curLine = reader.readLine();
        if (curLine == null) {
            return false;
        }
        m_countHibernate = 0;
        m_onionrouters = new Vector();
        m_exitnodes = new Vector();
        m_middlenodes = new Vector();
        m_onionroutersWithNames = new Hashtable();
        for (; ; ) {
            if (curLine.startsWith("router ")) {
                reader.reset();
                ORDescriptor ord = ORDescriptor.parse(reader);
                if (ord != null) {
                    if (ord.getHibernate()) {
                        m_countHibernate++;
                    }
                    add(ord);
                }
            }
            reader.mark(200);
            curLine = reader.readLine();
            if (curLine == null) {
                break;
            }
            if (curLine == null) {
                break;
            }
        }
        LogHolder.log(LogLevel.DEBUG, LogType.TOR, "Exit Nodes : " + m_exitnodes.size() + " Non-Exit Nodes : " + m_middlenodes.size());
        m_datePublished = published;
        return true;
    }
}
