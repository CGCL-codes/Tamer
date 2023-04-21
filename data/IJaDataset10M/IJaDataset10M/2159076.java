package eu.cherrytree.paj.editors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.UIManager;

/**
 *
 * @author yezu
 */
public class Settings implements Serializable {

    private String settingsFileName = ".config";

    public void loadSettings() {
        try {
            Settings set = new Settings();
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(settingsFileName));
            set = (Settings) in.readObject();
            in.close();
            this.currentLookAndFeel = set.currentLookAndFeel;
            this.setPathToModelLibrary(set.pathToModelLibrary);
            this.setPathToTextureLibrary(set.pathToTextureLibrary);
        } catch (Exception e) {
            System.err.println("Couldn't find configuration file, using default.");
        }
    }

    public void saveSettings() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(settingsFileName));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum lookAndFeel {

        SYSTEM, DARK_1, DARK_2, LIGHT_1, LIGHT_2
    }

    private String[] lookAndFeels = { "org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel", "org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel", "org.jvnet.substance.skin.SubstanceModerateLookAndFeel", "org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel" };

    public void setLookAndFeel(lookAndFeel laf) {
        currentLookAndFeel = laf;
    }

    public String getLookAndFeel() {
        return getLookAndFeel(currentLookAndFeel);
    }

    public String getLookAndFeel(lookAndFeel laf) {
        if (laf.equals(lookAndFeel.SYSTEM)) return UIManager.getSystemLookAndFeelClassName(); else return lookAndFeels[laf.ordinal() - 1];
    }

    public lookAndFeel getCurrentLookAndFeel() {
        return currentLookAndFeel;
    }

    public String getPathToModelLibrary() {
        return pathToModelLibrary;
    }

    public static String getModelLibraryPath() {
        return pathToModelLibrary_static;
    }

    public static String getTextureLibraryPath() {
        return pathToTextureLibrary_static;
    }

    public void setPathToModelLibrary(String pathToModelLibrary) {
        this.pathToModelLibrary = pathToModelLibrary;
        pathToModelLibrary_static = pathToModelLibrary;
    }

    public String getPathToTextureLibrary() {
        return pathToTextureLibrary;
    }

    public void setPathToTextureLibrary(String pathToTextureLibrary) {
        this.pathToTextureLibrary = pathToTextureLibrary;
        pathToTextureLibrary_static = pathToTextureLibrary;
    }

    private lookAndFeel currentLookAndFeel = lookAndFeel.SYSTEM;

    private String pathToModelLibrary = "./";

    private String pathToTextureLibrary = "./";

    private static String pathToModelLibrary_static = "./";

    private static String pathToTextureLibrary_static = "./";
}
