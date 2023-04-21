package cnaf.sidoc.ide.core.search.reftypes;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import cnaf.sidoc.ide.core.search.MetaFolderContainerProvider;

public class RefTypesQuerySpecification implements IXMLSearchRequestorProvider, IResourceProvider {

    public IXMLSearchRequestor getRequestor() {
        return RefTypesSearchRequestor.INSTANCE;
    }

    public IResource getResource(Object selectedNode, IResource resource) {
        return MetaFolderContainerProvider.INSTANCE.getResource(selectedNode, resource);
    }
}
