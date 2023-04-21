package org.xito.launcher.jnlp.xml;

import org.w3c.dom.*;

/**
 *
 * @author DRICHAN
 */
public abstract class AbstractNode {

    public String getText(Node node) {
        if (node.getFirstChild() != null) return node.getFirstChild().getNodeValue();
        return null;
    }
}
