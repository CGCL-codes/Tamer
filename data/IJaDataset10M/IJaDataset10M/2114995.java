package cnaf.sidoc.ide.publishers.core.internal.resources;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestor;
import org.eclipse.wst.xml.search.core.resource.IResourceRequestorProvider;
import org.eclipse.wst.xml.search.core.resource.IURIResolver;
import org.eclipse.wst.xml.search.core.resource.IURIResolverProvider;
import org.eclipse.wst.xml.search.core.resource.ResourceBaseURIResolver;
import org.eclipse.wst.xml.search.core.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import cnaf.sidoc.ide.core.SIDocXMLConstants;
import cnaf.sidoc.ide.core.model.ISIDocBaseCollectionProject;
import cnaf.sidoc.ide.core.util.SIDocModelUtils;

public class XSLFileOrFolderQuerySpecification implements IResourceRequestorProvider, IResourceProvider, IURIResolverProvider, SIDocXMLConstants {

    public IResourceRequestor getRequestor() {
        return WebResourcesRequestor.INSTANCE;
    }

    public IResource getResource(Object selectedNode, IResource resource) {
        ISIDocBaseCollectionProject project = SIDocModelUtils.getBaseCollectionProject(resource.getProject());
        if (project == null) {
            return null;
        }
        IContainer resourcesFolder = project.getXSLTFolder();
        Element xslElt = ((Attr) selectedNode).getOwnerElement();
        Element renderingResourceElt = (Element) xslElt.getParentNode();
        if (renderingResourceElt != null) {
            String baseDir = renderingResourceElt.getAttribute(XSL_BASEDIR_ATTR);
            if (!StringUtils.isEmpty(baseDir)) {
                IFolder f = (IFolder) ResourceBaseURIResolver.INSTANCE.getResource(resourcesFolder, baseDir, IFolder.class);
                if (f != null) {
                    resourcesFolder = f;
                }
            }
        }
        return resourcesFolder;
    }

    public IURIResolver getURIResolver(IFile file, Object selectedNode) {
        return ResourceBaseURIResolver.INSTANCE;
    }
}
