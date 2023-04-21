package org.scrinch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.scrinch.gui.MainFrame;
import org.scrinch.gui.WindowUtilities;
import org.scrinch.model.ScrinchEnvToolkit;
import org.scrinch.model.ScrinchException;
import org.scrinch.model.UndoManager;

public class Main {

    private static final String PROPERTIES_FILE_NAME = "scrinch.properties";

    public static final String LAST_USED_FILE = "last.file";

    public static final String LAST_EXPORT_DIR = "last.export.dir";

    private static final Properties userProperties = new Properties();

    public static String VERSION = "'UNKNOWN'";

    private static JarFile getCurrentJarFile() throws IOException {
        ClassLoader loader = Main.class.getClassLoader();
        URL mainClassURL = loader.getResource("org/scrinch/Main.class");
        String jarPath = mainClassURL.getPath();
        jarPath = jarPath.substring(0, jarPath.indexOf("!"));
        URL jarUrl = new URL(jarPath);
        File jarFile = new File(jarUrl.getPath());
        return new JarFile(jarFile);
    }

    private static String extractVersion() {
        String extractedVersion = null;
        String section = "org/scrinch/";
        String key = "Implementation-Version";
        try {
            JarFile jar = getCurrentJarFile();
            Manifest manifest = jar.getManifest();
            Attributes attributes = manifest.getAttributes(section);
            extractedVersion = attributes.getValue(key);
        } catch (Exception e) {
            Package versionInfo = Package.getPackage(section);
            if (versionInfo != null) {
                extractedVersion = versionInfo.getImplementationVersion();
            }
        }
        if (extractedVersion == null) {
            InputStream inProps = Main.class.getResourceAsStream("scrinch.properties");
            if (inProps != null) {
                try {
                    Properties props = new Properties();
                    props.load(inProps);
                    extractedVersion = props.getProperty("scrinch.version");
                } catch (Exception ex) {
                }
            }
        }
        if (extractedVersion == null) {
            extractedVersion = "(development version I guess)";
        }
        return extractedVersion;
    }

    private static void loadProperties() {
        File propsFile = new File(System.getProperty("user.home"), PROPERTIES_FILE_NAME);
        if (propsFile.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(propsFile);
                userProperties.load(in);
                in.close();
            } catch (Throwable e) {
                ScrinchEnvToolkit.logger.log(Level.SEVERE, "loadProperties failed", e);
            }
        }
    }

    private static void saveProperties() {
        File propsFile = new File(System.getProperty("user.home"), PROPERTIES_FILE_NAME);
        boolean canSave = true;
        if (propsFile.exists()) {
            canSave = propsFile.delete();
        }
        if (canSave) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(propsFile);
                userProperties.store(out, "Scrinch user properties file");
                out.close();
            } catch (Throwable e) {
                ScrinchEnvToolkit.logger.log(Level.SEVERE, "saveProperties failed", e);
            }
        } else {
            ScrinchEnvToolkit.logger.log(Level.SEVERE, "Could not delete existing properties file");
        }
    }

    public static void setUserProperty(String name, String value) {
        userProperties.setProperty(name, value);
        saveProperties();
    }

    public static String getUserProperty(String name) {
        return userProperties.getProperty(name);
    }

    private static void openFile(final MainFrame mainFrame, String fileName) throws ScrinchException {
        File f = new File(fileName);
        if (f.exists()) {
            mainFrame.open(f);
        }
    }

    private static void checkArgsAndOpenFile(final MainFrame mainFrame, final String[] args) {
        try {
            if (args.length > 1) {
                if (args[0].equals("-open")) {
                    openFile(mainFrame, args[1]);
                }
            } else if (args.length == 1) {
                openFile(mainFrame, args[0]);
            } else {
                String fileName = userProperties.getProperty(LAST_USED_FILE);
                if (fileName != null && new File(fileName).exists()) {
                    int result = JOptionPane.showConfirmDialog(mainFrame, "Do you want to open last opened file : \n" + fileName, "Scrinch : open last file?", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        openFile(mainFrame, fileName);
                    }
                }
            }
        } catch (Exception e) {
            ScrinchEnvToolkit.logger.log(Level.SEVERE, "checkArgsAndOpenFile failed", e);
        }
    }

    public static void main(String[] args) {
        try {
            Main.VERSION = Main.extractVersion();
            Locale.setDefault(Locale.ENGLISH);
            int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1000);
            if (maxMem < 110000) {
                JOptionPane.showMessageDialog(null, "Insufficient memory allocated, use parameter -Xmx128m to set at least 128 Mb max memory to run the Scrinch application.", "Not enough memory", JOptionPane.ERROR_MESSAGE);
                ScrinchEnvToolkit.exit(-1);
            } else {
                ScrinchEnvToolkit.initFileLogger();
                ScrinchEnvToolkit.logger.info("Starting SCRINCH, using max memory : " + maxMem + "Kb");
                loadProperties();
                ScrinchEnvToolkit toolkit = new ScrinchEnvToolkit();
                final MainFrame mainFrame = new MainFrame(toolkit);
                mainFrame.pack();
                WindowUtilities.centerFrame(mainFrame, null);
                checkArgsAndOpenFile(mainFrame, args);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        mainFrame.setVisible(true);
                    }
                });
                UndoManager.getInstance().start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            ScrinchEnvToolkit.exit(-1);
        }
    }
}
