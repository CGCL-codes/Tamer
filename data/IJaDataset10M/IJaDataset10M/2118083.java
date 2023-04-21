package es.gavab.eclipse.team.svnadmin.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import es.gavab.eclipse.team.svnadmin.Activator;
import es.gavab.eclipse.team.svnadmin.uimodel.Server;
import es.gavab.eclipse.team.svnadmin.uimodel.User;
import es.gavab.team.svnadmin.model.IRepository;
import es.gavab.team.svnadmin.model.IUser;

public class NewUserWizard extends Wizard implements INewWizard {

    NewUserPage userPage;

    protected IStructuredSelection selection;

    protected IWorkbench workbench;

    public NewUserWizard() {
        super();
    }

    public void addPages() {
        userPage = new NewUserPage(workbench, selection, this);
        addPage(userPage);
    }

    public boolean performFinish() {
        if ((userPage.nameTxt.getText().trim().equals("")) || (userPage.pass1Txt.getText().trim().equals("")) || (userPage.pass2Txt.getText().trim().equals(""))) {
            userPage.status = new Status(IStatus.ERROR, "not_used", 0, "Name, Password and its confirmation cannot be empty fields", null);
            userPage.applyToStatusLine(userPage.status);
            return false;
        }
        if (!userPage.pass1Txt.getText().equals(userPage.pass2Txt.getText())) {
            userPage.status = new Status(IStatus.ERROR, "not_used", 0, "Passwords don't match", null);
            userPage.applyToStatusLine(userPage.status);
            return false;
        }
        final Server svr = (Server) selection.getFirstElement();
        try {
            List<User> users = svr.getUsers();
            for (User usr : users) if (usr.getName().equals(userPage.nameTxt.getText())) {
                userPage.status = new Status(IStatus.ERROR, "not_used", 0, "User exists in the server", null);
                userPage.applyToStatusLine(userPage.status);
                return false;
            }
        } catch (Exception e) {
            svr.disconnect();
            Activator.displayError(workbench.getActiveWorkbenchWindow().getShell(), e.getMessage());
            Activator.refreshViewers(null);
            this.getShell().close();
        }
        if (userPage.selected.getItemCount() == 0) {
            userPage.status = new Status(IStatus.ERROR, "not_used", 0, "Must select a repository at least", null);
            userPage.applyToStatusLine(userPage.status);
            return false;
        }
        final String name = userPage.nameTxt.getText();
        final String pass = userPage.pass1Txt.getText();
        final String[] selrp = userPage.selected.getItems();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    monitor.beginTask("Creating User", 2);
                    createUser(svr, name, pass, selrp, monitor);
                } catch (IOException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
            Activator.refreshViewers(svr);
        } catch (InterruptedException e) {
            return true;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            svr.disconnect();
            Activator.displayError(workbench.getActiveWorkbenchWindow().getShell(), realException.getMessage());
            Activator.refreshViewers(null);
        }
        return true;
    }

    private void createUser(Server svr, String name, String pass, String[] selrepositories, IProgressMonitor monitor) throws IOException {
        IRepository rp = null;
        IUser usr = svr.getRepositoryManager().createUser(name, pass);
        for (String rpName : selrepositories) {
            rp = svr.getRepositoryManager().getRepository(rpName);
            rp.registerUser(usr);
        }
        monitor.worked(1);
        rp.commit();
        monitor.worked(1);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }
}
