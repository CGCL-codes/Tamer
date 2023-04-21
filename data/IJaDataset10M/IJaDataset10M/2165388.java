package com.rapidminer.tools.patterns;

/** Interface for the visitor pattern.
 * @author Simon Fischer
 */
public interface Visitor<T> {

    public void visit(T element);
}
