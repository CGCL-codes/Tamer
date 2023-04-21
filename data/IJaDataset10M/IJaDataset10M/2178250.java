package net.sf.drftpd.master;

/**
 * @author mog
 * @version $Id: UploaderPosition.java 1764 2007-08-04 02:01:21Z tdsoul $
 */
public class UploaderPosition implements Comparable {

    long _bytes;

    int _files;

    String _username;

    long _xfertime;

    public UploaderPosition(String username, long bytes, int files, long xfertime) {
        _username = username;
        _bytes = bytes;
        _files = files;
        _xfertime = xfertime;
    }

    public int compareTo(Object o) {
        return compareTo((UploaderPosition) o);
    }

    /** Sorts in reverse order so that the biggest shows up first.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(UploaderPosition o) {
        long thisVal = getBytes();
        long anotherVal = o.getBytes();
        return ((thisVal < anotherVal) ? 1 : ((thisVal == anotherVal) ? 0 : (-1)));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof UploaderPosition)) {
            return false;
        }
        UploaderPosition other = (UploaderPosition) obj;
        return getUsername().equals(other.getUsername());
    }

    public long getBytes() {
        return _bytes;
    }

    public int getFiles() {
        return _files;
    }

    public String getUsername() {
        return _username;
    }

    public long getXferspeed() {
        if (getXfertime() == 0) {
            return 0;
        }
        return (long) (getBytes() / (getXfertime() / 1000.0));
    }

    public long getXfertime() {
        return _xfertime;
    }

    public int hashCode() {
        return getUsername().hashCode();
    }

    public void updateBytes(long bytes) {
        _bytes += bytes;
    }

    public void updateFiles(int files) {
        _files += files;
    }

    public void updateXfertime(long xfertime) {
        _xfertime += xfertime;
    }
}
