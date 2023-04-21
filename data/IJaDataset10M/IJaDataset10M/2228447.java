package jreceiver.server.bus;

import java.util.*;
import org.apache.commons.logging.*;
import jreceiver.common.rec.driver.*;
import jreceiver.server.db.SignalDB;
import jreceiver.server.db.DriverBindingDB;
import jreceiver.server.db.KeyDB;
import jreceiver.server.util.db.*;

/**
 * business logic layer code related to infrared (and other) Signals.
 *
 * @author Reed Esau
 * @version $Revision: 1.3 $ $Date: 2002/12/29 00:44:06 $
 */
public class SignalBus extends KeyBus {

    /**
    * this class is implemented as a singleton
    */
    private static SignalBus singleton;

    /**
     * obtain an instance of this singleton
     * <p>
     * Note that this uses the questionable DCL pattern (search on
     * DoubleCheckedLockingIsBroken for more info)
     * <p>
     * @return the singleton instance for this JVM
     */
    public static SignalBus getInstance() {
        if (singleton == null) {
            synchronized (SignalBus.class) {
                if (singleton == null) singleton = new SignalBus(SignalDB.getInstance());
            }
        }
        return singleton;
    }

    /** ctor */
    protected SignalBus(KeyDB db) {
        super(db);
    }

    protected SignalDB getDB() {
        return (SignalDB) getKeyDB();
    }

    /**
     * Obtain a total count of signal keys for a driverbind
     */
    public int getKeyCountForDriverBinding(DriverBindingKey drvbind_key) throws BusException {
        if (log.isDebugEnabled()) log.debug("getKeyCountForDriverBinding: drvbind_key=" + drvbind_key);
        try {
            DriverBindingDB drvbind_db = DriverBindingDB.getInstance();
            String filter = drvbind_db.getFilterForKey(drvbind_key);
            return getKeyCount(filter);
        } catch (DatabaseException e) {
            throw new BusException("db-problem obtaining key count for drvbind", e);
        }
    }

    /**
     * Obtain a list of signal keys for the specified driverbind definition
     */
    public Vector getKeysForDriverBinding(DriverBindingKey drvbind_key, String order_by, int rec_offset, int rec_count) throws BusException {
        if (log.isDebugEnabled()) log.debug("getKeysForDriverBinding: drvbind_key=" + drvbind_key);
        try {
            DriverBindingDB drvbind_db = DriverBindingDB.getInstance();
            String filter = drvbind_db.getFilterForKey(drvbind_key);
            return getKeys(filter, order_by, rec_offset, rec_count);
        } catch (DatabaseException e) {
            throw new BusException("db-problem obtaining keys for drvbind", e);
        }
    }

    /**
     * Obtain a list of signals for the specified driverbind definition
     */
    public Vector getRecsForDriverBinding(DriverBindingKey drvbind_key, String order_by, int rec_offset, int rec_count) throws BusException {
        if (log.isDebugEnabled()) log.debug("getRecsForDriverBinding: drvbind_key=" + drvbind_key);
        try {
            DriverBindingDB drvbind_db = DriverBindingDB.getInstance();
            String filter = drvbind_db.getFilterForKey(drvbind_key);
            return getRecs(filter, order_by, null, rec_offset, rec_count);
        } catch (DatabaseException e) {
            throw new BusException("db-problem obtaining recs for drvbind", e);
        }
    }

    /**
     * Record a signal for the specified device, storing the results.
     */
    public void record(SignalKey key, Device device) throws BusException {
        if (log.isDebugEnabled()) log.debug("record: key=" + key + " device=" + device);
        DriverBus drv_bus = DriverBus.getInstance();
        String sig_data = drv_bus.recordSignalData(device);
        storeRec(new SignalRec(key, sig_data, null, null));
    }

    /**
     * Playback the signal for the specified device.
     */
    public void play(Signal sig, Device device) throws BusException {
        if (log.isDebugEnabled()) log.debug("play: sig=" + sig + " device=" + device);
        DriverBus drv_bus = DriverBus.getInstance();
        drv_bus.playSignalData(device, sig.getSignalData());
    }

    /**
     * clear a signal's data
     */
    public void clear(SignalKey key) throws BusException {
        storeRec(new SignalRec(key, null, null, null));
    }

    /**
    * logging sink
    */
    protected static Log log = LogFactory.getLog(SignalBus.class);
}
