package org.xaware.server.engine.instruction;

import org.xaware.server.engine.enums.YesNoValue;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareInvalidEnumerationValueException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * <p>This class is the base class for the xa:include Instruction. </p>
 * <p>The xa:include attribute is commonly used with error
 * handling elements. Using the xa:include="no" attribute and value in an element ensures that the element is processed
 * only when it is explicitly called by another element. For example, you can use xa:include="no" in an error message
 * element to ensure that the element is only processed when an error occurs and the element is explicitly called. If
 * the element is not explicitly called, the error message element is ignored. When an element contains the
 * xa:include="yes" attribute and value, the element is processed whether or not it is explicitly called by another
 * element. Take care when using this attribute in the BizDocument file�s root element, as this can cause invalid XML.</p>
 * 
 * @author Tim Uttormark edited by credd)
 */
public abstract class XAIncludeBaseInst extends Instruction {

    protected static XAwareLogger logger = XAwareLogger.getXAwareLogger(XAIncludeBaseInst.class.getName());

    protected YesNoValue includeValue = null;

    /**
     * This method performs configuration specific to each type of Instruction.
     * 
     * @throws XAwareException
     */
    @Override
    protected final void config() throws XAwareException {
        final String attrValue = instAttribute.getValue();
        try {
            includeValue = YesNoValue.getYesNoAttributeValue(attrValue);
        } catch (final XAwareInvalidEnumerationValueException e) {
            cleanupAttributes();
            final String errMsg = "Invalid value for xa:include attribute: " + attrValue;
            logger.severe(errMsg, "XAIncludeBaseInst", "setupInstruction");
            throw new XAwareException(errMsg, e);
        }
    }

    /**
     * Return false, because all the work is done in init().
     * 
     * @see org.xaware.server.engine.IInstruction#hasMoreExecutions()
     */
    @Override
    public final boolean hasMoreExecutions() {
        return false;
    }

    /**
     * All the work has been done in init().
     * 
     * @see org.xaware.server.engine.IInstruction#execute()
     */
    @Override
    public final void execute() throws XAwareException {
        throw new XAwareException("Method not implemented");
    }

    /**
     * Returns a boolean indicating whether this instruction causes the associated Element to not be visible for
     * purposes of streaming out. If this Instruction has nothing to do with visibility, false is returned. True is
     * returned only ion a few select cases, e.g, xa:visible="no" or xa:remove="yes".
     * 
     * @return a boolean indicating whether this instruction causes the associated Element to not be visible for
     *         purposes of streaming out.
     */
    @Override
    public final boolean isNotVisibleForStreamingOut() {
        return (includeValue == YesNoValue.NO);
    }

    /**
     * Pop this node off of the call stack so that no further
     * instructions are executed on it or its children.
     * 
     * @throws XAwareException
     *             if an error occurs completing the ScriptNode
     */
    protected void finishScriptNode() throws XAwareException {
        scriptNode.getContext().getRegistry().finishNode(false);
    }
}
