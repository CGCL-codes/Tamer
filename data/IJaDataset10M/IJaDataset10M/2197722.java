package ch.tarnet.library.swing;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * Ajoute une confirmation lorsque l'utilisateur choisit un fichier existant comme selection
 * lors d'un enregistrement de fichier.
 * 
 * source : http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4510535
 * 
 * @author tarrask
 *
 */
public class TSaferFileChooser extends JFileChooser {

    @Override
    public void approveSelection() {
        File file = getSelectedFile();
        if (getDialogType() == SAVE_DIALOG && file != null && file.exists()) {
            int answer = showSaveDisplayQuestion(file);
            if (answer == JOptionPane.NO_OPTION) {
                return;
            }
        }
        super.approveSelection();
    }

    private int showSaveDisplayQuestion(File file) {
        ResourceBundle bundle = ResourceBundle.getBundle("ch/tarnet/library/swing/resources/TSaferFileChooser");
        String message = String.format(bundle.getString("confirmation.message"), file.getName());
        String title = bundle.getString("confirmation.title");
        return JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    }
}
