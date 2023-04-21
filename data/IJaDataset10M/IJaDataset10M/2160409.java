package jdc.lib;

import java.util.LinkedList;
import java.util.Iterator;
import java.net.URL;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * <p>Title: Java DirectConnect client and lib</p>
 * <p>Description: This class represents storage of the root hub list</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class HubListStorage {

    /** get the logger for this package */
    protected static Logger libLogger = LoggerContainer.getLogger(HubListStorage.class);

    /** Linked list of root server URL:s. */
    private LinkedList _root_servers = new LinkedList();

    public HubListStorage() {
    }

    /**
   * Add a URL to the storage.
   * @param a_url The URL to add.
   * @todo Find duplicate
   */
    public void add(URL a_url) {
        if (!_root_servers.contains(a_url)) _root_servers.add(a_url);
        Configuration.instance().addListHost(a_url.toString());
        Configuration.instance()._storeValues();
    }

    public void remove(URL a_url) {
        _root_servers.remove(a_url);
        Iterator itr = getIterator();
        String updated = "";
        String nxt = "";
        while (itr.hasNext()) {
            nxt = (String) itr.next();
            updated += nxt;
            if (itr.hasNext()) updated += ",";
        }
    }

    public Iterator getIterator() {
        initFromString(Configuration.instance().listHosts());
        return _root_servers.iterator();
    }

    /**
   * Initialize this object from a ';' separated string of URL:s.
   *
   * @param to_parse The string to parse
   */
    public void initFromString(String to_parse) {
        _root_servers.clear();
        StringTokenizer tokenizer = new StringTokenizer(to_parse, ",");
        while (tokenizer.hasMoreTokens()) {
            String a_url = tokenizer.nextToken();
            try {
                _root_servers.add(new URL(a_url));
            } catch (Throwable e) {
                Configuration.instance().executeExceptionCallback(e);
            }
        }
    }
}
