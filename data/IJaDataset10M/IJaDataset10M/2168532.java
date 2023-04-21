package org.sdrinovsky.sdsvn.files;

import java.io.File;
import java.util.Date;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 *
 * @author sdrinovsky
 */
public class FileTableRow implements FileRow {

    public static final int STATUS_COLUMN = 0;

    public static final int FILE_COLUMN = 1;

    public static final int EXT_COLUMN = 2;

    public static final int FOLDER_COLUMN = 3;

    public static final int COMMITTED_REVISION_COLUMN = 4;

    public static final int REVISION_COLUMN = 5;

    public static final int MODIFIED_COLUMN = 6;

    public static final int URL_COLUMN = 7;

    private static String[] columnNames = { "Status", "Name", "Ext", "Folder", "Committed Revision", "Revision", "Modified", "Location" };

    private static Class[] columnClass = { String.class, String.class, String.class, File.class, Long.class, Long.class, Date.class, String.class };

    private static int[] columnWidths = { 65, 256, 85, 512, 85, 85, 185, 175 };

    /**
   * Method getColumnCount
   *
   *
   * @return
   *
   */
    public static int getColumnCount() {
        return columnNames.length;
    }

    /**
   * Method getColumnName
   *
   *
   * @param column
   *
   * @return
   *
   */
    public static String getColumnName(int column) {
        return columnNames[column];
    }

    /**
   * Method getColumnClass
   *
   *
   * @param column
   *
   * @return
   *
   */
    public static Class<?> getColumnClass(int column) {
        return columnClass[column];
    }

    /**
   * Method getColumnWidth
   *
   *
   * @param column
   *
   * @return
   *
   */
    public static int getColumnWidth(int column) {
        return columnWidths[column];
    }

    /**
   * Method getStatus
   *
   *
   * @param status
   *
   * @return
   *
   */
    public static char[] getStatus(SVNStatus status) {
        char[] s = { ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        s[0] = status.getNodeStatus().getCode();
        s[1] = status.getPropertiesStatus().getCode();
        if (status.isLocked()) {
            s[2] = 'L';
        }
        if (status.isCopied()) {
            s[3] = '+';
        }
        if (status.isSwitched()) {
            s[4] = 'S';
        }
        if (status.getLocalLock() != null) {
            s[5] = 'K';
        }
        if ((status.getRemotePropertiesStatus() != SVNStatusType.STATUS_NONE) || (status.getRemoteContentsStatus() != SVNStatusType.STATUS_NONE)) {
            s[6] = '*';
        }
        return s;
    }

    /**
   * Method isModified
   *
   *
   * @param status
   *
   * @return
   *
   */
    public static boolean isModified(String status) {
        if ((status != null) && (status.length() > 0)) {
            switch(status.charAt(0)) {
                case 'M':
                case 'C':
                case 'D':
                case 'A':
                    return true;
            }
        }
        return false;
    }

    /**
   * Method isConflicted
   *
   *
   * @param status
   *
   * @return
   *
   */
    public static boolean isConflicted(String status) {
        return (status != null) && (status.length() > 0) && (status.charAt(0) == 'C');
    }

    /**
   * Method isPropertyModified
   *
   *
   * @param status
   *
   * @return
   *
   */
    public static boolean isPropertyModified(String status) {
        return (status != null) && (status.length() > 1) && (status.charAt(1) == 'M');
    }

    public static final int PARSE_URL_SERVER = 0;

    public static final int PARSE_URL_BRANCH = 1;

    public static final int PARSE_URL_PATH = 2;

    /**
   * Method getBrnachNameFromURL
   *
   *
   * @param url
   * @param type
   *
   * @return
   *
   */
    public static String parseURL(String url, int type) {
        String branches = "/branches/";
        String tags = "/tags/";
        String trunk = "/trunk";
        int index = url.indexOf(branches);
        if (index >= 0) {
            int index2 = url.indexOf("/", index + branches.length() + 1);
            if (index2 > 0) {
                switch(type) {
                    case PARSE_URL_SERVER:
                        return url.substring(0, index);
                    case PARSE_URL_BRANCH:
                        return url.substring(index + 1, index2);
                    case PARSE_URL_PATH:
                        return url.substring(index2 + 1);
                    default:
                        return url;
                }
            } else {
                switch(type) {
                    case PARSE_URL_SERVER:
                        return url.substring(0, index);
                    case PARSE_URL_BRANCH:
                        return url.substring(index + 1);
                    case PARSE_URL_PATH:
                        return "";
                    default:
                        return url;
                }
            }
        }
        index = url.indexOf(tags);
        if (index >= 0) {
            int index2 = url.indexOf("/", index + tags.length() + 1);
            if (index2 > 0) {
                switch(type) {
                    case PARSE_URL_SERVER:
                        return url.substring(0, index);
                    case PARSE_URL_BRANCH:
                        return url.substring(index + 1, index2);
                    case PARSE_URL_PATH:
                        return url.substring(index2 + 1);
                    default:
                        return url;
                }
            } else {
                switch(type) {
                    case PARSE_URL_SERVER:
                        return url.substring(0, index);
                    case PARSE_URL_BRANCH:
                        return url.substring(index + 1);
                    case PARSE_URL_PATH:
                        return "";
                    default:
                        return url;
                }
            }
        }
        index = url.indexOf(trunk);
        if (index >= 0) {
            switch(type) {
                case PARSE_URL_SERVER:
                    return url.substring(0, index);
                case PARSE_URL_BRANCH:
                    return "trunk";
                case PARSE_URL_PATH:
                    if (url.length() >= index + 1 + trunk.length()) {
                        return url.substring(index + 1 + trunk.length());
                    } else {
                        return "";
                    }
                default:
                    return url;
            }
        }
        return url;
    }

    /**
   * Method switchBranch
   *
   *
   * @param url
   * @param branch
   *
   * @return
   *
   */
    public static String switchBranch(String url, String branch) {
        String server = FileTableRow.parseURL(url, FileTableRow.PARSE_URL_SERVER);
        String path = FileTableRow.parseURL(url, FileTableRow.PARSE_URL_PATH);
        return server + "/" + branch + "/" + path;
    }

    private final String status;

    private final File folder;

    private final String file;

    private final String ext;

    private final long revision;

    private final long committedRevision;

    private final Date modified;

    private final Location location;

    /**
   * Constructor FileTableRow
   *
   *
   * @param status
   *
   */
    public FileTableRow(SVNStatus status) {
        File statusFile = status.getFile();
        this.status = new String(getStatus(status));
        this.folder = statusFile.getParentFile();
        this.file = statusFile.getName();
        this.revision = status.getRevision().getNumber();
        this.committedRevision = status.getCommittedRevision().getNumber();
        int extIndex = file.lastIndexOf('.');
        if (statusFile.isFile() && (extIndex > 0)) {
            ext = file.substring(extIndex + 1);
        } else {
            ext = "";
        }
        long lastModified = statusFile.lastModified();
        if (lastModified > 0) {
            modified = new Date(lastModified);
        } else {
            modified = null;
        }
        if (status.getURL() != null) {
            location = new Location(statusFile, status.getURL().toString());
        } else {
            location = null;
        }
    }

    /**
   * Method get
   *
   *
   * @param index
   *
   * @return
   *
   */
    public Object get(int index) {
        switch(index) {
            case STATUS_COLUMN:
                return status;
            case FOLDER_COLUMN:
                return folder;
            case FILE_COLUMN:
                return file;
            case EXT_COLUMN:
                return ext;
            case MODIFIED_COLUMN:
                return modified;
            case COMMITTED_REVISION_COLUMN:
                return committedRevision;
            case REVISION_COLUMN:
                return revision;
            case URL_COLUMN:
                if (location != null) {
                    return location.getBranch();
                }
                return "";
            default:
                return null;
        }
    }

    /**
   * Method isCommitable
   *
   *
   * @return true if the file can be committed
   *
   */
    public boolean isCommitable() {
        boolean fileModified = (status.charAt(0) == 'A') || (status.charAt(0) == 'R') || (status.charAt(0) == '~') || (status.charAt(0) == 'D') || (status.charAt(0) == 'M') || (status.charAt(0) == 'C');
        boolean folderModified = (status.charAt(1) == 'C') || (status.charAt(1) == 'M');
        return fileModified || folderModified;
    }

    /**
   * Method isConflict
   *
   *
   * @return
   *
   */
    public boolean isConflict() {
        boolean fileModified = status.charAt(0) == 'C';
        boolean folderModified = status.charAt(1) == 'C';
        return fileModified || folderModified;
    }

    /**
   * Method isAddable
   *
   *
   * @return
   *
   */
    public boolean isAddable() {
        return status.charAt(0) == '?';
    }

    /**
   * Method isIgnored
   *
   *
   * @return
   *
   */
    public boolean isIgnored() {
        return status.charAt(0) == 'I';
    }

    /**
   * Method isMissing
   *
   *
   * @return
   *
   */
    public boolean isMissing() {
        return status.charAt(0) == '!';
    }

    /**
   * Method needsUpdate
   *
   *
   * @return
   *
   */
    public boolean needsUpdate() {
        return status.charAt(6) == '*';
    }

    /**
   * Method getStatus
   *
   *
   * @return
   *
   */
    @Override
    public String getStatus() {
        return status;
    }

    /**
   * Method getFile
   *
   *
   * @return
   *
   */
    public File getFolder() {
        return folder;
    }

    /**
   * Method getFile
   *
   *
   * @return
   *
   */
    @Override
    public File getFile() {
        return new File(folder, file);
    }

    /**
   * Method getFileName
   *
   *
   * @return
   *
   */
    public String getFileName() {
        return file;
    }

    /**
   * Method getExt
   *
   *
   * @return
   *
   */
    public String getExt() {
        return ext;
    }

    /**
   * Method getRevision
   *
   *
   * @return
   *
   */
    public long getRevision() {
        return revision;
    }

    /**
   * Method getCommitRevision
   *
   *
   * @return
   *
   */
    public long getCommitRevision() {
        return committedRevision;
    }

    /**
   * Method getModified
   *
   *
   * @return
   *
   */
    public Date getModified() {
        return modified;
    }

    /**
   * Method getURL
   *
   *
   * @return
   *
   */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
   * Method toString
   *
   *
   * @return
   *
   */
    @Override
    public String toString() {
        return getFile().toString();
    }
}
