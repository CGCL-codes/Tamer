package org.activebpel.rt.bpel.xpath.ast;

import java.util.Iterator;

/**
 * Used to traverse the XPath AST.
 */
public class AeXPathTreeTraverser {

    /** The xpath node visitor. */
    private IAeXPathNodeVisitor mVisitor;

    /**
    * Simple constructor.
    * 
    * @param aVisitor
    */
    public AeXPathTreeTraverser(IAeXPathNodeVisitor aVisitor) {
        setVisitor(aVisitor);
    }

    /**
    * Traverse the entire tree using the given visitor.
    * 
    * @param aXPathNode
    */
    public void traverse(AeAbstractXPathNode aXPathNode) {
        aXPathNode.accept(getVisitor());
        for (Iterator iter = aXPathNode.getChildren().iterator(); iter.hasNext(); ) {
            AeAbstractXPathNode child = (AeAbstractXPathNode) iter.next();
            traverse(child);
        }
    }

    /**
    * @return Returns the visitor.
    */
    protected IAeXPathNodeVisitor getVisitor() {
        return mVisitor;
    }

    /**
    * @param aVisitor The visitor to set.
    */
    protected void setVisitor(IAeXPathNodeVisitor aVisitor) {
        mVisitor = aVisitor;
    }
}
