package jdvi;

/**
 * This is a stack for integers. its used by the main dvi page rendering
 * @author <a href="mailto:timh@sfb288.math.tu-berlin.de">Tim Hoffmann</a>
 * @version $Revision: 1.2 $
 */
public final class IntStack {

    private int stack[];

    /**
     * The variable <code>value</code> holds the current value of the stack.
     *
     */
    public int value;

    int pointer = -1;

    /**
     * Creates a new <code>IntStack</code> instance.
     *
     * @param size the capacity of the stack.
     * @param init is the first <code>int</code> value to hold.
     */
    public IntStack(int size, int init) {
        value = init;
        stack = new int[size];
    }

    /**
     *  <code>push</code> pushes the current value on the stack.
     *
     */
    public void push() {
        pointer++;
        stack[pointer] = value;
    }

    /**
     * <code>pop</code> pops the last pushed value from the stack. It
     * will be stores in the value field and returned.
     *
     * @return an <code>int</code>: the now current value.
     */
    public int pop() {
        value = stack[pointer];
        pointer--;
        return value;
    }

    /**
     * <code>empty</code> empties the stack.
     *
     */
    public void empty() {
        pointer = -1;
    }

    /**
     * <code>isEmpty</code> tests wether the stack is empty.
     *
     * @return true if the stack is empty.
     */
    public boolean isEmpty() {
        if (pointer == -1) return true; else return false;
    }
}
