package org.pointrel.pointrel20090201.examples;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import org.pointrel.pointrel20090201.Session;
import org.pointrel.pointrel20090201.utilities.AuthorUtilities;
import org.pointrel.pointrel20090201.utilities.FileUtilities;
import org.pointrel.pointrel20090201.utilities.LicenseUtilities;

public class SimpleBrowserApplication extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private Session session;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SimpleBrowserApplication thisClass = new SimpleBrowserApplication();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }

    /**
	 * This is the default constructor
	 */
    public SimpleBrowserApplication() {
        super();
        String archivePath = FileUtilities.checkArchivePathAndExitIfNoValidSelection(FileUtilities.exampleArchivePath, this);
        session = new Session(AuthorUtilities.newDefaultAuthorList(), LicenseUtilities.newDefaultLicenseList());
        session.loadArchiveFromFile(archivePath);
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(610, 385);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
        }
        return jContentPane;
    }
}
