package org.jbudget.test;

import java.io.File;
import javax.swing.SwingUtilities;
import org.jbudget.gui.DirectoryLocationDialog;

/**
 *
 * @author petrov
 */
public class TestDirectoryLocationDialog {

    /** Creates a new instance of TestTransactionPanel */
    public TestDirectoryLocationDialog() {
    }

    public void showPanel() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                File dir = DirectoryLocationDialog.showDialog(null);
                System.out.println("Selected directory: " + dir.getAbsolutePath());
            }
        });
    }
}
