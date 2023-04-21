package de.bender_dv.ardgate.application;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.bender_dv.ardgate.database.ArdConnection;

public class ActivationGroup {

    private static final Log log = LogFactory.getLog(ActivationGroup.class);

    private int activationGroup;

    private Hashtable<String, ArdConnection> ardcon;

    public ActivationGroup(int activationGroup) {
        super();
        this.activationGroup = activationGroup;
        ardcon = new Hashtable<String, ArdConnection>();
    }

    public void setAttribute(String connectionKey, ArdConnection con) {
        con.setKey(connectionKey);
        ardcon.put(connectionKey, con);
    }

    public void removeAttribute(String connectionKey) {
        ardcon.remove(connectionKey);
    }

    public ArdConnection getAttribute(String connectionKey) {
        return ardcon.get(connectionKey);
    }

    void commit() {
        ArdConnection ac;
        for (Enumeration<ArdConnection> e = ardcon.elements(); e.hasMoreElements(); ) {
            ac = e.nextElement();
            ac.commit();
            log.info("Commit: " + ac.getKey());
        }
    }

    void rollback() {
        ArdConnection ac;
        log.debug("Rollback");
        for (Enumeration<ArdConnection> e = ardcon.elements(); e.hasMoreElements(); ) {
            ac = e.nextElement();
            ac.rollback();
            log.info("Rollback: " + ac.getKey());
        }
    }
}
