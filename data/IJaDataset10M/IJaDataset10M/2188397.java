package net.sf.fir4j.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

class Profile {

    private String profile;

    public Profile() {
        Settings p = new Settings();
        try {
            p.loadFrom(new FileInputStream(new File("settings/.profile")));
        } catch (Exception e) {
        }
        profile = p.get("profile", ".default");
        if (!getFile().exists()) {
            profile = ".default";
        }
    }

    public File getFile() {
        String filename = profile;
        if (filename.charAt(0) != '.') filename += ".properties";
        return new File("settings/" + filename);
    }

    public void set(String name) {
        profile = name;
        Settings p = new Settings();
        p.set("profile", profile);
        try {
            p.storeTo(new FileOutputStream(new File("settings/.profile")));
        } catch (Exception e) {
        }
    }

    public String get() {
        return profile;
    }

    public void getFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setCurrentDirectory(new File("settings"));
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                try {
                    String name = f.getName().toLowerCase();
                    return (name.endsWith(".properties"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "profile files";
            }
        });
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        }
    }
}
