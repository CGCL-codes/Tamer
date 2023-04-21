package com.sebulli.fakturama.export.sales;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import com.sebulli.fakturama.export.ExportWizardPageStartEndDate;

/**
 * Export wizard to export sales
 * 
 * @author Gerd Bartelt
 */
public class ExportWizardUnpaid extends Wizard implements IExportWizard {

    ExportWizardPageStartEndDate page1;

    ExportOptionPage page2;

    /**
	 * Constructor Adds the first page to the wizard
	 */
    public ExportWizardUnpaid() {
        setWindowTitle(_("Export"));
        page1 = new ExportWizardPageStartEndDate(_("List of unpaid invoices"), _("Select a periode.\nOnly the unpaid invoices with a date in this periode will be exported."), true);
        page2 = new ExportOptionPage(_("List of unpaid invoices"), _("Set some export options."));
        addPage(page1);
        addPage(page2);
    }

    /**
	 * Performs any actions appropriate in response to the user having pressed
	 * the Finish button, or refuse if finishing now is not permitted.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
    @Override
    public boolean performFinish() {
        Exporter exporter = new Exporter(page1.getStartDate(), page1.getEndDate(), page1.getDoNotUseTimePeriod(), page2.getShowZeroVatColumn(), Exporter.UNPAID);
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
