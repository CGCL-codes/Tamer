package edu.princeton.wordnet.wnscope.wizard;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFileChooser;

/**
 * File dialog utilities
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class FileDialogs {

    /**
	 * Extended file filter
	 * 
	 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
	 */
    static class XFileFilter extends javax.swing.filechooser.FileFilter {

        /**
		 * Extensions
		 */
        private final String[] theExtensions;

        /**
		 * Description
		 */
        private final String theDescription;

        /**
		 * Constructor
		 * 
		 * @param theseExtensions
		 *            extension
		 * @param thisDescription
		 *            description
		 */
        public XFileFilter(final String[] theseExtensions, final String thisDescription) {
            this.theExtensions = theseExtensions;
            this.theDescription = thisDescription;
        }

        @Override
        public boolean accept(final File thisFile) {
            for (final String thisExtension : this.theExtensions) if (thisFile.getName().toLowerCase().endsWith(thisExtension)) return true;
            return thisFile.isDirectory();
        }

        @Override
        public String getDescription() {
            return this.theDescription;
        }
    }

    /**
	 * Get directory
	 * 
	 * @param thisCurrentDirectory
	 *            current directory
	 * @return string for directory path
	 */
    public static String getFolder(final String thisCurrentDirectory) {
        final JFileChooser thisChooser = FileDialogs.makeFolderChooser();
        FileDialogs.setCurrentDirectory(thisChooser, thisCurrentDirectory);
        if (JFileChooser.APPROVE_OPTION == thisChooser.showOpenDialog(null)) return thisChooser.getSelectedFile().getPath();
        return null;
    }

    /**
	 * Make folder chooser
	 * 
	 * @return folder chooser
	 */
    private static JFileChooser makeFolderChooser() {
        final JFileChooser thisChooser = new JFileChooser();
        thisChooser.setDialogTitle(Messages.getString("FileDialogs.1"));
        thisChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        thisChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(final File thisFile) {
                return thisFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return Messages.getString("FileDialogs.2");
            }
        });
        return thisChooser;
    }

    /**
	 * @param thisChooser
	 *            file chooser
	 * @param thisCurrentDirectory
	 *            directory to set as current
	 */
    private static void setCurrentDirectory(final JFileChooser thisChooser, final String thisCurrentDirectory) {
        if (thisCurrentDirectory == null || thisCurrentDirectory.isEmpty()) return;
        File thisDirectory = null;
        if (thisCurrentDirectory.startsWith("file:")) {
            try {
                thisDirectory = new File(new URI(thisCurrentDirectory));
            } catch (final URISyntaxException thisException) {
            }
        } else {
            thisDirectory = new File(thisCurrentDirectory);
        }
        thisChooser.setCurrentDirectory(thisDirectory);
    }
}
