package net.sourceforge.code2uml.inspectors.java;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.sourceforge.code2uml.inspectors.FileInspector;
import net.sourceforge.code2uml.unitdata.UnitInfo;

/**
 * FleInspector responsible for reading java .jar files. Extends
 * java.util.Observable and notifies its observers after reading each .class
 * file from given jar (during execution of both inspect() and glance() 
 * methods).
 *
 * @author Mateusz Wenus
 */
public class JarFileInspector extends Observable implements FileInspector {

    private static ConcurrentMap<String, ConcurrentMap<String, String>> cache = new ConcurrentHashMap<String, ConcurrentMap<String, String>>();

    /**
     * Creates a new instance of JarFileInspector.
     */
    public JarFileInspector() {
    }

    /**
     * Get definitions of classes/interfaces/enums from specified jar. Notifies
     * its observers after reading each .class file from that jar. Sets
     * notification argument to Integer value equal to the number of units
     * found in given .jar file so far.
     *
     * @param filePath path to the file
     * @return collection of objects representing classes/interfaces/enums
     *         defined in file <code>filePath</code>
     */
    public Collection<UnitInfo> inspect(String filePath) {
        return processJarFile(filePath, null, UnitInfo.class);
    }

    /**
     * Returns from specified file definitions of those classes/interfaces/enums
     * which have qualified names belonging to <code>namesFilter</code>. If
     * <code>namesFilter</code> is null, all definitions are returned. Notifies
     * its observers after reading each .class file from that jar. Sets
     * notification argument to Integer value equal to the number of units
     * found in given .jar file so far. <br/>
     * <code>inspect(filePath, null)</code> is equivalent to
     * <code>inspect(filePath)</code>
     *
     * @param filePath path to the file to read from
     * @param namesFilter collection of qualified names of classes/interfaces/enums
     *        which are allowed to be returned; if this parameter is null all
     *        definitions will be returned
     * @return collection of objects representing classes/interfaces/enums
     *         defined in file <code>filePath</code> which names are in <code>
     *         namesFiler</code> or null
     */
    public Collection<UnitInfo> inspect(String filePath, Collection<String> namesFilter) {
        return processJarFile(filePath, namesFilter, UnitInfo.class);
    }

    /**
     * Returns qualified names of classes/interfaces/enums defined in specified
     * file. Notifies its observers after reading each .class file from that 
     * jar. Sets notification argument to Integer value equal to the number of 
     * qualified classes/interfaces/enums names found in given .jar file so far.
     *
     * @param filePath file to read from
     * @return qualified names of classes/interfaces/enums defined in specified
     *         file
     */
    public Collection<String> glance(String filePath) {
        return processJarFile(filePath, null, String.class);
    }

    /**
     * Processes a .jar file and returns result of that processing. Actual 
     * behaviour depends on <code>resultType</code>: <br/>
     * - if it is String.class this method returns qualified names of all
     *   classes/interfaces/enums contained in file <code>filePath</code><br/>
     * - if it is UnitInfo.class this method returns definitions of those
     *   classes/interfaces/enums contained in file <code>filePath</code> 
     *   which qualified names are in <code>namesFilter</code>
     *
     * @param filePath path to a .jar file to process
     * @param namesFilter qualified names of classes/interfaces/enums that are
     *        allowed to be returned; if this parameter is null then all
     *        classes/interfaces/enums will be returned
     * @param resultType either String.class or UnitInfo.class
     */
    private <T> Collection<T> processJarFile(String filePath, Collection<String> namesFilter, Class<T> resultType) {
        ZipInputStream in = null;
        ConcurrentMap<String, String> jarCache = cache.get(filePath);
        if (jarCache == null) {
            jarCache = new ConcurrentHashMap<String, String>();
            cache.put(filePath, jarCache);
        }
        try {
            in = new ZipInputStream(new FileInputStream(filePath));
            Collection<T> result = new LinkedList<T>();
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                if (namesFilter != null && jarCache.containsKey(entry.getName()) && !namesFilter.contains(jarCache.get(entry.getName()))) {
                    continue;
                }
                T t = processZipEntry(in, entry, resultType);
                if (t != null) {
                    result.add(t);
                    if (!jarCache.containsKey(entry.getName())) {
                        if (t instanceof String) {
                            jarCache.putIfAbsent(entry.getName(), (String) t);
                        } else {
                            jarCache.putIfAbsent(entry.getName(), ((UnitInfo) t).getName());
                        }
                    }
                    setChanged();
                    notifyObservers(result.size());
                    clearChanged();
                }
            }
            return result.isEmpty() ? null : result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Processes an entry of a zip file and returns the result of that 
     * processing. Actual behaviour depends on <code>resultType</code>:<br/>
     * - if it is String.class this method returns a qualified name of a
     *   class/interface/enum defined in entry <code>entry</code><br/>
     * - if it is UnitInfo.class this method returns a UnitInfo representing
     *   a class/interface/enum defined in entry <code>entry</code><br/>
     * If entry <code>entry</code> is not a .class file this method returns 
     * null.
     *
     * @param zipIn ZipInputStream to read ZipEntry data from
     * @param entry ZipEntry taht will be processed
     * @param resultType either String.class or UnitInfo.class
     */
    private <T> T processZipEntry(ZipInputStream zipIn, ZipEntry entry, Class<T> resultType) {
        if (!entry.getName().endsWith(".class")) return null;
        int compression = entry.getMethod();
        if (compression != ZipEntry.STORED && compression != ZipEntry.DEFLATED) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            DataInputStream in = new DataInputStream(zipIn);
            ClassFileReader reader = new ClassFileReader();
            if (resultType.equals(String.class)) return (T) reader.readUnitName(in); else if (resultType.equals(UnitInfo.class)) return (T) reader.read(in);
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
