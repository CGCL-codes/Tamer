package irc;

import java.io.*;

/**
 * File handling.
 */
public interface FileHandler {

    /**
   * Get an input stream from the given filename.
   * @param fileName file name to get inputstream from.
   * @return inputstream from the file, or null if unable.
   */
    public InputStream getInputStream(String fileName);
}
