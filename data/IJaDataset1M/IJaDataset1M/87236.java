package edu.uvm.cs.calendar.parser.abstractsyntax;

import edu.uvm.cs.calendar.parser.visitor.AbstractSyntaxVisitor;

public class Identifier {

    private String id;

    public Identifier(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
    }

    public void accept(AbstractSyntaxVisitor v) {
        v.visit(this);
    }

    public boolean equals(Object o) {
        if (o != null) {
            return id.equals(o.toString());
        }
        return false;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return id;
    }
}
