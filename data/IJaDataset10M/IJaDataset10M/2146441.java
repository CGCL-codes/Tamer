package it.unibg.cs.jtvguide.xmltv;

import it.unibg.cs.jtvguide.log.PublicLogger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * An MD5 hash checker (used for test changes in the program's configuration file.
 * @author Michele
 *
 */
public class MD5Checksum {

    /**
	 * Check if two MD5 are equals
	 * @param file a file to calculate its md5
	 * @param MD5 md5 value to compare
	 * @return hmmm... let's guess...
	 */
    public static boolean checkMD5(String file, String MD5) {
        try {
            return getMD5Checksum(file).equals(MD5);
        } catch (Exception e) {
            PublicLogger.getLogger().error(e);
            return false;
        }
    }

    /**
	 * Returns the MD5 checksum of a file
	 * @param filename to calculate MD5
	 * @return the calculated MD5
	 * @throws Exception
	 */
    public static String getMD5Checksum(String filename) {
        byte[] b = null;
        try {
            b = createChecksum(filename);
        } catch (Exception e) {
            PublicLogger.getLogger().error(e);
        }
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
	 * Read the md5 stored in file
	 * @return a string containing the md5
	 */
    public static String readMD5FromFile() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(DefaultPrefs.CONFIG_FILE_MD5));
            String MD5 = br.readLine();
            br.close();
            return MD5;
        } catch (FileNotFoundException e) {
            PublicLogger.getLogger().error(e);
            return null;
        } catch (IOException e) {
            PublicLogger.getLogger().error(e);
            return null;
        }
    }

    public static boolean writeMD5ToFile(File f) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(DefaultPrefs.CONFIG_FILE_MD5));
            bw.write(getMD5Checksum(f.toString()));
            bw.close();
            return true;
        } catch (IOException e) {
            PublicLogger.getLogger().error(e);
            return false;
        } catch (Exception e) {
            PublicLogger.getLogger().error(e);
            return false;
        }
    }

    private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }
}
