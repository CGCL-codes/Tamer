package testcase.pullup.precond.iface.clazz.jc.after;

import org.jmlspecs.annotation.Invariant;
import org.jmlspecs.annotation.ModelField;
import org.jmlspecs.annotation.ModelMethod;
import org.jmlspecs.annotation.NonNull;
import org.jmlspecs.annotation.Pure;
import org.jmlspecs.annotation.Represents;
import org.jmlspecs.jc.JC;
import org.jmlspecs.jc.JC.ModelSpec;
import org.jmlspecs.models.JMLObjectSequence;

public class BoundedStack implements BoundedStackInterface {

    @NonNull
    protected java.lang.Object[] theItems;

    protected int nextFree;

    protected int maxSize;

    public BoundedStack() {
        JC.specCase(JC.PUBLIC, JC.NORMAL);
        {
            JC.assignable(MAX_SIZE(), size(), theStack());
            JC.ensures(theStack().equals(new JMLObjectSequence()));
            JC.ensuresRedundantly(theStack().isEmpty() && (size() == 0));
        }
        this.maxSize = 10;
        this.theItems = new Object[this.maxSize];
        this.nextFree = 0;
        JC.assertTrue(JC.fresh(this.theItems) && (this.nextFree == 0) && (this.theItems.length == this.maxSize));
    }

    public BoundedStack(final int maxSize) {
        JC.specCase(JC.PUBLIC, JC.NORMAL);
        {
            JC.assignable(MAX_SIZE(), size(), theStack());
            JC.ensures(theStack().equals(new JMLObjectSequence()));
            JC.ensuresRedundantly(theStack().isEmpty() && (size() == 0) && (MAX_SIZE() == maxSize));
        }
        this.theItems = new Object[maxSize];
        this.nextFree = 0;
        this.maxSize = maxSize;
        JC.assertTrue(JC.fresh(this.theItems) && (this.nextFree == 0) && (this.theItems.length == maxSize));
    }

    @Override
    public Object clone() {
        final BoundedStack retValue = new BoundedStack(this.maxSize);
        retValue.nextFree = this.nextFree;
        for (int k = 0; k < this.nextFree; k++) {
            retValue.theItems[k] = this.theItems[k];
        }
        JC.assertTrue(JC.fresh(retValue) && (retValue.nextFree == this.nextFree) && new JC.ForAll() {

            int k;

            @Override
            public boolean pred() {
                return retValue.theItems[this.k] == BoundedStack.this.theItems[this.k];
            }

            @Override
            public boolean range() {
                return (0 <= this.k) && (this.k <= retValue.theItems.length - 1);
            }
        }.value());
        return retValue;
    }

    public int getSizeLimit() {
        return this.maxSize;
    }

    public boolean isEmpty() {
        return (this.nextFree == 0);
    }

    public boolean isFull() {
        return (this.nextFree == this.maxSize);
    }

    @Invariant
    protected boolean jcInvNextFreeInRange() {
        return (0 <= this.nextFree) && (this.nextFree <= this.theItems.length);
    }

    @Invariant
    protected boolean jcInvTheItemsNonNullElements() {
        return new JC.ForAll() {

            int i;

            @Override
            public boolean pred() {
                return BoundedStack.this.theItems[this.i] != null;
            }

            @Override
            public boolean range() {
                return (0 <= this.i) && (this.i < BoundedStack.this.nextFree);
            }
        }.value();
    }

    @Invariant(redundantly = true)
    public boolean jcInvTheStackRep() {
        return (theStack() != null) && (theStack().int_length() == this.nextFree) && new JC.ForAll() {

            int i;

            @Override
            public boolean pred() {
                return theStack().itemAt(this.i) == BoundedStack.this.theItems[this.i];
            }

            @Override
            public boolean range() {
                return (0 <= this.i) && (this.i < BoundedStack.this.nextFree);
            }
        }.value();
    }

    @ModelSpec
    public void jcModelMaxSizeIn() {
        JC.in(this.maxSize, MAX_SIZE());
    }

    @ModelSpec
    public void jcModelTheStackInMaps() {
        JC.in(this.theItems, theStack());
        JC.mapsInto(this.theItems[JC.ALL_INDEX], theStack());
        JC.in(this.nextFree, theStack());
    }

    @Represents
    @ModelField
    public int MAX_SIZE() {
        return this.maxSize;
    }

    public void pop() throws BoundedStackException {
        if (this.nextFree == 0) {
            throw new BoundedStackException("Tried to pop an empty stack.");
        } else {
            this.nextFree--;
            JC.assertTrue(this.nextFree == JC.old(this.nextFree) - 1);
            return;
        }
    }

    protected void printStack() {
        JC.specCase(JC.PUBLIC, JC.NORMAL);
        {
            JC.assignable(System.out);
            JC.ensures(JC.informally("prints a version of stack to System.out"));
        }
        System.out.println("The stack items are (top first):");
        System.out.println(toString());
        JC.assertTrue(JC.informally("toString() is appended to System.out"));
    }

    public void push(final Object x) throws BoundedStackException {
        if (this.nextFree == this.maxSize) {
            throw new BoundedStackException("Tried to push onto a full stack");
        } else if (x == null) {
            throw new NullPointerException("Argument x to push is null");
        } else {
            this.theItems[this.nextFree++] = x;
            JC.assertTrue((this.theItems[JC.old(this.nextFree)] == x) && (this.nextFree == JC.old(this.nextFree) + 1));
            return;
        }
    }

    @ModelField
    public int size() {
        return JC.someValue();
    }

    @Represents
    @ModelField
    public JMLObjectSequence theStack() {
        return theStackRep();
    }

    @Pure
    @ModelMethod
    protected JMLObjectSequence theStackRep() {
        JMLObjectSequence ret = new JMLObjectSequence();
        int i;
        for (i = 0; i < this.nextFree; i++) {
            ret = ret.insertFront(this.theItems[i]);
        }
        return ret;
    }

    public Object top() throws BoundedStackException {
        if (this.nextFree == 0) {
            throw new BoundedStackException("empty stack");
        } else {
            return this.theItems[this.nextFree - 1];
        }
    }

    @Override
    public String toString() {
        JC.specCase(JC.PUBLIC, JC.NORMAL);
        {
            JC.assignable(JC.NOTHING);
            JC.ensures((JC.result() != null) && JC.informally("a string encoding of this is returned"));
        }
        final StringBuffer ret = new StringBuffer(this.getClass().toString() + " [");
        boolean first = true;
        for (int k = this.nextFree - 1; k >= 0; k--) {
            if (first) {
                first = false;
            } else {
                ret.append(", ");
            }
            if (this.theItems[k] != null) {
                ret.append(this.theItems[k]);
            } else {
                ret.append("null");
            }
        }
        ret.append("]");
        JC.assertTrue(JC.informally("returnString is of the form \"[<top item>, <next item>, ...]\""));
        return ret.toString();
    }
}
