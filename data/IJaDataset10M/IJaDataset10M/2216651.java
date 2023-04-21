package com.loribel.commons.abstraction;

import com.loribel.commons.exception.GB_VisitorException;

/**
 * Abstraction of a selector.
 */
public interface GB_Selector {

    /**
     * accept visitor
     */
    Object accept(GB_SelectorVisitor a_visitor) throws GB_VisitorException;

    /**
     * Returns the type of selector.
     */
    int getType();

    /**
     * Returns true if null is accepted.
     */
    boolean isAcceptNull();

    /**
     * Must return true if the selector is ignored.
     */
    boolean isByPass();

    /**
     * Returns true if the selector correspond to an interval.
     */
    boolean isInterval();

    boolean isInverse();

    /**
     * If selector is interval, returns it, otherwise returns null.
     */
    GB_Interval toInterval();
}
