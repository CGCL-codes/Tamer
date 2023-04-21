package edu.gsbme.wasabi.UI.Wizard.MMLUtility.FMLWriter;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import edu.gsbme.geometrykernel.exception.DocumentGenerationError;
import edu.gsbme.geometrykernel.output.Comsol_to_FMLWriter;

/**
 * Allows user to choose the Comsol Geom File to be converted into FML FIle
 * 
 * This wizard provides file selection and input text field that designated where to generate the file
 * Also provides option to use XML or HDF5 format and relvant text fields
 * 
 * @author David
 *
 */
public class FMLWriterWizard extends Wizard implements INewWizard {

    public static void main(String[] arg) {
        Display display = new Display();
        FMLWriterWizard wiz = new FMLWriterWizard();
        WizardDialog dialog = new WizardDialog(display.getActiveShell(), wiz);
        dialog.create();
        wiz.init(null, null);
        dialog.open();
    }

    FMLWriterPage1 page1;

    public FMLWriterWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
	 * Adding the page to the wizard.
	 */
    public void addPages() {
        page1 = new FMLWriterPage1();
        addPage(page1);
    }

    @Override
    public boolean performFinish() {
        try {
            Comsol_to_FMLWriter writer = new Comsol_to_FMLWriter("Wizard Writer", page1.input_path.getText().trim(), page1.output_path.getText().trim(), page1.hdf5_check.getSelection(), page1.hdf5_path.getText().trim());
            writer.generateDocument();
            writer.write();
            System.out.println("xx");
        } catch (DocumentGenerationError e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
