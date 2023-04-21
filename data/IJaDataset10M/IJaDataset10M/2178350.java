package de.sambalmueslie.geocache_planer.gui.dialogs;

import java.awt.Frame;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import de.sambalmueslie.geocache_planer.common.logger.GCPlanerLogger;

/**
 * Displays an open file dialog only for gpx files.
 * 
 * @author Sambalmueslie
 * 
 * @date 21.05.2009
 * 
 */
public class OpenFileDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1354713550836722028L;

    /**
	 * constructor.
	 * 
	 * @param frame
	 *            the main window
	 */
    public OpenFileDialog(final Frame frame) {
        mainframe = frame;
    }

    /**
	 * show the dialog and get the file.
	 * 
	 * @param filter
	 *            the file filter
	 * @return the file
	 */
    public File show(final FileFilter filter) {
        final JFileChooser fc = new JFileChooser();
        if (filter != null) {
            fc.addChoosableFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
        }
        final int returnVal = fc.showOpenDialog(mainframe);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            logger.info("Opening: " + file.getName() + ".");
            return file;
        } else {
            logger.info("Open command cancelled by user.");
        }
        return null;
    }

    /** the logger. */
    private final GCPlanerLogger logger = GCPlanerLogger.getInstance();

    /** the main window. */
    private Frame mainframe = null;
}
