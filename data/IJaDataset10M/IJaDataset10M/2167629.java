package fr.insa.rennes.pelias.pcreator.wizards;

import java.util.UUID;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import fr.insa.rennes.pelias.framework.Service;
import fr.insa.rennes.pelias.pcreator.Application;
import fr.insa.rennes.pelias.pcreator.editors.ServiceEditor;
import fr.insa.rennes.pelias.pcreator.editors.ServiceEditorInput;
import fr.insa.rennes.pelias.pcreator.views.ServiceNavigator;
import fr.insa.rennes.pelias.platform.PSxSObjectReference;
import fr.insa.rennes.pelias.platform.Version;

/**
 * 
 * @author Kévin Le Corre
 *
 */
public class NewServiceWizard extends Wizard implements IWizard {

    private NewServicePageOne one;

    private Service service;

    public NewServiceWizard() {
        super();
        setForcePreviousAndNextButtons(false);
        setWindowTitle("Assistant de création de nouveau service");
        service = new Service(UUID.randomUUID(), "", new Version(1, 0));
    }

    public void addPages() {
        one = new NewServicePageOne(service);
        addPage(one);
    }

    public boolean performFinish() {
        Application.getCurrentServiceRepository().putObject(service, true, false);
        ((ServiceNavigator) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ServiceNavigator.ID)).getTreeViewer().refresh();
        PSxSObjectReference ref = service.getSelfSxSReference();
        try {
            IEditorPart monEditeur = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new ServiceEditorInput(ref), ServiceEditor.ID);
            ((ServiceEditor) monEditeur).getKeepVersion().setSelection(true);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        System.out.println("EDITEUR DE SERVICE - Création d'un nouveau service");
        return true;
    }
}
