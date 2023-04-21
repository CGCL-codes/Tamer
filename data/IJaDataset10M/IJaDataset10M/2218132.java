package org.apache.myfaces.trinidad.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;

/**
 * A resource loader implementation which loads resources
 * from a directory.  The returned resource URL will be null
 * for file resources that do not exist, or for relative paths
 * that attempt to access paths outside the root directory.
 *
 */
public class DirectoryResourceLoader extends ResourceLoader {

    /**
   * Constructs a new DirectoryResourceLoader.
   *
   * @param directory  the root directory
   */
    public DirectoryResourceLoader(File directory) {
        if (directory == null) throw new NullPointerException();
        if (!directory.isDirectory()) throw new IllegalArgumentException();
        _directory = directory;
        try {
            _directoryPath = _directory.getCanonicalPath();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
   * Constructs a new DirectoryResourceLoader.
   *
   * @param directory  the root directory
   * @param parent     the parent resource loader
   */
    public DirectoryResourceLoader(File directory, ResourceLoader parent) {
        super(parent);
        if (directory == null) throw new NullPointerException();
        if (!directory.isDirectory()) throw new IllegalArgumentException();
        _directory = directory;
        try {
            _directoryPath = _directory.getCanonicalPath();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    protected URL findResource(String path) throws IOException {
        if (path.charAt(0) == '/') path = path.substring(1);
        File file = new File(_directory, path).getCanonicalFile();
        boolean isContained = file.getCanonicalPath().startsWith(_directoryPath);
        return (isContained && file.exists()) ? file.toURI().toURL() : null;
    }

    private final File _directory;

    private final String _directoryPath;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(DirectoryResourceLoader.class);
}
