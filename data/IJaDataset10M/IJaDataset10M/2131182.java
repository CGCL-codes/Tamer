package org.nomadpim.core.xmldb;

import org.apache.xindice.xml.TextWriter;
import org.easymock.IArgumentMatcher;
import org.w3c.dom.Node;

/**
 * @tag todo.documentation: type description
 * 
 * @author <a href="mailto:lars.grammel@gmail.com">Lars Grammel</a>
 * @version $Revision$, $Date$
 */
public class XMLNodeEqualsMatcher implements IArgumentMatcher {

    private final Node node;

    public XMLNodeEqualsMatcher(Node node) {
        this.node = node;
    }

    public boolean matches(Object argument) {
        return node.isEqualNode((Node) argument);
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append(TextWriter.toString(node));
    }
}
