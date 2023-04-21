package it.polito.semreview.utils;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class Utilities {

    public static final String methodNode = "Method";

    public static final String[] bugSeverity = { "-low", "-medium", "-high" };

    public static String bugInstance = "BugInstance";

    public static String priorityAttr = "priority";

    public static String categoryAttr = "category";

    public static String[] bugCategories = { "BAD_PRACTICE", "CORRECTNESS", "STYLE", "PERFORMANCE", "MALICIOUS_CODE", "MULTITHREADED_CORRECTNESS", "SECURITY" };

    public static String startAttr = "start";

    public static String endAttr = "end";

    public static String srcLine = "SourceLine";

    public static String fieldNode = "Field";

    public static String classNode = "Class";

    public static String classNameAttr = "classname";

    public static String bugTypeAttr = "type";

    public static String removedByChangeAttr = "removedByChange";

    public static String introducedByChangeAttr = "introducedByChange";

    public static String firstAttr = "first";

    public static String lastAttr = "last";

    public static String nameAttr = "name";

    public static String summary = "FindBugsSummary";

    public static String totClasses = "total_classes";

    public static final String[] individualResultsFiles = { "_i_true_positive", "_i_false_positive", "_i_lab", "_i_home", "_i_NEW_CODE", "_i_ADDED", "_i_FIXED", "_i_REMOVED", "_i_STILL_THERE" };

    public static Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) {
        Vector<File> files = new Vector<File>();
        File[] entries = directory.listFiles();
        for (File entry : entries) {
            if (filter == null || filter.accept(directory, entry.getName())) {
                files.add(entry);
            }
            if (recurse && entry.isDirectory()) {
                files.addAll(listFiles(entry, filter, recurse));
            }
        }
        return files;
    }

    /**
	 * 
	 * @param path of the file to open
	 * @param lines to append
	 */
    public static void appendLineInFile(String path, String lines) {
        FileOutputStream fouts;
        try {
            fouts = new FileOutputStream(path, true);
            DataOutputStream douts = new DataOutputStream(fouts);
            douts.writeBytes(lines.toString());
            douts.flush();
            douts.close();
            fouts.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeCSV(File outFile, String firstRow) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(firstRow);
            bw.flush();
            fw.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDirectory(File sourceDir, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        File[] children = sourceDir.listFiles();
        for (File sourceChild : children) {
            String name = sourceChild.getName();
            File destChild = new File(destDir, name);
            if (sourceChild.isDirectory()) {
                copyDirectory(sourceChild, destChild);
            } else {
                copyFile(sourceChild, destChild);
            }
        }
    }

    public static void copyFile(File source, File dest) {
        if (!dest.exists()) {
            try {
                dest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File findHomeVersion(File dir, String substring) {
        File found = null;
        Collection<File> c = listFiles(dir, null, false);
        Iterator<File> it = c.iterator();
        while (it.hasNext()) {
            File f = it.next();
            if (f.getName().contains(substring)) {
                found = f;
                break;
            }
        }
        return found;
    }

    public static File getFileFromCollection(String name, Collection<File> filesCollection) {
        for (Iterator<File> iterator = filesCollection.iterator(); iterator.hasNext(); ) {
            File file = iterator.next();
            if (file.getName().equalsIgnoreCase(name)) return file;
        }
        return null;
    }

    public static boolean isChangeInUniqueLine(String line) {
        boolean changeUniqueLine = true;
        char[] chars = line.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            if (!Character.isDigit(chars[j])) {
                if (chars[j] == ':') {
                    changeUniqueLine = true;
                } else if (chars[j] == ',') {
                    changeUniqueLine = false;
                }
                break;
            }
        }
        return changeUniqueLine;
    }

    /**
	 * 
	 * @param path of the file to create, it exits if file already exists
	 * 
	 */
    public static File createFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            fatalError("Cannot create " + path + " - file already existing");
        } else {
            boolean created = false;
            try {
                created = f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                fatalError("Cannot create " + path);
            }
            if (!created) {
                fatalError("Cannot create " + path);
            }
        }
        return f;
    }

    /**
	 * 
	 * @param message
	 *            It prints out an error message and the exits
	 */
    public static void fatalError(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
