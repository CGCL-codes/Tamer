package org.mozilla.javascript.ast;

/**
 * Common interface for {@link ArrayLiteral} and {@link ObjectLiteral}
 * node types, both of which may appear in "destructuring" expressions or
 * contexts.
 */
public interface DestructuringForm {

    /**
   * Marks this node as being a destructuring form - that is, appearing
   * in a context such as {@code for ([a, b] in ...)} where it's the
   * target of a destructuring assignment.
   */
    void setIsDestructuring(boolean destructuring);

    /**
   * Returns true if this node is in a destructuring position:
   * a function parameter, the target of a variable initializer, the
   * iterator of a for..in loop, etc.
   */
    boolean isDestructuring();
}
