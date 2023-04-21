package microChatter.settings;

import mobi.ilabs.common.Debug;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.rms.RecordStore;
import mobi.ilabs.markup.MarkupElement;
import mobi.ilabs.markup.MarkupReader;
import mobi.ilabs.common.Base64;

/**
* @author �ystein Myhre
*/
public class PersistentStore implements Debug.Constants {

    private static final String RECORD_STORE_NAME = "PersistentStore";

    private static MarkupElement settings;

    static {
        settings = new MarkupElement("settings");
        read();
        if (DEBUG) Debug.println("PersistentStore.<init>: settings=" + settings);
    }

    private PersistentStore() {
    }

    public static void setAttribute(String key, String value) {
        settings.setAttribute(key, value);
    }

    public static String getAttribute(String key) {
        return (settings.getAttribute(key));
    }

    public static void addRecord(MarkupElement record) {
        settings.addChild(record);
    }

    public static Vector getRecords(String tagname) {
        return (settings.getChildren(tagname));
    }

    public static MarkupElement getRecord(String tagname, boolean create) {
        MarkupElement record = settings.getChild(tagname);
        if (record == null && create) {
            record = new MarkupElement(tagname);
            addRecord(record);
        }
        return (record);
    }

    public static void removeRecord(MarkupElement record) {
        settings.removeChild(record);
    }

    public static void write() {
        RecordStore rs = null;
        try {
            RecordStore.deleteRecordStore(RECORD_STORE_NAME);
        } catch (Exception ignore) {
        }
        Debug.log("PersistentStore.write", "PersistentStore Write: " + settings);
        try {
            rs = RecordStore.openRecordStore(RECORD_STORE_NAME, true);
            byte[] bytes = Base64.encode(settings.toXML().getBytes("utf-8"));
            rs.addRecord(bytes, 0, bytes.length);
        } catch (Exception e) {
            Debug.EXCEPTION("PersistentStore.write", e);
        } finally {
            try {
                rs.closeRecordStore();
            } catch (Exception ignore) {
            }
        }
    }

    private static void read() {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(RECORD_STORE_NAME, true);
            if (rs.getNumRecords() < 1) return;
            byte[] bytes = new byte[rs.getRecordSize(1)];
            rs.getRecord(1, bytes, 0);
            InputStream inputStream = new ByteArrayInputStream(Base64.decode(bytes));
            MarkupReader reader = new MarkupReader(inputStream);
            settings = reader.readElement(reader.readToken());
        } catch (Exception e) {
            if (DEBUG) Debug.EXCEPTION("PersistentStore.read", e);
        } finally {
            try {
                rs.closeRecordStore();
            } catch (Exception ignore) {
            }
        }
        Debug.log("PersistentStore.read", "PersistentStore Read: " + settings);
    }
}
