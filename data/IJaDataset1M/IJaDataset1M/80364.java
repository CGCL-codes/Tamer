package net.face2face.core.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;

/**
 *
 * @author Patrice
 */
public class PluginArchive {

    private static Logger logger = Logger.getLogger("net.face2face.plugin");

    File file;

    ZipFile pluginFile;

    Properties properties;

    /** Creates a new instance of PluginFile */
    public PluginArchive(File file) throws ZipException, IOException {
        this.file = file;
        this.pluginFile = new ZipFile(file);
        ZipEntry propertiesEntry = pluginFile.getEntry("plugin.properties");
        properties = new Properties();
        properties.load(pluginFile.getInputStream(propertiesEntry));
    }

    public ImageIcon getIcon() throws IOException {
        String iconFile = properties.getProperty("plugin.icon");
        ZipEntry iconEntry = pluginFile.getEntry(iconFile);
        if (iconEntry != null) {
            InputStream iconInputStream = pluginFile.getInputStream(iconEntry);
            byte[] iconData = new byte[(int) iconEntry.getSize()];
            int byteRead = 0;
            int offset = 0;
            do {
                byteRead = iconInputStream.read(iconData, offset, iconData.length - offset);
                offset += byteRead;
            } while (byteRead > 0);
            ImageIcon icon = new ImageIcon(iconData);
            return icon;
        }
        return null;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return properties.getProperty("plugin.name");
    }

    public String getVersion() {
        return properties.getProperty("plugin.version");
    }

    void expandTo(File pluginDir) throws IOException {
        Enumeration zipEntries = pluginFile.entries();
        while (zipEntries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
            InputStream zipInputStream = pluginFile.getInputStream(zipEntry);
            File entryFile = new File(pluginDir, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                entryFile.mkdirs();
            } else {
                entryFile.getParentFile().mkdirs();
                logger.info("copyiing " + entryFile.getPath());
                entryFile.createNewFile();
                OutputStream entryOutputStream = new FileOutputStream(entryFile);
                byte[] byteBuffer = new byte[1000];
                int byteRead = zipInputStream.read(byteBuffer);
                while (byteRead >= 0) {
                    entryOutputStream.write(byteBuffer, 0, byteRead);
                    byteRead = zipInputStream.read(byteBuffer);
                }
                entryOutputStream.close();
            }
        }
    }

    public void close() throws IOException {
        pluginFile.close();
    }

    private Vector<String> getClasspath() {
        String classpath = properties.getProperty("plugin.classpath");
        Vector<String> classpathes = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(classpath, ";");
        while (tokenizer.hasMoreTokens()) {
            String path = tokenizer.nextToken();
            classpathes.add(path);
        }
        return classpathes;
    }
}
