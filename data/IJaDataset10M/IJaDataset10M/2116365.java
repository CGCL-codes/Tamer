package com.loribel.commons.util.filter;

import com.loribel.commons.abstraction.GB_ObjectFilter;

/**
 * Abstract class for filter.
 *
 * @author Gregory Borelli
 */
public abstract class GB_ObjectFilterAbstract implements GB_ObjectFilter {

    private boolean acceptNull;

    private boolean inverse;

    public GB_ObjectFilterAbstract() {
        this(false, false);
    }

    public GB_ObjectFilterAbstract(boolean a_acceptNull, boolean a_inverse) {
        super();
        acceptNull = a_acceptNull;
        inverse = a_inverse;
    }

    public final boolean accept(Object a_object) {
        if (a_object == null) {
            return acceptNull;
        }
        boolean r = accept2(a_object);
        if (inverse) {
            return !r;
        } else {
            return r;
        }
    }

    protected abstract boolean accept2(Object a_item);

    public boolean isAcceptNull() {
        return acceptNull;
    }

    public boolean isInverse() {
        return inverse;
    }

    public void setAcceptNull(boolean a_acceptNull) {
        acceptNull = a_acceptNull;
    }

    public void setInverse(boolean a_inverse) {
        inverse = a_inverse;
    }
}
