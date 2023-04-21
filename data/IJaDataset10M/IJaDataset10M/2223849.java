package org.stamppagetor.ui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.stamppagetor.StampDocument;
import org.stamppagetor.UiPlatform;

/**
 * For exporting the album as PDF file 
 */
public class UiBundleExportPfd extends UiBundle {

    private static final FileNameExtensionFilter pdfFileFilter = new FileNameExtensionFilter("PDF documents", "pdf");

    public UiBundleExportPfd() {
        super("EXPORT_PDF", RuleStatic.TRUE);
        addUiItem(new UiItemMenu(1500, "Album", "Export As PDF..."));
    }

    @Override
    public boolean doAction(UiPlatform platform) {
        try {
            final StampDocument doc = platform.getDoc();
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(pdfFileFilter);
            final int returnVal = fc.showSaveDialog(platform.getMainFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File file = fc.getSelectedFile();
                doc.printPDF(file);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Can not export PDF!", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
