package org.rakiura.cpn;

/**
 * Represents an output Arc in Petri Net.
 *
 *<br><br>
 * OutputArc.java<br>
 * Created: Mon Sep 25 11:49:12 2000<br>
 *
 *@author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 *@version @version@ $Revision: 1.19 $
 *@since 1.0
 */
public class OutputArc extends AbstractArc {

    private static final long serialVersionUID = 3256720688977818678L;

    /**
	 * Default expression. */
    private Expression expr = new Expression() {

        public Multiset evaluate() {
            return getMultiset();
        }
    };

    /** Default expression as String. */
    private String expressionText = "return getMultiset();";

    /**
	 * Creates a new <code>OutputArcAdapter</code> instance.
	 */
    protected OutputArc() {
    }

    /**
	 * Creates a new <code>OutputArcAdapter</code> instance.
	 * @param aFrom a <code>Transition</code> value
	 * @param aTo a <code>Place</code> value
	 */
    public OutputArc(final Transition aFrom, final Place aTo) {
        super(aFrom, aTo);
        aFrom.addOutput(this);
        aTo.addOutput(this);
    }

    public void release() {
        getPlace().removeOutput(this);
        getTransition().removeOutput(this);
    }

    /**
	 * Returns a multiset of tokens expressed by the inscription.
	 * In JFern the inscription language is Java.
	 * @return a <code>Multiset</code> value
	 */
    public Multiset expression() {
        return this.expr.evaluate();
    }

    /**
	 * Sets the expression for this arc.
	 *@param anExpr an <code>Expression</code> value
	 */
    public void setExpression(final Expression anExpr) {
        this.expr = anExpr;
    }

    /**
	 * Sets the expression text.
	 *@param aText expression text.
	 */
    public void setExpressionText(final String aText) {
        this.expressionText = aText;
    }

    /**
	 * Returns the expression text.
	 *@return this arc expression text.
	 */
    public String getExpressionText() {
        return this.expressionText;
    }

    /**
	 * Visitor pattern.
	 * @param aVisitor a <code>NetVisitor</code> value
	 * @return a <code>NetElement</code> value
	 */
    public NetElement apply(final NetVisitor aVisitor) {
        aVisitor.outputArc(this);
        return this;
    }

    /**
	 * Cloning.
	 * @return the cloned copy of this output arc.
	 */
    public Object clone() {
        return (OutputArc) super.clone();
    }

    public String toString() {
        return "OutputArc: " + this.getTransition().getName() + " � " + this.getPlace().getName();
    }

    /**
	 * To be used in expression class. Returns this arc.
	 * @return this arc.
	 */
    OutputArc getThis() {
        return this;
    }

    /**
	 * Represents an expression for the output arc.
	 *
	 *<br><br>
	 * OutputArcExpression.java<br>
	 * Created: Tue Apr 23 23:34:20 2002<br>
	 *
	 *@author  <a href="mariusz@rakiura.org">Mariusz</a>
	 *@version $Revision: 1.19 $ $Date: 2007/05/16 05:08:54 $
	 *@since 2.0
	 */
    public class Expression implements Context {

        /**
		 * Evaluates this expression.
		 * Expression returns single multiset. This method
		 * implements the actual expression on the output arc.
		 * This implementation returns an empty multiset, which
		 * is equivalent to an empty arc inscription. User
		 * needs to overwrite this method to provide some output
		 * arc functionality, token generation, and the like.
		 *@return a single multiset.
		 */
        public Multiset evaluate() {
            return new Multiset();
        }

        public void var(final String aVariable) {
            getContext().var(aVariable);
        }

        public void var(final int aNumber) {
            getContext().var(aNumber);
        }

        public void var(final String aVariable, final Class type) {
            getContext().var(aVariable, type);
        }

        public void var(final int aNumber, final Class type) {
            getContext().var(aNumber, type);
        }

        public Object get(final String aVariable) {
            return getContext().get(aVariable);
        }

        public Multiset getMultiset() {
            return getContext().getMultiset();
        }

        /**
		 * returns the outputarc to which this expression belongs to.
		 * @return the outputarc to which this expression belongs to.
		 */
        public OutputArc getThisArc() {
            return getThis();
        }
    }
}
