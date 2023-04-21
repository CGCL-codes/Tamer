package net.sf.istcontract.editor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public abstract class AbstractDocumentNode extends Model {

    public AbstractDocumentNode(String title) {
        super(title);
    }

    protected abstract String getFileName();

    protected abstract void reloadDocument();

    protected IFile createFolderIfNeeded(RepositoryDetailsItem repositoryDetails) {
        ProjectNode node = repositoryDetails.getEnclosingProject();
        IProject project = node.getProject();
        IFolder repositoryFolder = project.getFolder(repositoryDetails.getName());
        try {
            if (repositoryFolder.exists() == false) {
                repositoryFolder.create(false, true, null);
            }
            String filepath = getFileName();
            return repositoryFolder.getFile(filepath);
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected IFile createTemporaryFile(RepositoryDetailsItem repositoryDetails, String contractAsString) {
        IFile file = createFolderIfNeeded(repositoryDetails);
        createFileWithContents(file.getLocation().toOSString(), contractAsString);
        return file;
    }

    public void createFileWithContents(String filepath, String contractAsString) {
        File file = new File(filepath);
        file.delete();
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(contractAsString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
