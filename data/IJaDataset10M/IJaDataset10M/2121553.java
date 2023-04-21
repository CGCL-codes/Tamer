package uips.support.storage;

import java.io.IOException;
import java.util.List;
import uips.support.storage.interfaces.IUipFile;

/**
 * Class providing acces for UIP application files. Files can be provided via various
 * services like ftp or stored on local disks. Another services can be easily added.
 * UIP application files are stored in disk cache and downloaded only if changed on server.
 * File pathes for files and folders are separated with /<br>
 * file path example: uipapplication/media/image.jpg<br>
 * dir path example: uipapplication/media
 *
 * @author Jindrich Basek (basekjin@fit.cvut.cz, CTU Prague, FIT)
 */
public class UipFilesAccessInstance implements IUipFilesAccessInstance {

    public static final IUipFilesAccessInstance UIP_FILES_ACCESS_INSTANCE = new UipFilesAccessInstance();

    @Override
    public List<String> recurseDir(String dir, String allowedExtensions[]) throws InterruptedException {
        return UipFilesAccess.recurseDir(dir, allowedExtensions);
    }

    @Override
    public IUipFile getFile(String path) throws IOException {
        return UipFilesAccess.getFile(path);
    }
}
