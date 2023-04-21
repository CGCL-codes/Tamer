package uips.support.uipfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import uips.support.Consts;
import uips.support.Messages;
import uips.support.Settings;

/**
 * Class providing acces for UIP application files. Files can be provided via various
 * services like ftp or stored on local disks. Another services can be easily added.
 * UIP application files are stored in disk cache and downloaded only if changed on server.
 * File pathes for files and folders are separated with /<br>
 * file path example: uipapplication/media/image.jpg<br>
 * dir path example: uipapplication/media
 *
 * @author Jindrich Basek (basekjin@fel.cvut.cz, CTU Prague,  FEE)
 */
public class UipFilesAccess {

    /**
     * Selected provider
     */
    private static IUipFilesStorageAccess fileAccess = null;

    /**
     * Opens storage with UIP appliaction files
     *
     * @throws StorageException Error opening storage
     */
    public static void open() throws StorageException {
        if (fileAccess == null) {
            try {
                URI appsUri = new URI(Settings.getApplicationsRoot());
                if (appsUri.getScheme().toLowerCase().equals("file")) {
                    fileAccess = new FileStorageAccess(appsUri.getPath().replace('/', Consts.FileSeparatorChar));
                } else if (appsUri.getScheme().toLowerCase().equals("ftp")) {
                    fileAccess = new FtpStorageAccess(appsUri.getHost(), appsUri.getPort(), appsUri.getUserInfo(), appsUri.getPath());
                } else {
                    throw new StorageException(String.format(Messages.getString("NotSupporetTypeOfUipAppliactionsStorage"), appsUri.getScheme()));
                }
            } catch (URISyntaxException ex) {
                fileAccess = new FileStorageAccess(System.getProperty("user.dir") + Consts.FileSeparator + Consts.ApplicationsDir);
            } catch (NullPointerException ex) {
                fileAccess = new FileStorageAccess(System.getProperty("user.dir") + Consts.FileSeparator + Consts.ApplicationsDir);
            }
        }
    }

    /**
     * Closes storage with UIP appliaction files.
     */
    public static void close() {
        if (fileAccess != null) {
            fileAccess.dispose();
            fileAccess = null;
        }
    }

    /**
     * Returns list with files witch given extensions included in folder specified
     * by dir parameter and its subfolders.
     *
     * @param dir dir where start looking for files<br>
     * dir path example: uipapplication/media
     * @param allowedExtensions array with allowed extensions - extension are without . (dot)
     * @return list with files witch given extensions included in folder specified
     * by dir parameter and its subfolders.
     */
    public static List<String> recurseDir(String dir, String allowedExtensions[]) throws InterruptedException {
        return fileAccess.recurseDir(dir, allowedExtensions);
    }

    /**
     * Constructs object implementing IUipFile interface that provides access
     * to specific file, reads file content
     *
     * @param path path to file<br>
     * file path example: uipapplication/media/image.jpg<br>
     * dir path example: uipapplication/media
     * @return object implementing IUipFile interface that provides access
     * to specific file, reads file content
     * @throws IOException Error if file not found or not readable
     */
    public static IUipFile getFile(String path) throws IOException {
        return fileAccess.getFile(path);
    }
}
