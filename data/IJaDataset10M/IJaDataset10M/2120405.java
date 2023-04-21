package uk.co.weft.pres.linkchecker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Vector;
import uk.co.weft.alert.AlertingException;
import uk.co.weft.alert.URLWatcher;
import uk.co.weft.dbutil.*;
import uk.co.weft.pres.server.*;

/**
 * A URL watcher which knows about hiding and deleting policies. It has to
 * have access to the database in order to retrieve and update data about the
 * link it watches.
 */
public class LinkWatcher extends URLWatcher {

    /**
	 * the magic token I will use to look up my database user name in my
	 * configuration
	 */
    public static final String USERNAMETOKEN = "watcher_db_username";

    /**
	 * the magic token I will use to look up my database password in my
	 * configuration
	 */
    public static final String PASSWORDTOKEN = "watcher_db_password";

    /** the key of the link I watch */
    Integer link = null;

    /** the url I use to access the database */
    String dburl = null;

    /** the password I use to access the database */
    String password = "xxxx";

    /** the username I use to access the database */
    String username = "watcher";

    /**
	 * initialise me with this configuration
	 */
    public void init(Map config) throws AlertingException {
        Object v = config.get(Link.KEYFN);
        if ((v != null) && v instanceof Integer) {
            link = (Integer) v;
        }
        v = config.get(USERNAMETOKEN);
        if (v != null) {
            username = v.toString();
        }
        v = config.get(PASSWORDTOKEN);
        if (v != null) {
            password = v.toString();
        }
        v = config.get(ConnectionPool.DBURLMAGICTOKEN);
        if (v != null) {
            dburl = v.toString();
        }
        Context context = createContext();
        config.put(URLWatcher.URLTOKEN, context.getValueAsString(Link.URLFN));
        super.init(config);
    }

    /**
	 * The standard URL Watcher generates email as soon as the page is
	 * unavailable. That isn't the behaviour we want...
	 */
    protected Vector check() throws Exception {
        Context context = createContext();
        Vector result = null;
        Vector alerts = super.check();
        if (!alerts.isEmpty()) {
            Integer fails = context.getValueAsInteger(Link.SPIDERFAILSFN);
            Integer notify = context.getValueAsInteger(LinkPolicy.NEDITORFN);
            Integer hide = context.getValueAsInteger(LinkPolicy.HIDEFN);
            Integer remove = context.getValueAsInteger(LinkPolicy.REMOVEFN);
            if (fails == null) {
                fails = new Integer(1);
            }
            int f = fails.intValue() + 1;
            context.put(Link.SPIDERFAILSFN, f);
            if ((remove != null) && (remove.intValue() >= f)) {
                TableDescriptor.getDescriptor(Link.TABLENAME, Link.KEYFN, context).drop(context);
            } else {
                if (hide != null && hide.intValue() >= f) {
                    context.put(Link.HIDDENFN, Boolean.TRUE);
                }
                if (notify != null && notify.intValue() >= f) {
                    result = alerts;
                }
                TableDescriptor.getDescriptor(Link.TABLENAME, Link.KEYFN, context).store(context);
            }
        }
        return result;
    }

    /**
	 * create me a context, preloaded with database values about the URL I
	 * watch; update my instance variable values if the underlying database
	 * has changed
	 */
    private Context createContext() throws AlertingException {
        Context context = new Context();
        context.put(ConnectionPool.DBURLMAGICTOKEN, dburl);
        context.put(ConnectionPool.DBPASSMAGICTOKEN, password);
        context.put(ConnectionPool.DBUSERMAGICTOKEN, username);
        context.put(Link.KEYFN, link);
        try {
            TableDescriptor.getDescriptor(Link.TABLENAME, Link.KEYFN, context).fetch(context);
            TableDescriptor.getDescriptor(LinkPolicy.TABLENAME, LinkPolicy.KEYFN, context).fetch(context);
            path = context.getValueAsString(Link.URLFN);
            try {
                watched = new URL(path);
            } catch (MalformedURLException e1) {
                throw new AlertingException("Bad URL syntax: " + path);
            }
        } catch (DataStoreException e) {
            throw new AlertingException(e.getMessage());
        }
        return context;
    }
}
