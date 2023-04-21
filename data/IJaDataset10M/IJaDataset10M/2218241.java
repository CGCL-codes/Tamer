package org.jdna.minecraft.tools;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class Application {

    public static final Application instance = new Application();

    public static Application getInstance() {
        return instance;
    }

    private Preferences preferences;

    private File homeDir;

    private File preferencesDir;

    private BackupManager backupManager;

    public Application() {
        homeDir = new File(System.getProperty("user.home"));
        preferencesDir = new File(homeDir, ".StucklessModManager");
        if (!preferencesDir.exists()) {
            preferencesDir.mkdirs();
        }
        File log4j = new File(preferencesDir, "log4j.properties");
        if (!log4j.exists()) {
            Properties props = new Properties();
            try {
                props.load(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
                props.setProperty("log4j.appender.STUCKLESS.File", new File(preferencesDir, "system.log").getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(log4j);
                props.store(fos, "Configured Log4j");
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (log4j.exists()) {
            PropertyConfigurator.configure(log4j.getAbsolutePath());
        } else {
            BasicConfigurator.configure();
        }
        preferences = new Preferences(preferencesDir);
        backupManager = new BackupManager(preferences);
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void error(String title, String msg) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public File chooseMinecraftDir() {
        JFileChooser fc = new JFileChooser(getPreferences().getMinecraftModsDir());
        fc.setDialogTitle("Select Mincraft Directory");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file;
    }

    public BackupManager getBackupManager() {
        return backupManager;
    }

    public void openUrl(String url) {
        BrowserLauncher.openURL(url);
    }

    public static Image image(String ico) {
        try {
            return ImageIO.read(Application.class.getClassLoader().getResource("res/icons/" + ico + ".png"));
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static Icon icon(String ico) {
        try {
            return new ImageIcon(Application.class.getClassLoader().getResource("res/icons/" + ico + ".png"));
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static Image imageRes(String res) {
        try {
            return ImageIO.read(Application.class.getClassLoader().getResource(res));
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
