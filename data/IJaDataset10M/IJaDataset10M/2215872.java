package org.primordion.xholon.tutorials;

/**
 * This is a simple non-Xholon class that can be used for testing XholonProxy.
 * 
 */
public class NonXholonClassOne {

    private int someInt = 0;

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }

    public String toString() {
        return "NonXholonClassOne someInt: " + Integer.toString(someInt);
    }
}
