package ProgramLaunchers;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Window;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import javax.swing.JOptionPane;

/**
 *
 * @author Stephen
 *
 * This class starts and runs the game.
 */
public class ProgramLauncher {

    public static Image getGameIcon(Window frame) {
        Image img = null;
        try {
            img = frame.getToolkit().getImage(ProgramLauncher.class.getResource("vg_icon_32.png"));
        } catch (Exception ex) {
            System.out.println("Icon not loaded");
        }
        MediaTracker tracker = new MediaTracker(frame);
        tracker.addImage(img, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return img;
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toLowerCase().indexOf("windows") != -1;
    }

    public static boolean isMac() {
        return System.getProperties().getProperty("os.name").toLowerCase().indexOf("mac") != -1;
    }

    /**
     * Get version number of Java VM.
     *
     * @author NeKromancer
     */
    private static void checkJavaVersion() {
        String version = System.getProperties().getProperty("java.version");
        boolean v12 = version.indexOf("1.2") != -1;
        boolean v13 = version.indexOf("1.3") != -1;
        boolean v14 = version.indexOf("1.4") != -1;
        if (false) {
            JOptionPane.showMessageDialog(null, "Video Guardian PC requires a java runtime greater than or equal to 5.0.\nPlease download a newer version of java from http://java.sun.com/", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        checkJavaVersion();
        ProgramLauncher2.main(args);
    }

    public static File getCoverWindowImagesFolder() {
        File f = new File(getRootFolder(), "images\\Cover Window Images\\");
        if (!f.exists()) f.mkdirs();
        return f;
    }

    /**
     * Get the root folder for the application
     */
    public static File getRootFolder() {
        URL url = ProgramLauncher.class.getResource("ProgramLauncher.class");
        int moveUpCount = ProgramLauncher.class.getName().split("\\.").length + 1;
        String fileName = url.getFile();
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File f = new File(fileName);
        for (int i = 0; i < moveUpCount; i++) f = f.getParentFile();
        if (!f.exists()) {
            System.err.println("Could not find root folder, does not exist: " + f);
            return new File(System.getProperties().getProperty("user.dir"));
        }
        return f;
    }
}
