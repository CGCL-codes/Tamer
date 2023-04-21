package net.sf.saxon.expr;

import net.sf.saxon.instruct.SlotManager;
import net.sf.saxon.om.ValueRepresentation;

/**
 * This class represents a stack frame holding details of the variables used in a function or in
 * an XSLT template.
 */
public class StackFrame {

    protected SlotManager map;

    protected ValueRepresentation[] slots;

    public StackFrame(SlotManager map, ValueRepresentation[] slots) {
        this.map = map;
        this.slots = slots;
    }

    public SlotManager getStackFrameMap() {
        return map;
    }

    public ValueRepresentation[] getStackFrameValues() {
        return slots;
    }

    public static final StackFrame EMPTY = new StackFrame(SlotManager.EMPTY, ValueRepresentation.EMPTY_VALUE_ARRAY);
}
