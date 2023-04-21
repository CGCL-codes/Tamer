package tasklist;

import java.io.*;
import java.util.Properties;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.MiscUtilities;

/**
 * This class has a large list of file types by file name extension. This list
 * is not all inclusive, but covers many of the more widely used file formats.
 * This class is to help identify binary files prior to loading them in a buffer,
 * which helps overall performance and helps reduce error messages.
 *
 * QUESTION: move this list to properties file? Make the list user-editable?
 * Answer: yes, do this. Some file types here may be text. The user may have
 * additional file types to add.
 */
public class Binary {

    static Properties binary;

    static {
        binary = new Properties();
        try {
            File homeDir = jEdit.getPlugin("tasklist.TaskListPlugin").getPluginHome();
            if (!homeDir.exists()) {
                homeDir.mkdir();
            }
            File propsFile = new File(homeDir, "binary.props");
            if (!propsFile.exists()) {
                binary.load(Binary.class.getResourceAsStream("/binary.props"));
                store(binary);
            } else {
                binary.load(new FileReader(propsFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void store(Properties props) {
        try {
            File homeDir = jEdit.getPlugin("tasklist.TaskListPlugin").getPluginHome();
            if (!homeDir.exists()) {
                homeDir.mkdir();
            }
            File propsFile = new File(homeDir, "binary.props");
            props.store(new FileWriter(propsFile), "Binary file extensions");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return A properties containing the list of known binary file types. The key
     * is a file extension, the value is a description of the file type.
     */
    public static Properties getBinaryTypes() {
        return binary;
    }

    /**
     * @param props A properties file containing a list of binary file types. The key
     * should be a file name extentions, the value should be a description of the file type.
     */
    public static void setBinaryTypes(Properties props) {
        binary = props;
        store(props);
    }

    /**
     * Restores the list of binary file types to the dist in binary.props. This lets
     * the user recover from accidentally deleting their list of binary file types.
     */
    public static void resetBinaryTypes() {
        binary = new Properties();
        try {
            File homeDir = jEdit.getPlugin("tasklist.TaskListPlugin").getPluginHome();
            if (!homeDir.exists()) {
                homeDir.mkdir();
            }
            binary.load(Binary.class.getResourceAsStream("/binary.props"));
            store(binary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the given filename represents a known binary file type.
     * @param filename A file name with extension.
     * @return True if the file name matches any of the known binary file types,
     * false otherwise. Note that a return value of "false" does not necessarily
     * mean the file is not binary.
     */
    public static boolean isBinary(String filename) {
        if (filename == null || filename.length() == 0) {
            throw new IllegalArgumentException("filename may not be null or empty");
        }
        for (Object ext : binary.keySet()) {
            if (filename.toLowerCase().endsWith((String) ext)) {
                return true;
            }
        }
        return false;
    }

    static boolean isBinary(Buffer buffer) {
        if (buffer == null || buffer.getLength() == 0 || buffer.getText() == null) {
            return false;
        }
        try {
            int maxChars = Math.min(buffer.getLength(), jEdit.getIntegerProperty("vfs.binaryCheck.length", 100));
            String bufferStart = buffer.getText(0, maxChars);
            ByteArrayInputStream bais = new ByteArrayInputStream(bufferStart.getBytes());
            return MiscUtilities.isBinary(bais);
        } catch (IOException e) {
            return false;
        }
    }
}
