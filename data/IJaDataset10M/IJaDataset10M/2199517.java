package net.sf.saxon.functions;

import net.sf.saxon.expr.*;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.Axis;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.pattern.AnyNodeTest;

/**
* Implement the XPath 2.0 root() function
*/
public class Root extends SystemFunction {

    /**
    * Simplify and validate.
     * @param visitor an expression visitor
     */
    public Expression simplify(ExpressionVisitor visitor) throws XPathException {
        useContextItemAsDefault();
        return simplifyArguments(visitor);
    }

    /**
    * Get the static properties of this expression (other than its type). The result is
    * bit-significant. These properties are used for optimizations. In general, if
    * property bit is set, it is true, but if it is unset, the value is unknown.
    */
    public int computeSpecialProperties() {
        int prop = StaticProperty.ORDERED_NODESET | StaticProperty.SINGLE_DOCUMENT_NODESET | StaticProperty.NON_CREATIVE;
        if ((getNumberOfArguments() == 0) || (argument[0].getSpecialProperties() & StaticProperty.CONTEXT_DOCUMENT_NODESET) != 0) {
            prop |= StaticProperty.CONTEXT_DOCUMENT_NODESET;
        }
        return prop;
    }

    /**
     * Add a representation of this expression to a PathMap. The PathMap captures a map of the nodes visited
     * by an expression in a source tree.
     * <p/>
     * <p>The default implementation of this method assumes that an expression does no navigation other than
     * the navigation done by evaluating its subexpressions, and that the subexpressions are evaluated in the
     * same context as the containing expression. The method must be overridden for any expression
     * where these assumptions do not hold. For example, implementations exist for AxisExpression, ParentExpression,
     * and RootExpression (because they perform navigation), and for the doc(), document(), and collection()
     * functions because they create a new navigation root. Implementations also exist for PathExpression and
     * FilterExpression because they have subexpressions that are evaluated in a different context from the
     * calling expression.</p>
     *
     * @param pathMap     the PathMap to which the expression should be added
     * @param pathMapNodeSet
     * @return the pathMapNode representing the focus established by this expression, in the case where this
     *         expression is the first operand of a path expression or filter expression. For an expression that does
     *         navigation, it represents the end of the arc in the path map that describes the navigation route. For other
     *         expressions, it is the same as the input pathMapNode.
     */
    public PathMap.PathMapNodeSet addToPathMap(PathMap pathMap, PathMap.PathMapNodeSet pathMapNodeSet) {
        AxisExpression root = new AxisExpression(Axis.ANCESTOR_OR_SELF, AnyNodeTest.getInstance());
        root.setContainer(getContainer());
        return root.addToPathMap(pathMap, pathMapNodeSet);
    }

    /**
    * Evaluate in a general context
    */
    public Item evaluateItem(XPathContext c) throws XPathException {
        NodeInfo start = (NodeInfo) argument[0].evaluateItem(c);
        if (start == null) {
            return null;
        }
        return start.getRoot();
    }
}
