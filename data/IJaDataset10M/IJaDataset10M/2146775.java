package net.entropysoft.dashboard.plugin.dashboard.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import net.entropysoft.dashboard.plugin.Activator;
import net.entropysoft.dashboard.plugin.IUIConstants;
import net.entropysoft.dashboard.plugin.dashboard.model.Dashboard;
import net.entropysoft.dashboard.plugin.dashboard.model.persistance.DashboardSerializationException;
import net.entropysoft.dashboard.plugin.dashboard.model.persistance.DashboardSerializer;
import org.dom4j.Document;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

/**
 * Page for Dashboard wizard
 *  
 * @author cedric
 */
public class DashboardWizardPage extends WizardNewFileCreationPage {

    private IWorkbench workbench;

    private static int dashboardCount = 1;

    public DashboardWizardPage(IWorkbench aWorkbench, IStructuredSelection selection) {
        super("dashboardWizardPage", selection);
        this.setTitle(Messages.DashboardWizardPage_CreateADashboardFile);
        this.setDescription(Messages.DashboardWizardPage_CreateANewDashboardFileResource);
        this.setImageDescriptor(Activator.getImageDescriptor(IUIConstants.IMG_DASHBOARD_WIZ));
        this.workbench = aWorkbench;
    }

    /**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent) {
        super.createControl(parent);
        this.setFileName("dashboard" + dashboardCount + ".dashboard");
        setPageComplete(validatePage());
    }

    protected InputStream getInitialContents() {
        Dashboard dashboard = new Dashboard();
        try {
            Document doc = DashboardSerializer.getInstance().serialize(dashboard, null);
            return new ByteArrayInputStream(doc.asXML().getBytes());
        } catch (DashboardSerializationException e) {
            return null;
        }
    }

    public boolean finish() {
        IFile newFile = createNewFile();
        if (newFile == null) return false;
        try {
            IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
            IWorkbenchPage page = dwindow.getActivePage();
            if (page != null) IDE.openEditor(page, newFile, true);
        } catch (org.eclipse.ui.PartInitException e) {
            e.printStackTrace();
            return false;
        }
        dashboardCount++;
        return true;
    }
}
