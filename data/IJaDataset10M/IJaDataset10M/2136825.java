package helpers;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Vanganh
 */
public class RMSHelper {

    /**
     * open a record store
     * @param store
     * @return a record store
     */
    public static RecordStore openRecordStore(String store) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(store, true);
            return rs;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * close a record store
     * @param rs
     */
    public static void closeRecordStore(RecordStore rs) {
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }
}
