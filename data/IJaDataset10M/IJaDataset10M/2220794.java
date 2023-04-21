package com.crypticbit.ipa.results;

import com.crypticbit.ipa.central.backupfile.BackupFile;

public abstract class AbstractLocation implements Location {

    private BackupFile bfd;

    protected AbstractLocation(BackupFile bfd) {
        this.bfd = bfd;
    }

    /**
	 * Gets the backup file which this location relates to
	 * 
	 * @return the backup file which this location relates to
	 */
    public BackupFile getBackupFile() {
        return bfd;
    }

    @Override
    public String toString() {
        return getLocationDescription() + " - " + getLocationExtract();
    }

    @Override
    public int compareTo(Location o) {
        if (o == null) return -1;
        if (o.getClass().equals(this.getClass())) return getBackupFile().compareTo(o.getBackupFile()); else return getClass().toString().compareTo(o.getClass().toString());
    }
}
