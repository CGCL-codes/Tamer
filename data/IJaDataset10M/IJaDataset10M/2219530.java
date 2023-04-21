package org.xaware.server.engine.instruction;

import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.xaware.shared.util.XAwareException;

/**
 * <p>
 * This instruction counts the number of child elements an element has and creates a new attribute whose name is the
 * xa:count attributes value, and value is the number of child elements. The attribute value cannot be empty. An empty
 * value will cause a JDOM exception.
 * </p>
 * 
 * @author jtarnowski (edited by credd)
 */
public class XACountInst extends Instruction {

    /** This instruction's name */
    private static final String INSTRUCTION_NAME = "xa:count";

    /** Attribute we will replace ourself with */
    private Attribute countAttribute = null;

    @Override
    protected void config() throws XAwareException {
    }

    /**
     * Try to create an attribute from our value
     * 
     * @see org.xaware.server.engine.instruction.Instruction#init()
     */
    @Override
    protected void init() throws XAwareException {
        final String value = instAttribute.getValue();
        try {
            countAttribute = new Attribute(value, "");
        } catch (final Exception e) {
            completed = true;
            throw new XAwareException(INSTRUCTION_NAME + " caught Exception creating Attribute named " + value + ". " + e);
        }
    }

    /**
     * Count our children and add the Attribute
     * 
     * @see org.xaware.server.engine.instruction.Instruction#execute()
     */
    @Override
    public void execute() throws XAwareException {
        completed = true;
        final Element ourElement = this.scriptNode.getElement();
        final List kids = ourElement.getChildren();
        if (kids != null && kids.size() > 0) {
            countAttribute.setValue(String.valueOf(kids.size()));
        } else {
            countAttribute.setValue("0");
        }
        ourElement.setAttribute(countAttribute);
    }

    /**
     * Tell whether or not there is more work to do.
     * 
     * @see org.xaware.server.engine.IInstruction#hasMoreExecutions()
     */
    @Override
    public boolean hasMoreExecutions() {
        return !completed;
    }
}
