package org.blackchat;

import org.imme.util.Log;
import javax.microedition.rms.RecordStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Options {

    private static final int OPTIONS_VERSION2 = 2;

    private static final Log log = Log.getInstance("blackchat");

    private static final String STORE_NAME = "blackchat";

    public static final String[] SORT_ID = new String[] { "UIN", "Status", "NickName", "LastName", "FullName" };

    public String user = null;

    public String password = null;

    public int numConnectRetry = 2;

    public boolean isConnectRetry = numConnectRetry > 0;

    public String skin = SkinInfo.SKIN_ID[0];

    public boolean foregroundBuzz = true;

    public boolean foregroundNotice = true;

    public boolean backgroundBuzz = true;

    public boolean backgroundNotice = true;

    public String sort = SORT_ID[0];

    public boolean hideOffline = false;

    public String[] contacts = new String[0];

    public void save() {
        log.enter("Options.save");
        log.out("Saving options " + this.toString());
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(out);
            dout.writeInt(OPTIONS_VERSION2);
            byte[] record1 = out.toByteArray();
            dout.close();
            out.close();
            out = new ByteArrayOutputStream();
            dout = new DataOutputStream(out);
            dout.writeUTF(user);
            dout.writeUTF(password);
            dout.writeInt(numConnectRetry);
            dout.writeUTF(skin);
            dout.writeBoolean(foregroundNotice);
            dout.writeBoolean(backgroundNotice);
            dout.writeUTF(sort);
            dout.writeBoolean(hideOffline);
            byte[] record2 = out.toByteArray();
            dout.close();
            out.close();
            out = new ByteArrayOutputStream();
            dout = new DataOutputStream(out);
            dout.writeInt(contacts.length);
            for (int i = 0; i < contacts.length; i++) {
                dout.writeUTF(contacts[i]);
            }
            byte[] record3 = out.toByteArray();
            dout.close();
            out.close();
            RecordStore store = RecordStore.openRecordStore(STORE_NAME, true);
            if (store.getNumRecords() == 0) {
                store.addRecord(record1, 0, record1.length);
                store.addRecord(record2, 0, record2.length);
                store.addRecord(record3, 0, record3.length);
            } else if (store.getNumRecords() == 2) {
                store.setRecord(1, record1, 0, record1.length);
                store.setRecord(2, record2, 0, record2.length);
                store.addRecord(record3, 0, record3.length);
            } else {
                store.setRecord(1, record1, 0, record1.length);
                store.setRecord(2, record2, 0, record2.length);
                store.setRecord(3, record3, 0, record3.length);
            }
            store.closeRecordStore();
        } catch (Exception e) {
            log.out("*** Exception saving options: " + e.getMessage());
        }
        log.exit();
    }

    public static Options load() {
        log.enter("Options.load");
        Options options = new Options();
        try {
            RecordStore store = RecordStore.openRecordStore(STORE_NAME, true);
            log.out("RecordStore has " + store.getNumRecords() + " records");
            if (store.getNumRecords() > 0) {
                if (store.getNumRecords() == 2) options.readV1(store); else options.read(store);
            }
            store.closeRecordStore();
        } catch (Exception e) {
            log.out("*** Exception loading options: " + e.getMessage());
        }
        log.exit();
        return options;
    }

    public static void deleteStore() {
        try {
            RecordStore.deleteRecordStore(STORE_NAME);
        } catch (Exception e) {
        }
    }

    private void read(RecordStore store) throws Exception {
        log.enter("Options.read");
        log.out("Loading record 1");
        byte[] data = store.getRecord(1);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(in);
        int version = din.readInt();
        din.close();
        in.close();
        log.out("Options version " + version);
        switch(version) {
            case OPTIONS_VERSION2:
                log.out("Loading record 2");
                data = store.getRecord(2);
                in = new ByteArrayInputStream(data);
                din = new DataInputStream(in);
                user = din.readUTF();
                password = din.readUTF();
                numConnectRetry = din.readInt();
                isConnectRetry = numConnectRetry > 0;
                skin = din.readUTF();
                foregroundNotice = din.readBoolean();
                backgroundNotice = din.readBoolean();
                sort = din.readUTF();
                hideOffline = din.readBoolean();
                din.close();
                in.close();
                log.out("Loading record 3");
                data = store.getRecord(3);
                in = new ByteArrayInputStream(data);
                din = new DataInputStream(in);
                int numContacts = din.readInt();
                if (numContacts > 0) {
                    contacts = new String[numContacts];
                    for (int i = 0; i < numContacts; i++) contacts[i] = din.readUTF();
                }
                din.close();
                in.close();
                log.out("Loaded options " + this);
                break;
            default:
                log.out("Invalid options version " + version);
        }
        log.exit();
    }

    private void readV1(RecordStore store) throws Exception {
        log.enter("Options.readV1");
        log.out("Loading record 1");
        byte[] data = store.getRecord(1);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(in);
        user = din.readUTF();
        password = din.readUTF();
        din.readBoolean();
        numConnectRetry = din.readInt();
        isConnectRetry = numConnectRetry > 0;
        if (din.available() > 0) skin = din.readUTF();
        if (din.available() > 0) foregroundBuzz = din.readBoolean();
        if (din.available() > 0) foregroundNotice = din.readBoolean();
        if (din.available() > 0) backgroundBuzz = din.readBoolean();
        if (din.available() > 0) backgroundNotice = din.readBoolean();
        din.close();
        in.close();
        log.out("Loading record 2");
        data = store.getRecord(2);
        in = new ByteArrayInputStream(data);
        din = new DataInputStream(in);
        int numContacts = din.readInt();
        if (numContacts > 0) {
            contacts = new String[numContacts];
            for (int i = 0; i < numContacts; i++) contacts[i] = din.readUTF();
        }
        din.close();
        in.close();
        log.out("Loaded options " + this);
        log.exit();
    }

    public String toString() {
        return "user=" + user + " password=" + password + " connect retry #=" + numConnectRetry + " skin=" + skin + " fg notice=" + foregroundNotice + " bg notice=" + backgroundNotice + " sort=" + sort + " hide offline=" + hideOffline;
    }
}
