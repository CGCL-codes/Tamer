package net.sourceforge.freejava.text.lop;

import java.lang.reflect.Method;
import java.util.Stack;

public abstract class LexMatchAcceptor {

    protected static final int VOID = 0;

    protected static final int YYTEXT = 1;

    private Stack<String> stack;

    protected void enter(String state) {
        if (stack == null) stack = new Stack<String>();
        stack.push(state);
    }

    protected void leave(String state) {
        stack.pop();
    }

    public String getState() {
        return stack.lastElement();
    }

    protected abstract void symbol(String name, String value);

    protected abstract void rule(_LexMatch match, Method action, int mode);
}
