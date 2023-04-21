package testcase.pushdown.invariant.iface.clazz.classic.after;

public class BoundedStack implements BoundedStackInterface {

    protected java.lang.Object[] theItems;

    protected int nextFree;

    protected int maxSize;

    public BoundedStack() {
        maxSize = 10;
        theItems = new Object[maxSize];
        nextFree = 0;
    }

    public BoundedStack(int maxSize) {
        theItems = new Object[maxSize];
        nextFree = 0;
        this.maxSize = maxSize;
    }

    public Object clone() {
        BoundedStack retValue = new BoundedStack(maxSize);
        retValue.nextFree = nextFree;
        for (int k = 0; k < nextFree; k++) {
            retValue.theItems[k] = theItems[k];
        }
        return retValue;
    }

    public int getSizeLimit() {
        return maxSize;
    }

    public boolean isEmpty() {
        return (nextFree == 0);
    }

    public boolean isFull() {
        return (nextFree == maxSize);
    }

    public void pop() throws BoundedStackException {
        if (nextFree == 0) {
            throw new BoundedStackException("Tried to pop an empty stack.");
        } else {
            nextFree--;
            return;
        }
    }

    public void push(Object x) throws BoundedStackException {
        if (nextFree == maxSize) {
            throw new BoundedStackException("Tried to push onto a full stack");
        } else if (x == null) {
            throw new NullPointerException("Argument x to push is null");
        } else {
            theItems[nextFree++] = x;
            return;
        }
    }

    public Object top() throws BoundedStackException {
        if (nextFree == 0) {
            throw new BoundedStackException("empty stack");
        } else {
            return theItems[nextFree - 1];
        }
    }

    public String toString() {
        StringBuffer ret = new StringBuffer(this.getClass().toString() + " [");
        boolean first = true;
        for (int k = nextFree - 1; k >= 0; k--) {
            if (first) {
                first = false;
            } else {
                ret.append(", ");
            }
            if (theItems[k] != null) {
                ret.append(theItems[k]);
            } else {
                ret.append("null");
            }
        }
        ret.append("]");
        return ret.toString();
    }

    protected void printStack() {
        System.out.println("The stack items are (top first):");
        System.out.println(toString());
    }
}
