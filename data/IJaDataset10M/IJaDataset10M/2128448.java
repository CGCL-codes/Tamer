package org.xvr.ui.linked;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class for dealing with external files in the ExtLinkedXtextEditor.
 * This class is responsible for managing the linked files - obtaining a link, unlinking it etc.
 * TODO: This class is specific to b3 in a few places (file extension, name of project for links,
 * use of UTF-8), but is otherwise generic.
 */
public class ExtLinkedFileHelper {

    public static final String AUTOLINK_PROJECT_NAME = "AutoLinked_B3ExternalFiles";

    public static final String ENCODING_UTF8 = "utf-8";

    /**
	 * Throws WrappedException - the URI must refer to an existing file!
	 * 
	 * @param uri
	 * @return
	 */
    public static IFile obtainLink(java.net.URI uri, boolean untitled) {
        try {
            IWorkspace ws = ResourcesPlugin.getWorkspace();
            IProject project = ws.getRoot().getProject(AUTOLINK_PROJECT_NAME);
            boolean newProject = false;
            if (!project.exists()) {
                project.create(null);
                newProject = true;
            }
            if (!project.isOpen()) {
                project.open(null);
                project.setHidden(true);
            }
            if (newProject) project.setDefaultCharset(ENCODING_UTF8, new NullProgressMonitor());
            IFile linkFile = null;
            if (untitled) {
                int firstFree = getFirstFreeUntitled(project);
                IFolder untitledFolder = project.getFolder("untitled");
                String fileName = "untitled" + (firstFree > 1 ? "-" + Integer.toString(firstFree) : "") + ".b3";
                linkFile = untitledFolder.getFile(fileName);
            } else {
                linkFile = project.getFile(uri.getPath());
            }
            if (linkFile.exists()) linkFile.refreshLocal(1, null); else {
                createLink(project, linkFile, uri);
            }
            return linkFile;
        } catch (CoreException e) {
            throw new WrappedException(e);
        }
    }

    public static void unlinkInput(IFileEditorInput input) {
        IFile file = input.getFile();
        if (file.isLinked() && file.getProject().getName().equals(ExtLinkedFileHelper.AUTOLINK_PROJECT_NAME)) {
            try {
                if (PlatformUI.isWorkbenchRunning() && !PlatformUI.getWorkbench().isClosing()) file.delete(true, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createLink(IProject project, IFile linkFile, java.net.URI uri) throws CoreException {
        IPath path = linkFile.getFullPath();
        IPath folders = path.removeLastSegments(1).removeFirstSegments(1);
        IPath checkPath = Path.ROOT;
        int segmentCount = folders.segmentCount();
        for (int i = 0; i < segmentCount; i++) {
            checkPath = checkPath.addTrailingSeparator().append(folders.segment(i));
            IFolder folder = project.getFolder(checkPath);
            if (!folder.exists()) folder.create(true, true, null);
        }
        linkFile.createLink(uri, IResource.ALLOW_MISSING_LOCAL, null);
    }

    private static int getFirstFreeUntitled(IProject project) throws CoreException {
        IFolder untitledFolder = project.getFolder("untitled");
        if (!untitledFolder.exists()) {
            untitledFolder.create(true, true, new NullProgressMonitor());
            return 0;
        }
        IResource[] resources = untitledFolder.members();
        int result = resources.length > 0 ? 1 : 0;
        for (IResource r : resources) {
            int dash = r.getName().indexOf("-");
            if (dash < 0) continue;
            int sequence = Integer.valueOf(r.getName().substring(dash + 1, r.getName().length() - 3));
            result = sequence > result ? sequence : result;
        }
        return result + 1;
    }
}
