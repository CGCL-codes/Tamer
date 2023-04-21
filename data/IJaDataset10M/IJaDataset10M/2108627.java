package org.xaware.ide.xadev.gui.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.xaware.ide.xadev.XADesignerLogger;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.ResourceUtils;
import org.xaware.ide.xadev.datamodel.ResourcePathDataNode;
import org.xaware.ide.xadev.tools.gui.packagetool.NavigatorPackageTool;
import org.xaware.ide.xadev.tools.gui.packagetool.PackageBuilder;

/**
 * @author hcurtis
 * Resource Navigator action to package the selected files into a XAR
 *
 */
public class PackageXarAction implements IObjectActionDelegate {

    private final ArrayList files = new ArrayList();

    private String candidatePackageName = null;

    private IResource candidateNameSource = null;

    private final DeployResourceVisitor resourceVistor = new DeployResourceVisitor(files);

    private final Hashtable relatedSources = new Hashtable();

    private boolean selectedArchive = false;

    /**
	 * 
	 */
    public PackageXarAction() {
        super();
    }

    public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
    }

    public void run(final IAction action) {
        final NavigatorPackageTool dialog = new NavigatorPackageTool();
        if (selectedArchive) {
            initPackageTool(dialog);
            return;
        }
        final Iterator iterFiles = files.iterator();
        final ArrayList packageCandidates = new ArrayList();
        boolean noResourcesList = false;
        final StringBuffer noResourceListFiles = new StringBuffer("The following files are not found in any resource list:\n");
        try {
            while (iterFiles.hasNext()) {
                final IFile f = (IFile) iterFiles.next();
                if (validResourcedFile(f)) {
                    packageCandidates.add(f.getFullPath().toString());
                    final ArrayList relatedCandidates = new ArrayList();
                    ResourceUtils.getRelatedDocuments(f.getLocation().toFile(), relatedCandidates);
                    for (int i = 0; i < relatedCandidates.size(); i++) {
                        System.out.println("<**> Referenced " + relatedCandidates.get(i) + " in " + f.getFullPath().toString());
                        relatedSources.put(relatedCandidates.get(i), f.getFullPath().toString());
                    }
                    packageCandidates.addAll(relatedCandidates);
                } else {
                    noResourceListFiles.append(f.getFullPath().toString()).append("\n");
                    noResourcesList = true;
                }
            }
            if (noResourcesList) {
                noResourceListFiles.append("\nThese files are not included in the package assembly, do you wish to continue?");
                final int result = ControlFactory.showConfirmDialog(noResourceListFiles.toString(), "Package File Candidate Selection", false);
                if (result == IDialogConstants.CANCEL_ID) {
                    return;
                }
            }
            dialog.open();
            dialog.getCancelButton().setEnabled(false);
            BusyIndicator.showWhile(dialog.getParent().getDisplay(), new Runnable() {

                public void run() {
                    final ArrayList pkgNodes = convertToResourcePathDataNodes(packageCandidates, dialog);
                    dialog.getNavigatorPackageBuilder().addDocuments(pkgNodes.toArray(), pkgNodes.size());
                }
            });
            dialog.getCancelButton().setEnabled(true);
            if (candidatePackageName != null) {
                dialog.setPackageFile(candidatePackageName);
            }
        } catch (final RuntimeException e) {
            XADesignerLogger.logError("Failed to complete Package assembly", e);
        }
    }

    /**
	 * Just a single XAR file was selected.  This method will start up the Package
	 * Tool and initialized the name of the XAR with the selected file path.
	 * @param dialog
	 */
    private void initPackageTool(final NavigatorPackageTool dialog) {
        final IFile archive = (IFile) files.get(0);
        final String filename = archive.getLocation().toOSString();
        dialog.open();
        dialog.setPackageFile(filename);
        final PackageBuilder pkgBuilder = dialog.getNavigatorPackageBuilder();
        if (pkgBuilder.buildTableFromXarFile(filename) == false) {
            final String msg = "Invalid XAR file: " + filename;
            ControlFactory.showMessageDialog(msg, "XAR File", MessageDialog.ERROR);
        }
    }

    /**
	 * Check the provided file to see if it participates in one of the resources
	 * assigned to the project
	 * @param f
	 * @return Returns true if a resource entry is found that matches the file's
	 * folder assignments.
	 */
    private boolean validResourcedFile(final IFile f) {
        boolean fileInResourcelist = false;
        if (f != null) {
            final String fLoc = f.getLocation().toOSString().replace('\\', '/');
            final Vector rList = ResourceUtils.getProjectResourceEntries(f.getProject().getName());
            final Enumeration eResList = rList.elements();
            while (eResList.hasMoreElements() && !fileInResourcelist) {
                final File resFile = (File) eResList.nextElement();
                final String resourceFolder = resFile.getAbsolutePath().replace('\\', '/');
                fileInResourcelist = fLoc.startsWith(resourceFolder);
            }
        }
        return fileInResourcelist;
    }

    /**
	 * Convert the array of string paths into ResourcePathDataNodes.  Each unique folder and depenent 
	 * path will be added to the ArrayList of ResourcePathDataNodes.  Files will have a reference to 
	 * their parent folder and dependent folder will ave a link to their parent folder.
	 * The relative path reflects the XAware Resource Path for each of the known files.
	 * @param packageCandidates
	 * @param pkgBuilder
	 * @return Returns a list of ResourcePathDataNodes representing the selected and
	 * related files
	 */
    protected ArrayList convertToResourcePathDataNodes(final ArrayList packageCandidates, final NavigatorPackageTool dialog) {
        final PackageBuilder pkgBuilder = dialog.getNavigatorPackageBuilder();
        pkgBuilder.startProcessMonitor(packageCandidates.size(), "Preparing selected documents");
        final ArrayList resources = new ArrayList();
        final IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        final String workSpaceRoot = myWorkspaceRoot.getLocation().toString();
        final Iterator iterPkgFiles = packageCandidates.iterator();
        while (iterPkgFiles.hasNext()) {
            String relativePath = null;
            final String forgPath = (String) iterPkgFiles.next();
            String fPath = forgPath.replace('\\', '/');
            String fabsPath = ResourceUtils.getAbsolutePath(fPath.trim()).replace('\\', '/');
            final boolean absoluteKnown = !fPath.equals(fabsPath);
            if (absoluteKnown) {
                final int leaderPos = fabsPath.indexOf(fPath);
                if (leaderPos >= 0) {
                    fabsPath = fabsPath.substring(0, leaderPos);
                    if (!fabsPath.endsWith("/")) {
                        fabsPath += "/";
                    }
                }
            } else {
                if (fPath.startsWith("/")) {
                    fPath = fPath.substring(1);
                }
                final IResource resource = myWorkspaceRoot.findMember(fPath);
                if (resource != null) {
                    fabsPath = resource.getLocation().toOSString();
                    relativePath = ResourceUtils.getRelativePath(fabsPath);
                } else {
                    final String referencingSource = (String) relatedSources.get(forgPath);
                    final IResource refSource = myWorkspaceRoot.findMember(referencingSource);
                    if (refSource instanceof IFile) {
                        final String localRelPath = ResourceUtils.getRelativePath(refSource.getLocation().toOSString());
                        pkgBuilder.addToMissingFiles(fPath, localRelPath);
                    }
                    dialog.getProgressMonitor().worked(1);
                    continue;
                }
            }
            String candidatePath = ResourceUtils.getRelativePath(fPath);
            candidatePath = candidatePath.replace('\\', '/');
            if (candidatePath.startsWith("/")) {
                candidatePath = candidatePath.substring(1);
            }
            ResourcePathDataNode nFolder = null;
            ResourcePathDataNode lastFolder = null;
            final StringBuffer dependentFolders = new StringBuffer();
            final StringTokenizer st = new StringTokenizer(candidatePath, "/");
            while (st.hasMoreTokens()) {
                final String folder = st.nextToken();
                if (dependentFolders.length() > 0) {
                    dependentFolders.append("/");
                }
                dependentFolders.append(folder);
                nFolder = new ResourcePathDataNode(folder, lastFolder);
                if (absoluteKnown) {
                    nFolder.setFilePath(new File(fabsPath + dependentFolders.toString()));
                }
                if (st.hasMoreTokens()) {
                    nFolder.setRelativePath(null);
                } else {
                    nFolder.setRelativePath(relativePath != null ? relativePath : dependentFolders.toString());
                }
                final int resIx = resources.indexOf(nFolder);
                if (resIx >= 0) {
                    nFolder = (ResourcePathDataNode) resources.get(resIx);
                } else {
                    resources.add(nFolder);
                }
                lastFolder = nFolder;
            }
            dialog.getProgressMonitor().worked(1);
        }
        pkgBuilder.endProcessMonitor();
        return resources;
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
        candidatePackageName = null;
        candidateNameSource = null;
        selectedArchive = false;
        action.setToolTipText("Create package assembly with selected files");
        files.clear();
        action.setToolTipText("Activate Package Tool with selected archive");
        if (selection instanceof IStructuredSelection) {
            final IStructuredSelection is = (IStructuredSelection) selection;
            for (final Iterator selectionIter = is.iterator(); selectionIter.hasNext(); ) {
                final Object aSelection = selectionIter.next();
                if (aSelection instanceof IFile) {
                    final IFile res = (IFile) aSelection;
                    if ("xar".equalsIgnoreCase(res.getFileExtension())) {
                        if (is.size() == 1) {
                            files.add(res);
                            selectedArchive = true;
                            action.setToolTipText("Activate Package Tool with selected archive");
                        }
                    } else {
                        files.add(res);
                        collectPackageName(res);
                    }
                } else if (aSelection instanceof IFolder) {
                    gatherFolderFiles((IFolder) aSelection);
                } else if (aSelection instanceof IProject) {
                    gatherProjectFiles((IProject) aSelection);
                } else if (aSelection instanceof IAdaptable) {
                    final IProject p = (IProject) ((IAdaptable) aSelection).getAdapter(IProject.class);
                    if (p != null) {
                        gatherProjectFiles((IProject) aSelection);
                    }
                }
            }
        }
        action.setEnabled(files.size() > 0);
    }

    private void collectPackageName(final IFile res) {
        if (candidatePackageName == null) {
            String candidate = res.getLocation().toOSString();
            final int extPos = candidate.lastIndexOf('.');
            if (extPos >= 0) {
                candidate = candidate.substring(0, extPos);
            }
            if (!candidate.endsWith(".")) {
                candidate += ".";
            }
            candidatePackageName = candidate + "xar";
        }
    }

    /**
	 * Gather the files in the selected open project and add them 
	 * to the target list for the new XAR package
	 * @param project
	 */
    private void gatherProjectFiles(final IProject project) {
        if (project == null) {
            return;
        }
        if (!project.isOpen()) {
            return;
        }
        try {
            if (candidateNameSource instanceof IProject && thereAreChildren(project)) {
                final String pName = ((IProject) candidateNameSource).getName();
                if (project.getName().compareTo(pName) < 0) {
                    setUpXarName(project);
                }
            }
            if (!(candidateNameSource instanceof IProject)) {
                setUpXarName(project);
            }
            project.accept(resourceVistor);
        } catch (final CoreException e) {
            XADesignerLogger.logError("Failed to process all selected resources", e);
        }
    }

    /**
	 * Creates the propsed XAR name from the project name
	 * @param project
	 */
    private void setUpXarName(final IProject project) {
        String candidate = project.getLocation().toOSString().replace('\\', '/');
        if (!candidate.endsWith("/")) {
            candidate += "/";
        }
        candidatePackageName = candidate + project.getName() + ".xar";
        candidateNameSource = project;
    }

    /**
	 * Gather the files under the folder that should be added to the
	 * target XAR package
	 * @param folder
	 */
    private void gatherFolderFiles(final IFolder folder) {
        if (folder == null) {
            return;
        }
        try {
            if (candidateNameSource instanceof IFolder && thereAreChildren(folder)) {
                final String fName = ((IFolder) candidateNameSource).getName();
                if (folder.getName().compareTo(fName) < 0) {
                    setUpXarName(folder);
                }
            } else if (!((candidateNameSource instanceof IProject) || (candidateNameSource instanceof IFolder))) {
                setUpXarName(folder);
            }
            folder.accept(resourceVistor);
        } catch (final CoreException e) {
            XADesignerLogger.logError("Failed to process all selected resources", e);
        }
    }

    /**
	 * Test the proposed folder or project to determine if there are
	 * IFile children
	 * @param res
	 * @return Returns true if a IFile child is found
	 */
    private boolean thereAreChildren(final IResource res) {
        boolean foundIFile = false;
        if (res instanceof IProject) {
            final IProject p = (IProject) res;
            try {
                final IResource children[] = p.members();
                for (int i = 0; i < children.length && !foundIFile; i++) {
                    if (children[i] instanceof IFile) {
                        foundIFile = true;
                    }
                }
                for (int i = 0; i < children.length && !foundIFile; i++) {
                    if (children[i] instanceof IFolder) {
                        foundIFile = thereAreChildren(children[i]);
                    }
                }
            } catch (final CoreException e) {
                XADesignerLogger.logError("Failed to acquire list of project " + p.getName() + " children", e);
            }
        } else if (res instanceof IFolder) {
            final IFolder f = (IFolder) res;
            try {
                final IResource children[] = f.members();
                for (int i = 0; i < children.length && !foundIFile; i++) {
                    if (children[i] instanceof IFile) {
                        foundIFile = true;
                    }
                }
                for (int i = 0; i < children.length && !foundIFile; i++) {
                    if (children[i] instanceof IFolder) {
                        foundIFile = thereAreChildren(children[i]);
                    }
                }
            } catch (final CoreException e) {
                XADesignerLogger.logError("Failed to acquire list of folder " + f.getFullPath() + " children", e);
            }
        }
        return foundIFile;
    }

    /**
	 * Creates the propsed XAR name from the folder name
	 * @param folder
	 */
    private void setUpXarName(final IFolder folder) {
        String candidate = folder.getLocation().toOSString().replace('\\', '/');
        if (!candidate.endsWith("/")) {
            candidate += "/";
        }
        candidatePackageName = candidate + folder.getName() + ".xar";
        candidateNameSource = folder;
    }

    /**
	 * This is the visitor class on the IProject or IFolder selections
	 * that will gather up the IFiles located in either of these selections
	 * @author hcurtis
	 *
	 */
    public class DeployResourceVisitor implements IResourceVisitor {

        ArrayList fCollection;

        public DeployResourceVisitor(final ArrayList f) {
            fCollection = f;
        }

        /**
		 * Collect the appropriate files (IFile) in the supplied ArrayList.
		 * Files that start with a period, are under the .externalToolBuilders
		 * folder, or XAR files will not be collected. 
		 */
        public boolean visit(final IResource resource) throws CoreException {
            boolean saveFile = true;
            if (resource instanceof IFile) {
                final IFile resFile = (IFile) resource;
                final IContainer container = resFile.getParent();
                if (container instanceof IFolder) {
                    final IFolder folder = (IFolder) container;
                    saveFile = !".externalToolBuilders".equals(folder.getName());
                }
                if (saveFile) {
                    final String fname = resFile.getName();
                    saveFile = !fname.startsWith(".");
                }
                if (saveFile) {
                    if (!"xar".equalsIgnoreCase(resFile.getFileExtension())) {
                        if (!fCollection.contains(resFile)) {
                            fCollection.add(resFile);
                        }
                    }
                }
            }
            return true;
        }
    }
}
