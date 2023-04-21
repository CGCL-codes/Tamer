package org.oslo.ocl20.syntax.ast.expressions;

public interface OrExpAS extends expressionsVisitable, uk.ac.kent.cs.kmf.patterns.Destroyable, LogicalExpAS {

    public java.lang.String toString();

    public boolean equals(java.lang.Object o);

    public int hashCode();

    public java.lang.Object clone();
}
