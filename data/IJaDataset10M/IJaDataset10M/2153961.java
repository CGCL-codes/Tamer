package org.ajc.maximo4eclipse.templates;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * <p>
 * Generic class that provides code to all templates of Maximo objects such as
 * Mbos and Flds.
 * </p>
 * 
 * @author Antonio Jacob Costa
 */
public abstract class DefaultTemplate {

    /**
   * <p>
   * Creates the folders, if necessary, and the file and fills its contents.
   * </p>
   * 
   * @param file
   * @param monitor
   * @throws CoreException
   */
    public void createFile(String folderName, IFile file, IProgressMonitor monitor, IContainer container, InputStream stream) throws CoreException {
        try {
            final IFolder iFolder = container.getFolder(new Path(folderName));
            if (!iFolder.exists()) {
                String[] folderParts = folderName.split("/");
                String folderToCurrent = new String();
                for (int i = 0; i < folderParts.length; i++) {
                    if (folderToCurrent.length() == 0) {
                        folderToCurrent = folderParts[i];
                    } else {
                        folderToCurrent = folderToCurrent.concat("/").concat(folderParts[i]);
                    }
                    IFolder iCreationFolder = container.getFolder(new Path(folderToCurrent));
                    if (!iCreationFolder.exists()) {
                        iCreationFolder.create(true, true, monitor);
                    }
                }
            }
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
        }
    }

    /**
   * <p>
   * Creates the file or files necessary.
   * </p>
   * 
   * @param mboName
   * @param packageName
   * @param monitor
   * @param resource
   */
    public abstract void createFile(String mboName, String packageName, String superclassName, IProgressMonitor monitor, IResource resource) throws CoreException;
}
