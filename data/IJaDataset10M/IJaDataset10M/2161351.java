package org.deft.share;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import org.deft.repository.RepositoryFactory;
import org.deft.repository.Util;
import org.deft.repository.fragment.Chapter;
import org.deft.repository.fragment.CodeFile;
import org.deft.repository.fragment.Fragment;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;

public class FragmentFileEditorInput extends FileEditorInput implements IFragmentEditorInput, IStreamEditorInput {

    private Fragment fragment;

    public FragmentFileEditorInput(IFile file) {
        super(file);
    }

    public FragmentFileEditorInput(CodeFile codeFile) {
        this(createTempFileEclipseWay(codeFile));
        this.fragment = codeFile;
    }

    public FragmentFileEditorInput(Chapter chapter, IFile docFile) {
        this(docFile);
        this.fragment = chapter;
    }

    protected static IFile createTempFileEclipseWay(CodeFile codeFile) {
        IResource resource = codeFile.getWorkspaceRepresentation();
        if (resource instanceof IFile) {
            IFile source = (IFile) resource;
            try {
                IResource parentResource = codeFile.getParent().getWorkspaceRepresentation();
                if (parentResource instanceof IFolder) {
                    IFolder revisionFolder = ((IFolder) parentResource).getFolder(codeFile.getUUID().toString());
                    String[] parts = codeFile.getName().split("\\.");
                    String extension = parts[parts.length - 1];
                    String tempFileName = source.getName() + "." + extension;
                    IFile target = revisionFolder.getFile(tempFileName);
                    if (!target.exists()) {
                        InputStream is = source.getContents();
                        target.create(is, true, null);
                    }
                    ResourceAttributes attributes = new ResourceAttributes();
                    attributes.setReadOnly(true);
                    target.setResourceAttributes(attributes);
                    return target;
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected static IFile createTempFile(CodeFile codeFile) {
        IPath path = Util.getAbsolutePathFromCodeFile(codeFile);
        File file = new File(path.toOSString());
        String[] parts = codeFile.getName().split("\\.");
        String extension = parts[parts.length - 1];
        IPath ext = path.addFileExtension(extension);
        File tempFile = new File(ext.toOSString());
        if (tempFile.exists()) {
            boolean deleted = tempFile.delete();
            System.out.println("deleted: " + deleted);
        }
        try {
            boolean created = tempFile.createNewFile();
            if (created) {
                FileOutputStream fos = new FileOutputStream(tempFile);
                FileInputStream fis = new FileInputStream(file);
                while (fis.available() > 0) {
                    fos.write(fis.read());
                }
                fis.close();
                fos.close();
                IFile iFile = Util.getFileFromPath(ext);
                return iFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return ImageDescriptor.getMissingImageDescriptor();
    }

    public String getName() {
        return fragment.getName();
    }

    public IPersistableElement getPersistable() {
        return this;
    }

    public String getToolTipText() {
        return fragment.getName();
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    /**
	 * Two FragmentEditorInputs are equal if the two containing Fragments are
	 * equal. The two Fragments are equal if they have the same UUID.
	 */
    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof IFragmentEditorInput) {
            IFragmentEditorInput other = (IFragmentEditorInput) arg0;
            return fragment.equals(other.getFragment());
        }
        return false;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public InputStream getInputStream() {
        return RepositoryFactory.getRepository().getInputStream(fragment);
    }

    public OutputStream getOutputStream() {
        return RepositoryFactory.getRepository().getOutputStream(fragment);
    }

    public String getFactoryId() {
        return FragmentEditorInputFactory.ID;
    }

    public void saveState(IMemento memento) {
        memento.putString(KEY, fragment.getUUID().toString());
    }

    @Override
    public IPath getPath() {
        IResource resource = fragment.getWorkspaceRepresentation();
        return resource.getLocation();
    }
}

class TemporaryFileCreationWorkspaceModifyOperation extends WorkspaceModifyOperation {

    private IFile resultFile;

    private CodeFile codeFile;

    private boolean finished = false;

    private boolean running = false;

    public TemporaryFileCreationWorkspaceModifyOperation(CodeFile codeFile) {
        super();
        this.codeFile = codeFile;
    }

    @Override
    protected synchronized void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        running = true;
        resultFile = FragmentFileEditorInput.createTempFile(codeFile);
        running = false;
        finished = true;
    }

    public IFile getResult() {
        return resultFile;
    }

    public boolean hasFinished() {
        return finished;
    }

    public boolean isRunning() {
        return running;
    }
}
