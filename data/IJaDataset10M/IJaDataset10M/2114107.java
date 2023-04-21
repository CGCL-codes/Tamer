package org.codehaus.janino.util.iterator;

import java.util.*;
import org.codehaus.janino.*;

/**
 * An {@link java.util.Iterator} that iterates over scoped statements.
 * This iterator is used to iterate over the objects that implement
 * the Scope interface until there is no longer an enclosing scope.
 */
public class ScopeIterator implements Iterator {

    protected Java.Scope s;

    public ScopeIterator(Java.Statement stmt) {
        s = stmt.getEnclosingScope();
    }

    public ScopeIterator(Java.Scope es) {
        s = es;
    }

    public boolean hasNext() {
        return (s instanceof Java.Scope);
    }

    public Object next() {
        return (s = s.getEnclosingScope());
    }

    public void remove() {
        throw new RuntimeException("unsupported remove()");
    }
}
