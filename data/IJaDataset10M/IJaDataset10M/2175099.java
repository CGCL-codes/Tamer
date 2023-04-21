package org.ujac.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Name: FileUtils<br>
 * Description: Class providing common utility methods for file handling.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2005/01/05 19:32:13  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 2024 $
 */
public class FileUtils {

    /** The I/O buffer size. */
    public static final int IO_BUFFER_SIZE = 2048;

    /**
   * Loads the contents of the given file.
   * @param file The file to load.
   * @return The loaded data from the file.
   * @exception IOException In case the loading of the file failed.
   */
    public static final byte[] loadFile(File file) throws IOException {
        if (file == null) {
            throw new IOException("The given file must not be null.");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(fis, IO_BUFFER_SIZE);
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            while (numRead > 0) {
                bos.write(buffer, 0, numRead);
                numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bos.toByteArray();
    }

    /**
   * Copies the contents of the given file <code>src</code> into 
   * the given file <code>dst</code>.
   * @param src The file to copy from.
   * @param dst The file to copy to.
   * @return The number of bytes copied.
   * @exception IOException In case the loading of the file failed.
   */
    public static final int copyFile(File src, File dst) throws IOException {
        if (src == null) {
            throw new IOException("The given source file must not be null.");
        }
        if (dst == null) {
            throw new IOException("The given destination file must not be null.");
        }
        int numCopied = 0;
        FileOutputStream os = new FileOutputStream(dst);
        FileInputStream is = new FileInputStream(src);
        try {
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            while (numRead != -1) {
                os.write(buffer, 0, numRead);
                numCopied += numRead;
                numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return numCopied;
    }
}
