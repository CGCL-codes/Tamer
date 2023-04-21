package org.das2.util.filesystem;

import java.net.URI;
import java.net.UnknownHostException;
import org.das2.util.filesystem.FileSystem.FileSystemOfflineException;

/**
 * Creates a FileSystem for reading files via HTTP and HTTPS.
 * @author jbf
 */
public class HttpFileSystemFactory implements FileSystemFactory {

    public HttpFileSystemFactory() {
    }

    public FileSystem createFileSystem(URI root) throws FileSystemOfflineException, UnknownHostException {
        System.err.println("createFileSystem: " + root);
        HttpFileSystem hfs = HttpFileSystem.createHttpFileSystem(root);
        if (!FileSystemSettings.hasAllPermission()) hfs.setAppletMode(true);
        return hfs;
    }
}
