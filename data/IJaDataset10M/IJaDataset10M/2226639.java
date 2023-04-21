package de.renier.jkeepass.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import de.renier.jkeepass.JKeePass;
import de.renier.jkeepass.Messages;
import de.renier.jkeepass.util.Utils;

/**
 * ExportPlainAction
 *
 * @author <a href="mailto:software@renier.de">Renier Roth</a>
 */
public class ExportPlainAction extends AbstractAction {

    private static final long serialVersionUID = -2019995764108891546L;

    public ExportPlainAction() {
        super(Messages.getString("ExportPlain.0"), new ImageIcon(OpenAction.class.getResource("/org/javalobby/icons/20x20/SaveAs.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode rootNode = JKeePass.application.getElementTreePanel().getRootNode();
        if (rootNode != null) {
            File saveFile = null;
            try {
                final JFileChooser fc = new JFileChooser();
                int ret = fc.showSaveDialog(JKeePass.application);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    saveFile = fc.getSelectedFile();
                    if (saveFile.exists()) {
                        int result = JOptionPane.showConfirmDialog(JKeePass.application, Messages.getString("ExportPlain.2") + saveFile.getPath() + Messages.getString("ExportPlain.3"), Messages.getString("ExportPlain.4"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    FileOutputStream outFile = new FileOutputStream(saveFile);
                    Utils.outputNodeUnencrypted(outFile, JKeePass.application.getElementTreePanel().getRootNode());
                    outFile.flush();
                    outFile.close();
                }
            } catch (Exception ioe) {
                JOptionPane.showConfirmDialog(JKeePass.application, Messages.getString("ExportPlain.5") + saveFile != null ? saveFile.getPath() : "" + "\nError message: " + ioe.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
        }
    }
}
