package gov.nasa.jpf.doc.stacksim;

import java.util.Stack;

public class StackSimulator implements Cloneable {

    protected Stack<StackValue> stack = new Stack<StackValue>();

    public StackSimulator clone() throws CloneNotSupportedException {
        StackSimulator retval;
        retval = (StackSimulator) super.clone();
        retval.stack = (Stack<StackValue>) this.stack.clone();
        return retval;
    }

    public void push(Object value) {
        if (value == null) stack.push(new UnknownValue()); else stack.push(new KnownValue(value));
    }

    public void pushUnknown() {
        pushUnknown(1);
    }

    public void pushUnknown(int size) {
        stack.push(new UnknownValue(size));
    }

    public Object pop() {
        return stack.pop().getValue();
    }

    public Object peek() {
        return stack.peek().getValue();
    }

    public void pushUnknownWords(int words) {
        for (int i = 0; i < words; i++) stack.push(new UnknownValue());
    }

    public void popWords(int words) {
        int ctr = 0;
        while (ctr < words) {
            ctr += stack.pop().size();
        }
        if (ctr != words) {
            throw new IllegalArgumentException("Illegal number of words popped");
        }
    }

    public int size() {
        int size = 0;
        for (StackValue frame : stack) {
            size += frame.size();
        }
        return size;
    }

    public Object getValueFromTop(int distanceFromTop) {
        int position = stack.size() - distanceFromTop - 1;
        return stack.get(position).getValue();
    }
}
