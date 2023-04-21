package prealpha.util;

import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

public class FileFinder {

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        Preferences preferences = Preferences.userNodeForPackage(FileFinder.class);
        File directory = new File(preferences.get("StartDirectory", "."));
        chooser.setCurrentDirectory(directory);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            preferences.put("StartDirectory", file.getAbsolutePath());
            System.out.println(file.getAbsolutePath());
        }
    }
}
