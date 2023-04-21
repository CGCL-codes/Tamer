package com.panopset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.panopset.Util.*;

/**
 * Applications shouldn't care whether or not input comes from a file
 * or memory.
 *
 * If constructed with a File, the file will be processed line by line.
 *
 * If constructed with a String array or Vector, those objects will also
 * be processed in proper order.
 *
 * @author Karl Dinwiddie
 */
public class StringLineSupplier implements Commons {

    private String name;

    private String fullName;

    private String[] a;

    private BufferedReader r;

    public File file;

    private List<String> l;

    private int index = -1;

    /**
     * Call this method if you want to process the lines again from the first one.
     */
    public void reset() {
        index = 0;
        if (r != null) {
            try {
                r.close();
            } catch (IOException ex) {
                Logger.getLogger(StringLineSupplier.class.getName()).log(Level.INFO, null, ex);
            }
        }
        if (file != null && file.exists()) {
            try {
                r = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StringLineSupplier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param file text File to process.
     */
    public StringLineSupplier(File file) {
        this.file = file;
        name = file.getName();
        fullName = getCanonicalPath(file);
        reset();
    }

    /**
     *
     * @param stringArray String Array to process.
     */
    public StringLineSupplier(String[] array) {
        a = array;
        reset();
    }

    /**
     *
     * @param list String list to process.
     */
    public StringLineSupplier(List<String> list) {
        l = list;
        reset();
    }

    public String getName() {
        if (name == null) {
            name = EMPTY_STRING;
        }
        return name;
    }

    public String getFullName() {
        if (fullName == null) {
            fullName = getName();
        }
        return fullName;
    }

    public String getText() {
        reset();
        StringWriter sw = new StringWriter();
        String s = next();
        while (s != null) {
            sw.append(s);
            sw.append(getReturnChar());
            s = next();
        }
        return sw.toString();
    }

    /**
     * 
     * @return null if there are no more lines available.
     */
    public String next() {
        if (index == -1) {
            throw new RuntimeException("reset() must be called first.");
        }
        if (r != null) {
            return nextFromFile();
        } else if (l != null) {
            return nextFromList();
        } else if (a != null) {
            return nextFromArray();
        }
        return null;
    }

    public String nextFromFile() {
        String s = null;
        try {
            s = r.readLine();
            if (s == null) {
                r.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(StringLineSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check(s);
    }

    private String check(String s) {
        if (s == null) {
            index = -1;
        }
        return s;
    }

    public String nextFromList() {
        return check(l.get(index++));
    }

    private String nextFromArray() {
        return check(index > (a.length - 1) ? null : a[index++]);
    }
}
