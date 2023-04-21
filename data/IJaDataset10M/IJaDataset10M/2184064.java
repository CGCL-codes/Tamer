package org.openconcerto.ui.preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupProps extends AbstractProps {

    protected static BackupProps instance;

    protected String getPropsFileName() {
        return "./Configuration/Backup.properties";
    }

    public static synchronized BackupProps getInstance() {
        if (instance == null) {
            instance = new BackupProps();
        }
        return instance;
    }

    public boolean isActive() {
        String s = getProperty("IsActive");
        if (s != null && s.trim().length() > 0) {
            return Boolean.valueOf(s);
        } else {
            return false;
        }
    }

    public void setActive(boolean b) {
        setProperty("IsActive", String.valueOf(b));
    }

    public String getDestination() {
        return getProperty("Destination");
    }

    public void setDestination(String value) {
        setProperty("Destination", value);
    }

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm");

    public String getLastBackup() {
        String value = getProperty("LastBackup");
        if (value != null && value.trim().length() > 0) {
            long l = Long.valueOf(value);
            Date d = new Date(l);
            return this.dateFormat.format(d);
        } else {
            return null;
        }
    }

    public void setLastBackup(Date d) {
        setProperty("LastBackup", String.valueOf(d.getTime()));
    }

    public int getErrors() {
        String value = getProperty("Errors");
        if (value != null && value.trim().length() > 0) {
            return Integer.valueOf(value);
        } else {
            return 0;
        }
    }

    public void setErrors(int i) {
        setProperty("Errors", String.valueOf(i));
    }
}
