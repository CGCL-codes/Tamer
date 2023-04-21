package br.gov.demoiselle.eclipse.main.ui.crud;

import java.net.MalformedURLException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import br.gov.demoiselle.eclipse.util.utility.EclipseUtil;

public class CrudWizardPage extends WizardPage implements Listener {

    private ResourceBundle resource;

    public CrudProject managerPage;

    private IProject fProject;

    protected CrudWizardPage(String name, IStructuredSelection selection) {
        super(name);
        resource = EclipseUtil.getResourceBundle();
        this.setTitle(resource.getString("wizard.title"));
        this.setDescription(resource.getString("wizard.description.edit"));
        this.fProject = (IProject) selection.getFirstElement();
    }

    public void createControl(Composite parent) {
        managerPage = new CrudProject(fProject, parent, SWT.FILL);
        this.setControl(managerPage);
        addListeners();
        validate();
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public void dialogChanged(String error) {
        updateStatus(error);
    }

    public void handleEvent(Event arg0) {
        validate();
    }

    /**
	 * Adicionar listeners aos botões
	 */
    private void addListeners() {
        managerPage.getJdbcButton().addListener(SWT.Selection, this);
        managerPage.getHibernateButton().addListener(SWT.Selection, this);
        managerPage.getJPAButton().addListener(SWT.Selection, this);
        managerPage.getSearchPojoButton().addListener(SWT.Selection, this);
        managerPage.getSearchDaoButton().addListener(SWT.Selection, this);
        managerPage.getSearchBcButton().addListener(SWT.Selection, this);
        managerPage.getSearchMbButton().addListener(SWT.Selection, this);
        managerPage.getSearchPagesButton().addListener(SWT.Selection, this);
    }

    @Override
    public void performHelp() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {
                PlatformUI.getWorkbench().getHelpSystem().displayHelpResource("/br.gov.demoiselle.eclipse.help/html/crud/crud.html");
            }
        });
    }

    public void applyActions() throws MalformedURLException, Exception {
        if (managerPage != null) {
            managerPage.applyAction();
        }
    }

    private void validate() {
        if (managerPage.getPojoText().equals("")) {
            dialogChanged(resource.getString("message.select.pojo"));
        } else if (managerPage.getDaoText().equals("")) {
            dialogChanged(resource.getString("message.select.package.dao"));
        } else if (managerPage.getBcText().equals("")) {
            dialogChanged(resource.getString("message.select.package.bc"));
        } else if (managerPage.getMbText().equals("")) {
            dialogChanged(resource.getString("message.select.package.mb"));
        } else if (managerPage.getPagesText().equals("")) {
            dialogChanged(resource.getString("message.select.path.page"));
        } else if (managerPage.getConnectionType().equals("")) {
            dialogChanged(resource.getString("message.select.type.connection"));
        } else {
            dialogChanged(null);
        }
    }
}
