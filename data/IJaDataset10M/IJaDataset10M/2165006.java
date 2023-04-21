package com.loribel.commons.abstraction;

import java.io.Serializable;

/**
 * Abstraction of an interval.
 */
public interface GB_SelectorSet extends GB_Selector, Serializable {

    void setAcceptNull(boolean a_flag);

    void setInverse(boolean a_flag);

    /**
     * Returns the type of interval.
     */
    void setType(int a_type);
}
