package photospace.vfs;

import java.io.*;

/**
 * C5
 */
public interface FileSystem {

    File getRoot();

    void setRoot(File root) throws IOException;

    File getFile(String path) throws IOException;

    File getAsset(String path) throws FileNotFoundException, IOException;

    File getDirectory(String path) throws FileNotFoundException, IOException;

    String getPath(File file);
}
