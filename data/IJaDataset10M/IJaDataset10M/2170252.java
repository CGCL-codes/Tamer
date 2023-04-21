package cnaf.sidoc.ide.core.search.docflows.query;

import org.eclipse.wst.xml.search.core.queryspecifications.requestor.IXMLSearchRequestor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import cnaf.sidoc.ide.core.search.docobjecttypes.DocObjectTypesSearchRequestor;

public abstract class AbstractX2DocObjectType2StateType extends AbstractStringQueryBuilderX2Y2StateType {

    public AbstractX2DocObjectType2StateType(boolean fullMatch) {
        super(fullMatch);
    }

    @Override
    protected String getQuery(Node selectedNode) {
        Element docobjectType = getDocobjectType(selectedNode);
        if (docobjectType == null) {
            return null;
        }
        return "/docobject-types/docobject-type[@uri=\"" + docobjectType.getAttribute(URI_ATTR) + "\"]";
    }

    protected IXMLSearchRequestor getRequestor() {
        return DocObjectTypesSearchRequestor.INSTANCE;
    }

    protected abstract Element getDocobjectType(Node selectedNode);
}
