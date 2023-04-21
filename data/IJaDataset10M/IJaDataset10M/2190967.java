package com.volantis.mcs.themes;

/**
 * Interface defining all visitor methods that a concrete Theme Visitor needs
 * to implement, one for each object type which may call the visitor.
 */
public interface ThemeVisitor {

    /**
     * Called to process the targetObject, normally targetObject will have
     * called this method.
     *
     * @param targetObject Object that is to be processed
     * @return true if the calling Rule should traverse its children,
     *         false if the traversal has been done by this visit method.
     */
    boolean visit(Rule targetObject);

    /**
     * Called to process the targetObject, normally targetObject will have
     * called this method.
     *
     * @param targetObject Object that is to be processed
     * @return true if the calling StyleProperties should traverse its
     *         children, false if the traversal has been done by this visit
     *         method.
     */
    boolean visit(StyleProperties targetObject);

    /**
     * Called to process the targetObject, normally targetObject will have
     * called this method.
     *
     * @param targetObject Object that is to be processed
     * @return true if the calling Selector should traverse its children,
     *         false if the traversal has been done by this visit method.
     */
    boolean visit(Selector targetObject);
}
