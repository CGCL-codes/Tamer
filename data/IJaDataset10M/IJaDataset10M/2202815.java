package org.suren.core.os;

import java.io.File;

public class UnixFile extends File {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5676563218679158383L;

    private String permissionsStr;

    private int permissions;

    private long size;

    private boolean dir;

    private boolean link;

    private int gid;

    private int uid;

    private int acTime;

    private String acTimeStr;

    private int mTime;

    private String mTimeStr;

    private int flags;

    private String[] extended;

    private String path;

    private String realPath;

    public UnixFile(String pathname) {
        super(pathname);
        setPath(pathname);
    }

    public String getPermissionsStr() {
        return permissionsStr;
    }

    public void setPermissionsStr(String permissionsStr) {
        this.permissionsStr = permissionsStr;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAcTime() {
        return acTime;
    }

    public void setAcTime(int acTime) {
        this.acTime = acTime;
    }

    public String getAcTimeStr() {
        return acTimeStr;
    }

    public void setAcTimeStr(String acTimeStr) {
        this.acTimeStr = acTimeStr;
    }

    public int getmTime() {
        return mTime;
    }

    public void setmTime(int mTime) {
        this.mTime = mTime;
    }

    public String getmTimeStr() {
        return mTimeStr;
    }

    public void setmTimeStr(String mTimeStr) {
        this.mTimeStr = mTimeStr;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String[] getExtended() {
        return extended;
    }

    public void setExtended(String[] extended) {
        this.extended = extended;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
