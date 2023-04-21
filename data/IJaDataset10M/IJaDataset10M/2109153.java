package net.matmas.pneditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.matmas.pneditor.PNEditor;
import net.matmas.pneditor.filechooser.EpsFileType;
import net.matmas.util.GraphicsTools;
import net.matmas.pneditor.filechooser.FileChooserDialog;
import net.matmas.pneditor.filechooser.FileType;
import net.matmas.pneditor.filechooser.FileTypeException;
import net.matmas.pneditor.filechooser.PetriNetFileType;
import net.matmas.pneditor.filechooser.PngFileType;

/**
 *
 * @author matmas
 */
public class SaveFileAsAction extends Action {

    public SaveFileAsAction() {
        String name = "Save as...";
        putValue(NAME, name);
        putValue(SMALL_ICON, GraphicsTools.getIcon("pneditor/SaveAs16.gif"));
        putValue(SHORT_DESCRIPTION, name);
    }

    public void actionPerformed(ActionEvent e) {
        List<FileType> fileTypes = new ArrayList<FileType>();
        fileTypes.add(new PetriNetFileType());
        fileTypes.add(new PngFileType());
        fileTypes.add(new EpsFileType());
        FileChooserDialog chooser = new FileChooserDialog();
        if (PNEditor.getInstance().getCurrentFile() != null) {
            chooser.setSelectedFile(PNEditor.getInstance().getCurrentFile());
        }
        for (FileType fileType : fileTypes) {
            chooser.addChoosableFileFilter(fileType);
        }
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(PNEditor.getInstance().getCurrentDirectory());
        chooser.setDialogTitle("Save as...");
        if (chooser.showSaveDialog(PNEditor.getInstance().getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            FileType chosenFileType = (FileType) chooser.getFileFilter();
            if (!file.exists() || JOptionPane.showOptionDialog(PNEditor.getInstance().getMainFrame(), "Selected file already exists. Overwrite?", "Save as " + file.getName(), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { "Overwrite", "Cancel" }, "Cancel") == JOptionPane.YES_OPTION) {
                try {
                    chosenFileType.save(PNEditor.getInstance().getDocument(), file);
                } catch (FileTypeException ex) {
                    JOptionPane.showMessageDialog(PNEditor.getInstance().getMainFrame(), ex.getMessage());
                }
            }
            PNEditor.getInstance().setCurrentFile(file);
            PNEditor.getInstance().getUndoManager().setDocumentModified(false);
        }
        PNEditor.getInstance().setCurrentDirectory(chooser.getCurrentDirectory());
    }
}
