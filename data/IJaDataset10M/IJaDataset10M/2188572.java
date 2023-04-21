package com.sebulli.fakturama.export.products;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.export.EmptyWizardPage;

/**
 * Export wizard to export sales
 * 
 * @author Gerd Bartelt
 */
public class ExportWizard extends Wizard implements IExportWizard {

    EmptyWizardPage page1;

    /**
	 * Constructor Adds the first page to the wizard
	 */
    public ExportWizard() {
        setWindowTitle(_("Export"));
        page1 = new EmptyWizardPage(_("Export all products"), _("Export the products in an OpenOffice.org Calc table."), Activator.getImageDescriptor("/icons/preview/products.png"));
        addPage(page1);
    }

    /**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button, or refuse if finishing now is not permitted.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
    @Override
    public boolean performFinish() {
        Exporter exporter = new Exporter();
        return exporter.export();
    }

    /**
	 * Initializes this creation wizard using the passed workbench and object
	 * selection.
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
