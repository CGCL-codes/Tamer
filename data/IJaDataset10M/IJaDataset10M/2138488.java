package net.sf.drftpd.remotefile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Creates a single RemoteFile object that is not linked to any other objects.
 * 
 * @author mog
 * @version $Id: StaticRemoteFile.java 664 2004-07-11 11:59:38Z  $
 */
public class StaticRemoteFile extends AbstractRemoteFile {

    private long _checkSum;

    private String _groupname;

    private boolean _isDeleted;

    private long _lastModified;

    private long _length;

    private String _link = null;

    private String _name;

    private List _rslaves;

    private String _username;

    private long _xfertime;

    /**
	 * @param rslaves null indicates that this is a directory.
	 */
    public StaticRemoteFile(List rslaves, String name, String owner, String group, long size, long lastModified) {
        _rslaves = rslaves;
        _name = name;
        _username = owner;
        _groupname = group;
        _length = size;
        _lastModified = lastModified;
    }

    public StaticRemoteFile(List rslaves, String name, String owner, String group, long size, long lastModified, long checkSum) {
        this(rslaves, name, owner, group, size, lastModified);
        _checkSum = checkSum;
    }

    public StaticRemoteFile(List rslaves, String name, long size) {
        this(rslaves, name, null, null, size, System.currentTimeMillis());
    }

    public StaticRemoteFile(String name) {
        _name = name;
    }

    public StaticRemoteFile(String name, List rslaves) {
        this(name);
        _rslaves = rslaves;
    }

    public long getCheckSumCached() {
        return _checkSum;
    }

    /**
	 * StaticRemoteFile cannot be linked
	 * @return java.util.Collections.EMPTY_LIST
	 */
    public Collection getFiles() {
        return Collections.EMPTY_LIST;
    }

    public String getGroupname() {
        return _groupname;
    }

    public String getLinkPath() {
        return _link;
    }

    public String getName() {
        return _name;
    }

    public String getParent() {
        throw new UnsupportedOperationException("getParent() does not exist in StaticRemoteFile");
    }

    public String getPath() {
        throw new UnsupportedOperationException();
    }

    public Collection getSlaves() {
        return _rslaves;
    }

    public String getUsername() {
        return _username;
    }

    public long getXfertime() {
        return _xfertime;
    }

    public boolean isDeleted() {
        return _isDeleted;
    }

    public boolean isDirectory() {
        return _rslaves == null;
    }

    public boolean isFile() {
        return _rslaves != null;
    }

    public boolean isLink() {
        return _link != null;
    }

    public long lastModified() {
        return _lastModified;
    }

    public long length() {
        return _length;
    }

    /**
	 * Sets the checkSum.
	 * @param checkSum The checkSum to set
	 */
    public void setCheckSum(long checkSum) {
        _checkSum = checkSum;
    }

    public void setDeleted(boolean isDeleted) {
        _isDeleted = isDeleted;
    }

    public void setGroupname(String groupname) {
        _groupname = groupname;
    }

    public void setLastModified(long lastmodified) {
        _lastModified = lastmodified;
    }

    public void setLength(long length) {
        _length = length;
    }

    public void setLink(String link) {
        _link = link;
    }

    public void setRSlaves(List rslaves) {
        _rslaves = rslaves;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public void setXfertime(long xfertime) {
        _xfertime = xfertime;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append(getClass().getName() + "[");
        if (isDirectory()) ret.append("[isDirectory(): true]");
        if (isFile()) ret.append("[isFile(): true]");
        ret.append("[length(): " + length() + "]");
        ret.append(getName());
        ret.append("]");
        ret.append("[rslaves:" + _rslaves + "]");
        return ret.toString();
    }
}
