package com.jklas.search.query.operator;

import java.util.Set;
import com.jklas.search.engine.dto.ObjectResult;
import com.jklas.search.index.MasterAndInvertedIndexReader;

public abstract class Operator<E extends ObjectResult> {

    public abstract int hashCode();

    public abstract boolean equals(Object other);

    public abstract Set<E> work(MasterAndInvertedIndexReader reader);
}
