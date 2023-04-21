package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.ArrayIndexOutOfBoundsExecutiveException;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.ThreadInfo;

/**
 * abstraction for long array loads
 */
public abstract class LongArrayLoadInstruction extends ArrayLoadInstruction {

    protected void push(ThreadInfo th, ElementInfo e, int index) throws ArrayIndexOutOfBoundsExecutiveException {
        e.checkLongArrayBounds(index);
        th.longPush(e.getLongElement(index));
    }

    protected int getElementSize() {
        return 2;
    }
}
