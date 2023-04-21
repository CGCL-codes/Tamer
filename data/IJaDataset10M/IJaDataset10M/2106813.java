package org.jpedal.examples.simpleviewer.gui;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import javax.swing.JComponent;
import org.jpedal.examples.simpleviewer.Commands;
import org.jpedal.examples.simpleviewer.Values;
import org.jpedal.examples.simpleviewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.exception.PdfException;

public class SingleViewTransferHandler extends BaseTransferHandler {

    public SingleViewTransferHandler(Values commonValues, GUIThumbnailPanel thumbnails, SwingGUI currentGUI, Commands currentCommands) {
        super(commonValues, thumbnails, currentGUI, currentCommands);
    }

    public boolean importData(JComponent src, Transferable transferable) {
        try {
            Object dragImport = getImport(transferable);
            if (dragImport instanceof String) {
                String url = (String) dragImport;
                if (url.indexOf("file:/") != url.lastIndexOf("file:/")) currentGUI.showMessageDialog("You may only import 1 file at a time"); else openFile(url);
            } else if (dragImport instanceof List) {
                List files = (List) dragImport;
                if (files.size() == 1) {
                    File file = (File) files.get(0);
                    openFile(file.getAbsolutePath());
                } else {
                    currentGUI.showMessageDialog("You may only import 1 file at a time");
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected void openFile(String file) throws PdfException {
        String testFile = file.toLowerCase();
        boolean isValid = ((testFile.endsWith(".pdf")) || (testFile.endsWith(".fdf")) || (testFile.endsWith(".tif")) || (testFile.endsWith(".tiff")) || (testFile.endsWith(".png")) || (testFile.endsWith(".jpg")) || (testFile.endsWith(".jpeg")));
        if (isValid) {
            currentCommands.openTransferedFile(file);
        } else {
            currentGUI.showMessageDialog("You may only import a valid PDF or image");
        }
    }
}
