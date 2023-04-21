package business;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import presentation.WirelessRedstoneInstaller;
import presentation.WirelessRedstoneInstaller.InstallerPanel.InstallerPanelModList;
import data.JarHandler;
import data.Mod;
import data.RepositoryHandler;
import data.VersionHandler;

public class Installer implements Runnable {

    private JarHandler jar;

    private JLabel label;

    private Map<String, Mod> mods;

    public Installer(File jarfile) {
        hookJar(jarfile);
    }

    public void hookJar(File file) {
        jar = new JarHandler(file);
    }

    public void install(JLabel label, Map<String, Mod> mods, String minecraftVer) {
        this.label = label;
        this.mods = mods;
        if (!VersionHandler.checkVersion(minecraftVer, jar.verFile)) {
            int select = JOptionPane.showConfirmDialog(null, "Minecraft version does not match mod version. Are you sure you want to continue?", "WARNING", JOptionPane.YES_NO_OPTION);
            if (select == 1) return;
        }
        if (!WirelessRedstoneInstaller.repos.testConnection(label)) return;
        Thread thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        label.setForeground(Color.BLACK);
        Map<String, File> files = new TreeMap<String, File>();
        label.setText("Installation Starting...");
        try {
            files.putAll(jar.extractJarArchive(label));
        } catch (NullPointerException e) {
            label.setForeground(Color.RED);
            label.setText("Installation Failed.");
            return;
        }
        for (String key : mods.keySet()) {
            if (mods.get(key).checkbox.isSelected()) {
                List<String> files2 = WirelessRedstoneInstaller.repos.getModFileList(mods.get(key));
                for (String file : files2) {
                    label.setText("Installing: " + mods.get(key).title + ": " + file);
                    byte[] data = WirelessRedstoneInstaller.repos.getFile(WirelessRedstoneInstaller.repos.url + "/" + key + "/" + file);
                    File fileOut = jar.addFileToJar(file, data);
                    if (fileOut == null) {
                        label.setForeground(Color.RED);
                        label.setText("Installation Failed.");
                        return;
                    }
                    files.put(fileOut.getAbsolutePath().substring(jar.tmpDir.getAbsolutePath().length() + 1).replaceAll("\\\\", "/"), fileOut);
                }
            }
        }
        File[] farr = new File[files.size()];
        int i = 0;
        for (String key : files.keySet()) {
            farr[i] = files.get(key);
            i++;
        }
        if (jar.createJarArchive(farr, label)) label.setText("Installation Done."); else {
            label.setForeground(Color.RED);
            label.setText("Installation Failed.");
            return;
        }
    }
}
