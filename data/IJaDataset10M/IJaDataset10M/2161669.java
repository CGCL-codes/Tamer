package testcase.pushdown.invariant.iface.clazz.classic.before;

public interface BoundedStackInterface extends BoundedThing {

    public void pop() throws BoundedStackException;

    public void push(Object x) throws BoundedStackException, NullPointerException;

    public Object top() throws BoundedStackException;
}
