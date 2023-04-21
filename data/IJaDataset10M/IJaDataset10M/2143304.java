package org.w3c.tidy.servlet.data;

import java.util.Map;
import java.util.Hashtable;
import org.w3c.tidy.servlet.ResponseRecord;
import org.w3c.tidy.servlet.ResponseRecordRepository;

/**
 * Static Class to store Validation results and Error.
 * @todo automatically remove old data
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision: 749 $ ($Author: fgiust $)
 */
public class DefaultResponseRecordRepository implements ResponseRecordRepository {

    private Map data;

    private Object lastPK;

    /**
     * Create the Repository
     */
    public DefaultResponseRecordRepository() {
        this.data = new Hashtable();
    }

    /**
     * {@inheritDoc}
     */
    public void addRecord(ResponseRecord result) {
        Object key = result.getRequestID();
        this.data.put(key, result);
        this.lastPK = key;
    }

    /**
     * {@inheritDoc}
     */
    public Object getLastPK() {
        return this.lastPK;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResponseID(String keyString) {
        if (keyString == null) {
            return new Long(0);
        }
        if (keyString.equalsIgnoreCase("last")) {
            return getLastPK();
        } else {
            return new Long(keyString);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseRecord getRecord(Object key) {
        if (key == null) {
            return null;
        }
        return (ResponseRecord) this.data.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseRecord getRecord(Object key, int sleep) {
        if (key == null) {
            return null;
        }
        ResponseRecord item = null;
        long stop = System.currentTimeMillis() + sleep;
        while ((item == null) && (stop > System.currentTimeMillis())) {
            item = getRecord(key);
            if ((item != null) || (sleep == 0)) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        return item;
    }
}
