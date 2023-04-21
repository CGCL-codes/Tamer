package org.eclipse.smd.java.action;

import java.net.URI;
import org.apache.log4j.Logger;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.smd.gef.editor.SMDEditor;
import org.eclipse.smd.java.model.IJavaGenerationContributor;
import org.eclipse.smd.model.StatesMachines;
import org.eclipse.smd.rcp.Activator;
import org.eclipse.smd.rcp.ICommandIds;
import org.eclipse.smd.rcp.conf.lang.Language;
import org.eclipse.ui.IEditorPart;

/**
 * L'action pour g�n�rer un projet java en fonction du mod�le.
 * @author Pierrick HYMBERT (phymbert [at] users.sourceforge.net) 
 */
public class JavaGenerateAction extends WorkbenchPartAction {

    /**
     * Le logger associ� a cette instance.
     */
    protected Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Instancie l'action pour g�n�rer un projet java en fonction du mod�le.
     * @param editor
     *            L'�diteur SMD.
     */
    public JavaGenerateAction(IEditorPart editor) {
        super(editor);
        logger.debug("Cr�ation de l'action pour g�n�rer un projet java en fonction du mod�le.");
        setId(ICommandIds.CMD_GENERATE_JAVA);
        setActionDefinitionId(ICommandIds.CMD_GENERATE_JAVA);
        setImageDescriptor(Activator.getImageDescriptor("icons/Java.gif"));
    }

    /**
     * Initializes this action's text.
     */
    protected void init() {
        setId(ICommandIds.CMD_GENERATE_JAVA);
        setText(Language.get(Language.CMD_GENERATE_JAVA));
        setToolTipText(Language.get(Language.CMD_GENERATE_JAVA));
    }

    /**
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    protected boolean calculateEnabled() {
        return true;
    }

    /**
     * Execute l'action.
     */
    public void run() {
        try {
            SMDEditor editor = ((SMDEditor) getWorkbenchPart());
            StatesMachines statesMachines = editor.getStatesMachines();
            IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
            IPath locationPath = new Path(Platform.getLocation().toString());
            URI locationURI = URIUtil.toURI(locationPath);
            IProject project = workspaceRoot.getProject(statesMachines.getName());
            if (project.exists()) {
                project.delete(true, null);
            }
            IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
            if (locationURI != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(locationURI)) {
                locationURI = null;
            }
            desc.setLocationURI(locationURI);
            project.create(desc, null);
            project.open(null);
            if (!project.hasNature(JavaCore.NATURE_ID)) {
                IProjectDescription description = project.getDescription();
                String[] prevNatures = description.getNatureIds();
                String[] newNatures = new String[prevNatures.length + 1];
                System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
                newNatures[prevNatures.length] = JavaCore.NATURE_ID;
                description.setNatureIds(newNatures);
                project.setDescription(description, null);
            }
            IJavaProject javaProject = JavaCore.create(project);
            IPath projPath = project.getFullPath();
            IPath outputLocation = new Path(projPath + "/bin").makeAbsolute();
            if (!workspaceRoot.exists(outputLocation)) {
                IFolder folder = workspaceRoot.getFolder(outputLocation);
                createDerivedFolder(folder, true, true, null);
                folder.setDerived(true);
            }
            IPath srcLocation = new Path(projPath + "/src").makeAbsolute();
            if (!workspaceRoot.exists(srcLocation)) {
                IFolder folder = workspaceRoot.getFolder(srcLocation);
                createDerivedFolder(folder, true, true, null);
                folder.setDerived(true);
            }
            IClasspathEntry[] classpath = new IClasspathEntry[] { JavaCore.newSourceEntry(srcLocation), JavaCore.newVariableEntry(new Path("JRE_LIB"), new Path("JRE_SRC"), new Path("JRE_SRCROOT")) };
            javaProject.setRawClasspath(classpath, outputLocation, null);
            IJavaGenerationContributor generationContributor = (IJavaGenerationContributor) statesMachines.getAdapter(IJavaGenerationContributor.class);
            AST ast = AST.newAST(AST.JLS3);
            generationContributor.generateJava(ast, javaProject, null, null);
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.openError(getWorkbenchPart().getSite().getShell(), "Erreur durant la g�n�ration du java", "Une erreur est survenue pendant la g�n�ration du java:\n" + e.getMessage());
        }
    }

    public static void createDerivedFolder(IFolder folder, boolean force, boolean local, IProgressMonitor monitor) throws CoreException {
        if (!folder.exists()) {
            IContainer parent = folder.getParent();
            if (parent instanceof IFolder) {
                createDerivedFolder((IFolder) parent, force, local, null);
            }
            folder.create(force ? (IResource.FORCE | IResource.DERIVED) : IResource.DERIVED, local, monitor);
        }
    }
}
