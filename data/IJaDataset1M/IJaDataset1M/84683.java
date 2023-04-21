package org.jpox.samples.dependentfield;

/**
 * Implementation of an interface that is marked as dependent in relations.
 * 
 * @version $Revision: 1.1 $
 */
public class DepInterfaceImpl1 implements DepInterface {

    String name;

    int value;

    public DepInterfaceImpl1(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
