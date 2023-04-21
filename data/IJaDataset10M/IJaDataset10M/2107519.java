package com.workplacesystems.queuj.utils.collections.helpers;

import com.workplacesystems.queuj.utils.collections.Filter;

/** Basic filter which includes everything */
public class IncludeAllFilter<E> implements Filter<E> {

    public boolean isValid(E obj) {
        return true;
    }
}
