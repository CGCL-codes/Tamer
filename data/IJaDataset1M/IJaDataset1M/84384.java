package net.sf.ajio.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;

/**
 * AJIO
 * 
 * @author Olivier CHABROL olivierchabrol@users.sourceforge.net
 * @copyright (C)2004 Olivier CHABROL
 */
public class Jarer {

    private static Logger _log = Logger.getLogger(Jar.class.getName());

    /**
     * direct a source directory to a JarOutputStream
     * 
     * @param sourceDir
     *            <code>String</code> the directory to be jarred
     * @param out
     *            <code>JarOutputStream</code> the jar output stream
     * @param parent
     *            <code>String</code> the parent of the current entry
     */
    public static void jar(String sourceDir, JarOutputStream out, String parent) {
        try {
            File f = new File(sourceDir);
            String files[] = f.list();
            if (parent != "") {
                parent = parent + "/";
            }
            File tempFile;
            BufferedInputStream origin = null;
            byte data[] = new byte[2048];
            for (int i = 0; i < files.length; i++) {
                _log.debug(sourceDir + "/" + files[i]);
                tempFile = new File(sourceDir + File.separatorChar + files[i]);
                if (tempFile.isFile()) {
                    FileInputStream fi = new FileInputStream(sourceDir + File.separatorChar + files[i]);
                    origin = new BufferedInputStream(fi, 2048);
                    JarEntry entry = new JarEntry(parent + files[i]);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, 2048)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                } else if (tempFile.isDirectory()) {
                    JarEntry entry = new JarEntry(parent + files[i]);
                    out.putNextEntry(entry);
                    jar(sourceDir + File.separatorChar + files[i], out, parent + files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toJarString(String jarEntry) {
        return jarEntry.replace('\\', '/');
    }

    public static void jar(Map map, JarOutputStream out) throws IOException {
        Iterator it = map.keySet().iterator();
        String tempFilePath = null;
        String key = null;
        BufferedInputStream origin = null;
        byte data[] = new byte[2048];
        File tempFile = null;
        while (it.hasNext()) {
            key = (String) it.next();
            tempFilePath = (String) map.get(key);
            tempFile = new File((String) map.get(key));
            if (!tempFile.isDirectory()) {
                _log.debug("Ajout de " + tempFilePath);
                try {
                    FileInputStream fi = new FileInputStream(tempFilePath);
                    origin = new BufferedInputStream(fi, 2048);
                    JarEntry entry = new JarEntry(toJarString(key));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, 2048)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * jar a source directory
     * 
     * @param sourceDir
     *            <code>String</code> the source directory to be jarred
     * @param jarDest
     *            <code></code> the output jar file
     * @param manifest
     *            <code></code> the manifest used for the jarring operation
     */
    public static void jar(Map map, String jarDest, Manifest manifest) {
        _log.debug("jar(map, " + jarDest + ", " + manifest + ")");
        try {
            JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarDest)));
            jar(map, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * jar a source directory
     * 
     * @param sourceDir
     *            <code>String</code> the source directory to be jarred
     * @param jarDest
     *            <code></code> the output jar file
     * @param manifest
     *            <code></code> the manifest used for the jarring operation
     */
    public static void jar(String sourceDir, String jarDest, Manifest manifest) {
        _log.debug("jar(" + sourceDir + ", " + jarDest + ", " + manifest + ")");
        try {
            JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarDest)));
            jar(sourceDir, out, "");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
