package org.norecess.nolatte.ast;

import org.norecess.nolatte.ast.visitors.DatumVisitor;

public class Null implements INull {

    public static final Statement NULL_STATEMENT = new DatumAsStatement(new Null());

    private static final long serialVersionUID = -3040902903203488073L;

    public Null() {
    }

    public <T> T accept(DatumVisitor<T> visitor) {
        return visitor.visitNull(this);
    }

    public boolean isTrue() {
        return true;
    }

    public boolean isApplicable() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null) && getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        return (int) serialVersionUID;
    }

    public int getLine() {
        return 0;
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
