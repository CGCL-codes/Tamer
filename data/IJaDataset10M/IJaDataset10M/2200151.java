package edu.clemson.cs.r2jt.analysis;

import edu.clemson.cs.r2jt.type.Type;

/**
 * <p>A <code>TypeCorrespondence</code> represents one type that may be
 * substituted for another, but not necessarily the other way around.
 * <code>type1</code> is the type you'd like and <code>type2</code> is the type
 * you're willing to accept.</p>
 */
public class TypeCorrespondence {

    private Type type1;

    private Type type2;

    public TypeCorrespondence(Type type1, Type type2) {
        this.type1 = type1;
        this.type2 = type2;
    }

    public Type getType1() {
        return type1;
    }

    public Type getType2() {
        return type2;
    }
}
