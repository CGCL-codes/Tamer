package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.ThreadInfo;

/**
 * DOCUMENT ME!
 */
public class IFNULL extends IfInstruction {

    public boolean popConditionValue(ThreadInfo ti) {
        return (ti.pop() == -1);
    }

    public int getByteCode() {
        return 0xC6;
    }
}
