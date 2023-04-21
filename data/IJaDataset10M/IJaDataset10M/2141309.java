package org.elmarweber.sf.appletrailerfs;

import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 * Contains constants and utility methods for files hosted on the FTP server.
 * 
 * @author Elmar Weber (appletrailerfs@elmarweber.org)
 */
public class FtpHosting {

    public static final String URL_PREFIX = "http://appletrailerfs.elmarweber.org/files";

    public static final String LOCAL_PREFIX = "./ftp/files";

    public static String getUrl(String suffix) {
        return URL_PREFIX + suffix;
    }

    public static String getLocalPath(String suffix) {
        return LOCAL_PREFIX + suffix;
    }

    /**
     * Generates a unique filename for a file based on its content.
     * 
     * @param file
     *            the file to hash.
     * 
     * @return the MD5 checksum as an uppercase string.
     * 
     * @throws IOException
     *             in case any I/O error occurs while reading the file.
     */
    public static String hashFileForFtp(File file) throws IOException {
        return DigestUtils.md5Hex(FileUtils.readFileToByteArray(file)).toUpperCase();
    }

    /**
     * Generates a unique filename for a file based on the specified string
     * which represents the file contents.
     * 
     * @param content
     *            the file contents.
     * 
     * @return the MD5 checksum as an uppercase string.
     * 
     */
    public static String hashFileForFtp(String content) {
        return DigestUtils.md5Hex(content).toUpperCase();
    }
}
