package com.google.gwt.dev.js.ast;

/**
 * The context in which a JsNode visitation occurs. This represents the set of
 * possible operations a JsVisitor subclass can perform on the currently visited
 * node.
 * 
 * @param <T>
 */
public interface JsContext<T extends JsVisitable<T>> {

    boolean canInsert();

    boolean canRemove();

    void insertAfter(T node);

    void insertBefore(T node);

    boolean isLvalue();

    void removeMe();

    void replaceMe(T node);
}
