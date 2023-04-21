package cnaf.sidoc.ide.core.search;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import cnaf.sidoc.ide.core.model.ISIDocBaseCollectionProject;
import cnaf.sidoc.ide.core.model.ISIDocInfraCoreProject.SIDocProjectType;
import cnaf.sidoc.ide.core.util.SIDocModelUtils;

public class MetaFolderContainerProvider implements IResourceProvider, IMultiResourceProvider {

    public static MetaFolderContainerProvider INSTANCE = new MetaFolderContainerProvider();

    public IContainer getResource(Object selectedNode, String collectionURI) {
        ISIDocBaseCollectionProject project = SIDocModelUtils.getBaseCollectionProject(collectionURI);
        if (project == null) {
            return null;
        }
        return project.getMetaFolder();
    }

    public IResource getResource(Object selectedNode, IResource resource) {
        ISIDocBaseCollectionProject project = SIDocModelUtils.getBaseCollectionProject(resource.getProject());
        if (project == null) {
            if (SIDocModelUtils.isSIDocInfraCoreProject(resource.getProject())) {
                SIDocProjectType type = SIDocModelUtils.getSIDocProjectType(resource);
                if (type == SIDocProjectType.DIFF) {
                    return SIDocModelUtils.getSIDocModel().getInfraCoreProject().getDiffMetaFolder();
                } else {
                    return SIDocModelUtils.getSIDocModel().getInfraCoreProject().getMetaFolder();
                }
            }
            return null;
        }
        return project.getMetaFolder();
    }

    public IResource[] getResources(Object selectedNode, IResource resource) {
        IResource[] containers = new IResource[1];
        containers[0] = getResource(selectedNode, resource);
        return containers;
    }
}
