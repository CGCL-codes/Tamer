package net.sf.jeda.gedasymbols;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.NbBundle;

/**
 *
 * @author eduardo-costa
 */
public class ImportAction extends AbstractAction {

    private static final String IMPORT_DEST = "JEDAPalette/Nodes/gEDA";

    private static final JFileChooser CHOOSER;

    static {
        CHOOSER = new JFileChooser();
        FileUtil.preventFileChooserSymlinkTraversal(CHOOSER, null);
        CHOOSER.setDialogTitle(NbBundle.getMessage(ImportAction.class, "LBL_SelectSymbols"));
        CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
        CHOOSER.setMultiSelectionEnabled(true);
        CHOOSER.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (!f.isFile()) {
                    return false;
                }
                return (f.getName().endsWith(".sym"));
            }

            @Override
            public String getDescription() {
                return NbBundle.getMessage(ImportAction.class, "LBL_GEDASymbols");
            }
        });
    }

    public ImportAction() {
        super(NbBundle.getMessage(ImportAction.class, "LBL_ImportFromGEDA"));
    }

    public void actionPerformed(ActionEvent e) {
        JComponent src = (JComponent) e.getSource();
        if (JFileChooser.APPROVE_OPTION == CHOOSER.showOpenDialog(src)) {
            File[] syms = CHOOSER.getSelectedFiles();
            FileObject dst = Repository.getDefault().getDefaultFileSystem().findResource(IMPORT_DEST);
            try {
                for (File sym : syms) {
                    FileObject symfo = FileUtil.toFileObject(sym);
                    DataObject.find(symfo);
                    FileUtil.copyFile(symfo, dst, symfo.getName());
                }
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
            }
        }
    }
}
