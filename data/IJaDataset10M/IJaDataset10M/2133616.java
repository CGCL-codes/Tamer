package corina.platform;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.UIManager;
import corina.core.AbstractSubsystem;
import corina.gui.Bug;
import corina.logging.CorinaLog;

/**
 * Platform subsystem that takes care of platform-specific things.
 * @author Aaron Hamid
 */
public class Platform extends AbstractSubsystem {

    private static final CorinaLog log = new CorinaLog(Platform.class);

    private boolean isMac;

    private boolean isWindows;

    private boolean isUnix;

    public String getName() {
        return "Platform";
    }

    public void init() {
        super.init();
        isMac = System.getProperty("mrj.version") != null;
        String osname = System.getProperty("os.name");
        isWindows = osname != null && osname.indexOf("Windows") != -1;
        isUnix = !isMac && !isWindows;
        if (isMac) {
            String version = System.getProperty("java.version");
            if (version != null && version.startsWith("1.4")) System.setProperty("apple.laf.useScreenMenuBar", "true"); else System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");
            System.setProperty("com.apple.macos.use-file-dialog-packages", "false");
            UIManager.put("JFileChooser.packageIsTraversable", "never");
        }
        String slafclassname = UIManager.getSystemLookAndFeelClassName();
        if (slafclassname != null) try {
            UIManager.setLookAndFeel(slafclassname);
        } catch (Exception e) {
            log.error("Error setting system look and feel class", e);
        }
        Netware.workaround();
        Macintosh.configureMenus();
        setInitialized(true);
    }

    public void destroy() {
        super.destroy();
        setInitialized(false);
    }

    public boolean isMac() {
        return isMac;
    }

    public boolean isWindows() {
        return isWindows;
    }

    public boolean isUnix() {
        return isUnix;
    }

    /**
   * Open a folder in the system file browser
   */
    public void open(String folder) {
        String[] command;
        if (isWindows) {
            boolean isDir = new File(folder).isDirectory();
            command = new String[] { "explorer", (isDir ? folder : "/select," + folder) };
        } else if (isMac) {
            command = new String[] { "open", "-a", "/System/Library/CoreServices/Finder.app/", folder };
        } else {
            new Bug(new IllegalArgumentException("Platform.open() not implemented on unix yet!"));
            return;
        }
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException ioe) {
            new Bug(ioe);
        }
    }

    public String getTrash() {
        if (isWindows) return "C:\\recycled\\";
        if (isMac) return System.getProperty("user.home") + "/.Trash/";
        return null;
    }

    public void setModified(JFrame window, boolean mod) {
        if (isMac) window.getRootPane().putClientProperty("windowModified", mod ? Boolean.TRUE : Boolean.TRUE);
    }

    public String getCopyModifier() {
        if (isMac) return "alt"; else return "control";
    }
}
