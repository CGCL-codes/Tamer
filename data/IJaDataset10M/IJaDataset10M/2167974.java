package com.sebulli.fakturama.importer.csv.contacts;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import com.sebulli.fakturama.importer.ImportOptionPage;
import com.sebulli.fakturama.importer.ImportProgressDialog;
import com.sebulli.fakturama.views.datasettable.ViewContactTable;
import com.sebulli.fakturama.views.datasettable.ViewDataSetTable;

/**
 * A wizard to import tables in CSV file format
 * 
 * @author Gerd Bartelt
 */
public class ImportWizard extends Wizard implements IImportWizard {

    ImportOptionPage optionPage;

    String selectedFile = "";

    /**
	 * Constructor
	 * 
	 * Creates a new wizard with one page
	 */
    public ImportWizard() {
        setWindowTitle(_("Import CSV"));
        optionPage = new ImportOptionPage(_("Import Contacts"), _("Set some import options."), "/icons/preview/contacts2.png");
        optionPage.setPageComplete(true);
        addPage(optionPage);
        setNeedsProgressMonitor(true);
    }

    /**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
    public boolean performFinish() {
        FileDialog fileDialog = new FileDialog(this.getShell());
        fileDialog.setFilterExtensions(new String[] { "*.csv" });
        String userLocation;
        userLocation = Platform.getUserLocation().getURL().getPath();
        fileDialog.setFilterPath(userLocation);
        fileDialog.setText(_("Select file to import"));
        fileDialog.setFilterNames(new String[] { _("Table as CSV") + " (*.csv)" });
        selectedFile = fileDialog.open();
        if (selectedFile != null) {
            if (!selectedFile.isEmpty()) {
                Importer csvImporter = new Importer();
                csvImporter.importCSV(selectedFile, false, optionPage.getUpdateExisting(), optionPage.getUpdateWithEmptyValues());
                ImportProgressDialog dialog = new ImportProgressDialog(this.getShell());
                dialog.setStatusText(csvImporter.getResult());
                ViewDataSetTable view = (ViewDataSetTable) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ViewContactTable.ID);
                if (view != null) view.refresh();
                return (dialog.open() == ImportProgressDialog.OK);
            }
        }
        return false;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
