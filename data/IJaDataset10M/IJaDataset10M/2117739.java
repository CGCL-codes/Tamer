package net.sf.mzmine.modules.visualization.tic;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Helper utilities of exporting chromatogram.
 *
 * @author $Author: plusik $
 * @version $Revision: 2728 $
 */
public class ExportChromatogramHelper {

    private static final String CSV_EXTENSION = "csv";

    private static JFileChooser exportChooser = null;

    /**
     * Utility class - no public constructor.
     */
    private ExportChromatogramHelper() {
    }

    /**
     * Prompt the user for the file to export to.
     *
     * @param parent   parent window for dialogs.
     * @param fileName default name of selected file.
     * @return the selected file (or null if no choice is made).
     */
    public static File getExportFile(final Component parent, final String fileName) {
        if (exportChooser == null) {
            exportChooser = new JFileChooser();
            exportChooser.setApproveButtonMnemonic('E');
            exportChooser.setApproveButtonToolTipText("Export chromatogram to the selected file");
            exportChooser.setDialogTitle("Export Chromatogram");
            exportChooser.addChoosableFileFilter(new FileNameExtensionFilter("Comma-separated values files", CSV_EXTENSION));
        }
        exportChooser.setSelectedFile(applyFileNameExtension(new File(fileName)));
        File file = null;
        for (boolean done = false; !done; ) {
            done = true;
            if (exportChooser.showDialog(parent, "Export") == JFileChooser.APPROVE_OPTION) {
                file = applyFileNameExtension(exportChooser.getSelectedFile());
                if (file.exists() && JOptionPane.showConfirmDialog(parent, "Do you want to overwrite the file?", "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                    done = false;
                }
            }
        }
        return file;
    }

    /**
     * Returns the file with the correct extension.
     *
     * @param file the file.
     * @return if the file is not null and doesn't end with the correct extension then a new file with the extension
     *         appended to the name is returned, otherwise the file is returned unchanged.
     */
    private static File applyFileNameExtension(File file) {
        if (file != null) {
            final String name = file.getName();
            final String extension = '.' + CSV_EXTENSION;
            if (!name.toLowerCase().endsWith(extension)) {
                file = new File(file.getParent(), name + extension);
            }
        }
        return file;
    }
}
