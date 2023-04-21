package jarexplorer.explorer;

import jarexplorer.explorer.event.EndFileJarEvent;
import jarexplorer.explorer.event.EndJarEvent;
import jarexplorer.explorer.event.EndNestedJarEvent;
import jarexplorer.explorer.event.EntryJarEvent;
import jarexplorer.explorer.event.JarEvent;
import jarexplorer.explorer.event.ProblemJarEvent;
import jarexplorer.explorer.event.StartFileJarEvent;
import jarexplorer.explorer.event.StartNestedJarEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarExplorer extends Observable {

    private final class ZipFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith("zip") || name.endsWith("jar");
        }
    }

    private String m_filename;

    public JarExplorer(String dirName) {
        m_filename = dirName;
    }

    public void parse() {
        try {
            File root = new File(m_filename);
            if (root.isDirectory()) {
                for (File file : root.listFiles(new ZipFilenameFilter())) {
                    if (file.isFile()) {
                        notifyEvent(new StartFileJarEvent(file.getName()));
                        processFile(file);
                        notifyEvent(new EndFileJarEvent(file.getName()));
                    }
                }
            } else {
                notifyEvent(new StartFileJarEvent(root.getName()));
                processFile(root);
                notifyEvent(new EndFileJarEvent(root.getName()));
            }
        } catch (Exception e) {
            notifyEvent(new ProblemJarEvent(e.getMessage()));
        }
        notifyEvent(new EndJarEvent());
    }

    private void processFile(File file) throws Exception {
        ZipFile jf = new ZipFile(file);
        Enumeration en = jf.entries();
        while (en.hasMoreElements()) {
            ZipEntry jarEntry = (ZipEntry) en.nextElement();
            notifyEvent(new EntryJarEvent(jarEntry.getName()));
            if (jarEntry.getName().endsWith("jar")) {
                String timeStamp = System.currentTimeMillis() + "";
                File nestedJarFile = new File(timeStamp + file.getName());
                System.out.println(jarEntry.getName());
                InputStream is = jf.getInputStream(jarEntry);
                FileOutputStream fos = new java.io.FileOutputStream(nestedJarFile);
                while (is.available() > 0) {
                    fos.write(is.read());
                }
                fos.close();
                is.close();
                notifyEvent(new StartNestedJarEvent(jarEntry.getName()));
                processFile(nestedJarFile);
                nestedJarFile.delete();
                notifyEvent(new EndNestedJarEvent(jarEntry.getName()));
            }
        }
    }

    private void notifyEvent(JarEvent je) {
        setChanged();
        notifyObservers(je);
    }
}
