package macyou;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Macyou
 */
public class RMS {

    private static RMS instance = null;

    private RecordStore rs = null;

    public RMS() {
    }

    /**
     * Save current recite progress.
     * @param w
     * @param number
     * @param n
     */
    public void save(String dictName, int number, int n) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(dictName);
            dos.writeInt(number);
            dos.writeInt(n);
            byte rec[] = baos.toByteArray();
            baos.close();
            dos.close();
            int num = rs.getNumRecords();
            if (num > 0) {
                rs.setRecord(1, rec, 0, rec.length);
            } else {
                rs.addRecord(rec, 0, rec.length);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ByteArrayToRecord get() {
        try {
            int num = rs.getNumRecords();
            System.out.println("records num: " + num);
            if (num > 0) {
                byte[] record = rs.getRecord(1);
                ByteArrayToRecord br = new ByteArrayToRecord(record);
                return br;
            }
        } catch (Exception e) {
            System.err.println("Failed opening record store !");
        }
        return null;
    }

    public void open() {
        try {
            rs = RecordStore.openRecordStore("mrecite", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closeRs() {
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized RMS getInstance() {
        if (instance == null) {
            instance = new RMS();
        }
        return instance;
    }
}
