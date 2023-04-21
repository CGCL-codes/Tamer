package net.sourceforge.j2mesafe;

import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

/**
 * holds a vector of Entry instances for the List
 */
public class EntryMgr {

    private static final String STORE = "j2mesafe.data";

    private static byte[] EMTY_DATA = new byte[0];

    private CryptMgr cryptMgr;

    private Vector entries = new Vector();

    public EntryMgr(CryptMgr cryptMgr) {
        this.cryptMgr = cryptMgr;
    }

    public void deleteAll() {
        try {
            RecordStore.deleteRecordStore(STORE);
        } catch (RecordStoreNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        entries.removeAllElements();
    }

    public Entry createEntry(String label, String body) {
        Entry entry = new Entry(label, body);
        entries.addElement(entry);
        saveEntry(entry);
        return entry;
    }

    public void deleteEntry(Entry entry) {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(STORE, true);
            rs.deleteRecord(entry.getId());
            entries.removeElement(entry);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeEntry(Entry entry) {
        saveEntry(entry);
    }

    private void saveEntry(Entry entry) {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(STORE, true);
            byte[] b = entry.getBytes();
            b = cryptMgr.encrypt(b);
            int id = entry.getId();
            if (id == 0) {
                id = rs.addRecord(b, 0, b.length);
                entry.setId(id);
            } else {
                rs.setRecord(id, b, 0, b.length);
            }
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * loads all records from store. CryptMgr must be initialized
   * to decrypt the entries
   */
    public void initialize() {
        RecordStore rs = null;
        RecordEnumeration en = null;
        entries.removeAllElements();
        try {
            rs = RecordStore.openRecordStore(STORE, true);
            en = rs.enumerateRecords(null, null, false);
            while (en.hasNextElement()) {
                int id = en.nextRecordId();
                byte[] bytes = rs.getRecord(id);
                bytes = cryptMgr.decrypt(bytes);
                Entry entry = new Entry(id, bytes);
                entries.addElement(entry);
            }
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } finally {
            if (en != null) en.destroy();
            try {
                if (rs != null) rs.closeRecordStore();
            } catch (RecordStoreException e1) {
                e1.printStackTrace();
            }
        }
    }

    public Vector getEntries() {
        return entries;
    }
}
