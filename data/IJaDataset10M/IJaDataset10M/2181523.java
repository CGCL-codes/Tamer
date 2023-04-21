package as.ide.core.builder;

import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class ASBuilderVisitor implements IResourceVisitor {

    private ArrayList<IFile> fileList = new ArrayList<IFile>();

    @Override
    public boolean visit(IResource resource) throws CoreException {
        if (resource.isDerived()) {
            return false;
        }
        if (resource.getType() == IResource.FILE && "as".equals(resource.getFileExtension())) {
            fileList.add((IFile) resource);
        }
        return true;
    }

    public IFile[] getFilesToBuild() {
        return fileList.toArray(new IFile[0]);
    }
}
