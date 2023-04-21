package test.zmpp.testutil;

import java.io.File;

/**
 * Usefule utility methods for testing ZMPP.
 * @author Wei-ju Wu
 * @version 1.5
 */
public class ZmppTestUtil {

    /**
   * Helper function to create a file local to the project given the
   * local path.
   * @param localPath the local path
   * @return the File
   */
    public static File createLocalFile(String localPath) {
        return new File(System.getProperty("user.dir") + File.separatorChar + localPath);
    }
}
