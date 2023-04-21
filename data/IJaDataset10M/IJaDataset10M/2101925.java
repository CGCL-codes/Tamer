package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import edu.ucdavis.genomics.metabolomics.binbase.gui.swt.plugin.BellerophonPlugin;

/**
 * imports a new library
 * 
 * @author wohlgemuth
 * 
 */
public class ImportLibraryAction extends Action {

    private ProgressMonitorDialog monitor = null;

    private Logger logger = Logger.getLogger(getClass());

    private FileDialog dialog = null;

    public ImportLibraryAction(Viewer viewer) {
        this.setToolTipText("import a new library");
        this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(BellerophonPlugin.NAME, "icons/new.GIF"));
        this.dialog = new FileDialog(viewer.getControl().getShell(), SWT.OPEN);
        this.dialog.setFilterNames(new String[] { "MSP files (*.msp)" });
        this.dialog.setFilterExtensions(new String[] { "*.msp", "*.MSP" });
        this.monitor = new ProgressMonitorDialog(viewer.getControl().getShell());
        this.monitor.setBlockOnOpen(false);
        this.monitor.setCancelable(false);
    }

    public void run() {
        logger.info("start action");
        final String file = this.dialog.open();
        logger.info("importing file: " + file);
        if (file == null) {
            logger.info("no file selected");
            return;
        } else {
            GenericActionMethods.importLibraryFromMsp(file);
        }
    }
}
