package corina.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import corina.core.App;

public class Alert {

    public static void error(String title, String text) {
        JOptionPane.showMessageDialog(null, text, maybeTitle(title), JOptionPane.ERROR_MESSAGE, treeIcon);
    }

    public static void message(String title, String text) {
        JOptionPane.showMessageDialog(null, text, maybeTitle(title), JOptionPane.INFORMATION_MESSAGE, treeIcon);
    }

    public static void errorLoading(String filename, IOException ioe) {
        if (ioe instanceof FileNotFoundException) {
            Alert.error("File not found", "The file \"" + filename + "\" could not be\n" + "loaded, because it doesn't exist.  It may have\n" + "been moved or deleted");
        }
    }

    private static String maybeTitle(String title) {
        return (App.platform.isMac() ? "" : title);
    }

    private static final Icon treeIcon = Builder.getIcon("Tree-icon.png");
}
