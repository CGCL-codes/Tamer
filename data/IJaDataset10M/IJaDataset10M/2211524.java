package org.tigris.subversion.javahl;

import java.util.Date;

/**
 * this class is returned by SVNClientInterface.info2 and contains information
 * about items in the repository or working copy
 * @since 1.2
 */
public class Info2 implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * the path of the item
     */
    private String path;

    /**
     * the url of the item
     */
    private String url;

    /**
     * the revision of the item
     */
    private long rev;

    /**
     * the item kinds (see NodeKind)
     */
    private int kind;

    /**
     * the root URL of the repository
     */
    private String reposRootUrl;

    /**
     * the UUID of the repository
     */
    private String reposUUID;

    /**
     * the revision of the last change
     */
    private long lastChangedRev;

    /**
     * the date of the last change in ns
     */
    private long lastChangedDate;

    /**
     * the author of the last change
     */
    private String lastChangedAuthor;

    /**
     * the information about any lock (may be null)
     */
    private Lock lock;

    /**
     * the flag if the remaining fields are set
     */
    private boolean hasWcInfo;

    /**
     * the scheduled operation at next commit (see ScheduleKind)
     */
    private int schedule;

    /**
     * if the item was copied, the source url
     */
    private String copyFromUrl;

    /**
     * if the item was copied, the source rev
     */
    private long copyFromRev;

    /**
     * the last time the item was changed in ns
     */
    private long textTime;

    /**
     * the last time the properties of the items were changed in ns
     */
    private long propTime;

    /**
     * the checksum of the item
     */
    private String checksum;

    /**
     * if the item is in conflict, the filename of the base version file
     */
    private String conflictOld;

    /**
     * if the item is in conflict, the filename of the last repository version
     * file
     */
    private String conflictNew;

    /**
     * if the item is in conflict, the filename of the working copy
     * version file
     */
    private String conflictWrk;

    /**
     * the property reject file
     */
    private String prejfile;

    /**
     * The name of the changelist.
     * @since 1.5
     */
    private String changelistName;

    /**
     * The size of the file after being translated into its local
     * representation, or <code>-1</code> if unknown.  Not applicable
     * for directories.
     * @since 1.5
     */
    private long workingSize;

    /**
     * The size of the file in the repository (untranslated,
     * e.g. without adjustment of line endings and keyword
     * expansion). Only applicable for file -- not directory -- URLs.
     * For working copy paths, size will be <code>-1</code>.
     * @since New in 1.5.
     */
    private long reposSize;

    /**
     * The depth of the item.
     * @since 1.6
     */
    private int depth;

    /**
     * Info on any tree conflict of which this node is a victim.
     * @since 1.6
     */
    private ConflictDescriptor treeConflict;

    /**
     * constructor to build the object by native code. See fields for
     * parameters
     * @param path
     * @param url
     * @param rev
     * @param kind
     * @param reposRootUrl
     * @param reposUUID
     * @param lastChangedRev
     * @param lastChangedDate
     * @param lastChangedAuthor
     * @param lock
     * @param hasWcInfo
     * @param schedule
     * @param copyFromUrl
     * @param copyFromRev
     * @param textTime
     * @param propTime
     * @param checksum
     * @param conflictOld
     * @param conflictNew
     * @param conflictWrk
     * @param prejfile
     * @param depth
     * @param treeConflict
     */
    Info2(String path, String url, long rev, int kind, String reposRootUrl, String reposUUID, long lastChangedRev, long lastChangedDate, String lastChangedAuthor, Lock lock, boolean hasWcInfo, int schedule, String copyFromUrl, long copyFromRev, long textTime, long propTime, String checksum, String conflictOld, String conflictNew, String conflictWrk, String prejfile, String changelistName, long workingSize, long reposSize, int depth, ConflictDescriptor treeConflict) {
        this.path = path;
        this.url = url;
        this.rev = rev;
        this.kind = kind;
        this.reposRootUrl = reposRootUrl;
        this.reposUUID = reposUUID;
        this.lastChangedRev = lastChangedRev;
        this.lastChangedDate = lastChangedDate;
        this.lastChangedAuthor = lastChangedAuthor;
        this.lock = lock;
        this.hasWcInfo = hasWcInfo;
        this.schedule = schedule;
        this.copyFromUrl = copyFromUrl;
        this.copyFromRev = copyFromRev;
        this.textTime = textTime;
        this.propTime = propTime;
        this.checksum = checksum;
        this.conflictOld = conflictOld;
        this.conflictNew = conflictNew;
        this.conflictWrk = conflictWrk;
        this.prejfile = prejfile;
        this.changelistName = changelistName;
        this.workingSize = workingSize;
        this.reposSize = reposSize;
        this.depth = depth;
        this.treeConflict = treeConflict;
    }

    /**
     * return the path of the item
     */
    public String getPath() {
        return path;
    }

    /**
     * return the url of the item
     */
    public String getUrl() {
        return url;
    }

    /**
     * return the revision of the item
     */
    public long getRev() {
        return rev;
    }

    /**
     * return the item kinds (see NodeKind)
     */
    public int getKind() {
        return kind;
    }

    /**
     * return the root URL of the repository
     */
    public String getReposRootUrl() {
        return reposRootUrl;
    }

    /**
     * return the UUID of the repository
     */
    public String getReposUUID() {
        return reposUUID;
    }

    /**
     * return the revision of the last change
     */
    public long getLastChangedRev() {
        return lastChangedRev;
    }

    /**
     * return the date of the last change
     */
    public Date getLastChangedDate() {
        if (lastChangedDate == 0) return null; else return new Date(lastChangedDate / 1000);
    }

    /**
     * return the author of the last change
     */
    public String getLastChangedAuthor() {
        return lastChangedAuthor;
    }

    /**
     * return the information about any lock (may be null)
     */
    public Lock getLock() {
        return lock;
    }

    /**
     * return the flag if the working copy fields are set
     */
    public boolean isHasWcInfo() {
        return hasWcInfo;
    }

    /**
     * return the scheduled operation at next commit (see ScheduleKind)
     */
    public int getSchedule() {
        return schedule;
    }

    /**
     * return if the item was copied, the source url
     */
    public String getCopyFromUrl() {
        return copyFromUrl;
    }

    /**
     * return if the item was copied, the source rev
     */
    public long getCopyFromRev() {
        return copyFromRev;
    }

    /**
     * return the last time the item was changed
     */
    public Date getTextTime() {
        if (textTime == 0) return null; else return new Date(textTime / 1000);
    }

    /**
     * return the last time the properties of the items were changed
     */
    public Date getPropTime() {
        if (propTime == 0) return null; else return new Date(propTime / 1000);
    }

    /**
     * return the checksum of the item
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * return if the item is in conflict, the filename of the base version file
     */
    public String getConflictOld() {
        return conflictOld;
    }

    /**
     * return if the item is in conflict, the filename of the last repository
     * version file
     */
    public String getConflictNew() {
        return conflictNew;
    }

    /**
     * return if the item is in conflict, the filename of the working copy
     * version file
     */
    public String getConflictWrk() {
        return conflictWrk;
    }

    /**
     * return the property reject file
     */
    public String getPrejfile() {
        return prejfile;
    }

    /**
     * @return The name of the changelist.
     * @since 1.5
     */
    public String getChangelistName() {
        return changelistName;
    }

    /**
     * @return The size of the file after being translated into its
     * local representation, or <code>-1</code> if unknown.  Not
     * applicable for directories.
     * @since 1.5
     */
    public long getWorkingSize() {
        return workingSize;
    }

    /**
     * @return The size of the file in the repository (untranslated,
     * e.g. without adjustment of line endings and keyword
     * expansion). Only applicable for file -- not directory -- URLs.
     * For working copy paths, size will be <code>-1</code>.
     * @since New in 1.5.
     */
    public long getReposSize() {
        return reposSize;
    }

    /**
     * @return The depth of the directory or <code>null</code> if the
     * item is a file.
     * @since New in 1.5.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return the tree conflict of which this node is a victim, or null if none
     * @since New in 1.6.
     */
    public ConflictDescriptor getConflictDescriptor() {
        return treeConflict;
    }

    /**
     * @return A string representation of this info.
     */
    public String toString() {
        return getUrl();
    }
}
