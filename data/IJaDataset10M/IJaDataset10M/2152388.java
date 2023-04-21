package net.sf.saxon.expr.sort;

import net.sf.saxon.expr.*;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trace.ExpressionPresenter;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.ItemType;

/**
 * A DocumentSorter is an expression that sorts a sequence of nodes into
 * document order.
 */
public class DocumentSorter extends UnaryExpression {

    private NodeOrderComparer comparer;

    public DocumentSorter(Expression base) {
        super(base);
        int props = base.getSpecialProperties();
        if (((props & StaticProperty.CONTEXT_DOCUMENT_NODESET) != 0) || (props & StaticProperty.SINGLE_DOCUMENT_NODESET) != 0) {
            comparer = LocalOrderComparer.getInstance();
        } else {
            comparer = GlobalOrderComparer.getInstance();
        }
    }

    /**
     * Get a name identifying the kind of expression, in terms meaningful to a user.
     * @return a name identifying the kind of expression, in terms meaningful to a user.
     *         The name will always be in the form of a lexical XML QName, and should match the name used
     *         in explain() output displaying the expression.
     */
    public String getExpressionName() {
        return "documentSort";
    }

    public NodeOrderComparer getComparer() {
        return comparer;
    }

    public Expression simplify(ExpressionVisitor visitor) throws XPathException {
        operand = visitor.simplify(operand);
        if ((operand.getSpecialProperties() & StaticProperty.ORDERED_NODESET) != 0) {
            return operand;
        }
        return this;
    }

    public Expression optimize(ExpressionVisitor visitor, ItemType contextItemType) throws XPathException {
        operand = visitor.optimize(operand, contextItemType);
        if ((operand.getSpecialProperties() & StaticProperty.ORDERED_NODESET) != 0) {
            return operand;
        }
        if (operand instanceof PathExpression) {
            return visitor.getConfiguration().getOptimizer().makeConditionalDocumentSorter(this, (PathExpression) operand);
        }
        return this;
    }

    public int computeSpecialProperties() {
        return operand.getSpecialProperties() | StaticProperty.ORDERED_NODESET;
    }

    /**
     * Copy an expression. This makes a deep copy.
     *
     * @return the copy of the original expression
     */
    public Expression copy() {
        return new DocumentSorter(getBaseExpression().copy());
    }

    /**
     * Promote this expression if possible
     */
    public Expression promote(PromotionOffer offer, Expression parent) throws XPathException {
        Expression exp = offer.accept(parent, this);
        if (exp != null) {
            return exp;
        } else {
            operand = doPromotion(operand, offer);
            return this;
        }
    }

    public SequenceIterator iterate(XPathContext context) throws XPathException {
        return new DocumentOrderIterator(operand.iterate(context), comparer);
    }

    public boolean effectiveBooleanValue(XPathContext context) throws XPathException {
        return operand.effectiveBooleanValue(context);
    }

    /**
     * Diagnostic print of expression structure. The abstract expression tree
     * is written to the supplied output destination.
     */
    public void explain(ExpressionPresenter out) {
        out.startElement("sortAndDeduplicate");
        out.emitAttribute("intraDocument", comparer instanceof LocalOrderComparer ? "true" : "false");
        operand.explain(out);
        out.endElement();
    }
}
