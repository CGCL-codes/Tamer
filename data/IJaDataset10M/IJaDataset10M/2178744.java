package fido.grammar;

import java.util.*;
import fido.db.WordSense;

/**
 * Represents all of the grammar expressions for a word.  If
 * the word in the input has any open links, Grammar creates 
 * a new StackElement to be placed on the GrammarStack.
 *
 * @see GrammarStack
 */
public class StackElement implements Cloneable {

    private Vector openExpressions;

    private WordSense sense;

    /**
	 * Creates a new StackElement.  Each element contains one to many
	 * open Expresssions and a WordSense.
	 *
	 * @param sense WordSense for the element.
	 */
    public StackElement(WordSense sense) {
        this.sense = sense;
        openExpressions = new Vector();
    }

    /**
	 * Add an open expression.
	 * 
	 * @param exp Grammar expression for the element.
	 * @param sense WordSense for the element.
	 */
    public void addOpenExpression(NodeExpression exp) {
        openExpressions.add(exp);
    }

    /**
	 * Get the first element of the open expression queue.
	 * 
	 * @return First expression in queue
	 */
    public NodeExpression getExpression() {
        return (NodeExpression) openExpressions.elementAt(0);
    }

    /**
	 * Returns an iterator over the expressions in this element.
	 *
	 * @return Iterator for the expressions
	 */
    public Iterator expressionIterator() {
        return openExpressions.iterator();
    }

    /**
	 * Tests for an open expressions held by this element.
	 *
	 * @return True if any open expressions exist.  False otherwise.
	 */
    public boolean hasMoreExpressions() {
        return openExpressions.isEmpty();
    }

    /**
	 * Tests if:
	 * <UL>
	 * <LI> There are no more expressions
	 * <LI> The only expression is a multi-node and it has been used
	 * <UL>
	 *
	 * @return True if there are no more expressions or the only expression 
	 *         is used.  False otherwise.
	 *
	 * @see NodeExpression#isNodeMulti()
	 * @see NodeExpression#isMultiNodeUsed()
	 */
    public boolean allExpressionsUsed() {
        if (openExpressions.isEmpty() == false) {
            NodeExpression exp = (NodeExpression) openExpressions.elementAt(0);
            if ((exp.isNodeMulti() == true) && (exp.isMultiNodeUsed() == true) && (openExpressions.size() == 1)) {
                return true;
            } else return false;
        } else return true;
    }

    /**
	 * Removes the first element of the open expression queue.
	 */
    public void removeExpression() {
        openExpressions.remove(0);
    }

    /**
	 * Returns the WordSense associated with this element.
	 *
	 * @return The WordSense associated with this element.
	 */
    public WordSense getNodeWordSense() {
        return sense;
    }

    /**
	 * Creates a deep copy of this element.
	 *
	 * @return A new copy of this element.
	 */
    public StackElement cloneStackElement() {
        StackElement node = new StackElement(sense);
        for (Iterator it = openExpressions.iterator(); it.hasNext(); ) {
            NodeExpression exp = (NodeExpression) it.next();
            NodeExpression clone = exp.cloneExpression();
            node.addOpenExpression(clone);
        }
        return node;
    }
}
