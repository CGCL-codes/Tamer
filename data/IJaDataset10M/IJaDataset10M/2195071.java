package cnaf.sidoc.ide.core.search.origins;

import java.util.Collection;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.EqualsStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.w3c.dom.Node;
import cnaf.sidoc.ide.core.model.ISIDocBaseCollectionProject;
import cnaf.sidoc.ide.core.util.SIDocModelUtils;

public class EqualsStringQueryBuilderIgnoreRef extends EqualsStringQueryBuilder {

    public static IStringQueryBuilder INSTANCE = new EqualsStringQueryBuilderIgnoreRef();

    public String getId() {
        return null;
    }

    @Override
    protected void build(StringBuilder xpath, String[] targetNodes, int startIndex, Object selectedNode) {
        super.build(xpath, targetNodes, startIndex, selectedNode);
        IFile file = DOMUtils.getFile((IDOMNode) selectedNode);
        String collectionURI = file.getProject().getName();
        ISIDocBaseCollectionProject project = SIDocModelUtils.getBaseCollectionProject(file.getProject());
        if (project != null) {
            xpath.append("[@col=\"");
            xpath.append(collectionURI);
            xpath.append("\"");
            Collection<ISIDocBaseCollectionProject> subCollections = project.getSubCollections();
            for (ISIDocBaseCollectionProject subCollection : subCollections) {
                xpath.append("or @col=\"");
                xpath.append(subCollection.getProject().getName());
                xpath.append("\"");
            }
            xpath.append("]");
        }
    }
}
