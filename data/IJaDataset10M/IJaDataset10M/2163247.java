package com.spoledge.audao.parser.gql.impl.soft.func;

import com.google.appengine.api.datastore.Key;

/**
 * The parent of all 1-argument GAE KEY functions.
 */
public abstract class KeyFunc1 extends Func1 {

    protected Object getFunctionValue(Object arg) {
        if (arg == null) return null;
        if (arg instanceof Key) return getFunctionValue((Key) arg);
        throw new IllegalArgumentException();
    }

    protected abstract Object getFunctionValue(Key arg);
}
