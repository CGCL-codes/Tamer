package supersync.ftp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import supersync.file.AbstractFile;
import supersync.file.SimplePermissions;

/** Caches the contents of directories of the ftp site.
 *
 * @author Brandon Drkae
 */
public class CachedFTPSite {

    protected HashMap<String, CachedFTPDirectory> cachedDirectories = new HashMap<String, CachedFTPDirectory>();

    protected MyFtpClient client = null;

    protected boolean useCache = true;

    protected FtpConnectionListener connectionListener = new FtpConnectionListener() {

        @Override
        public void chmod(SimplePermissions l_permissions, String l_fileName) {
            chmodResultsReceived(l_permissions, l_fileName);
        }

        @Override
        public void mlsd(String l_directory, ArrayList<FileInfo> l_mlsdResults) {
            mlsdResultsReceived(l_directory, l_mlsdResults);
        }

        @Override
        public void mfmt(String l_fileLocation, Date l_date) {
            mfmtResultsReceived(l_fileLocation, l_date);
        }

        @Override
        public void mkdir(String l_fileName) {
            mkdirResultsReceived(l_fileName);
        }

        @Override
        public void removeFileOrDir(String l_fileName) {
            removeFileOrDirResponseReceived(l_fileName);
        }

        @Override
        public void rename(String l_from, String l_to) {
            renameResponseReceived(l_from, l_to);
        }

        @Override
        public void uploadFinished(String l_folder) {
            uploadFinishedResponseReceived(l_folder);
        }
    };

    public CachedFTPSite(MyFtpClient l_client) {
        this.client = l_client;
        this.client.addFtpConnectionListener(this.connectionListener);
    }

    protected void chmodResultsReceived(SimplePermissions l_permissions, String l_fileName) {
        String fullFileLocation = this.client.getFullPath(l_fileName);
        String directoryPath = AbstractFile.getParentOfFile(fullFileLocation, "/");
        CachedFTPDirectory cachedDirectory = this.cachedDirectories.get(this.formatDirectoryName(directoryPath));
        if (null == cachedDirectory) {
            return;
        }
        for (FileInfo fileInfo : cachedDirectory.fileList) {
            if (fileInfo.fileName.equals(l_fileName)) {
                fileInfo.permissions = l_permissions;
                break;
            }
        }
    }

    /** Formats the directory name so that it is in a standard format so that it can be looked up in the list of stored directories.
     */
    public String formatDirectoryName(String l_directory) {
        if (l_directory.endsWith("/")) {
            return l_directory.substring(0, l_directory.length() - 1);
        }
        return l_directory;
    }

    public CachedFTPDirectory getCachedDirectory(String l_directoryPath) {
        if (false == this.useCache) {
            return null;
        }
        String lookupKey = this.formatDirectoryName(l_directoryPath);
        if (false == this.cachedDirectories.containsKey(lookupKey)) {
            return null;
        }
        return this.cachedDirectories.get(lookupKey);
    }

    protected void mfmtResultsReceived(String l_fileLocation, Date l_date) {
        String fullFileLocation = this.client.getFullPath(l_fileLocation);
        int directoryPathEnd = fullFileLocation.lastIndexOf("/");
        String directoryPath = fullFileLocation.substring(0, directoryPathEnd);
        String fileName = fullFileLocation.substring(directoryPathEnd + 1);
        CachedFTPDirectory cachedDirectory = this.cachedDirectories.get(this.formatDirectoryName(directoryPath));
        if (null == cachedDirectory) {
            return;
        }
        for (FileInfo fileInfo : cachedDirectory.fileList) {
            if (fileInfo.fileName.equals(fileName)) {
                fileInfo.modificationDate = l_date;
                break;
            }
        }
    }

    protected void mkdirResultsReceived(String l_fileName) {
        String fullFileLocation = this.client.getFullPath(l_fileName);
        String parentDirectoryName = AbstractFile.getParentOfFile(fullFileLocation, "/");
        String fileName = AbstractFile.getFileNameOfFile(fullFileLocation, "/");
        if (null == parentDirectoryName || null == fileName) {
            return;
        }
        this.cachedDirectories.remove(this.formatDirectoryName(parentDirectoryName));
    }

    protected void mlsdResultsReceived(String l_directory, ArrayList<FileInfo> l_mlsdResults) {
        CachedFTPDirectory cachedFtpDirectory = CachedFTPDirectory.getFromMLSDResult(l_mlsdResults);
        this.cachedDirectories.put(this.formatDirectoryName(l_directory), cachedFtpDirectory);
    }

    protected void removeFileOrDirResponseReceived(String l_fileName) {
        String parentDirectoryName = this.formatDirectoryName(this.client.getCachedPWD());
        CachedFTPDirectory cachedDirectory = this.getCachedDirectory(parentDirectoryName);
        if (null != cachedDirectory) {
            FileInfo removedFile = null;
            for (FileInfo fileItem : cachedDirectory.fileList) {
                if (fileItem.fileName.equals(l_fileName)) {
                    removedFile = fileItem;
                    break;
                }
            }
            if (null == removedFile) {
                this.cachedDirectories.remove(parentDirectoryName);
                return;
            }
            cachedDirectory.fileList.remove(removedFile);
        }
        this.cachedDirectories.remove(parentDirectoryName + "/" + l_fileName);
    }

    public void renameResponseReceived(String l_from, String l_to) {
        String from_parentDirectoryName = this.formatDirectoryName(AbstractFile.getParentOfFile(l_from, "/"));
        String from_fileName = AbstractFile.getFileNameOfFile(l_from, "/");
        String to_parentDirectoryName = this.formatDirectoryName(AbstractFile.getParentOfFile(l_to, "/"));
        String to_fileName = AbstractFile.getFileNameOfFile(l_to, "/");
        FileInfo fromFile = null;
        if (null != from_parentDirectoryName && null != from_fileName) {
            CachedFTPDirectory from_cachedDirectory = this.getCachedDirectory(from_parentDirectoryName);
            if (null != from_cachedDirectory) {
                for (FileInfo fileItem : from_cachedDirectory.fileList) {
                    if (fileItem.fileName.equals(from_fileName)) {
                        fromFile = fileItem;
                        break;
                    }
                }
                if (null != fromFile) {
                    from_cachedDirectory.fileList.remove(fromFile);
                }
            }
        }
        CachedFTPDirectory to_cachedDirectory = this.getCachedDirectory(to_parentDirectoryName);
        if (null == to_cachedDirectory) {
            return;
        }
        if (null == fromFile) {
            this.cachedDirectories.remove(to_parentDirectoryName);
        } else {
            to_cachedDirectory.fileList.add(fromFile);
        }
    }

    public void setUseCache(boolean l_value) {
        this.cachedDirectories.clear();
        if (false == l_value) {
            this.client.removeFtpConnectionListener(this.connectionListener);
        } else {
            this.client.addFtpConnectionListener(this.connectionListener);
        }
        this.useCache = false;
    }

    protected void uploadFinishedResponseReceived(String l_folder) {
        String directory = this.formatDirectoryName(l_folder);
        if (this.cachedDirectories.containsKey(directory)) {
            this.cachedDirectories.remove(directory);
        }
    }
}
