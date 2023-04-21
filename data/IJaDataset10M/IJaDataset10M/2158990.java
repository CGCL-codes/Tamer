package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;

/**
 * Generic notion of reference object that know how to query/modify the
 * target objects based on some property/index.
 */
public abstract class Ref implements Serializable {

    public boolean has(Context cx) {
        return true;
    }

    public abstract Object get(Context cx);

    public abstract Object set(Context cx, Object value);

    public boolean delete(Context cx) {
        return false;
    }
}
