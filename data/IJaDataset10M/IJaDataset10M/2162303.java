package net.sourceforge.liftoff.installer;

import java.util.*;
import java.io.*;
import java.util.zip.*;

/**
 * This class is a container for the elemnts from the classpath. 
 * <p>
 * It will be initialized from the elements of the system classpath
 * on startup.
 * <p>
 * Usualy there is only one ClasspathInfo Object. You can get a reference
 * to it from the <h href="installer.Info.html">Info</a> Object.
 * <p>
 * Other Objects can register themself as an 'Observer' of a
 * ClasspathInfo Object, everytime the classpath is changed they
 * get a notification.
 *
 * @author Andreas Hofmeister
 * @version $Revision: 1.2 $
 *
 */
public class ClasspathInfo extends Observable {

    /**
     * This vector contains the elements from the classpath.
     */
    private Vector parts;

    private Vector extraParts;

    /**
     * Create a new ClasspathInfo object.
     */
    public ClasspathInfo() {
        parts = new Vector();
        extraParts = new Vector();
        loadFromSystemClasspath();
    }

    /**
     * Load the 'parts'-Vector from the system classpath.
     */
    private void loadFromSystemClasspath() {
        parts.removeAllElements();
        String classpath = Info.getProperty("java.class.path");
        StringTokenizer tok = new StringTokenizer(classpath, File.pathSeparator);
        while (tok.hasMoreElements()) {
            parts.addElement(tok.nextToken());
        }
        Info.setProperty("classpath", asString());
    }

    /**
     * Add an element to the classpath.
     * <p>
     * This method also notify the Observers about the change.
     *
     * @param path the new Element.
     */
    public void add(String path) {
        parts.insertElementAt(path, 0);
        Info.setProperty("classpath", asString());
        setChanged();
        notifyObservers();
    }

    /**
     * Remove an element from the classpath.
     * <p>
     * This method also notify the Observers about the change.
     *
     * @param path the element to remove.
     */
    public void remove(String path) {
        parts.removeElement(path);
        Info.setProperty("classpath", asString());
        setChanged();
        notifyObservers();
    }

    /**
     * Return an enumeration of all parts in the classpath.
     *
     * @return an Enumeration of all parts of the classpath. This
     * Enumeration my be empty but is nevver null.
     */
    public Enumeration parts() {
        return parts.elements();
    }

    /**
     * returns the size of the classpath, this is the number of
     * parts.
     *
     * @return the number of elements in the classpath.
     */
    public int size() {
        return parts.size();
    }

    /**
     * return the classpath as String.
     * <p>
     * The returned string is suitable as a value for the
     * <code>CLASSPATH</code> variable in scripts.
     * <p>
     * If there are no elements in the classpath, the result is
     * an empty string.
     * <p>
     * @return the classpath as a string.
     */
    public String asString() {
        String classpath = "";
        Enumeration pe = extraParts.elements();
        while (pe.hasMoreElements()) {
            classpath += File.pathSeparator + (String) pe.nextElement();
        }
        pe = parts();
        while (pe.hasMoreElements()) {
            classpath += File.pathSeparator + (String) pe.nextElement();
        }
        if (classpath.length() > 0) classpath = classpath.substring(1);
        return classpath;
    }

    /**
     * Find the path of a resource in the classpath. <p>
     *
     * This method iterates over the elements in the classpath.
     * If the element is a Zip or JAR file we will try to find the
     * resource in this file, if it is a directory we append
     * the directory name to the name of the resource and look if the
     * result is a file.
     * 
     * @return the first element from the classpath that contains
     *         the resource, null if the resource was not found.
     * 
     */
    public String findResource(String res) {
        Enumeration pe = parts();
        while (pe.hasMoreElements()) {
            String part = (String) pe.nextElement();
            File pf = new File(part);
            if (pf.isDirectory()) {
                if (!part.endsWith("" + File.separatorChar)) {
                    part = part + File.separatorChar;
                }
                String rfn = part + res.replace('/', File.separatorChar);
                File rf = new File(rfn);
                if (rf.exists()) return rfn;
            } else {
                ZipFile zip = null;
                ZipEntry zipE = null;
                try {
                    zip = new ZipFile(part);
                    zipE = zip.getEntry(res);
                } catch (IOException e) {
                    zip = null;
                }
                if (zipE != null) return res;
            }
        }
        return null;
    }

    public void clearExtraParts() {
        extraParts.removeAllElements();
        Info.setProperty("classpath", Info.getClasspathInfo().asString());
    }

    public void addExtraPart(String part) {
        extraParts.addElement(part);
        Info.setProperty("classpath", Info.getClasspathInfo().asString());
    }
}
