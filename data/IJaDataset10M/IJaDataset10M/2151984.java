package org.charvolant.tmsnet.ui;

import org.charvolant.tmsnet.i18n.TMSUIBundle;
import org.charvolant.tmsnet.model.FileEntry;
import org.charvolant.tmsnet.util.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * A panel for renaming files.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class FilenamePanel extends InformationPanel<FilenameModel> {

    /**
   * Construct a rename panel.
   *
   * @param model The model
   * @param resources The resources
   * @param parent The parent container
   * @param style Style flags
   * 
   * @throws Exception if unable to build the panel
   */
    public FilenamePanel(FilenameModel model, ResourceManager resources, Composite parent, int style) throws Exception {
        super(model, false, resources, parent, style);
    }

    /**
   * Get the properties to display
   *
   * @return The properties list
   * 
   * @see org.charvolant.tmsnet.ui.AbstractInformationPanel#getProperties()
   */
    @Override
    protected String[] getProperties() {
        return new String[] { "name" };
    }

    /**
   * Open a file name dialog on a file.
   * 
   * @param parent The parent shell
   * @param resources The resource manager
   * @param model The file name model
   * @param titleKey The title key
   * 
   * @return The new file name or null for cancelled
   * 
   * @throws Exception if unable to open the dialog
   */
    protected static String open(Shell parent, ResourceManager resources, FilenameModel model, String titleKey) throws Exception {
        Shell sh = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        FilenamePanel panel = new FilenamePanel(model, resources, sh, SWT.NONE);
        FormDialog<FilenamePanel> dialog = new FormDialog<FilenamePanel>(panel, resources, sh, SWT.BORDER);
        boolean ok;
        sh.setText(resources.getString(titleKey));
        ok = dialog.openDialog();
        return ok ? model.getName() : null;
    }

    /**
   * Open a rename dialog on a file.
   * 
   * @param parent The parent shell
   * @param resources The resource manager
   * @param file The file
   * 
   * @return The new file name or null for cancelled
   * 
   * @throws Exception if unable to open the dialog
   */
    public static String rename(Shell parent, ResourceManager resources, FileEntry file) throws Exception {
        return open(parent, resources, new RenameModel(file), TMSUIBundle.TITLE_RENAME);
    }

    /**
   * Open a create dialog for a directory.
   * 
   * @param parent The parent shell
   * @param resources The resource manager
   * 
   * @return The new file name or null for cancelled
   * 
   * @throws Exception if unable to open the dialog
   */
    public static String createDirectory(Shell parent, ResourceManager resources) throws Exception {
        return open(parent, resources, new FilenameModel(), TMSUIBundle.TITLE_CREATE_DIRECTORY);
    }
}
