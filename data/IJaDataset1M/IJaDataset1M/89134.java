package com.visitrend.ndvis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;

/**
 * Utilities that various other classes in the ndvis package use, thus I've put
 * them in here so they're accessible for all. To save memory per JVM and
 * because these methods work the same for all I've made this a singleton with
 * static classes. Therefore until I add a non-static method, you can call the
 * methods in here statically without getting any instance.
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 * 
 */
public class Utils {

    private static Utils instance;

    private Utils() {
    }

    public static Utils getInstance() {
        if (instance == null) instance = new Utils();
        return instance;
    }

    /**
	 * This method creates 2 new short[]s with every call so don't put it in a
	 * loop over the database or anything that will cause repetitive calling of
	 * this method and generate a bunch of short[]s.
	 * 
	 * @param paramValues
	 * @param oldOrder
	 * @param newOrder
	 * @return a short[] with the newly ordered parameter values
	 */
    public static short[] reorderShorts(short[] paramValues, short[] oldOrder, short[] newOrder) {
        short[] orig = new short[paramValues.length];
        for (int k = 0; k < paramValues.length; k++) {
            orig[oldOrder[k]] = paramValues[k];
        }
        short[] answer = new short[paramValues.length];
        for (int k = 0; k < paramValues.length; k++) {
            answer[k] = orig[newOrder[k]];
        }
        return answer;
    }

    public static void sync(short[] a, short[] b) {
        for (int k = 0; k < a.length; k++) {
            a[k] = b[k];
        }
    }

    public static String doublesToString(double[] d) {
        String str = ("\n");
        for (int k = 0; k < d.length; k++) {
            str += d[k] + " ";
        }
        return str;
    }

    public static void printDoubles(double[] d) {
        System.out.print(doublesToString(d));
    }

    public static void printDoubles(double[][] d) {
        for (int k = 0; k < d.length; k++) {
            printDoubles(d[k]);
        }
    }

    public static void printShorts(short[] d) {
        String str = ("\n");
        for (int k = 0; k < d.length; k++) {
            str += d[k] + " ";
        }
        System.out.print(str);
    }

    /**
	 * Copy a file from its source location to the destination.
	 * If the destination file already exists, the user will be
	 * prompted whether or not to overwrite.
	 * 
	 * Adapted from code found on the web here:
	 * http://www.roseindia.net/java/example/java/io/MovingFile.shtml
	 * 
	 * @param source
	 *    The file to copy
	 * @param dest
	 *    The destination, where the file will be copied to
	 * @return
	 *    true if the file is copied, false otherwise
	 * @throws IOException
	 */
    public static boolean copyFile(File source, File dest) throws IOException {
        int answer = JOptionPane.YES_OPTION;
        if (dest.exists()) {
            answer = JOptionPane.showConfirmDialog(null, "File " + dest.getAbsolutePath() + "\n already exists.  Overwrite?", "Warning", JOptionPane.YES_NO_OPTION);
        }
        if (answer == JOptionPane.NO_OPTION) return false;
        dest.createNewFile();
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
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
