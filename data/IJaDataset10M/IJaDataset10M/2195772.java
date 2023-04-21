package net.sourceforge.jfilecrypt.algorithms;

import java.io.*;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class is an simple utility which encapsulates a Vector<File>.
 * It provides only those methods an algorithm should be able to call
 * on the underlying Vector.
 * @since 0.2.1
 */
public class FileList {

    private Vector<File> vecFiles = new Vector<File>();

    /**
     * This constructor calls FileList(String files, ";").
     * @param files
     */
    public FileList(String files) {
        this(files, ":");
    }

    /**
     * This constructor parses a string containing a list of files
     * separated by the second argument.
     * @param files
     * @param separator
     */
    public FileList(String files, String separator) {
        parse(files, separator);
    }

    /**
     * This constructor simply adds every file of the specified array to itself.
     * @param files
     */
    public FileList(File[] files) {
        for (File f : files) {
            vecFiles.add(f);
        }
    }

    /**
     * This special constructor creates a FileList with only one file.
     * @param file
     */
    public FileList(File file) {
        vecFiles.add(file);
    }

    /**
     * like FileList(File[] files), but it uses an java.util.List instead of an array.
     * @param list
     */
    public FileList(List list) {
        for (int i = 0; i < list.size(); i++) {
            vecFiles.add(new File((String) list.get(i)));
        }
    }

    private void parse(String files, String separator) {
        StringTokenizer st = new StringTokenizer(files, separator, false);
        while (st.hasMoreTokens()) {
            String file = st.nextToken();
            vecFiles.add(new File(file));
        }
    }

    /**
     * returns the Vector-own iterator.
     * @return
     */
    public Iterator getIterator() {
        return vecFiles.iterator();
    }

    /**
     * returns the file at the given position.
     * @param pos
     * @return
     */
    public File get(int pos) {
        if (pos > vecFiles.size()) {
            return null;
        } else {
            return vecFiles.get(pos);
        }
    }

    /**
     * return the count of files stored.
     * @return
     */
    public int size() {
        return vecFiles.size();
    }

    /**
     * Adds a file to the list.
     * @param f
     */
    public void add(File f) {
        vecFiles.add(f);
    }
}
