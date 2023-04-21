package de.jchirp.data;

import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import de.jchirp.view.JChirpWindow;
import de.jchirp.view.UpdaterView;

public class Updater {

    public static String updateURL = "http://www.ret-world.de/JChirp/";

    private Properties update;

    private URL updatefile;

    private File updaterFile;

    private File file;

    private UpdaterView updater;

    private ArrayList<File> files;

    public boolean checkUpdate() {
        try {
            updatefile = new URL(updateURL + "update.xml");
            update = new Properties();
            update.loadFromXML(updatefile.openStream());
            if (update.get("name").equals("JChirp")) {
                String[] ownVersion = JChirpWindow.version.split("\\.");
                String[] newVersion = ((String) update.get("version")).split("\\.");
                if (Integer.parseInt(ownVersion[0]) < Integer.parseInt(newVersion[0])) {
                    if (JOptionPane.showConfirmDialog(null, "A new version is available. Update now?", "Updater", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        this.foundNewVersion();
                        return true;
                    }
                } else {
                    if (Integer.parseInt(ownVersion[0]) == Integer.parseInt(newVersion[0])) {
                        if (Integer.parseInt(ownVersion[1]) < Integer.parseInt(newVersion[1])) {
                            if (JOptionPane.showConfirmDialog(null, "A new version is available. Update now?", "Updater", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                this.foundNewVersion();
                                return true;
                            }
                        } else {
                            if (Integer.parseInt(ownVersion[1]) == Integer.parseInt(newVersion[1])) {
                                if (Integer.parseInt(ownVersion[2]) < Integer.parseInt(newVersion[2])) {
                                    if (JOptionPane.showConfirmDialog(null, "A new version is available. Update now?", "Updater", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                        this.foundNewVersion();
                                        return true;
                                    }
                                } else {
                                    if (Integer.parseInt(ownVersion[2]) == Integer.parseInt(newVersion[2])) {
                                    } else {
                                    }
                                }
                            } else {
                            }
                        }
                    } else {
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not connect to Server. Check your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Bad update file. Check your internet connection and try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not get update data. Check your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void foundNewVersion() {
        updater = new UpdaterView();
        updater.setLabelText("Initiating Updater...");
        updater.setProgress(0);
        updater.setLocationRelativeTo(null);
        updater.setVisible(true);
        URL pathUrl = ClassLoader.getSystemResource("img/icon.png");
        String path = pathUrl.toString();
        path = path.substring(4, path.length() - 14);
        try {
            file = new File(new URI(path));
            updaterFile = new File(new URI(path.substring(0, path.length() - 4) + "Updater.jar"));
            if (updaterFile.exists()) {
                updaterFile.delete();
            }
            updater.setProgress(25);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        FileChannel in = (new FileInputStream(file)).getChannel();
                        FileChannel out = (new FileOutputStream(updaterFile)).getChannel();
                        in.transferTo(0, file.length(), out);
                        updater.setProgress(50);
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startUpdater();
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not create Updater. Check folder permission.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startUpdater() {
        try {
            updater.setProgress(75);
            if (Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/" + "java -jar " + updaterFile.getCanonicalPath() + " -update " + file.getCanonicalPath()) == null) {
                JOptionPane.showMessageDialog(null, "Could not start Updater!\n (java -jar " + updaterFile.getCanonicalPath() + " -update " + file.getCanonicalPath());
            }
            Thread.sleep(500);
            updater.setProgress(100);
            Thread.sleep(1000);
            updater.dispose();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not start Updater.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(String path) {
        try {
            file = new File(path);
            final String path2 = file.getCanonicalPath().substring(0, file.getCanonicalPath().length() - file.getName().length());
            updaterFile = new File(path2 + "update.zip");
            if (updaterFile.exists()) {
                updaterFile.delete();
            }
            final URL updatePath = new URL(updateURL + "update.zip");
            updater = new UpdaterView();
            updater.setLabelText("Downloading Updates...");
            updater.setProgress(0);
            updater.setLocationRelativeTo(null);
            updater.setVisible(true);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        byte[] buf = new byte[1024];
                        URLConnection uc = updatePath.openConnection();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(updaterFile));
                        InputStream in = uc.getInputStream();
                        int byteRead, byteWritten = 0;
                        while ((byteRead = in.read(buf)) != -1) {
                            out.write(buf, 0, byteRead);
                            byteWritten += byteRead;
                        }
                        in.close();
                        out.close();
                        extractUpdate(path2);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Update error! Could not download update data. Check your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Update error! Could not connect to Server. Check your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void extractUpdate(String path) {
        try {
            files = new ArrayList<File>();
            updater.setProgress(25);
            ZipFile zf = new ZipFile(updaterFile);
            ZipFile jf = new ZipFile(file);
            updater.setLabelText("Extracting Updates...");
            Enumeration<? extends ZipEntry> en = jf.entries();
            while (en.hasMoreElements()) {
                extractEntry(jf, en.nextElement(), path);
            }
            updater.setProgress(50);
            en = zf.entries();
            while (en.hasMoreElements()) {
                extractEntry(zf, en.nextElement(), path);
            }
            updater.setProgress(75);
            updater.setLabelText("Implementing Updates...");
            updaterFile.delete();
            file.delete();
            for (File f : files) {
                if (!f.isDirectory()) {
                    compressEntry(f, file.getCanonicalPath(), file.getCanonicalPath().substring(0, file.getCanonicalPath().length() - file.getName().length()));
                }
            }
            updater.setProgress(100);
            if (Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/" + "java -jar " + file.getCanonicalPath() + " -startAfterUpdate " + file.getCanonicalPath().substring(0, (file.getCanonicalPath().length() - 4)) + "Updater.jar") == null) {
                JOptionPane.showMessageDialog(null, "Could not start Updater!\n (java -jar " + updaterFile.getCanonicalPath() + " -update " + file.getCanonicalPath());
            }
            Thread.sleep(500);
            updater.dispose();
            for (File f : files) {
                f.delete();
            }
            System.exit(2);
        } catch (ZipException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not extract files to patch. Check folder permission.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not read files. Maybe the update file is corrupt.\nTry to update again.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void compressEntry(File f, String dest, String appPath) {
        try {
            int buffer = 1024;
            byte data[] = new byte[buffer];
            BufferedInputStream origin = null;
            FileOutputStream destination = new FileOutputStream(dest);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(destination));
            FileInputStream fi = new FileInputStream(f);
            origin = new BufferedInputStream(fi, buffer);
            ZipEntry entry = new ZipEntry(f.getCanonicalPath().substring(appPath.length(), f.getCanonicalPath().length()));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, buffer)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                JOptionPane.showMessageDialog(null, "Update error! Could not compress file " + f.getCanonicalPath(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void extractEntry(ZipFile zf, ZipEntry entry, String destDir) throws IOException {
        byte[] buffer = new byte[1024];
        File file = new File(destDir, entry.getName());
        files.add(file);
        if (entry.isDirectory()) file.mkdirs(); else {
            new File(file.getParent()).mkdirs();
            InputStream is = null;
            OutputStream os = null;
            try {
                is = zf.getInputStream(entry);
                os = new FileOutputStream(file);
                for (int len; (len = is.read(buffer)) != -1; ) {
                    os.write(buffer, 0, len);
                }
            } finally {
                if (os != null) os.close();
                if (is != null) is.close();
            }
        }
    }
}
