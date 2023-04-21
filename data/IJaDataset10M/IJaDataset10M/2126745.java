package org.broadleafcommerce.extensibility.context.merge.handlers;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This adapter class allows the developer to create a merge handler
 * instance and only override a subset of the functionality, instead of
 * having to provide an independent, full implementation of the MergeHandler
 * interface.
 * 
 * @author jfischer
 *
 */
public class MergeHandlerAdapter implements MergeHandler {

    public MergeHandler[] getChildren() {
        return null;
    }

    public String getName() {
        return null;
    }

    public int getPriority() {
        return 0;
    }

    public String getXPath() {
        return null;
    }

    public Node[] merge(NodeList nodeList1, NodeList nodeList2, Node[] exhaustedNodes) {
        return null;
    }

    public void setChildren(MergeHandler[] children) {
    }

    public void setName(String name) {
    }

    public void setPriority(int priority) {
    }

    public void setXPath(String xpath) {
    }
}
