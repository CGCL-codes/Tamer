package au.vermilion.PC;

import static au.vermilion.Vermilion.logger;
import au.vermilion.fileio.IFileHandle;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.logging.Level;

/**
 * This implements the IFileHandle interface for PC, simply by storing a path
 * to the file.
 */
public final class PCFileHandle implements IFileHandle {

    /**
     * The path to the file this handle is representing.
     */
    private final String fileName;

    /**
     * Creates a file handle pointing to the specified path.
     * @param path The path to the file this handle is representing.
     */
    public PCFileHandle(String path) {
        fileName = path;
    }

    @Override
    public OutputStream openOutput() {
        try {
            return new BufferedOutputStream(new FileOutputStream(fileName));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public InputStream openInput() {
        try {
            return new BufferedInputStream(new FileInputStream(fileName));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error getting file handle", ex);
        }
        return null;
    }

    @Override
    public boolean canOpenInput() {
        if (fileName == null) return false;
        File f = new File(fileName);
        if (f.exists() == false) return false;
        if (f.isDirectory()) return false;
        return true;
    }

    @Override
    public boolean canOpenOutput() {
        if (fileName == null) return false;
        return true;
    }

    /**
     * Returns the string represenation of this file handle.
     * @return The path to the file this handle is representing.
     */
    @Override
    public String toString() {
        return fileName;
    }
}
