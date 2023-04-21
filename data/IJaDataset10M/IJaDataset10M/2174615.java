package cnaf.sidoc.ide.core.search.categorizers;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import cnaf.sidoc.ide.core.search.MultiMetaSubCollectionFoldersContainerProvider;

public class CategorizersLocalAndFederationQuerySpecification implements IXMLSearchRequestorProvider, IMultiResourceProvider {

    public IXMLSearchRequestor getRequestor() {
        return CategorizersSearchRequestor.INSTANCE;
    }

    public IResource[] getResources(Object selectedNode, IResource resource) {
        return MultiMetaSubCollectionFoldersContainerProvider.INSTANCE.getResources(selectedNode, resource);
    }
}
