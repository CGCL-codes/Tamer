package testcase.pullup.invariant.iface.clazz.classic.after;

public interface BoundedStackInterface extends BoundedThing {

    public void pop() throws BoundedStackException;

    public void push(Object x) throws BoundedStackException, NullPointerException;

    public Object top() throws BoundedStackException;
}
