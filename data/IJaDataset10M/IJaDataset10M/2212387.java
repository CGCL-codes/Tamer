package fw4ex.authoride.plugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.apache.commons.httpclient.HttpClient;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.jdom.JDOMException;
import fw4ex.authoride.plugin.Activator;
import fw4ex.authoride.plugin.communication.IRestResult;
import fw4ex.authoride.plugin.communication.authentication.IAuthResult;
import fw4ex.authoride.plugin.communication.authentication.IAuthentication;
import fw4ex.authoride.plugin.communication.authentication.rest.RestAuthentication;
import fw4ex.authoride.plugin.parsers.BaseFileParser;
import fw4ex.authoride.plugin.preferences.PreferenceConstants;
import fw4ex.utils.Time;

public class ExerciseWizard extends Wizard implements INewWizard {

    private WizardNewProjectCreationPage project;

    private ExerciseWizardConfigPage page;

    private IWorkbench workbench;

    private ISelection selection;

    private String sub_address;

    private String check_address;

    public ExerciseWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New FW4EX Exercise");
        sub_address = Activator.getPrefString(PreferenceConstants.P_SUB_ADDRESS);
        check_address = Activator.getPrefString(PreferenceConstants.P_CHECK_ADDRESS);
    }

    @Override
    public void addPages() {
        project = new WizardNewProjectCreationPage("projectCreationPage");
        project.setTitle("Exercise Name");
        project.setDescription("Choose a name for the exercise");
        addPage(project);
        page = new ExerciseWizardConfigPage("projectConfigPage", selection);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        final String projectName = project.getProjectName();
        final IProject projectHandle = project.getProjectHandle();
        final String auth_address = page.getAuthAddress();
        final String sub_address = page.getSubAddress();
        final String check_address = page.getCheckAddress();
        final String login = page.getLogin();
        final String password = page.getPassword();
        final boolean save = page.getSave();
        if (save) {
            Activator.setPrefString(PreferenceConstants.P_AUTH_ADDRESS, auth_address);
            Activator.setPrefString(PreferenceConstants.P_SUB_ADDRESS, sub_address);
            Activator.setPrefString(PreferenceConstants.P_CHECK_ADDRESS, check_address);
            Activator.setPrefString(PreferenceConstants.P_LOGIN, login);
            Activator.setPrefString(PreferenceConstants.P_PASSWORD, password);
        }
        this.sub_address = sub_address;
        this.check_address = check_address;
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                Activator.log("Creating exercise " + projectName, 0, 0);
                try {
                    IAuthResult res = connectClient(auth_address, login, password);
                    switch(res.getStatus()) {
                        case IRestResult.SUCCESS:
                            if ("".equals(Activator.getPrefString(PreferenceConstants.P_FIRST_NAME))) {
                                Activator.setPrefString(PreferenceConstants.P_FIRST_NAME, res.getFirstName());
                            }
                            if ("".equals(Activator.getPrefString(PreferenceConstants.P_MIDDLE_NAME))) {
                                Activator.setPrefString(PreferenceConstants.P_MIDDLE_NAME, res.getMiddleName());
                            }
                            if ("".equals(Activator.getPrefString(PreferenceConstants.P_LAST_NAME))) {
                                Activator.setPrefString(PreferenceConstants.P_LAST_NAME, res.getLastName());
                            }
                            if ("".equals(Activator.getPrefString(PreferenceConstants.P_POST_LAST_NAME))) {
                                Activator.setPrefString(PreferenceConstants.P_POST_LAST_NAME, res.getPostLastName());
                            }
                            break;
                        default:
                            break;
                    }
                    buildProject(projectName, projectHandle, monitor, res);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, true, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    private IAuthResult connectClient(String url, String login, String password) {
        HttpClient client = Activator.getContext().getClient();
        IAuthentication auth = new RestAuthentication(url, client);
        auth.setIdentity(login, password);
        IAuthResult res = auth.authenticate();
        return res;
    }

    private void buildProject(String projectName, IProject projectHandle, IProgressMonitor monitor, IAuthResult authResult) throws CoreException {
        monitor.beginTask("Creating " + projectName, 500);
        projectHandle.create(monitor);
        projectHandle.open(monitor);
        String projectPath = File.separator + projectName;
        String xmlContent = "";
        try {
            xmlContent = Activator.getFileContentFromPlugin("xml" + File.separator + "base.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseFileParser parser = new BaseFileParser(null);
        try {
            if (authResult != null) {
                parser.parse(xmlContent);
                parser.setExerciseName(projectName);
                parser.setExerciseNickName(projectName);
                parser.setExerciseDate(Time.printXSDDate());
                String fn = Activator.getPrefBool(PreferenceConstants.P_ALWAYS_NAME) ? Activator.getPrefString(PreferenceConstants.P_FIRST_NAME) : authResult.getFirstName();
                parser.setAuthorFirstName(fn);
                String mn = Activator.getPrefBool(PreferenceConstants.P_ALWAYS_NAME) ? Activator.getPrefString(PreferenceConstants.P_MIDDLE_NAME) : "";
                if (!"".equals(mn)) {
                    parser.setAuthorMiddleName(mn);
                }
                String ln = Activator.getPrefBool(PreferenceConstants.P_ALWAYS_NAME) ? Activator.getPrefString(PreferenceConstants.P_LAST_NAME) : authResult.getLastName();
                parser.setAuthorLastName(ln);
                String pln = Activator.getPrefBool(PreferenceConstants.P_ALWAYS_NAME) ? Activator.getPrefString(PreferenceConstants.P_POST_LAST_NAME) : "";
                if (!"".equals(pln)) {
                    parser.setAuthorPostLastName(pln);
                }
                String email = Activator.getPrefString(PreferenceConstants.P_EMAIL);
                parser.setAuthorEmail(email);
                parser.setTags(new String[] { projectName });
                xmlContent = parser.toXmlString();
            }
        } catch (JDOMException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        addFile(projectPath, "fw4ex.properties", "", monitor);
        addFile(projectPath, "fw4ex.xml", xmlContent, monitor);
        addFolder(projectPath, "data", monitor);
        addFolder(projectPath, "tests", monitor);
        addFolder(projectPath, "pseudos", monitor);
        addFolder(projectPath + File.separator + "pseudos", "null", monitor);
        addFolder(projectPath + File.separator + "pseudos", "half", monitor);
        addFolder(projectPath + File.separator + "pseudos", "perfect", monitor);
        final IFile properties_file = projectHandle.getFile("fw4ex.properties");
        getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                Properties prop = new Properties();
                prop.put(PreferenceConstants.P_SUB_ADDRESS, sub_address);
                prop.put(PreferenceConstants.P_CHECK_ADDRESS, check_address);
                try {
                    File f = properties_file.getLocation().toFile();
                    prop.store(new FileOutputStream(f), "FW4EX Properties file");
                    properties_file.refreshLocal(IResource.DEPTH_ZERO, null);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (CoreException e1) {
                    e1.printStackTrace();
                }
            }
        });
        final IFile xml_file = projectHandle.getFile("fw4ex.xml");
        getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                BasicNewResourceWizard.selectAndReveal(xml_file, workbench.getActiveWorkbenchWindow());
                IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
                try {
                    IDE.openEditor(page, xml_file, true);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

    public boolean addFolder(String path, String name, IProgressMonitor monitor) {
        Activator.log("addFolder : " + path + File.separator + name, 0, 0);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(path));
        IContainer container = (IContainer) resource;
        if (container == null) {
            return false;
        }
        final IFolder folder = container.getFolder(new Path(name));
        try {
            folder.create(true, true, monitor);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addFile(String path, String name, String content, IProgressMonitor monitor) {
        Activator.log("addFile : " + path + File.separator + name, 0, 0);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(path));
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(name));
        try {
            InputStream stream = openContentStream(content);
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static InputStream openContentStream(String content) {
        String contents = content;
        return new ByteArrayInputStream(contents.getBytes());
    }
}
