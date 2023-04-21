package fido.grammar;

import java.util.*;
import fido.db.WordSense;

/**
 * Each SentenceThread contains a GrammarStack that is used to push and
 * pop StackElements.  Each StackElement represents a grammar expression.
 * Grammar will push a new open link as a new StackElement onto the
 * GrammarStack.  Each close link will attempt to find the next matching
 * link on the GrammarStack.<P>
 *
 * Internally, the Grammar stack has a StackElement for the head of
 * the stack and a hold element.  The hold element will be the next
 * element to push onto the stack.  If the element is immediately
 * pushed onto the stack, it is possible the Grammar module will
 * match a close link with an open link in the same word.
 *
 * @see fido.grammar.StackElement
 * @see fido.grammar.Grammar
 */
public class GrammarStack {

    private Stack stack;

    private StackElement hold;

    /**
	 * Creates a new empty GrammarStack instance.
	 */
    public GrammarStack() {
        stack = new Stack();
        hold = null;
    }

    /**
	 * Puts the hold element onto the top of the stack.
	 */
    public void push() {
        if (hold == null) return;
        stack.push(hold);
        hold = null;
    }

    /**
	 * Starts with the top of the stack and looks for an open link
	 * element matching the parameter string.  The element matches
	 * if the head element contains a string that matches the parameter.
	 * If the head element does not match, the head node may be a
	 * multi-match node.  If the multi-node has already been used,
	 * it is legal to remove the node from the stack and continue
	 * to look for a match.<P>
	 * If the node matches, and it is a multi-node, the node is set
	 * to being used at least once.  If it is not a multi-node, it
	 * is removed from the stack.
	 *
	 * @param str String value of the open link to look for on the stack
	 *
	 * @return The WordSense for the element that matches the close
	 *         link string in the parameter.
	 *
	 * @see NodeExpression#setMultiNodeUsed()
	 */
    public WordSense traverseStack(String str) {
        if (stack.empty() == true) return null;
        StackElement element = (StackElement) stack.peek();
        NodeExpression expression = element.getExpression();
        while (true) {
            if (expression.getExpressionName().equals(str) == true) {
                if (expression.isNodeMulti() == true) {
                    expression.setMultiNodeUsed();
                    return element.getNodeWordSense();
                } else {
                    element.removeExpression();
                    WordSense ws = element.getNodeWordSense();
                    if (element.hasMoreExpressions() == true) stack.pop();
                    return ws;
                }
            } else {
                if ((expression.isNodeMulti() == false) && (expression.isMultiNodeUsed() == false)) {
                    return null;
                }
                element.removeExpression();
                if (element.hasMoreExpressions() == true) stack.pop();
                expression = element.getExpression();
            }
        }
    }

    /**
	 * Stores an open link on the stack in the hold area.  Links
	 * in the hold area cannot be used to match close links.
	 * This prevents an open link to match a close link 
	 * within the same word.
	 *
	 * @param exp Expression to hold.
	 * @param sense WordSense to store along with the expression
	 */
    public void holdExpression(NodeExpression exp, WordSense sense) {
        if (hold == null) hold = new StackElement(sense);
        hold.addOpenExpression(exp);
    }

    /**
	 * Tests the stack to be empty.  Empty is defined as either:
	 * <UL>
	 * <LI> there are no elements on the stack
	 * <LI> if the head node is a multi-node, it has already
	 *      been used, there are no more expressions in the node, and there
	 *      is no more elements on the stack after the multi-node.
	 * </UL>
	 *
	 * @return True if the stack is empty.  False otherwise.
	 *
	 * @see StackElement#allExpressionsUsed()
	 */
    public boolean isEmpty() {
        if (stack.empty() == true) return true;
        StackElement element = (StackElement) stack.peek();
        if ((element.allExpressionsUsed() == true) && (stack.size() == 1)) {
            return true;
        } else return false;
    }

    /**
	 * Returns the element at the top of the stack.  This will
	 * not return the hold element.
	 *
	 * @return StackElement at the top of the stack.
	 */
    public StackElement getHeadElement() {
        return (StackElement) stack.peek();
    }

    /**
	 * Returns an iterator over the StackElements in this stack.
	 *
	 * @return Iterator for the StackElements
	 */
    public Iterator elementIterator() {
        return stack.iterator();
    }

    /**
	 * Makes a deep copy of the GrammarStack.  This needs to be
	 * a deep copy because elements of the stack are modified
	 * during processing.  Creating a new object separates the
	 * data between SentenceThreads.
	 *
	 * @return A new copy of the GrammarStack.
	 */
    public GrammarStack cloneGrammarStack() {
        GrammarStack gs = new GrammarStack();
        for (Iterator it = stack.iterator(); it.hasNext(); ) {
            StackElement element = (StackElement) it.next();
            StackElement clonedElement = element.cloneStackElement();
            gs.stack.add(clonedElement);
        }
        if (hold == null) gs.hold = null; else gs.hold = hold.cloneStackElement();
        return gs;
    }
}
