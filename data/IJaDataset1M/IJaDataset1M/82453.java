package ro.codemart.installer.core.utils.xml;

import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import ro.codemart.installer.core.InstallerException;

/**
 * Removes a node from an XML file, given the path and other info
 */
public class RemoveNodeOperation extends XmlOperation {

    private String path;

    public RemoveNodeOperation(Document document, JXPathContext jxPathContext, String path) {
        super(document, jxPathContext);
        this.path = path;
    }

    @Override
    public void execute() throws InstallerException {
        super.execute();
        Node existNode = XMLUtilities.getNode(document, path);
        if (existNode != null && existNode.getParentNode() != null) {
            Node parent = existNode.getParentNode();
            parent.removeChild(existNode);
        }
    }
}
