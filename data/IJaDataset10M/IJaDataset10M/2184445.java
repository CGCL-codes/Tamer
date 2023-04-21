package cnaf.sidoc.ide.core.search.docflows;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.container.IMultiResourceProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilderProvider;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestorProvider;
import cnaf.sidoc.ide.core.search.MultiMetaFoldersLocalAndParentFeedrationContainerProvider;
import cnaf.sidoc.ide.core.search.docflows.query.SQBRetrievalRequest2AssetType2StateType;
import cnaf.sidoc.ide.core.search.docflows.query.SQBRetrievalRequest2DocObjectType2StateType;

public class RetrievalRequest2AssetType2StateTypeQuerySpecification implements IXMLSearchRequestorProvider, IMultiResourceProvider, IStringQueryBuilderProvider {

    public IXMLSearchRequestor getRequestor() {
        return DocflowsSearchRequestor.INSTANCE;
    }

    public IResource[] getResources(Object selectedNode, IResource resource) {
        String collectionURI = SQBRetrievalRequest2DocObjectType2StateType.INSTANCE_EQUALS.getCollectionURI((IDOMNode) selectedNode);
        return MultiMetaFoldersLocalAndParentFeedrationContainerProvider.INSTANCE.getResources((IDOMNode) selectedNode, collectionURI);
    }

    public IStringQueryBuilder getEqualsStringQueryBuilder() {
        return SQBRetrievalRequest2AssetType2StateType.INSTANCE_EQUALS;
    }

    public IStringQueryBuilder getStartsWithStringQueryBuilder() {
        return SQBRetrievalRequest2AssetType2StateType.INSTANCE_STARTS_WITH;
    }
}
