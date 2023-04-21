package net.sourceforge.xcool.parserimpl;

import antlr.Token;
import antlr.Token;
import antlr.collections.AST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.sourceforge.xcool.lang.XQLParserTokenTypes;

/**
 * A more concrete implementation of a <code>QueryNode</code>.
 *
 * @author   <a href='mailto:leonchiver@yahoo.de'>Leon Chiver</a>
 */
public class XQLNode extends QueryNode {

    public XQLNode() {
    }

    public XQLNode(Token token) {
        super(token);
    }

    public Iterator getRealNodes() {
        return new OneElementIterator(this);
    }

    /**
     * Returns the filter node of this node.
     *
     * @return   the filter node of this node.
     */
    public Filter getFilterNode() {
        AST child = getFirstChild();
        while (child != null) {
            if (child instanceof Filter) {
                return (Filter) child;
            }
            child = child.getNextSibling();
        }
        return null;
    }

    public Node getResult() {
        if (isResultNode()) {
            return getPermanentResultNode();
        } else {
            QueryNode child = (QueryNode) getFirstChild();
            while (child instanceof Filter) {
                child = (QueryNode) child.getNextSibling();
            }
            return child.getResult();
        }
    }

    private XQLNode getLeafNode() {
        AST child = getFirstChild();
        XQLNode xqlNodeChild = null;
        while (child != null) {
            if (child instanceof XQLNode) {
                xqlNodeChild = (XQLNode) child;
                break;
            }
            child = child.getNextSibling();
        }
        if (xqlNodeChild != null) {
            if (xqlNodeChild.getLeafNode() == null) {
                return xqlNodeChild;
            } else {
                return xqlNodeChild.getLeafNode();
            }
        } else {
            return null;
        }
    }

    public String toString() {
        return super.toString() + (isRoot() ? "[root]" : "");
    }

    public void addChild(AST ast) {
        XQLNode leafNode = getLeafNode();
        if (ast instanceof Filter) {
            XQLNode searchForFilterNode;
            if (leafNode != null) {
                searchForFilterNode = leafNode;
            } else {
                searchForFilterNode = this;
            }
            Filter currentFilter = searchForFilterNode.getFilterNode();
            if (currentFilter != null) {
                AST currentFilterCondition = currentFilter.getFirstChild();
                AST newFilterCondition = ((Filter) ast).getFirstChild();
                AndOperator andOperator = new AndOperator(new Token(XQLParserTokenTypes.AND, "$and$"));
                andOperator.setText("$and$");
                andOperator.addChild(currentFilterCondition);
                andOperator.addChild(newFilterCondition);
                currentFilter.setFirstChild(andOperator);
                return;
            }
        }
        if (leafNode != null) {
            leafNode.addChild(ast);
        } else {
            super.addChild(ast);
        }
    }
}
