package net.sf.joafip.kvstore.entity;

import net.sf.joafip.NotStorableClass;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ToBackupRecord implements Comparable<ToBackupRecord> {

    private final long positionInFile;

    private final byte[] data;

    public ToBackupRecord(final long positionInFile, final byte[] data) {
        super();
        this.positionInFile = positionInFile;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public long getPositionInFile() {
        return positionInFile;
    }

    public int compareTo(final ToBackupRecord other) {
        final int compareTo;
        if (positionInFile < other.positionInFile) {
            compareTo = -1;
        } else if (positionInFile > other.positionInFile) {
            compareTo = 1;
        } else {
            compareTo = 0;
        }
        return compareTo;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (positionInFile ^ (positionInFile >>> 32));
        return result;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        final ToBackupRecord other = (ToBackupRecord) obj;
        if (positionInFile != other.positionInFile) return false;
        return true;
    }
}
