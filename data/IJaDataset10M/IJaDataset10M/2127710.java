package org.decisiondeck.xmcda_oo.utils;

public interface Modifier<T> {

    /**
     * @param target
     *            the object whose state is to be possibly modified. Not <code>null</code>.
     * @return <code>true</code> iff the target object changed as a result of this method call.
     */
    public boolean modify(T target);
}
