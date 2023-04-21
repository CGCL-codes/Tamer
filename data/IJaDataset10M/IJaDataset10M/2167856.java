package org.xaware.server.engine.instruction;

import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.xaware.server.engine.controller.ScriptProcessor;
import org.xaware.server.engine.exceptions.XAwareConfigurationException;
import org.xaware.server.utils.SwitchHelper;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * <p>This class is the implementation for the xa:if/xa:elseif/xa:else element instuction.</p>
 * <p>This instruction implements an if-then/else-if/else instruction.  The xa:if clause and xa:elseif clause each
 * include an xa:condition attribute, which is evaluated as a boolean expression.  The condition is examined to determine if
 * it contains the value 'true' or 'false', either contained as a literal string, or evaluated by a functoid or substitution.
 * If so, the string value is converted directly to boolean.  Otherwise, the string is taken as a boolean expression and
 * sent to an expression processor.  The expression is passed to an expression evaluator (currently based on the XPath expression
 * evaluator), and can contain boolean operators to test for equality, greater than, less than, as well as complex expressions
 * joined by 'AND' and 'OR'.
 * </p>
 * <p>
 * The structure for this instruction includes a mandatory xa:if (with mandatory xa:condition attribute), followed by any number of 
 * optional siblings 'xa:elseif' (each with mandatory xa:condtion attribute), followed by an optional sibling 'xa:else'.
 * The conditions are checked sequentially until one of them evaluates to 'true'.  Once found, the children of that clause
 * are preserved, and promoted one level, effectively replacing the entire instruction.  If none evaluate to 'true', then
 * the xa:else clause, if present, is selected, and its children are promoted one level, effectively replacing the entire instruction.
 * </p>
 * 
 * @author Kirstan Vandersluis
 */
public class XAIfThenInst extends Instruction {

    private static XAwareLogger logger = XAwareLogger.getXAwareLogger(ScriptProcessor.class.getName());

    private static final String className = "XAIfThenInst";

    /**
     * Configuration.
     * 
     * @see org.xaware.server.engine.instruction.Instruction#config()
     */
    @Override
    protected void config() throws XAwareException {
    }

    /**
     * Initialization method does most of the processing.
     * <p>
     * Evaluate the sequence of conditions in if and elseif siblings, looking for a boolean true
     * condition.  Only that element's children are preserved.  If no condition is true, and an else
     * element exists, the children of else are preserved.  All other elements are removed.
     * After the proper element is selected, the children are shifted out to effectively replace the xa:if
     * instruction.
     * <p>
     * 
     * @see org.xaware.server.engine.instruction.Instruction#init()
     */
    @Override
    public void init() throws XAwareException {
        final Element elem = scriptNode.getElement();
        Element parent = elem.getParentElement();
        int index = parent.getChildren().indexOf(elem);
        int maxIndex = parent.getChildren().size() - 1;
        int origIndex = index;
        while (true) {
            if (index > maxIndex) {
                break;
            }
            Element ifthenElem = (Element) parent.getChildren().get(index);
            if (ifthenElem.getNamespace().equals(XAwareConstants.xaNamespace) && (ifthenElem.getName().equals("if") || ifthenElem.getName().equals("elseif"))) {
                String condition = ifthenElem.getAttributeValue("condition", XAwareConstants.xaNamespace);
                if (condition == null) {
                    String sErr = "xa:" + ifthenElem.getName() + " has no xa:condition";
                    logger.warning(sErr, className, "init");
                    throw new XAwareConfigurationException(sErr);
                }
                if (index > origIndex) {
                    condition = scriptNode.substitute(condition, null);
                }
                boolean bCondition;
                if (condition.equalsIgnoreCase("true")) {
                    bCondition = true;
                } else if (condition.equalsIgnoreCase("false")) {
                    bCondition = false;
                } else {
                    String xpathExpr = "%xa-current::(" + condition + ")%";
                    String exprResult = scriptNode.substitute(xpathExpr, null);
                    if (exprResult.equalsIgnoreCase("true")) {
                        bCondition = true;
                    } else {
                        bCondition = false;
                    }
                }
                if (!bCondition) {
                    ifthenElem.removeContent();
                    index++;
                    continue;
                } else {
                    promoteChildren(ifthenElem, elem);
                    break;
                }
            } else if (ifthenElem.getNamespace().equals(XAwareConstants.xaNamespace) && ifthenElem.getName().equals("else")) {
                promoteChildren(ifthenElem, elem);
                break;
            } else {
                break;
            }
        }
        index = parent.getChildren().indexOf(elem);
        Element ifthenElem;
        do {
            parent.getChildren().remove(index);
            if (index < parent.getChildren().size()) ifthenElem = (Element) parent.getChildren().get(index); else break;
        } while (ifthenElem.getNamespace().equals(XAwareConstants.xaNamespace) && (ifthenElem.getName().equals("elseif") || ifthenElem.getName().equals("else")));
        this.completed = true;
    }

    /**
     * hasMoreExecutions always returns false, because the real work is done in init().
     * 
     * @see org.xaware.server.engine.IInstruction#hasMoreExecutions()
     */
    @Override
    public boolean hasMoreExecutions() {
        return false;
    }

    /**
     * Execute. This method should never be called, because the real work is done in init().
     * 
     * @see org.xaware.server.engine.instruction.Instruction#execute()
     */
    @Override
    public void execute() throws XAwareException {
        throw new XAwareException("Method not implemented");
    }

    /**
     * Get the name of this instruction
     * 
     * @see org.xaware.server.engine.IInstruction#getName()
     */
    @Override
    public String getName() {
        return "xa:if";
    }

    /**
     * promote children to a specific location
     * @param fromElem - children of this element will be promoted
     * @param beforeElem - location to move to, immediately before this element
     */
    private void promoteChildren(Element fromElem, Element beforeElem) {
        Element parentElem = beforeElem.getParentElement();
        int origIndex = parentElem.getChildren().indexOf(beforeElem);
        List<Element> children = fromElem.getChildren();
        Iterator<Element> iter = children.iterator();
        while (iter.hasNext()) {
            Element child = (Element) iter.next();
            iter.remove();
            parentElem.getChildren().add(origIndex, child);
            origIndex++;
        }
    }
}
