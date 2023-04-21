package org.jpox.samples.detach.fetchdepth;

/**
 * @author Marco Schulze - nlmarco at users dot sourceforge dot net
 */
public class A {

    private String name;

    private B b;

    protected A() {
    }

    public A(String name) {
        this.name = name;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public String getName() {
        return name;
    }
}
