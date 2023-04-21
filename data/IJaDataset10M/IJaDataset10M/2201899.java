package test.xito.dazzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.xito.dazzle.utilities.MacApplicationUtilities;
import org.xito.dialog.DialogManager;

/**
 *
 * @author deane
 */
public class MacApplicationHelperTest {

    public static void main(String[] args) {
        if (!MacApplicationUtilities.isRunningOnMac()) {
            DialogManager.showMessage(null, "Not a Mac", "Application not running on a Mac");
            System.exit(0);
        }
        JFrame frame = new JFrame("Mac App Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MacApplicationUtilities.installAboutActionHandler(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("About Pressed");
            }
        });
        MacApplicationUtilities.installPreferenceActionHandler(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Preferences Pressed");
            }
        });
        MacApplicationUtilities.installQuitActionHandler(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Quit Pressed");
                System.exit(0);
            }
        });
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
