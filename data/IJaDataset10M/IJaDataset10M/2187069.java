package net.sourceforge.xcool.parserimpl;

import antlr.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Base class for a <code>XQL</code> operator.
 *
 * @author   <a href='mailto:leonchiver@yahoo.de'>Leon Chiver</a>
 */
public abstract class XQLOperator extends QueryNode {

    public XQLOperator() {
    }

    public XQLOperator(Token token) {
        super(token);
    }

    /**
     * Returns an iterator over the node's children real children.
     *
     * @return   an iterator over the node's children real children.
     */
    public Iterator getRealNodes() {
        ArrayList realNodes = new ArrayList();
        QueryNode child = (QueryNode) getFirstChild();
        while (child != null) {
            Iterator childRealNodesIterator = child.getRealNodes();
            while (childRealNodesIterator.hasNext()) {
                realNodes.add(childRealNodesIterator.next());
            }
            child = (QueryNode) child.getNextSibling();
        }
        return realNodes.iterator();
    }
}
