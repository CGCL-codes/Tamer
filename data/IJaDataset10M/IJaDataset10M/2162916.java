package auxx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Yan Couto
 */
public class FileManager {

    private static ArrayList<File> files = new ArrayList<File>();

    protected FileManager() {
    }

    public static Scanner getReader(File f) {
        try {
            return new Scanner(new BufferedReader(new FileReader(f)));
        } catch (Exception ex) {
            return null;
        }
    }

    public static ObjectInputStream getObjectReader(File f) {
        try {
            return new ObjectInputStream(new FileInputStream(f));
        } catch (Exception ex) {
            return null;
        }
    }

    public static PrintWriter getWriter(File f, boolean append) {
        try {
            return new PrintWriter(new BufferedWriter(new FileWriter(f, append)), true);
        } catch (Exception ex) {
            return null;
        }
    }

    public static ObjectOutputStream getObjectWriter(File f, boolean append) {
        try {
            return new ObjectOutputStream(new FileOutputStream(f, append));
        } catch (Exception ex) {
            return null;
        }
    }

    public static String[] getFullText(File f) {
        ArrayList<String> lines = new ArrayList<String>();
        Scanner sc = getReader(f);
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        String[] a = new String[lines.size()];
        return lines.toArray(a);
    }

    public static File[] getFiles(File dir) {
        if (dir.isFile()) return null;
        getFilesAux(dir);
        File[] f = new File[files.size()];
        f = files.toArray(f);
        files.clear();
        return f;
    }

    private static void getFilesAux(File f) {
        File[] sons = f.listFiles();
        for (File son : sons) {
            if (son.isDirectory()) getFilesAux(son); else files.add(son);
        }
    }

    public static boolean isEmpty(File f) {
        if (f.isDirectory() && f.listFiles() == null) return true;
        if (f.isFile() && !getReader(f).hasNext()) return true;
        return false;
    }

    public static void delete(File f) {
        if (f.isFile() || FileManager.isEmpty(f)) f.delete(); else {
            File[] sons = f.listFiles();
            for (File son : sons) delete(son);
            f.delete();
        }
    }

    public static void deleteAllIn(File f) {
        File[] sons = f.listFiles();
        for (File son : sons) delete(son);
    }

    public static void addFilter(JFileChooser toAdd, String extension, String description) {
        String[] ext = { extension };
        String[] desc = { description };
        addFilters(toAdd, ext, desc);
    }

    public static void addFilters(JFileChooser toAdd, String[] extension, String[] description) {
        String regex[][] = new String[extension.length][];
        for (int i = 0; i < regex.length; i++) regex[i] = extension[i].split("\t");
        for (int i = 0; i < regex.length; i++) {
            for (int k = 0; k < regex[i].length; k++) {
                regex[i][k] = (regex[i][k].startsWith(".") ? "" : ".") + regex[i][k];
            }
        }
        if (regex.length != description.length) throw new IllegalArgumentException();
        int count = regex.length;
        for (int i = 0; i < count; i++) {
            final String[] reg = regex[i];
            final String desc = description[i];
            toAdd.addChoosableFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    boolean b = false;
                    for (String r : reg) if (f.getName().endsWith(r)) b = true;
                    return b;
                }

                @Override
                public String getDescription() {
                    String d = desc + " (";
                    for (String r : reg) {
                        d += r;
                        if (r != reg[reg.length - 1]) d += ",";
                    }
                    d += ")";
                    return d;
                }
            });
        }
    }
}
