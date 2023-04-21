package com.funambol.syncclient.sps;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.rms.RecordStore;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.PIMException;
import com.funambol.syncclient.blackberry.Configuration;
import com.funambol.syncclient.blackberry.parser.EventParser;
import com.funambol.syncclient.blackberry.parser.ParserFactory;
import com.funambol.syncclient.util.StaticDataHelper;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.StringMatch;

/**
 * This class provide abstract methods
 * to access device database
 *
 */
public abstract class DataStore {

    public static final char RECORD_STATE_NEW = 'N';

    public static final char RECORD_STATE_DELETED = 'D';

    public static final char RECORD_STATE_UPDATED = 'U';

    public static final char RECORD_STATE_UNSIGNED = ' ';

    protected static final long PERSISTENCE_KEY = 0x50b137116d9be33cL;

    public static final int MAX_ITEM_NUMBER = 3;

    protected PIMList pimList;

    protected int page;

    protected Enumeration items;

    protected String alertCode;

    public static DataStore getDataStore(String datastoreName) {
        if (Configuration.contactSourceUri.equals(datastoreName)) {
            return new ContactDataStore();
        } else if (Configuration.mailSourceUri.equals(datastoreName)) {
            return new MailDataStore();
        } else {
            return new EventDataStore();
        }
    }

    protected DataStore() {
        pimList = null;
        page = MAX_ITEM_NUMBER;
    }

    /**
     * Set last timestamp in dedicate recordStore
     * @param lastTimestamp
     * @throws DataAccessException
     **/
    public abstract void setLastTimestamp(long lastTimestamp) throws DataAccessException;

    /**
     * @return last timestamp from dedicate recordstore
     * @throws DataAccessException
     **/
    public abstract long getLastTimestamp() throws DataAccessException;

    /**
     * if record exist in database, update records
     * if record not exist in database, add record
     * @param record record to store
     * @throws DataAccessException
     **/
    public abstract Record setRecord(Record record, boolean modify) throws DataAccessException;

    /**
     * Delete a record from the event database.
     * @param record
     * @throws DataAccessException
     */
    public abstract void deleteRecord(Record record) throws DataAccessException;

    /**
     * return no deleted records from device recordstore
     *
     * @return records found
     *
     * @throws DataAccessException
     **/
    public abstract boolean getNextRecords(Vector v) throws DataAccessException;

    /**
     * return record from recordstore
     * filter by record state
     *
     * @return records found
     *
     * @throws DataAccessException
     **/
    public abstract boolean getNextRecords(Vector v, char state) throws DataAccessException;

    public abstract void startDSOperations();

    public abstract void resetModificationCursor();

    /**
     * execute commit recordstore operations
     * remove records signed as DELETED 'D'
     * mark UNSIGNED ' ' records signed as NEW 'N' and UPDATED 'U'
     *
     * @throws DataAccessException
     *
     */
    public abstract void commitDSOperations() throws DataAccessException;

    public abstract void appendData(String data, long key);

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setAlertCode(String alertCode) {
        this.alertCode = alertCode;
    }
}
