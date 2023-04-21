package ao.dd.shell.def;

import ao.util.io.FileInfo;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import com.glub.secureftp.bean.RemoteFile;
import org.apache.commons.net.ftp.FTPFile;
import org.joda.time.DateTime;

/**
 * User: aostrovsky
 * Date: 22-Jun-2009
 * Time: 12:50:24 PM
 */
public class ShellFile extends FileInfo {

    public ShellFile(String pathOrName, SFTPv3FileAttributes fileInfo) {
        this(pathOrName, fileInfo.size, fileInfo.isDirectory(), new DateTime(1000L * fileInfo.mtime));
    }

    public ShellFile(String pathOrName, RemoteFile fileInfo) {
        this(pathOrName, fileInfo.getFileSize(), fileInfo.isDirectory(), new DateTime(fileInfo.getDate().getTime().getTime()));
    }

    public ShellFile(FTPFile fileInfo) {
        this(fileInfo.getName(), fileInfo.getSize(), fileInfo.isDirectory(), new DateTime(fileInfo.getTimestamp().getTimeInMillis()));
    }

    public ShellFile(String filePath, long fileSize, boolean fileIsDirectory, DateTime modifiedTime) {
        super(filePath, fileSize, fileIsDirectory, modifiedTime.getMillis());
    }

    public DateTime modifiedTime() {
        return new DateTime(modified());
    }

    public boolean isBetween(DateTime fromInclusive, DateTime upToInclusive) {
        return isBetween((fromInclusive == null ? -1 : fromInclusive.getMillis()), (upToInclusive == null ? -1 : upToInclusive.getMillis()));
    }

    public String toDetailedString() {
        return (isDirectory() ? "[D] " : "[F] '") + path() + "' (" + size() + " | " + modifiedTime() + ")";
    }
}
