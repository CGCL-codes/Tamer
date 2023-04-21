package pl.rzarajczyk.utils.application;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * TODO: restrict resources to certain folder only
 * @author rafalz
 */
public class ResourcesManager {

    public URI getSourceCodeUri() throws IOException {
        Class<?> clazz = getClass();
        try {
            return clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public boolean isApplicationPacked() throws IOException {
        URI uri = getSourceCodeUri();
        File file = new File(uri);
        return file.isFile();
    }

    public void unpack(String classpathResourcePrefix, File dir) throws IOException {
        if (isApplicationPacked()) {
            unpackFromJar(classpathResourcePrefix, dir);
        } else {
            unpackFromDir(classpathResourcePrefix, dir);
        }
    }

    void unpackFromJar(String classpathResource, File dir) throws IOException {
        JarFile jarFile = new JarFile(getSourceCodeUri().getPath());
        unpackFromJar(jarFile, classpathResource, dir);
    }

    void unpackFromJar(JarFile jarFile, String classpathResource, File dir) throws IOException {
        try {
            Enumeration<JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                String name = entry.getName();
                if (name.startsWith(classpathResource)) {
                    if (name.endsWith("/")) {
                        File subdir = new File(dir, name);
                        if (!subdir.mkdirs()) throw new IOException("Could not create dir: " + subdir);
                    } else {
                        name = "/" + name;
                        File file = new File(dir, name);
                        InputStream input = getClass().getResourceAsStream(name);
                        OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                        try {
                            ByteStreams.copy(input, output);
                        } catch (IOException e) {
                            throw new IOException("Error during copying from classpath:" + name + " to file:" + file, e);
                        } finally {
                            output.close();
                        }
                    }
                }
            }
        } finally {
            jarFile.close();
        }
    }

    List<String> getDirContents(File dir, String prefix) {
        List<String> result = new ArrayList<String>();
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                result.add(prefix + file.getName());
            } else {
                result.add(prefix + file.getName() + File.separator);
                result.addAll(getDirContents(file, prefix + file.getName() + File.separator));
            }
        }
        return result;
    }

    void unpackFromDir(String classpathResource, File dir) throws IOException {
        File sourceDir = new File(getSourceCodeUri());
        unpackFromDir(sourceDir, classpathResource, dir);
    }

    void unpackFromDir(File sourceDir, String classpathResource, File dir) throws IOException {
        List<String> contents = getDirContents(sourceDir, "");
        for (String name : contents) {
            if (name.startsWith(classpathResource)) {
                if (name.endsWith(File.separator)) {
                    File subdir = new File(dir, name);
                    if (!subdir.mkdirs()) throw new IOException("Could not create dir: " + subdir);
                } else {
                    File fileFrom = new File(sourceDir, name);
                    File toFile = new File(dir, name);
                    Files.copy(fileFrom, toFile);
                }
            }
        }
    }
}
