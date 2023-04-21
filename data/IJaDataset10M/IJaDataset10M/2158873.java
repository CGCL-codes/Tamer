package org.rubypeople.rdt.refactoring.documentprovider;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.rubypeople.rdt.core.RubyModelException;
import org.rubypeople.rdt.internal.core.util.Util;

public class WorkspaceDocumentProvider extends DocumentProvider {

    private IFile activeFile;

    private String activeFileContent;

    private IProject activeProject;

    private IWorkspaceRoot workspaceRoot;

    public WorkspaceDocumentProvider(IFile activeFile) {
        this.activeFile = activeFile;
        this.activeProject = activeFile.getProject();
        workspaceRoot = this.activeFile.getWorkspace().getRoot();
        this.activeFileContent = getFileContent(getActiveFileName());
    }

    public String getActiveFileContent() {
        return activeFileContent;
    }

    public String getActiveFileName() {
        return activeFile.getFullPath().toOSString();
    }

    public IFile getIFile(String fileName) {
        fileName = makePathAbsoulte(fileName);
        return workspaceRoot.getFile(new Path(fileName));
    }

    public Collection<String> getFileNames() {
        ArrayList<String> fileNames = new ArrayList<String>();
        fillFileNames(fileNames, activeProject);
        return fileNames;
    }

    private void fillFileNames(ArrayList<String> fileNames, IContainer container) {
        try {
            for (IResource resource : container.members()) {
                if (resource.getType() == IResource.FILE) {
                    IFile currentFile = (IFile) resource;
                    String fileExtension = currentFile.getFileExtension();
                    if (fileExtension != null && fileExtension.equals("rb")) {
                        fileNames.add(currentFile.getFullPath().toOSString());
                    }
                }
                if (resource instanceof IContainer) {
                    IContainer subContainer = (IContainer) resource;
                    fillFileNames(fileNames, subContainer);
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    public String getFileContent(String fileName) {
        IFile currentFile = getIFile(fileName);
        try {
            return new String(Util.getResourceContentsAsCharArray(currentFile));
        } catch (RubyModelException e) {
        }
        return null;
    }

    private String makePathAbsoulte(String path) {
        Path absolutePath = new Path(path);
        if (absolutePath.isAbsolute()) {
            return absolutePath.toOSString();
        }
        return activeFile.getFullPath().removeLastSegments(1).append(path).toOSString();
    }
}
