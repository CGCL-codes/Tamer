package org.owasp.orizon.mirage;

public class Occurrence extends Item {

    public Occurrence(int line, String name, String filename) {
        super(line, name, filename);
    }

    public String toString() {
        return "\tline: " + getLineNo() + " stmt: " + getName();
    }
}
