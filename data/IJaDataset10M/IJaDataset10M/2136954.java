package windowsserver.fileexplorer;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Mohamed
 *
 */
public interface FileExplorer {

    public String getAllDrives(String key);

    public String getAllFolders(String path, String key);

    public String getAllFiles(String path, String key);

    public String search(String path, String target, String f_d_b, String key);

    public boolean deleteFileOrFolder(String path, String key);

    public boolean createNewFileOrFolder(boolean isFolder, String path, String key);

    public String getFileOrFolderProperties(String path, String key);

    public String getDriveProperties(String driveName, String key);

    public boolean copy(String source, String destination, String key);

    public boolean move(String source, String destination, String key);

    public boolean editFile(String filePath, String updates, String key);

    public String getFile(String path, String key);

    public boolean rename(String path, String newName, String key);

    public boolean runProgram(String path, String key);

    public boolean shareFolder(String path, int numberOfUsers, String shareName, String description, String key);

    public boolean setAccessPermission(String path, String domain, String userName, String permission, String group, String key);

    public boolean setSharePermission(String userName, String shareName, String path, String permission, String key);

    public boolean setOfflineSetings(String path, String cacheType, String key);

    public boolean compress(String path, String key);

    public String getAllDomains(String key);

    public String getAllPCsInDomain(String domainName, String key);

    public String getAllSharedFolders(String key);

    public boolean deleteShare(String pathName, String key);

    public boolean modifiySharingFile(String path, int numberOfUsers, String description, String key);
}
