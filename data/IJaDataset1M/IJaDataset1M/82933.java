package org.gello.client.actions.projectsView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gello.client.Application;
import org.gello.client.IImageKeys;
import org.gello.client.manager.Project;
import org.gello.client.manager.ServerException_Exception;
import org.gello.client.model.ObjectXMLSerializer;
import org.gello.client.model.Session;
import org.gello.client.views.Projects;

public class ImportProjectAction extends Action implements ActionFactory.IWorkbenchAction {

    public static final String ID = "org.gello.client.actions.projectsView.ImportProject";

    public ImportProjectAction(IWorkbenchWindow window) {
        setId(ID);
        setText("&Import");
        setToolTipText("Import Project");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.IMPORT_PROJECT));
    }

    /**
	 * Imports a project from an xml file.  It serializes that xml file
	 * from xml to a Project object, sets some properties on it, and then
	 * adds it to the server and updates the UI if successful.
	 * 
	 */
    @SuppressWarnings("unchecked")
    public void run() {
        Project project = new Project();
        FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
        fileDialog.setFilterExtensions(new String[] { "*.xml" });
        String path = fileDialog.open();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            String line = in.readLine();
            String file = "";
            while (line != null) {
                file += line;
                line = in.readLine();
            }
            Project gelloProject = (Project) new ObjectXMLSerializer().fromXML(file);
            project.setName(gelloProject.getName());
            project.setTemplateType(gelloProject.getTemplateType());
            project.setDescr(gelloProject.getDescr());
            project.setDrugClass(gelloProject.getDrugClass());
            project.setContents(gelloProject.getContents());
            project.setCreatedby(Session.getInstance().getConnectionDetails().getUsername());
            project.setRepository("Local Repository");
            project.setStatus("Imported Project");
            project.setUniqueId(UUID.randomUUID().toString());
            Project updatedProject = null;
            try {
                updatedProject = Application.getManager().addProject(project);
            } catch (ServerException_Exception e) {
                e.printStackTrace();
            }
            if (updatedProject != null) {
                ArrayList<Project> projectList = (ArrayList<Project>) Projects.viewer.getInput();
                projectList.add(updatedProject);
                Projects.viewer.refresh();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
    }
}
