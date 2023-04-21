package testcase.pullup.precond.iface.iface.classic.after;

public interface BoundedThing {

    public int getSizeLimit();

    public boolean isEmpty();

    public boolean isFull();

    public Object clone() throws CloneNotSupportedException;
}
