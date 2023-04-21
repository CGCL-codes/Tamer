package br.com.dreamsource.mobile.jmesql.io;

import java.util.Enumeration;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;
import br.com.dreamsource.mobile.jmesql.exceptions.SQLException;
import java.util.Hashtable;

public class Properties extends Hashtable {

    /** Creates a new instance of Properties */
    private String FileName;

    private RecordStore RecStore;

    private String SEPARATOR = "}->{";

    private boolean isOpen;

    public Properties() {
    }

    public boolean checkFileExists() {
        try {
            RecStore = RecordStore.openRecordStore(this.FileName, false);
            RecStore.closeRecordStore();
            return true;
        } catch (RecordStoreNotFoundException rsnfe) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void open() throws SQLException {
        try {
            RecStore = RecordStore.openRecordStore(this.FileName, true);
            RecordEnumeration RecEnum = RecStore.enumerateRecords(null, null, false);
            while (RecEnum.hasNextElement()) {
                String buff = new String(RecEnum.nextRecord());
                String Key = buff.substring(0, buff.indexOf(this.SEPARATOR));
                StringBuffer Buff = new StringBuffer(buff);
                Buff.delete(0, buff.indexOf(this.SEPARATOR) + 4);
                String Value = Buff.toString();
                put(Key, Value);
            }
            this.isOpen = true;
        } catch (RecordStoreException e) {
            new SQLException(e.getMessage());
        }
    }

    public void save() throws SQLException {
        try {
            Enumeration Enum = keys();
            if (this.checkFileExists()) {
                RecStore.closeRecordStore();
            }
            this.open();
            while (Enum.hasMoreElements()) {
                String key = (String) Enum.nextElement();
                String value = (String) get(key);
                String rec = key + this.SEPARATOR + value;
                RecStore.addRecord(rec.getBytes(), 0, rec.getBytes().length);
            }
        } catch (Exception e) {
            String Str = e.getMessage();
            throw new SQLException(e.getMessage());
        }
    }

    public void close() throws SQLException {
        try {
            if (this.isOpen) {
                RecStore.closeRecordStore();
                isOpen = false;
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }

    public String getProperty(String key) {
        if (containsKey(key)) {
            return (String) get(key);
        } else {
            return null;
        }
    }

    public Enumeration propertyNames() {
        return keys();
    }

    public void setProperty(String name, String value) {
        this.put(name, value);
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
