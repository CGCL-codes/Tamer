package sk.fiit.mitandao.gui.modulepanels.predefinedpanels;

import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

/**
 * The save dialog. It's difference from the it's superclass is, that the
 * confirmation button has the 'save' label
 * 
 * @author Tomas Jelinek
 * 
 */
public class FileSavePanel extends FileChooserPanel {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = getChooser().showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            getPathField().setText(getChooser().getSelectedFile().getAbsolutePath());
        }
    }
}
