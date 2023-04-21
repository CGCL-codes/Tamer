package org.jopenray.operation;

public class UnknownACOperation extends Operation {

    public UnknownACOperation(int a, int b, int c, int d) {
        allocate(20);
        setHeader(0xAC, 0, 0, 0, 0);
        buffer.addInt16(a);
        buffer.addInt16(b);
        buffer.addInt16(c);
        buffer.addInt16(d);
    }

    public UnknownACOperation() {
        this(0, 1, 0, 4);
    }

    @Override
    public void dump() {
        System.out.println("UnknownACOperation");
    }
}
