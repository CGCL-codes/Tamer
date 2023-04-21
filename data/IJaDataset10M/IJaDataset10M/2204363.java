package org.maverickdbms.basic;

import java.util.EmptyStackException;

/**
* Class to handle the current label to be executed
*/
public class mvLabelStack {

    final int STACK_START_SIZE = 10;

    private int[] stack = new int[STACK_START_SIZE];

    private int top = -1;

    public void push(int value) {
        if (++top >= stack.length) {
            int[] old = stack;
            stack = new int[2 * stack.length];
            System.arraycopy(old, 0, stack, 0, old.length);
        }
        stack[top] = value;
    }

    public final void pop() {
        if (top-- < 0) {
            throw new EmptyStackException();
        }
    }

    public final int peek() {
        try {
            return stack[top];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EmptyStackException();
        }
    }

    public final boolean empty() {
        return (top < 0);
    }
}
