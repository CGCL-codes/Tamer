package net.jautomock.util;

import java.io.File;
import java.io.IOException;
import net.jaoplib.annotations.NonNullArgs;
import org.apache.commons.io.FileUtils;

/**
 * Utility class to help dealing with files.
 * 
 * @author Philipp Kumar
 */
public class FileHelper {

    /**
     * Creates the given file and every necessary but nonexistent parent
     * directory.
     * 
     * @throws IOException
     *             if the file itself or any necessary parent directory could
     *             not be created, or if the file already exists
     */
    @NonNullArgs
    public static void forceCreateFile(File file) throws IOException {
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            FileUtils.forceMkdir(parentDir);
        }
        if (!file.createNewFile()) {
            throw new IOException("File exists: " + file.getAbsolutePath());
        }
    }
}
