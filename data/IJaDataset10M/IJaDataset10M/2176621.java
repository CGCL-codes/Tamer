package jreceiver.server.bus;

import java.net.URL;
import org.apache.commons.logging.*;
import jreceiver.common.rec.site.*;
import jreceiver.server.db.KeyDB;
import jreceiver.server.db.SiteDB;
import jreceiver.server.util.db.DatabaseException;

/**
 * business logic layer code for Site management.
 *
 * @author Reed Esau
 * @version $Revision: 1.3 $ $Date: 2002/12/29 00:44:06 $
 */
public class SiteBus extends KeyBus {

    /** ctor */
    protected SiteBus(KeyDB db) {
        super(db);
    }

    protected SiteDB getDB() {
        return (SiteDB) getKeyDB();
    }

    /**
    * this class is implemented as a singleton
    */
    private static SiteBus singleton;

    /**
     * obtain an instance of this singleton
     * <p>
     * Note that this uses the questionable DCL pattern (search on
     * DoubleCheckedLockingIsBroken for more info)
     * <p>
     * @return the singleton instance for this JVM
     */
    public static SiteBus getInstance() {
        if (singleton == null) {
            synchronized (SiteBus.class) {
                if (singleton == null) singleton = new SiteBus(SiteDB.getInstance());
            }
        }
        return singleton;
    }

    /**
     * obtain the home content address; null if not set
     */
    public URL getHomeContentUrl() throws BusException {
        try {
            SiteRec rec = (SiteRec) getDB().getRec(Site.SITE_HOME, null);
            return rec.getContentURL();
        } catch (DatabaseException e) {
            throw new BusException("db-problem obtaining home content address", e);
        }
    }

    /**
     * assign the home content address
     */
    public void setHomeContentUrl(URL url) throws BusException {
        if (url == null) throw new IllegalArgumentException();
        try {
            SiteRec rec = (SiteRec) getDB().getRec(Site.SITE_HOME, null);
            rec.setContentURL(url);
            getDB().storeRec(rec);
        } catch (DatabaseException e) {
            throw new BusException("db-problem assigning home content address", e);
        }
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(SiteBus.class);
}
