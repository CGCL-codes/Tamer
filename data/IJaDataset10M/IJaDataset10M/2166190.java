package edu.gatech.cc.jcrasher.plans.stmt;

/**
 * Hides a single code statement.
 * We mostly use it for one of the following.
 * <ul>
 * <li>To invoke some method to modify a instance (side-effect).
 * <li>To invoke some method/ constructor under test.
 * </ul>
 * 
 * Unless otherwise noted:
 * <ul>
 * <li>Each reference parameter of every method must be non-null.
 * <li>Each reference return value must be non-null.
 * </ul>
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 * http://java.sun.com/docs/books/jls/third_edition/html/statements.html#14.5
 */
public interface Statement<T> extends BlockStatement<T> {
}
