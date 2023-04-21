package org.jmlspecs.javacontract.example.istack;

import org.jmlspecs.annotation.Invariant;
import org.jmlspecs.annotation.ModelField;
import org.jmlspecs.annotation.ModelMethod;
import org.jmlspecs.annotation.NonNull;
import org.jmlspecs.annotation.Pure;
import org.jmlspecs.annotation.Represents;
import org.jmlspecs.javacontract.JC;
import org.jmlspecs.javacontract.JC.DataGroupSpec;
import org.jmlspecs.javacontract.lib.collection.CollectionFactory;
import org.jmlspecs.javacontract.lib.collection.Sequence;
import org.jmlspecs.javacontract.predicate.IntRangePredicate;

public class BoundedStack implements BoundedStackInterface, BoundedStackInterface.IJCBoundedStackInterface {

    @NonNull
    protected java.lang.Object[] theItems;

    protected int nextFree;

    protected int maxSize;

    public BoundedStack() {
        if (JC.ENABLED) {
            JC.spec(JC.PUBLIC, JC.NORMAL_BEHAVIOR, JC.assignable(MAX_SIZE(), size(), theStack()), JC.redundant(JC.ensures(theStack().equals(CollectionFactory.newSequence())), JC.ensures(theStack().isEmpty() && (size() == 0))));
        }
        this.maxSize = 10;
        this.theItems = new Object[this.maxSize];
        this.nextFree = 0;
        if (JC.ENABLED) {
            JC.assertTrue(JC.fresh(this.theItems) && (this.nextFree == 0) && (this.theItems.length == this.maxSize));
        }
    }

    public BoundedStack(final int maxSize) {
        if (JC.ENABLED) {
            JC.spec(JC.PUBLIC, JC.NORMAL_BEHAVIOR, JC.assignable(MAX_SIZE(), size(), theStack()), JC.redundant(JC.ensures(theStack().equals(CollectionFactory.newSequence())), JC.ensures(theStack().isEmpty() && (size() == 0) && (MAX_SIZE() == maxSize))));
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
        if (JC.ENABLED) {
            JC.assertTrue(JC.fresh(retValue) && (retValue.nextFree == this.nextFree) && JC.all(new IntRangePredicate(0, retValue.theItems.length - 1) {

                @Override
                public boolean pred(final int i) {
                    return retValue.theItems[i] == BoundedStack.this.theItems[i];
                }
            }));
        }
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

    @DataGroupSpec
    public void jcDataGroupSpecs() {
        JC.in(this.maxSize, MAX_SIZE());
        JC.in(this.theItems, theStack());
        JC.mapsInto(this.theItems[JC.ALL_INDEX], theStack());
        JC.in(this.nextFree, theStack());
    }

    @Invariant
    protected boolean jcInvNextFreeInRange() {
        return (0 <= this.nextFree) && (this.nextFree <= this.theItems.length);
    }

    @Invariant
    protected boolean jcInvTheItemsNonNullElements() {
        return JC.all(new IntRangePredicate(0, BoundedStack.this.nextFree - 1) {

            @Override
            public boolean pred(final int i) {
                return BoundedStack.this.theItems[i] != null;
            }
        });
    }

    @Invariant(redundantly = true)
    public boolean jcInvTheStackRep() {
        return (theStack() != null) && (theStack().int_length() == this.nextFree) && JC.all(new IntRangePredicate(0, BoundedStack.this.nextFree - 1) {

            @Override
            public boolean pred(final int i) {
                return theStack().itemAt(i) == BoundedStack.this.theItems[i];
            }
        });
    }

    @ModelField
    @Represents
    public int MAX_SIZE() {
        return this.maxSize;
    }

    public void pop() throws BoundedStackException {
        if (this.nextFree == 0) {
            throw new BoundedStackException("Tried to pop an empty stack.");
        } else {
            this.nextFree--;
            if (JC.ENABLED) {
                JC.assertTrue(this.nextFree == JC.old(this.nextFree) - 1);
            }
            return;
        }
    }

    protected void printStack() {
        if (JC.ENABLED) {
            JC.spec(JC.PUBLIC, JC.NORMAL_BEHAVIOR, JC.assignable(System.out), JC.ensures(JC.informally("prints a version of stack to System.out")));
        }
        System.out.println("The stack items are (top first):");
        System.out.println(toString());
        if (JC.ENABLED) {
            JC.assertTrue(JC.informally("toString() is appended to System.out"));
        }
    }

    public void push(final Object x) throws BoundedStackException {
        if (this.nextFree == this.maxSize) {
            throw new BoundedStackException("Tried to push onto a full stack");
        } else if (x == null) {
            throw new NullPointerException("Argument x to push is null");
        } else {
            this.theItems[this.nextFree++] = x;
            if (JC.ENABLED) {
                JC.assertTrue((this.theItems[JC.old(this.nextFree)] == x) && (this.nextFree == JC.old(this.nextFree) + 1));
            }
            return;
        }
    }

    @ModelField
    public int size() {
        return JCBoundedStackInterface.v.size();
    }

    @Represents
    @ModelField
    public Sequence<Object> theStack() {
        return theStackRep();
    }

    @Pure
    @ModelMethod
    protected Sequence<Object> theStackRep() {
        Sequence<Object> ret = CollectionFactory.newSequence();
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
        if (JC.ENABLED) {
            JC.spec(JC.PUBLIC, JC.NORMAL_BEHAVIOR, JC.assignable(JC.nothing), JC.ensures((JC.result() != null) && JC.informally("a string encoding of this is returned")));
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
        if (JC.ENABLED) {
            JC.assertTrue(JC.informally("returnString is of the form \"[<top item>, <next item>, ...]\""));
        }
        return ret.toString();
    }
}
