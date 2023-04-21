package com.l2jserver.gameserver.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This is a class loader for the dynamic extensions used by DynamicExtension class.
 *
 * @version $Revision: $ $Date: $
 * @author  galun
 */
public class JarClassLoader extends ClassLoader {

    private static Logger _log = Logger.getLogger(JarClassLoader.class.getCanonicalName());

    HashSet<String> _jars = new HashSet<String>();

    public void addJarFile(String filename) {
        _jars.add(filename);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] b = loadClassData(name);
            return defineClass(name, b, 0, b.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
    }

    private byte[] loadClassData(String name) throws IOException {
        byte[] classData = null;
        for (String jarFile : _jars) {
            ZipFile zipFile = null;
            DataInputStream zipStream = null;
            try {
                File file = new File(jarFile);
                zipFile = new ZipFile(file);
                String fileName = name.replace('.', '/') + ".class";
                ZipEntry entry = zipFile.getEntry(fileName);
                if (entry == null) continue;
                classData = new byte[(int) entry.getSize()];
                zipStream = new DataInputStream(zipFile.getInputStream(entry));
                zipStream.readFully(classData, 0, (int) entry.getSize());
                break;
            } catch (IOException e) {
                _log.log(Level.WARNING, jarFile + ": " + e.getMessage(), e);
                continue;
            } finally {
                try {
                    zipFile.close();
                } catch (Exception e) {
                }
                try {
                    zipStream.close();
                } catch (Exception e) {
                }
            }
        }
        if (classData == null) throw new IOException("class not found in " + _jars);
        return classData;
    }
}
