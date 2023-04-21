package net.sf.ovanttasks.ovnative.demos;

import java.io.IOException;
import javax.swing.JOptionPane;
import net.sf.ovanttasks.ovnative.win32.Registry;

/**
 * Demonstrates the usage of {@link Registry}.
 * 
 * @author lars.gersmann@roxes.com
 * @author arnep@users.sf.net
 */
public class RegistryInstallUninstall {

    public RegistryInstallUninstall(String[] args) throws IOException {
        if (args.length == 0) {
            Object answer = JOptionPane.showInputDialog(null, "Install or uninstall?", "Title", JOptionPane.QUESTION_MESSAGE, null, new Object[] { "install", "uninstall" }, "install");
            if ("install".equals(answer)) {
                install();
            } else if ("uninstall".equals(answer)) {
                uninstall();
            }
        }
        if (args.length != 1) {
            System.out.println(getClass().getName() + "(-install|-uninstall)");
            System.out.println("show how to add/remove entries in windows software setup");
            return;
        }
        if (args[0].equals("-install")) {
            install();
        } else if (args[0].equals("-uninstall")) {
            uninstall();
        } else {
            System.out.println(getClass().getName() + "(-install|-uninstall)");
        }
    }

    void install() {
        Registry reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\ROXES Technologies InstallUninstall Example");
        if (!reg.canOpenWith(Registry.KEY_ALL_ACCESS)) {
            reg.create();
        } else {
            return;
        }
        reg.setValue("DisplayName", "ROXES Technologies InstallUninstall Example");
        reg.setValue("UninstallString", "javaw -cp \"" + System.getProperty("java.class.path") + System.getProperty("java.class.path") + System.getProperty("path.separator") + System.getProperty("user.dir") + "\" InstallUninstall -uninstall");
        reg.setValue("DisplayIcon", System.getProperty("user.dir") + System.getProperty("file.separator") + "roxes.ico");
        reg.setValue("Comments", "ROXES Technologes provides Next Generation Web Technologies.");
        reg.setValue("DisplayVersion", "1.0");
        reg.setValue("HelpLink", "http://www.roxes.com/produkte/win32forjava");
        reg.setValue("Publisher", "ROXES Technologies");
        reg.setValue("URLInfoAbout", "http://www.roxes.com");
        reg.setValue("URLUpdateInfo", "http://www.roxes.com/produkte/win32forjava");
        msgBox("Installation successful");
    }

    void uninstall() {
        Registry reg = new Registry(Registry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\ROXES Technologies InstallUninstall Example");
        if (reg.canOpenWith(Registry.KEY_ALL_ACCESS)) {
            reg.delete();
            msgBox("Deinstallation successful");
        } else {
            msgBox("Already uninstalled");
            return;
        }
    }

    static void msgBox(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void main(String[] args) throws IOException {
        new RegistryInstallUninstall(args);
        System.exit(0);
    }
}
