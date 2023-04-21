package de.fmf.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author fma (Falko Macheleidt)
 * @since 21.02.2006
 */
public class ODTUtil {

    public static String JAVAC13 = "javac1.3";

    public static String JAVAC14 = "javac1.4";

    public static String JAVAC15 = "javac1.5";

    public static String JAVAC16 = "javac1.6";

    public static String VERSION_NUMBER_ERROR = "error";

    /**
     * Deletes the given File or Folder. (recursive)
     * 
     * @param the
     *            file or folder to delete
     */
    public static void deleteAll(File f) {
        if (f != null) {
            File[] toDels = f.listFiles();
            if (toDels != null) {
                for (int i = 0; i < toDels.length; i++) {
                    File todel = toDels[i];
                    if (todel.isDirectory()) deleteAll(todel);
                    todel.delete();
                }
            }
        }
        f.delete();
    }

    /**
     * scans a directory (recursive)
     * 
     * @param dir
     * @return a collection containing all files and folders in the given
     *         directory
     */
    public static Collection resolveDirContent(File dir) {
        Collection files = new ArrayList();
        if ((dir != null) && !dir.isDirectory()) return null;
        scanDir(files, dir);
        return files;
    }

    /**
     * scans a directory, adds the the files and folders to a collection
     * (recursive)
     * 
     * @param files
     * @param dir
     */
    private static void scanDir(Collection files, File dir) {
        File[] content = dir.listFiles();
        if (content != null) {
            for (int i = 0; i < content.length; i++) {
                File f = content[i];
                if (f.isDirectory()) scanDir(files, f);
                files.add(f);
            }
        }
    }

    /**
     * 
     * Checks the java version of a given class file. !make sure that the given
     * File is a Java class File!
     * 
     * @param fileToCheck
     * @return version Number of a given class File
     */
    public static String checkClassVersion(File fileToCheck) {
        String ret = VERSION_NUMBER_ERROR;
        int minor = 0;
        int major = 0;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(fileToCheck));
            int magic = in.readInt();
            if (magic != 0xcafebabe) {
                System.out.println(fileToCheck + " is not a valid class!");
            }
            minor = in.readUnsignedShort();
            major = in.readUnsignedShort();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch(major) {
            case 47:
                {
                    if (minor == 0) ret = JAVAC13;
                    break;
                }
            case 48:
                {
                    if (minor == 0) ret = JAVAC14;
                    break;
                }
            case 49:
                {
                    if (minor == 0) ret = JAVAC15;
                    break;
                }
            case 50:
                {
                    if (minor == 0) ret = JAVAC16;
                    break;
                }
            default:
                System.out.println("major  minor Java platform version");
                System.out.println("----------------------------------");
                System.out.println("45       3           1.0");
                System.out.println("45       3           1.1");
                System.out.println("46       0           1.2");
                System.out.println("47       0           1.3");
                System.out.println("48       0           1.4");
                System.out.println("49       0           1.5");
                System.out.println("50       0           1.6\n\n");
                System.out.println(fileToCheck + "\tmajor=" + major + "\tminor=" + minor);
                ret = "UNKNOWN VERSION NUMBER ... \tmajor=" + major + "\tminor=" + minor;
                break;
        }
        return ret;
    }
}
