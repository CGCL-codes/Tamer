package de.knup.jedi.jayshare;

import org.w3c.tools.crypt.*;
import java.io.*;

/**
 * Uses w3c implementation of the MD5 algo.
 *
 * Created: Wed Jan 23 14:45:42 2002
 *
 * @author <a href="mailto:lucky@knup.de">Lukas Kolbe</a>
 * @version 0.1 $Revision: 1.4 $
 */
public class HashWrapper {

    /**
   * Calculate an MD5 hash of a String
   * @param from is the String to be calculated
   * @return the MD5 hash
   */
    public static String getHash(String from) {
        Md5 md5 = new Md5(from);
        md5.processString();
        return md5.getStringDigest();
    }

    /**
   * Calculate an MD5 hash of a file
   * @param file is the file to be calculated
   * @return the MD5 hash
   * @exception IOException
   */
    public static String getHash(File file) throws IOException {
        Md5 md5 = new Md5(new FileInputStream(file));
        md5.getDigest();
        return md5.getStringDigest();
    }
}
